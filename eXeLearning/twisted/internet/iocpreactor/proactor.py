# Copyright (c) 2001-2004 Twisted Matrix Laboratories.
# See LICENSE for details.


from twisted.internet import defer, base, main
from twisted.internet.interfaces import IReactorTCP, IReactorUDP, IReactorArbitrary, IReactorProcess
from twisted.python import threadable, log, reflect
from zope.interface import implements, implementsOnly

import tcp, udp, process, process_waiter
from _iocp import iocpcore

class Proactor(iocpcore, base.ReactorBase, log.Logger):
    # TODO: IReactorArbitrary, IReactorUDP, IReactorMulticast,
    # IReactorSSL (or leave it until exarkun finishes TLS)
    # IReactorCore (cleanup)
    implementsOnly(IReactorTCP, IReactorUDP, IReactorArbitrary, IReactorProcess)
    handles = None
    iocp = None

    def __init__(self):
        iocpcore.__init__(self)
        base.ReactorBase.__init__(self)
        self.logstr = reflect.qual(self.__class__)
        self.processWaiter = process_waiter.ProcessWaiter(self)
#        self.completables = {}

    def startRunning(self):
        threadable.registerAsIOThread()
        self.fireSystemEvent('startup')
        self.running = 1

    def run(self):
        self.startRunning()
        self.mainLoop()

    def mainLoop(self):
        while self.running:
            try:
                while self.running:
                    # Advance simulation time in delayed event
                    # processors.
                    self.runUntilCurrent()
                    t2 = self.timeout()
                    t = self.running and t2
                    self.doIteration(t)
            except KeyboardInterrupt:
                self.stop()
            except:
                log.msg("Unexpected error in main loop.")
                log.deferr()
            else:
                log.msg('Main loop terminated.')

    def removeAll(self):
        return []

    def installWaker(self):
        pass

    def wakeUp(self):
        def ignore(ret, bytes, arg):
            pass
        if not threadable.isInIOThread():
            self.issuePostQueuedCompletionStatus(ignore, None)
            
    def listenTCP(self, port, factory, backlog=50, interface=''):
        p = tcp.Port((interface, port), factory, backlog)
        p.startListening()
        return p

    def connectTCP(self, host, port, factory, timeout=30, bindAddress=None):
        c = tcp.Connector((host, port), factory, timeout, bindAddress)
        c.connect()
        return c
           
    def listenUDP(self, port, protocol, interface='', maxPacketSize=8192):
        p = udp.Port((interface, port), protocol, maxPacketSize)
        p.startListening()
        return p

    def connectUDPblah(self, remotehost, remoteport, protocol, localport=0,
                  interface='', maxPacketSize=8192):
        p = udp.ConnectedPort((remotehost, remoteport), (interface, localport), protocol, maxPacketSize)
        p.startListening()
        return p

    def listenWith(self, portType, *args, **kw):
        p = portType(*args, **kw)
        p.startListening()
        return p

    def connectWith(self, connectorType, *args, **kw):
        c = connectorType(*args, **kw)
        c.connect()
        return c

    def spawnProcess(self, processProtocol, executable, args=(), env={}, path=None, usePTY=0):
        """Spawn a process."""
        return process.Process(self, processProtocol, executable, args, env, path)
    
    def logPrefix(self):
        return self.logstr
        
def install():
    from twisted.python import threadable
    p = Proactor()
    threadable.init()
    main.installReactor(p)

