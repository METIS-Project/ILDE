
# Copyright (c) 2001-2004 Twisted Matrix Laboratories.
# See LICENSE for details.


from twisted.trial import unittest

try:
    import cStringIO as StringIO
except ImportError:
    import StringIO

import sys
# Twisted Imports
from twisted.spread import banana
from twisted.python import failure
from twisted.internet import protocol, main

class MathTestCase(unittest.TestCase):
    def testInt2b128(self):
        funkylist = range(0,100) + range(1000,1100) + range(1000000,1000100) + [1024 **10l]
        for i in funkylist:
            x = StringIO.StringIO()
            banana.int2b128(i, x.write)
            v = x.getvalue()
            y = banana.b1282int(v)
            assert y == i, "y = %s; i = %s" % (y,i)

class BananaTestCase(unittest.TestCase):

    encClass = banana.Pynana
    
    def setUp(self):
        self.io = StringIO.StringIO()
        self.enc = self.encClass()
        self.enc.makeConnection(protocol.FileWrapper(self.io))
        self.enc._selectDialect("none")
        self.enc.expressionReceived = self.putResult

    def putResult(self, result):
        self.result = result

    def tearDown(self):
        self.enc.connectionLost(failure.Failure(main.CONNECTION_DONE))
        del self.enc

    def testString(self):
        self.enc.sendEncoded("hello")
        l = []
        self.enc.dataReceived(self.io.getvalue())
        assert self.result == 'hello'

    def testLong(self):
        self.enc.sendEncoded(1015l)
        self.enc.dataReceived(self.io.getvalue())
        assert self.result == 1015l, "should be 1015l, got %s" % self.result
        
    def testNegativeLong(self):
        self.enc.sendEncoded(-1015l)
        self.enc.dataReceived(self.io.getvalue())
        assert self.result == -1015l, "should be -1015l, got %s" % self.result

    def testInteger(self):
        self.enc.sendEncoded(1015)
        self.enc.dataReceived(self.io.getvalue())
        assert self.result == 1015, "should be 1015, got %s" % self.result

    def testNegative(self):
        self.enc.sendEncoded(-1015)
        self.enc.dataReceived(self.io.getvalue())
        assert self.result == -1015, "should be -1015, got %s" % self.result
        
    def testFloat(self):
        self.enc.sendEncoded(1015.)
        self.enc.dataReceived(self.io.getvalue())
        assert self.result == 1015.

    def testList(self):
        foo = [1, 2, [3, 4], [30.5, 40.2], 5, ["six", "seven", ["eight", 9]], [10], []]
        self.enc.sendEncoded(foo)
        self.enc.dataReceived(self.io.getvalue())
        assert self.result == foo, "%s!=%s" % (repr(self.result), repr(self.result))

    def testPartial(self):
        foo = [1, 2, [3, 4], [30.5, 40.2], 5,
               ["six", "seven", ["eight", 9]], [10],
               # TODO: currently the C implementation's a bit buggy...
               sys.maxint * 3l, sys.maxint * 2l, sys.maxint * -2l]
        self.enc.sendEncoded(foo)
        for byte in self.io.getvalue():
            self.enc.dataReceived(byte)
        assert self.result == foo, "%s!=%s" % (repr(self.result), repr(foo))

    def feed(self, data):
        for byte in data:
            self.enc.dataReceived(byte)
    def testOversizedList(self):
        data = '\x02\x01\x01\x01\x01\x80'
        # list(size=0x0101010102, about 4.3e9)
        self.failUnlessRaises(banana.BananaError, self.feed, data)
    def testOversizedString(self):
        data = '\x02\x01\x01\x01\x01\x82'
        # string(size=0x0101010102, about 4.3e9)
        self.failUnlessRaises(banana.BananaError, self.feed, data)

    def testCrashString(self):
        crashString = '\x00\x00\x00\x00\x04\x80'
        # string(size=0x0400000000, about 17.2e9)

        #  cBanana would fold that into a 32-bit 'int', then try to allocate
        #  a list with PyList_New(). cBanana ignored the NULL return value,
        #  so it would segfault when trying to free the imaginary list.

        # This variant doesn't segfault straight out in my environment.
        # Instead, it takes up large amounts of CPU and memory...
        #crashString = '\x00\x00\x00\x00\x01\x80'
        # print repr(crashString)
        #self.failUnlessRaises(Exception, self.enc.dataReceived, crashString)
        try:
            # should now raise MemoryError
            self.enc.dataReceived(crashString)
        except banana.BananaError:
            pass

    def testCrashNegativeLong(self):
        # There was a bug in cBanana which relied on negating a negative integer
        # always giving a postive result, but for the lowest possible number in
        # 2s-complement arithmetic, that's not true, i.e.
        #     long x = -2147483648;
        #     long y = -x;
        #     x == y;  /* true! */
        # (assuming 32-bit longs)
        self.enc.sendEncoded(-2147483648)
        self.enc.dataReceived(self.io.getvalue())
        assert self.result == -2147483648, "should be -2147483648, got %s" % self.result

            
testCases = [MathTestCase, BananaTestCase]

try:
    from twisted.spread import cBanana
except ImportError:
    pass
else:
    # XXX: Eww.  It shouldn't be this hard to install cBanana.
    banana.cBanana = cBanana
    cBanana.pyb1282int = banana.b1282int
    cBanana.pyint2b128 = banana.int2b128
    class CananaTestCase(BananaTestCase):
        encClass = banana.Canana

    testCases.append(CananaTestCase)


