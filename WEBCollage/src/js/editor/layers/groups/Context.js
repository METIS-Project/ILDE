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
