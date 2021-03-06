from twisted.scripts import trial
from twisted.trial import unittest, runner
from twisted.trial.test import packages
from twisted.python import util
import os, re, sys, StringIO


def runTrial(*args):
    from twisted.trial import reporter
    config = trial.Options()
    config.parseOptions(args)
    output = StringIO.StringIO()
    myRunner = runner.TrialRunner(
        reporter.VerboseTextReporter,
        stream=output,
        workingDirectory=config['temp-directory'])
    suite = trial._getSuite(config)
    result = myRunner.run(suite)
    return output.getvalue()


class TestImportErrors(packages.PackageTest):
    """Actually run trial as if on the command line and check that the output
    is what we expect.
    """

    debug = False
    parent = "_testImportErrors"

    def setUp(self):
        packages.PackageTest.setUp(self, self.parent)

    def tearDown(self):
        packages.PackageTest.tearDown(self, self.parent)

    def runTrial(self, *args):
        oldPath = sys.path[:]
        sys.path.append(self.parent)
        try:
            return self.runTrialPure(*args)
        finally:
            sys.path = oldPath

    def runTrialPure(self, *args):
        return runTrial('--temp-directory', self.mktemp(), *args)
 
    def _print(self, stuff):
        print stuff
        return stuff

    def failUnlessIn(self, container, containee, *args, **kwargs):
        # redefined to be useful in callbacks
        super(TestImportErrors, self).failUnlessIn(
            containee, container, *args, **kwargs)
        return container

    def failIfIn(self, container, containee, *args, **kwargs):
        # redefined to be useful in callbacks
        super(TestImportErrors, self).failIfIn(
            containee, container, *args, **kwargs)
        return container

    def test_trialRun(self):
        self.runTrial()

    def test_nonexistentModule(self):
        d = self.runTrial('twisted.doesntexist')
        self.failUnlessIn(d, '[ERROR]')
        self.failUnlessIn(d, 'twisted.doesntexist')
        return d

    def test_nonexistentPackage(self):
        d = self.runTrial('doesntexist')
        self.failUnlessIn(d, 'doesntexist')
        self.failUnlessIn(d, 'ValueError')
        self.failUnlessIn(d, '[ERROR]')
        return d

    def test_nonexistentPackageWithModule(self):
        d = self.runTrial('doesntexist.barney')
        self.failUnlessIn(d, 'doesntexist.barney')
        self.failUnlessIn(d, 'ValueError')
        self.failUnlessIn(d, '[ERROR]')
        return d

    def test_badpackage(self):
        d = self.runTrial('badpackage')
        self.failUnlessIn(d, '[ERROR]')
        self.failUnlessIn(d, 'badpackage')
        self.failIfIn(d, 'IOError')
        return d

    def test_moduleInBadpackage(self):
        d = self.runTrial('badpackage.test_module')
        self.failUnlessIn(d, "[ERROR]")
        self.failUnlessIn(d, "badpackage.test_module")
        self.failIfIn(d, 'IOError')
        return d

    def test_badmodule(self):
        d = self.runTrial('package.test_bad_module')
        self.failUnlessIn(d, '[ERROR]')
        self.failUnlessIn(d, 'package.test_bad_module')
        self.failIfIn(d, 'IOError')
        self.failIfIn(d, '<module')
        return d

    def test_badimport(self):
        d = self.runTrial('package.test_import_module')
        self.failUnlessIn(d, '[ERROR]')
        self.failUnlessIn(d, 'package.test_import_module')
        self.failIfIn(d, 'IOError')
        self.failIfIn(d, '<module')
        return d

    def test_recurseImport(self):
        d = self.runTrial('package')
        self.failUnlessIn(d, '[ERROR]')
        self.failUnlessIn(d, 'test_bad_module')
        self.failUnlessIn(d, 'test_import_module')
        self.failIfIn(d, '<module')
        self.failIfIn(d, 'IOError')
        return d

    def test_recurseImportErrors(self):
        d = self.runTrial('package2')
        self.failUnlessIn(d, '[ERROR]')
        self.failUnlessIn(d, 'package2')
        self.failUnlessIn(d, 'test_module')
        self.failUnlessIn(d, "No module named frotz")
        self.failIfIn(d, '<module')
        self.failIfIn(d, 'IOError')
        return d

    def test_nonRecurseImportErrors(self):
        d = self.runTrial('-N', 'package2')
        self.failUnlessIn(d, '[ERROR]')
        self.failUnlessIn(d, "No module named frotz")
        self.failIfIn(d, '<module')
        return d

    def test_regularRun(self):
        d = self.runTrial('package.test_module')
        self.failIfIn(d, '[ERROR]')
        self.failIfIn(d, 'IOError')
        self.failUnlessIn(d, 'OK')
        self.failUnlessIn(d, 'PASSED (successes=1)')
        return d
    
    def test_filename(self):
        d = self.runTrialPure(os.path.join(self.parent,
                                           'package', 'test_module.py'))
        self.failIfIn(d, '[ERROR]')
        self.failIfIn(d, 'IOError')
        self.failUnlessIn(d, 'OK')
        self.failUnlessIn(d, 'PASSED (successes=1)')
        return d

    def test_dosFile(self):
        ## XXX -- not really an output test, more of a script test
        d = self.runTrialPure(os.path.join(self.parent,
                                           'package', 'test_dos_module.py'))
        self.failIfIn(d, '[ERROR]')
        self.failIfIn(d, 'IOError')
        self.failUnlessIn(d, 'OK')
        self.failUnlessIn(d, 'PASSED (successes=1)')
        return d
