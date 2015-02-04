
# Copyright (c) 2001-2004 Twisted Matrix Laboratories.
# See LICENSE for details.



"""
Test cases for defer module.
"""

from __future__ import nested_scopes

from twisted.trial import unittest, util
from twisted.internet import reactor, defer
from twisted.python import failure, log


class GenericError(Exception): pass

class DeferredTestCase(unittest.TestCase):

    def setUp(self):
        self.callback_results = None
        self.errback_results = None
        self.callback2_results = None

    def _callback(self, *args, **kw):
        self.callback_results = args, kw
        return args[0]

    def _callback2(self, *args, **kw):
        self.callback2_results = args, kw

    def _errback(self, *args, **kw):
        self.errback_results = args, kw

    def testCallbackWithoutArgs(self):
        deferred = defer.Deferred()
        deferred.addCallback(self._callback)
        deferred.callback("hello")
        self.failUnlessEqual(self.errback_results, None)
        self.failUnlessEqual(self.callback_results, (('hello',), {}))

    def testCallbackWithArgs(self):
        deferred = defer.Deferred()
        deferred.addCallback(self._callback, "world")
        deferred.callback("hello")
        self.failUnlessEqual(self.errback_results, None)
        self.failUnlessEqual(self.callback_results, (('hello', 'world'), {}))

    def testCallbackWithKwArgs(self):
        deferred = defer.Deferred()
        deferred.addCallback(self._callback, world="world")
        deferred.callback("hello")
        self.failUnlessEqual(self.errback_results, None)
        self.failUnlessEqual(self.callback_results,
                             (('hello',), {'world': 'world'}))

    def testTwoCallbacks(self):
        deferred = defer.Deferred()
        deferred.addCallback(self._callback)
        deferred.addCallback(self._callback2)
        deferred.callback("hello")
        self.failUnlessEqual(self.errback_results, None)
        self.failUnlessEqual(self.callback_results,
                             (('hello',), {}))
        self.failUnlessEqual(self.callback2_results,
                             (('hello',), {}))

    def testDeferredList(self):
        defr1 = defer.Deferred()
        defr2 = defer.Deferred()
        defr3 = defer.Deferred()
        dl = defer.DeferredList([defr1, defr2, defr3])
        result = []
        def cb(resultList, result=result):
            result.extend(resultList)
        def catch(err):
            return None
        dl.addCallbacks(cb, cb)
        defr1.callback("1")
        defr2.addErrback(catch)
        # "catch" is added to eat the GenericError that will be passed on by
        # the DeferredList's callback on defr2. If left unhandled, the
        # Failure object would cause a log.err() warning about "Unhandled
        # error in Deferred". Twisted's pyunit watches for log.err calls and
        # treats them as failures. So "catch" must eat the error to prevent
        # it from flunking the test.
        defr2.errback(GenericError("2"))
        defr3.callback("3")
        self.failUnlessEqual([result[0],
                    #result[1][1] is now a Failure instead of an Exception
                              (result[1][0], str(result[1][1].value)),
                              result[2]],

                             [(defer.SUCCESS, "1"),
                              (defer.FAILURE, "2"),
                              (defer.SUCCESS, "3")])

    def testEmptyDeferredList(self):
        result = []
        def cb(resultList, result=result):
            result.append(resultList)

        dl = defer.DeferredList([])
        dl.addCallbacks(cb)
        self.failUnlessEqual(result, [[]])

        result[:] = []
        dl = defer.DeferredList([], fireOnOneCallback=1)
        dl.addCallbacks(cb)
        self.failUnlessEqual(result, [])
        
    def testDeferredListFireOnOneError(self):
        defr1 = defer.Deferred()
        defr2 = defer.Deferred()
        defr3 = defer.Deferred()
        dl = defer.DeferredList([defr1, defr2, defr3], fireOnOneErrback=1)
        result = []
        dl.addErrback(result.append)

        # consume errors after they pass through the DeferredList (to avoid
        # 'Unhandled error in Deferred'.
        def catch(err):
            return None
        defr2.addErrback(catch)

        # fire one Deferred's callback, no result yet
        defr1.callback("1")
        self.failUnlessEqual(result, [])

        # fire one Deferred's errback -- now we have a result
        defr2.errback(GenericError("from def2"))
        self.failUnlessEqual(len(result), 1)

        # extract the result from the list
        failure = result[0]

        # the type of the failure is a FirstError
        self.failUnless(issubclass(failure.type, defer.FirstError), 
            'issubclass(failure.type, defer.FirstError) failed: '
            'failure.type is %r' % (failure.type,)
        )

        # FirstErrors act like tuples for backwards compatibility :(
        firstError = failure.value
        self.failUnlessEqual(firstError[0], firstError.subFailure)
        self.failUnlessEqual(firstError[1], firstError.index)

        # check that the GenericError("2") from the deferred at index 1
        # (defr2) is intact inside failure.value
        self.failUnlessEqual(firstError.subFailure.type, GenericError)
        self.failUnlessEqual(firstError.subFailure.value.args, ("from def2",))
        self.failUnlessEqual(firstError.index, 1)

        

    def testDeferredListDontConsumeErrors(self):
        d1 = defer.Deferred()
        dl = defer.DeferredList([d1])

        errorTrap = []
        d1.addErrback(errorTrap.append)

        result = []
        dl.addCallback(result.append)

        d1.errback(GenericError('Bang'))
        self.failUnlessEqual('Bang', errorTrap[0].value.args[0])
        self.failUnlessEqual(1, len(result))
        self.failUnlessEqual('Bang', result[0][0][1].value.args[0])

    def testDeferredListConsumeErrors(self):
        d1 = defer.Deferred()
        dl = defer.DeferredList([d1], consumeErrors=True)

        errorTrap = []
        d1.addErrback(errorTrap.append)

        result = []
        dl.addCallback(result.append)

        d1.errback(GenericError('Bang'))
        self.failUnlessEqual([], errorTrap)
        self.failUnlessEqual(1, len(result))
        self.failUnlessEqual('Bang', result[0][0][1].value.args[0])

    def testDeferredListFireOnOneErrorWithAlreadyFiredDeferreds(self):
        # Create some deferreds, and errback one
        d1 = defer.Deferred()
        d2 = defer.Deferred()
        d1.errback(GenericError('Bang'))

        # *Then* build the DeferredList, with fireOnOneErrback=True
        dl = defer.DeferredList([d1, d2], fireOnOneErrback=True)
        result = []
        dl.addErrback(result.append)
        self.failUnlessEqual(1, len(result))

        d1.addErrback(lambda e: None)  # Swallow error

    def testDeferredListWithAlreadyFiredDeferreds(self):
        # Create some deferreds, and err one, call the other
        d1 = defer.Deferred()
        d2 = defer.Deferred()
        d1.errback(GenericError('Bang'))
        d2.callback(2)

        # *Then* build the DeferredList
        dl = defer.DeferredList([d1, d2])

        result = []
        dl.addCallback(result.append)

        self.failUnlessEqual(1, len(result))

        d1.addErrback(lambda e: None)  # Swallow error

    def testTimeOut(self):
        d = defer.Deferred()
        d.setTimeout(1.0)
        l = []
        d.addErrback(l.append)
        # Make sure the reactor is shutdown
        d.addBoth(lambda x, r=reactor: r.crash())

        self.assertEquals(l, [])
        reactor.run()
        self.assertEquals(len(l), 1)
        self.assertEquals(l[0].type, defer.TimeoutError)

    def testImmediateSuccess(self):
        l = []
        d = defer.succeed("success")
        d.addCallback(l.append)
        self.assertEquals(l, ["success"])

    def testImmediateSuccess2(self):
        l = []
        d = defer.succeed("success")
        # this is how trial.util.deferredResult works
        # NOTE: never actually use trial.util.deferredResult. It doesn't work.
        d.setTimeout(1.0)
        d.addCallback(l.append)
        self.assertEquals(l, ["success"])

    def testImmediateFailure(self):
        l = []
        d = defer.fail(GenericError("fail"))
        d.addErrback(l.append)
        self.assertEquals(str(l[0].value), "fail")

    def testPausedFailure(self):
        l = []
        d = defer.fail(GenericError("fail"))
        d.pause()
        d.addErrback(l.append)
        self.assertEquals(l, [])
        d.unpause()
        self.assertEquals(str(l[0].value), "fail")

    def testCallbackErrors(self):
        l = []
        d = defer.Deferred().addCallback(lambda _: 1/0).addErrback(l.append)
        d.callback(1)
        self.assert_(isinstance(l[0].value, ZeroDivisionError))
        l = []
        d = defer.Deferred().addCallback(
            lambda _: failure.Failure(ZeroDivisionError())).addErrback(l.append)
        d.callback(1)
        self.assert_(isinstance(l[0].value, ZeroDivisionError))

    def testUnpauseBeforeCallback(self):
        d = defer.Deferred()
        d.pause()
        d.addCallback(self._callback)
        d.unpause()

    def testReturnDeferred(self):
        d = defer.Deferred()
        d2 = defer.Deferred()
        d2.pause()
        d.addCallback(lambda r, d2=d2: d2)
        d.addCallback(self._callback)
        d.callback(1)
        assert self.callback_results is None, "Should not have been called yet."
        d2.callback(2)
        assert self.callback_results is None, "Still should not have been called yet."
        d2.unpause()
        assert self.callback_results[0][0] == 2, "Result should have been from second deferred:%s"% (self.callback_results,)

    def testGatherResults(self):
        # test successful list of deferreds
        l = []
        defer.gatherResults([defer.succeed(1), defer.succeed(2)]).addCallback(l.append)
        self.assertEquals(l, [[1, 2]])
        # test failing list of deferreds
        l = []
        dl = [defer.succeed(1), defer.fail(ValueError)]
        defer.gatherResults(dl).addErrback(l.append)
        self.assertEquals(len(l), 1)
        self.assert_(isinstance(l[0], failure.Failure))
        # get rid of error
        dl[1].addErrback(lambda e: 1)

    def testMaybeDeferred(self):
        S, E = [], []
        d = defer.maybeDeferred((lambda x: x + 5), 10)
        d.addCallbacks(S.append, E.append)
        self.assertEquals(E, [])
        self.assertEquals(S, [15])

        S, E = [], []
        try:
            '10' + 5
        except TypeError, e:
            expected = str(e)
        d = defer.maybeDeferred((lambda x: x + 5), '10')
        d.addCallbacks(S.append, E.append)
        self.assertEquals(S, [])
        self.assertEquals(len(E), 1)
        self.assertEquals(str(E[0].value), expected)

        d = defer.Deferred()
        reactor.callLater(0.2, d.callback, 'Success')
        d.addCallback(self.assertEquals, 'Success')
        d.addCallback(self._testMaybeError)
        return d

    def _testMaybeError(self, ignored):
        d = defer.Deferred()
        reactor.callLater(0.2, d.errback, failure.Failure(RuntimeError()))
        self.assertFailure(d, RuntimeError)
        return d


class AlreadyCalledTestCase(unittest.TestCase):
    def setUp(self):
        self._deferredWasDebugging = defer.getDebugging()
        defer.setDebugging(True)

    def tearDown(self):
        defer.setDebugging(self._deferredWasDebugging)

    def _callback(self, *args, **kw):
        pass
    def _errback(self, *args, **kw):
        pass

    def _call_1(self, d):
        d.callback("hello")
    def _call_2(self, d):
        d.callback("twice")
    def _err_1(self, d):
        d.errback(failure.Failure(RuntimeError()))
    def _err_2(self, d):
        d.errback(failure.Failure(RuntimeError()))

    def testAlreadyCalled_CC(self):
        d = defer.Deferred()
        d.addCallbacks(self._callback, self._errback)
        self._call_1(d)
        self.failUnlessRaises(defer.AlreadyCalledError, self._call_2, d)

    def testAlreadyCalled_CE(self):
        d = defer.Deferred()
        d.addCallbacks(self._callback, self._errback)
        self._call_1(d)
        self.failUnlessRaises(defer.AlreadyCalledError, self._err_2, d)

    def testAlreadyCalled_EE(self):
        d = defer.Deferred()
        d.addCallbacks(self._callback, self._errback)
        self._err_1(d)
        self.failUnlessRaises(defer.AlreadyCalledError, self._err_2, d)

    def testAlreadyCalled_EC(self):
        d = defer.Deferred()
        d.addCallbacks(self._callback, self._errback)
        self._err_1(d)
        self.failUnlessRaises(defer.AlreadyCalledError, self._call_2, d)


    def _count(self, linetype, func, lines, expected):
        count = 0
        for line in lines:
            if (line.startswith(' %s:' % linetype) and
                line.endswith(' %s' % func)):
                count += 1
        self.failUnless(count == expected)

    def _check(self, e, caller, invoker1, invoker2):
        # make sure the debugging information is vaguely correct
        lines = e.args[0].split("\n")
        # the creator should list the creator (testAlreadyCalledDebug) but not
        # _call_1 or _call_2 or other invokers
        self._count('C', caller, lines, 1)
        self._count('C', '_call_1', lines, 0)
        self._count('C', '_call_2', lines, 0)
        self._count('C', '_err_1', lines, 0)
        self._count('C', '_err_2', lines, 0)
        # invoker should list the first invoker but not the second
        self._count('I', invoker1, lines, 1)
        self._count('I', invoker2, lines, 0)

    def testAlreadyCalledDebug_CC(self):
        d = defer.Deferred()
        d.addCallbacks(self._callback, self._errback)
        self._call_1(d)
        try:
            self._call_2(d)
        except defer.AlreadyCalledError, e:
            self._check(e, "testAlreadyCalledDebug_CC", "_call_1", "_call_2")
        else:
            self.fail("second callback failed to raise AlreadyCalledError")

    def testAlreadyCalledDebug_CE(self):
        d = defer.Deferred()
        d.addCallbacks(self._callback, self._errback)
        self._call_1(d)
        try:
            self._err_2(d)
        except defer.AlreadyCalledError, e:
            self._check(e, "testAlreadyCalledDebug_CE", "_call_1", "_err_2")
        else:
            self.fail("second errback failed to raise AlreadyCalledError")

    def testAlreadyCalledDebug_EC(self):
        d = defer.Deferred()
        d.addCallbacks(self._callback, self._errback)
        self._err_1(d)
        try:
            self._call_2(d)
        except defer.AlreadyCalledError, e:
            self._check(e, "testAlreadyCalledDebug_EC", "_err_1", "_call_2")
        else:
            self.fail("second callback failed to raise AlreadyCalledError")

    def testAlreadyCalledDebug_EE(self):
        d = defer.Deferred()
        d.addCallbacks(self._callback, self._errback)
        self._err_1(d)
        try:
            self._err_2(d)
        except defer.AlreadyCalledError, e:
            self._check(e, "testAlreadyCalledDebug_EE", "_err_1", "_err_2")
        else:
            self.fail("second errback failed to raise AlreadyCalledError")

    def testNoDebugging(self):
        defer.setDebugging(False)
        d = defer.Deferred()
        d.addCallbacks(self._callback, self._errback)
        self._call_1(d)
        try:
            self._call_2(d)
        except defer.AlreadyCalledError, e:
            self.failIf(e.args)
        else:
            self.fail("second callback failed to raise AlreadyCalledError")


    def testSwitchDebugging(self):
        # Make sure Deferreds can deal with debug state flipping
        # around randomly.  This is covering a particular fixed bug.
        defer.setDebugging(False)
        d = defer.Deferred()
        d.addBoth(lambda ign: None)
        defer.setDebugging(True)
        d.callback(None)

        defer.setDebugging(False)
        d = defer.Deferred()
        d.callback(None)
        defer.setDebugging(True)
        d.addBoth(lambda ign: None)


class LogTestCase(unittest.TestCase):

    def setUp(self):
        self.c = []
        log.addObserver(self.c.append)

    def tearDown(self):
        log.removeObserver(self.c.append)

    def testErrorLog(self):
        c = self.c
        defer.Deferred().addCallback(lambda x: 1/0).callback(1)
# do you think it is rad to have memory leaks glyph
##        d = defer.Deferred()
##        d.addCallback(lambda x: 1/0)
##        d.callback(1)
##        del d
        c2 = [e for e in c if e["isError"]]
        self.assertEquals(len(c2), 2)
        c2[1]["failure"].trap(ZeroDivisionError)
        log.flushErrors(ZeroDivisionError)


class DeferredTestCaseII(unittest.TestCase):
    def setUp(self):
        self.callbackRan = 0

    def testDeferredListEmpty(self):
        """Testing empty DeferredList."""
        dl = defer.DeferredList([])
        dl.addCallback(self.cb_empty)

    def cb_empty(self, res):
        self.callbackRan = 1
        self.failUnlessEqual([], res)

    def tearDown(self):
        self.failUnless(self.callbackRan, "Callback was never run.")

class OtherPrimitives(unittest.TestCase):
    def _incr(self, result):
        self.counter += 1

    def setUp(self):
        self.counter = 0

    def testLock(self):
        lock = defer.DeferredLock()
        lock.acquire().addCallback(self._incr)
        self.failUnless(lock.locked)
        self.assertEquals(self.counter, 1)

        lock.acquire().addCallback(self._incr)
        self.failUnless(lock.locked)
        self.assertEquals(self.counter, 1)

        lock.release()
        self.failUnless(lock.locked)
        self.assertEquals(self.counter, 2)

        lock.release()
        self.failIf(lock.locked)
        self.assertEquals(self.counter, 2)

        self.assertRaises(TypeError, lock.run)

        firstUnique = object()
        secondUnique = object()

        controlDeferred = defer.Deferred()
        def helper(self, b):
            self.b = b
            return controlDeferred

        resultDeferred = lock.run(helper, self=self, b=firstUnique)
        self.failUnless(lock.locked)
        self.assertEquals(self.b, firstUnique)

        resultDeferred.addCallback(lambda x: setattr(self, 'result', x))

        lock.acquire().addCallback(self._incr)
        self.failUnless(lock.locked)
        self.assertEquals(self.counter, 2)

        controlDeferred.callback(secondUnique)
        self.assertEquals(self.result, secondUnique)
        self.failUnless(lock.locked)
        self.assertEquals(self.counter, 3)

        lock.release()
        self.failIf(lock.locked)

    def testSemaphore(self):
        N = 13
        sem = defer.DeferredSemaphore(N)

        controlDeferred = defer.Deferred()
        def helper(self, arg):
            self.arg = arg
            return controlDeferred

        results = []
        uniqueObject = object()
        resultDeferred = sem.run(helper, self=self, arg=uniqueObject)
        resultDeferred.addCallback(results.append)
        resultDeferred.addCallback(self._incr)
        self.assertEquals(results, [])
        self.assertEquals(self.arg, uniqueObject)
        controlDeferred.callback(None)
        self.assertEquals(results.pop(), None)
        self.assertEquals(self.counter, 1)

        self.counter = 0
        for i in range(1, 1 + N):
            sem.acquire().addCallback(self._incr)
            self.assertEquals(self.counter, i)

        sem.acquire().addCallback(self._incr)
        self.assertEquals(self.counter, N)

        sem.release()
        self.assertEquals(self.counter, N + 1)

        for i in range(1, 1 + N):
            sem.release()
            self.assertEquals(self.counter, N + 1)

    def testQueue(self):
        N, M = 2, 2
        queue = defer.DeferredQueue(N, M)

        gotten = []

        for i in range(M):
            queue.get().addCallback(gotten.append)
        self.assertRaises(defer.QueueUnderflow, queue.get)

        for i in range(M):
            queue.put(i)
            self.assertEquals(gotten, range(i + 1))
        for i in range(N):
            queue.put(N + i)
            self.assertEquals(gotten, range(M))
        self.assertRaises(defer.QueueOverflow, queue.put, None)

        gotten = []
        for i in range(N):
            queue.get().addCallback(gotten.append)
            self.assertEquals(gotten, range(N, N + i + 1))

        queue = defer.DeferredQueue()
        gotten = []
        for i in range(N):
            queue.get().addCallback(gotten.append)
        for i in range(N):
            queue.put(i)
        self.assertEquals(gotten, range(N))

        queue = defer.DeferredQueue(size=0)
        self.assertRaises(defer.QueueOverflow, queue.put, None)

        queue = defer.DeferredQueue(backlog=0)
        self.assertRaises(defer.QueueUnderflow, queue.get)

