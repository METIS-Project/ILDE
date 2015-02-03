# -*- test-case-name: twisted -*-

# Copyright (c) 2001-2004 Twisted Matrix Laboratories.
# See LICENSE for details.


"""
Twisted: The Framework Of Your Internet.
"""

# Ensure the user is running the version of python we require.
import sys
if not hasattr(sys, "version_info") or sys.version_info < (2,3):
    raise RuntimeError("Twisted requires Python 2.3 or later.")
del sys

# Ensure zope.interface is installed
try:
    from zope.interface import Interface
    del Interface
except ImportError:
    raise ImportError, "you need zope.interface installed (http://zope.org/Products/ZopeInterface/)"

# Ensure compat gets imported
from twisted.python import compat
del compat

# setup version
__version__ = '2.2.0'
