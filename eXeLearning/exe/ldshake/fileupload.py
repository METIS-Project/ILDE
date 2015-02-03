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
from exe                    import globals as G

from exe.ldshake.document   import LdShakeDocument
import threading
import random
import re
import cgi
import codecs
import json
import os

class UploadFolder(Resource):

    def __init__(self):
        Resource.__init__(self)
        self.tmpdir = TempDirPath()
        #self.isLeaf = True

    def getChild(self, name, request):
        #return self.render_GET(request)
        return self

    def render_POST(self, request):
        #check the parameters
        required_headers = ['file','name']

        for r_h in required_headers:
            if r_h not in request.args:
                return 'error'

        if len(request.args['name']) != len(request.args['file']):
            return 'error'

        nelems = len(request.args['name'])
        filenamelist = []

        for nfile in xrange(0, nelems):
            filename = self.sanitize_filename(request.args['name'][nfile])
            filenamelist.append(self.sanitize_filename(request.args['name'][nfile]))

            if filename == False:
                return 'error'

            if 'preview' not in request.args:
                session = request.getSession()
                tmpdir =  G.application.webServer.getSessionFolder(session.uid)
                tmpdir = unicode(str(tmpdir), 'utf-8')
            else:
                root = G.application.webServer.root
                preview = root.children.get('previews')
                tmpdir = unicode(str(preview.getFolder(request.getSession().uid)), 'utf-8')

            fullpath = tmpdir + u'/' + filename

            try:
                upload_fd = open(fullpath, "wb")
                upload_fd.write(request.args['file'][nfile])
                upload_fd.close()
            except:
                return 'error'

        request.setHeader('Content-Type', 'application/json')

        if len(filenamelist) == 1:
            filenamelist = filenamelist[0]

        return json.dumps({'filename':filenamelist})

    def sanitize_filename(self, filename):
        filename = unicode(filename, 'utf-8')

        filename = re.sub(
            r'[^\w\.]+',
            u'_',
            filename,
            flags = re.U)

        filename = re.sub(
            r'^\.+',
            u'_',
            filename,
            flags = re.U)

        if len(filename) < 1:
            filename = False

        return filename
