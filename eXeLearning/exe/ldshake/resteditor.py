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
import threading
import random
import cgi

class LdshakeRestEditor(Resource):

    documents = {}
    id_count = 1
    id_count_lock = threading.Lock()

    def __init__(self, webserver):
        Resource.__init__(self)
        self.exe_root   = webserver.root
        self.config     = webserver.config
        self.webserver  = webserver

    def getChild(self, name, request):
        if name == '':
            return self
        else:
            values = name.split('.',2)
            if len(values) == 2:
                name = values[0]

            return self.children.get(name)

    def render_GET(self, request):
        return 1

    def render_POST(self, request):
        #check the parameters
        required_headers = ['ldshake_frame_origin','name','sectoken']

        for r_h in required_headers:
            if r_h not in request.args:
                return 'error'

        #get an id and increment the counter
        self.id_count_lock.acquire()
        id = self.id_count;
        self.id_count += 1
        self.id_count_lock.release()

        package_name = request.args['sectoken'][0]

        if 'document' not in request.args:
            package = Package(package_name)
        else:
            fullpath = '/var/local/exelearning/' + package_name + '_' + str(random.randrange(1,999999)) + '.elp'
            try:
                elp_file = open(fullpath, "wb")
                elp_file.write(request.args['document'][0])
                elp_file.close()
                package = Package.load(fullpath, True, None)
                package.set_name(package_name)
            except:
                return 'error'

        self.putChild(str(id), LdShakeDocument({
            'name':     request.args['name'][0],
            'sectoken': request.args['sectoken'][0],
            'ldshake_frame_origin': request.args['ldshake_frame_origin'][0]
        }, self, package, self.webserver))

        #return the resource URL
        return request.prePathURL() + str(id) + '/'