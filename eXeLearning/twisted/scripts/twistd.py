
# Copyright (c) 2001-2004 Twisted Matrix Laboratories.
# See LICENSE for details.


from twisted.python import log, syslog
from twisted.python.util import switchUID
from twisted.application import app, service
from twisted.scripts import mktap
from twisted import copyright

import os, errno, sys

class ServerOptions(app.ServerOptions):
    synopsis = "Usage: twistd [options]"

    optFlags = [['nodaemon','n',  "don't daemonize"],
                ['quiet', 'q', "No-op for backwards compatability."],
                ['originalname', None, "Don't try to change the process name"],
                ['syslog', None,   "Log to syslog, not to file"],
                ['euid', '',
                 "Set only effective user-id rather than real user-id. "
                 "(This option has no effect unless the server is running as "
                 "root, in which case it means not to shed all privileges "
                 "after binding ports, retaining the option to regain "
                 "privileges in cases such as spawning processes. "
                 "Use with caution.)"],
               ]

    optParameters = [
                     ['prefix', None,'twisted',
                      "use the given prefix when syslogging"],
                     ['pidfile','','twistd.pid',
                      "Name of the pidfile"],
                     ['chroot', None, None,
                      'Chroot to a supplied directory before running'],
                     ['uid', 'u', None, "The uid to run as."],
                     ['gid', 'g', None, "The gid to run as."],
                    ]
    zsh_altArgDescr = {"prefix":"Use the given prefix when syslogging (default: twisted)",
                       "pidfile":"Name of the pidfile (default: twistd.pid)",}
    #zsh_multiUse = ["foo", "bar"]
    #zsh_mutuallyExclusive = [("foo", "bar"), ("bar", "baz")]
    zsh_actions = {"pidfile":'_files -g "*.pid"', "chroot":'_dirs'}
    zsh_actionDescr = {"chroot":"chroot directory"}

    def opt_version(self):
        """Print version information and exit.
        """
        print 'twistd (the Twisted daemon) %s' % copyright.version
        print copyright.copyright
        sys.exit()

    def postOptions(self):
        app.ServerOptions.postOptions(self)
        if self['pidfile']:
            self['pidfile'] = os.path.abspath(self['pidfile'])


def checkPID(pidfile):
    if not pidfile:
        return
    if os.path.exists(pidfile):
        try:
            pid = int(open(pidfile).read())
        except ValueError:
            sys.exit('Pidfile %s contains non-numeric value' % pidfile)
        try:
            os.kill(pid, 0)
        except OSError, why:
            if why[0] == errno.ESRCH:
                # The pid doesnt exists.
                log.msg('Removing stale pidfile %s' % pidfile, isError=True)
                os.remove(pidfile)
            else:
                sys.exit("Can't check status of PID %s from pidfile %s: %s" %
                         (pid, pidfile, why[1]))
        else:
            sys.exit("""\
Another twistd server is running, PID %s\n
This could either be a previously started instance of your application or a
different application entirely. To start a new one, either run it in some other
directory, or use the --pidfile and --logfile parameters to avoid clashes.
""" %  pid)

def removePID(pidfile):
    if not pidfile:
        return
    try:
        os.unlink(pidfile)
    except OSError, e:
        if e.errno == errno.EACCES or e.errno == errno.EPERM:
            log.msg("Warning: No permission to delete pid file")
        else:
            log.msg("Failed to unlink PID file:")
            log.deferr()
    except:
        log.msg("Failed to unlink PID file:")
        log.deferr()

def startLogging(logfilename, sysLog, prefix, nodaemon):
    if logfilename == '-':
        if not nodaemon:
            print 'daemons cannot log to stdout'
            os._exit(1)
        logFile = sys.stdout
    elif sysLog:
        syslog.startLogging(prefix)
    elif nodaemon and not logfilename:
        logFile = sys.stdout
    else:
        logFile = app.getLogFile(logfilename or 'twistd.log')
        try:
            import signal
        except ImportError:
            pass
        else:
            def rotateLog(signal, frame):
                from twisted.internet import reactor
                reactor.callFromThread(logFile.rotate)
            signal.signal(signal.SIGUSR1, rotateLog)
        
    if not sysLog:
        log.startLogging(logFile)
    sys.stdout.flush()


def daemonize():
    # See http://www.erlenstar.demon.co.uk/unix/faq_toc.html#TOC16
    if os.fork():   # launch child and...
        os._exit(0) # kill off parent
    os.setsid()
    if os.fork():   # launch child and...
        os._exit(0) # kill off parent again.
    os.umask(077)
    null=os.open('/dev/null', os.O_RDWR)
    for i in range(3):
        try:
            os.dup2(null, i)
        except OSError, e:
            if e.errno != errno.EBADF:
                raise
    os.close(null)

def shedPrivileges(euid, uid, gid):
    if uid is not None or gid is not None:
        switchUID(uid, gid, euid)
        extra = euid and 'e' or ''
        log.msg('set %suid/%sgid %s/%s' % (extra, extra, uid, gid))

def launchWithName(name):
    if name and name != sys.argv[0]:
        exe = os.path.realpath(sys.executable)
        log.msg('Changing process name to ' + name)
        os.execv(exe, [name, sys.argv[0], '--originalname']+sys.argv[1:])

def setupEnvironment(config):
    if config['chroot'] is not None:
        os.chroot(config['chroot'])
        if config['rundir'] == '.':
            config['rundir'] = '/'
    os.chdir(config['rundir'])
    if not config['nodaemon']:
        daemonize()
    if config['pidfile']:
        open(config['pidfile'],'wb').write(str(os.getpid()))

def startApplication(config, application):
    process = service.IProcess(application, None)
    if not config['originalname']:
        launchWithName(process.processName)
    setupEnvironment(config)
    #import pydevd
    #pydevd.settrace('saden-pc.lan', port=9999, stdoutToServer=True, stderrToServer=True, suspend=False)
    service.IService(application).privilegedStartService()

    uid, gid = mktap.getid(config['uid'], config['gid'])
    if uid is None:
        uid = process.uid
    if gid is None:
        gid = process.gid

    shedPrivileges(config['euid'], uid, gid)
    app.startApplication(application, not config['no_save'])


def runApp(config):
    checkPID(config['pidfile'])
    passphrase = app.getPassphrase(config['encrypted'])
    app.installReactor(config['reactor'])
    config['nodaemon'] = config['nodaemon'] or config['debug']
    oldstdout = sys.stdout
    oldstderr = sys.stderr
    startLogging(config['logfile'], config['syslog'], config['prefix'],
                 config['nodaemon'])
    app.initialLog()
    application = app.getApplication(config, passphrase)
    startApplication(config, application)
    app.runReactorWithLogging(config, oldstdout, oldstderr)
    removePID(config['pidfile'])
    app.reportProfile(config['report-profile'],
                      service.IProcess(application).processName)
    log.msg("Server Shut Down.")


def run():
    app.run(runApp, ServerOptions)


