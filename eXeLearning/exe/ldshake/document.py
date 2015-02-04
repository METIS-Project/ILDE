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
from twisted.python         import log
from exe.webui.renderable   import File
from exe.export.scormexport import ScormExport
from exe.export.websiteexport    import WebsiteExport

import random
import cgi

class LdShakeDocument(Resource):
    livepage = None

    def __init__(self, params, ldshake_root, package, webserver):
        Resource.__init__(self)
        self.properties = params
        self.bindPackage(package)
        self.ldshake_root = ldshake_root
        self.isLeaf = True
        self.client = LdShakeRestClient()
        self.webserver = webserver

    def getChild(self, name, request):
        #return self.render_GET(request)
        return self

    def render_GET(self, request):
        order = None;
        values = request.prepath[len(request.prepath)-1].split('.',2)
        if len(values) == 2:
            order = values[1]
        if len(request.postpath) > 0:
            order = request.postpath[0]

        if order is not None:
            if order == 'summary':
                result = self.render_export('zipFile')
            if order == 'scorm':
                result = self.render_export('scorm1.2')

            if result == False:
                return self.render_error(request, 'ldshake: exception saving the export '+order)
            else:
                return result

        filename = self.package.name + '.elp'
        fullpath = self.webserver.config.dataDir/filename
        try:
            self.package.save(fullpath)
        except:
            return self.render_error(request, 'ldshake: exception saving the package')

        return File(fullpath)

    def render_error(self, request, msg='error'):
        log.err(msg);
        #log.msg(msg);
        request.setResponseCode(500)
        request.setHeader('Content-Type','text/plain')
        return msg

    def render_export(self, format):
        filename = self.package.name + '_' + str(random.randrange(1,999999))
        fullpath = self.webserver.config.dataDir/filename
        stylesDir  = self.webserver.config.stylesDir/self.package.style

        try:
            if format == 'zipFile':
                websiteExport = WebsiteExport(self.webserver.config, stylesDir, fullpath + '.zip')
                websiteExport.exportZip(self.package)
            if format == 'scorm1.2':
                scormExport = ScormExport(self.webserver.config, stylesDir, fullpath + '.zip', 'scorm1.2')
                scormExport.export(self.package)
        except:
            return False
        return File(fullpath + '.zip')

    def bindPackage(self, package):
        self.package = package

    def bindLivePage(self, livepage):
        self.livepage = livepage


class LdShakeRestClient:

    message = ''

    def alert(self, message):
        self.message = message