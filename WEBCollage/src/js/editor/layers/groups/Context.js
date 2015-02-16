/**
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Web Collage.

Web Collage is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Web Collage is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*global IDPool, DesignInstance, ClfpsCommon */
var Context = {
    isClassSelected : function() {
        return DesignInstance.data.participants.length > 0;
    },
    getStudentRoles : function(actId) {
        var roles = [];
        var act = IDPool.getObject(actId);
        for(var i = 0; i < act.learners.length; i++) {
            roles.push(IDPool.getObject(act.learners[i]));
        }
        return roles;
    },
    getGroupNumber : function(roleId, instanceId) {
        var groups = instanceId ? DesignInstance.instanciasGrupoProcedenInstancia(roleId, instanceId) : DesignInstance.instanciasGrupo(roleId);
        return groups.length;
    },
    getGroupComposition : function(roleId, instanceId) {
        var participants = [];

        var groups = instanceId ? DesignInstance.instanciasGrupoProcedenInstancia(roleId, instanceId) : DesignInstance.instanciasGrupo(roleId);

        for(var i = 0; i < groups.length; i++) {
            participants[i] = [];
            for(var j = 0; j < groups[i].participants.length; j++) {
                participants[i].push(groups[i].participants[j]);
            }
        }
        return participants;
    },
    getAvailableStudents : function(clfpId, instanceId) {
        var clfp = IDPool.getObject(clfpId);
        //Si depende de una instancia son todos los asignados a aquellas instancia
        var available = instanceId ? IDPool.getObject(instanceId).participants : ClfpsCommon.obtainAvailableStudentsClfpMain(clfp);
        return available;
    }
};
