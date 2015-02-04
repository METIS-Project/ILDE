#/*********************************************************************************
# * LdShake is a platform for the social sharing and co-edition of learning designs
# * Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
# *
# * (Contributors, alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P.
# *
# * This program is free software; you can redistribute it and/or modify it under
# * the terms of the GNU Affero General Public License version 3 as published by the
# * Free Software Foundation with the addition of the following permission added
# * to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK
# * IN WHICH THE COPYRIGHT IS OWNED BY Universitat Pompeu Fabra (UPF), Barcelona,
# * UPF DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
# *
# * This program is distributed in the hope that it will be useful, but WITHOUT
# * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
# * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
# *
# * You should have received a copy of the GNU Affero General Public License along with
# * this program; if not, see http://www.gnu.org/licenses.
# *
# * You can contact the Interactive Technologies Group (GTI), Universitat Pompeu Fabra, Barcelona.
# * headquarters at c/Roc Boronat 138, Barcelona, or at email address davinia.hernandez@upf.edu
# *
# * The interactive user interfaces in modified source and object code versions
# * of this program must display Appropriate Legal Notices, as required under
# * Section 5 of the GNU Affero General Public License version 3.
# *
# * In accordance with Section 7(b) of the GNU Affero General Public License version 3,
# * these Appropriate Legal Notices must retain the display of the "Powered by
# * LdShake" logo with a link to the website http://ldshake.upf.edu.
# * If the display of the logo is not reasonably feasible for
# * technical reasons, the Appropriate Legal Notices must display the words
# * "Powered by LdShake" with the link to the website http://ldshake.upf.edu.
# ********************************************************************************/

from twisted.web.resource   import Resource
from exe.engine.package     import Package
from exe.engine.path        import TempDirPath
from exe.webui.renderable   import File
from exe                    import globals as G

from exe.ldshake.document   import LdShakeDocument
import threading
import random
import re
import cgi
import codecs
import json
import mimetypes
import os.path

class PreviewFolder(Resource):

    def __init__(self):
        Resource.__init__(self)
        self.sessiontmpdir = {}
        #self.isLeaf = True

    def getChild(self, name, request):
        return self

    def getFolder(self,uid):
        tmpfolder = G.application.webServer.getSessionFolder(uid)
        tmpfolder = tmpfolder.joinpath('previews')

        try:
            if not tmpfolder.isdir():
                tmpfolder.mkdir()
        except:
            return 'error'

        return str(tmpfolder)

    def render_GET(self, request):
        if len(request.prepath) < 2:
            return 'error'
        #check the parameters
        tmppath     = unicode(str(self.getFolder(request.getSession().uid)), 'utf-8')
        filename    = unicode(request.prepath[1], 'utf-8')

        if os.path.isfile(tmppath + u'/' + filename) == False:
            return 'error'
        mime = mimetypes.guess_type(filename)
        request.setHeader('Content-Type', mime)
        return File(tmppath + u'/' + filename)