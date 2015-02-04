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
from exe.ldshake.document   import LdShakeDocument
import cgi

class LdshakeGUI(Resource):

    def __init__(self, webserver):
        Resource.__init__(self)
        self.exe_root = webserver.root

    def getChild(self, name, request):
        return self.render_GET(request)

    def render_GET(self, request):
        if 'document_id' not in request.args:
            return None

        document_resource = self.exe_root\
            .children.get('ldshake')\
            .children.get('ldsdoc')\
            .children.get(request.args['document_id'][0])

        package = document_resource.package

        session = request.getSession()
        session.packageStore.addPackage(package)
        document_resource.bindPackage(package)

        # Create session temp folder
        self.exe_root.webServer.getSessionFolder(session.uid)
        self.exe_root.packagesession[package.name] = session.uid

        live_page = self.exe_root.bindNewPackage(package, session)
        live_page.rest_properties = document_resource.properties
        document_resource.bindLivePage(live_page)

        #log.info("Created a new package name="+ package.name)

        # Tell the web browser to show it
        request.redirect('/' + package.name.encode('utf8'))

        return ''