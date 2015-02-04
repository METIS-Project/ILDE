# -*- test-case-name: twisted.test.test_process.ProcessTestCase.testStdio -*-

# Copyright (c) 2001-2004 Twisted Matrix Laboratories.
# See LICENSE for details.

"""Standard input/out/err support.

API Stability: unstable (pending deprecation in favor of a reactor-based API)

This module exposes one name, StandardIO, which is a factory that takes an
IProtocol provider as an argument.  It connects that protocol to standard input
and output on the current process.

It should work on any UNIX and also on Win32 (with some caveats: due to
platform limitations, it will perform very poorly on Win32).

Future Plans:

    support for stderr, perhaps
    Rewrite to use the reactor instead of an ad-hoc mechanism for connecting
        protocols to transport.


Maintainer: U{James Y Knight <mailto:foom@fuhm.net>}
"""

from twisted.python.runtime import platform

if platform.isWindows():
    from twisted.internet._win32stdio import StandardIO
else:
    from twisted.internet._posixstdio import StandardIO
