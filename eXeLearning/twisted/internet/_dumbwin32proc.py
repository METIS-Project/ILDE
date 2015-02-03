# -*- test-case-name: twisted.test.test_process -*-
"""

http://isometric.sixsided.org/_/gates_in_the_head/

"""

import os

# Win32 imports
import win32api
import win32con
import win32event
import win32file
import win32pipe
import win32process
import win32security

# security attributes for pipes
PIPE_ATTRS_INHERITABLE = win32security.SECURITY_ATTRIBUTES()
PIPE_ATTRS_INHERITABLE.bInheritHandle = 1

from zope.interface import implements
from twisted.internet.interfaces import IProcessTransport, IConsumer, IProducer

from twisted.python import components
from twisted.python.win32 import cmdLineQuote

from twisted.internet import error
from twisted.python import failure

from twisted.internet import _pollingfile

def debug(msg):
    import sys
    print msg
    sys.stdout.flush()

class _Reaper(_pollingfile._PollableResource):

    def __init__(self, proc):
        self.proc = proc

    def checkWork(self):
        if win32event.WaitForSingleObject(self.proc.hProcess, 0) != win32event.WAIT_OBJECT_0:
            return 0
        exitCode = win32process.GetExitCodeProcess(self.proc.hProcess)
        if exitCode == 0:
            err = error.ProcessDone(exitCode)
        else:
            err = error.ProcessTerminated(exitCode)
        self.deactivate()
        self.proc.protocol.processEnded(failure.Failure(err))
        return 0


class Process(_pollingfile._PollingTimer):
    """A process that integrates with the Twisted event loop.

    If your subprocess is a python program, you need to:

     - Run python.exe with the '-u' command line option - this turns on
       unbuffered I/O. Buffering stdout/err/in can cause problems, see e.g.
       http://support.microsoft.com/default.aspx?scid=kb;EN-US;q1903

     - If you don't want Windows messing with data passed over
       stdin/out/err, set the pipes to be in binary mode::

        import os, sys, mscvrt
        msvcrt.setmode(sys.stdin.fileno(), os.O_BINARY)
        msvcrt.setmode(sys.stdout.fileno(), os.O_BINARY)
        msvcrt.setmode(sys.stderr.fileno(), os.O_BINARY)

    """
    implements(IProcessTransport, IConsumer, IProducer)

    buffer = ''

    def __init__(self, reactor, protocol, command, args, environment, path):
        _pollingfile._PollingTimer.__init__(self, reactor)
        self.protocol = protocol

        # security attributes for pipes
        sAttrs = win32security.SECURITY_ATTRIBUTES()
        sAttrs.bInheritHandle = 1

        # create the pipes which will connect to the secondary process
        self.hStdoutR, hStdoutW = win32pipe.CreatePipe(sAttrs, 0)
        self.hStderrR, hStderrW = win32pipe.CreatePipe(sAttrs, 0)
        hStdinR,  self.hStdinW  = win32pipe.CreatePipe(sAttrs, 0)

        win32pipe.SetNamedPipeHandleState(self.hStdinW,
                                          win32pipe.PIPE_NOWAIT,
                                          None,
                                          None)

        # set the info structure for the new process.
        StartupInfo = win32process.STARTUPINFO()
        StartupInfo.hStdOutput = hStdoutW
        StartupInfo.hStdError  = hStderrW
        StartupInfo.hStdInput  = hStdinR
        StartupInfo.dwFlags = win32process.STARTF_USESTDHANDLES

        # Create new handles whose inheritance property is false
        pid = win32api.GetCurrentProcess()

        tmp = win32api.DuplicateHandle(pid, self.hStdoutR, pid, 0, 0, win32con.DUPLICATE_SAME_ACCESS)
        win32file.CloseHandle(self.hStdoutR)
        self.hStdoutR = tmp

        tmp = win32api.DuplicateHandle(pid, self.hStderrR, pid, 0, 0, win32con.DUPLICATE_SAME_ACCESS)
        win32file.CloseHandle(self.hStderrR)
        self.hStderrR = tmp

        tmp = win32api.DuplicateHandle(pid, self.hStdinW, pid, 0, 0, win32con.DUPLICATE_SAME_ACCESS)
        win32file.CloseHandle(self.hStdinW)
        self.hStdinW = tmp

        # Add the specified environment to the current environment - this is
        # necessary because certain operations are only supported on Windows
        # if certain environment variables are present.
        env = os.environ.copy()
        env.update(environment or {})

        # create the process
        cmdline = ' '.join([cmdLineQuote(a) for a in args])
        # TODO: error detection here.
        self.hProcess, hThread, dwPid, dwTid = win32process.CreateProcess(command, cmdline, None, None, 1, 0, env, path, StartupInfo)
        win32file.CloseHandle(hThread)

        # close handles which only the child will use
        win32file.CloseHandle(hStderrW)
        win32file.CloseHandle(hStdoutW)
        win32file.CloseHandle(hStdinR)

        self.closed = 0
        self.closedNotifies = 0

        # set up everything
        self.stdout = _pollingfile._PollableReadPipe(
            self.hStdoutR,
            lambda data: self.protocol.childDataReceived(1, data),
            self.outConnectionLost)

        self.stderr = _pollingfile._PollableReadPipe(
                self.hStderrR,
                lambda data: self.protocol.childDataReceived(2, data),
                self.errConnectionLost)

        self.stdin = _pollingfile._PollableWritePipe(
            self.hStdinW, self.inConnectionLost)

        for pipewatcher in self.stdout, self.stderr, self.stdin:
            self._addPollableResource(pipewatcher)


        # notify protocol
        self.protocol.makeConnection(self)

        # (maybe?) a good idea in win32er, otherwise not
        # self.reactor.addEvent(self.hProcess, self, 'inConnectionLost')


    def signalProcess(self, signalID):
        if signalID in ("INT", "TERM", "KILL"):
            win32process.TerminateProcess(self.hProcess, 1)

    def write(self, data):
        """Write data to the process' stdin."""
        self.stdin.write(data)

    def writeSequence(self, seq):
        """Write data to the process' stdin."""
        self.stdin.writeSequence(seq)

    def closeChildFD(self, fd):
        if fd == 0:
            self.closeStdin()
        elif fd == 1:
            self.closeStdout()
        elif fd == 2:
            self.closeStderr()
        else:
            raise NotImplementedError("Only standard-IO file descriptors available on win32")

    def closeStdin(self):
        """Close the process' stdin.
        """
        self.stdin.close()

    def closeStderr(self):
        self.stderr.close()

    def closeStdout(self):
        self.stdout.close()

    def loseConnection(self):
        """Close the process' stdout, in and err."""
        self.closeStdin()
        self.closeStdout()
        self.closeStderr()

    def outConnectionLost(self):
        self.protocol.childConnectionLost(1)
        self.connectionLostNotify()

    def errConnectionLost(self):
        self.protocol.childConnectionLost(2)
        self.connectionLostNotify()

    def inConnectionLost(self):
        self.protocol.childConnectionLost(0)
        self.connectionLostNotify()

    def connectionLostNotify(self):
        """Will be called 3 times, by stdout/err threads and process handle."""
        self.closedNotifies = self.closedNotifies + 1
        if self.closedNotifies == 3:
            self.closed = 1
            self._addPollableResource(_Reaper(self))

    # IConsumer
    def registerProducer(self, producer, streaming):
        self.stdin.registerProducer(producer, streaming)

    def unregisterProducer(self):
        self.stdin.unregisterProducer()

    # IProducer
    def pauseProducing(self):
        self._pause()

    def resumeProducing(self):
        self._unpause()

    def stopProducing(self):
        self.loseConnection()



components.backwardsCompatImplements(Process)
