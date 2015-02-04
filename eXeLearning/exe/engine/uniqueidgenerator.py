# ===========================================================================
# eXe 
# Copyright 2004-2006, University of Auckland
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
# ===========================================================================

"""
Utility class to create (reasonably) unique identifiers
"""

import logging
import re
import time

from os.path import getmtime

log = logging.getLogger(__name__)

# ===========================================================================

class UniqueIdGenerator(object):
    """
    Utility class to create (reasonably) unique identifiers
    """
    nextId = 1

    def __init__(self, packageName, exePath):
        """Initialize the generator"""
        self.prefix  = u"eXe" 
        self.prefix += re.sub(ur"\W", u"", packageName)[-10:]

        if exePath:
            self.prefix += u"%x" % int(getmtime(exePath))


    def generate(self):
        """
        Generate the next identifier
        Identifier is made up of
        "eXe" + last 10 alphanumeric characters of packageName +
         timestamp of eXe program + current timestamp + a sequential number 
        """
        uniqueId  = self.prefix
        uniqueId += u"%x" % int(time.time()*100)
        uniqueId += u"%x" % UniqueIdGenerator.nextId
        UniqueIdGenerator.nextId += 1

        return uniqueId


# ===========================================================================
