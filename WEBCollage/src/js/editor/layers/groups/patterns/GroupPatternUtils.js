var GroupPatternUtils = {

    getFirstStudentRoleGroupNumber : function(instanceId, actId) {
        var roleId = Context.getStudentRoles(actId)[0].id;
        var groupCount = Context.getGroupNumber(roleId, instanceId);
        return new GroupNumber(roleId, groupCount);
    },

    getGroupParticipants : function(instanceId, actId) {
        var groupId = Context.getStudentRoles(actId)[0].id;
        var participants = Context.getGroupComposition(groupId, instanceId);
        return new GroupParticipants(participants);
    },

    /********************************/

    fixParticipantMovements : function(students, result, previousProposal, movements, numberExplanation) {
        if (movements.length == 0) {
            this.appendExplanation(result, numberExplanation);
        } else {
            result.ok = false;
            if (previousProposal) {
                result.fixable = false;
            } else {
                result.fixable = true;
                result.proposal.moveFromTo(movements, students);
            }
            this.addAlert(result, numberExplanation);
        }
    },

    compareExactParticipantNumber : function(group, index, number, movements) {
        if (group.length != number) {
            movements.push({
                group : index,
                count : number - group.length
            });
        }

        return number;
    },

    compareMinParticipantNumber : function(group, index, min, movements) {
        if (group.length < min) {
            movements.push({
                group : index,
                count : min - group.length
            });
            return min;
        } else {
            return group.length;
        }
    },
    compareMaxParticipantNumber : function(group, index, max, movements) {
        if (group.length > max) {
            movements.push({
                group : index,
                count : max - group.length
            });
            return max;
        } else {
            return group.length;
        }
    },
    compareMinMaxParticipantNumber : function(group, index, min, max, movements) {
        if (group.length < min) {
            movements.push({
                group : index,
                count : min - group.length
            });
            return min;
        } else if (group.length > max) {
            movements.push({
                group : index,
                count : max - group.length
            });
            return max;
        } else {
            return group.length;
        }
    },
    /********************************/

    /**
     * private
     */
    fixGroupNumber : function(result, previousProposal, number, explanation) {
        result.ok = false;
        //if (previousProposal) {
        //Comprobar si existe una propuesta previa en la que el número de grupos no coincide con la propuesta actual
        if (previousProposal && result.proposal.number!=number) {
            result.fixable = false;
        } else {
            result.fixable = true;
            result.proposal.setNumber(number);
        }
        this.addAlert(result, explanation);
    },
    /**
     * Works for acts with only one student role
     * @param {Object} instanceId
     * @param {Object} result
     * @param {Object} previousProposal
     * @param {Object} actId
     * @param {Object} number
     * @param {Object} numberExplanation
     */
    compareExactGroupNumber : function(instanceId, result, previousProposal, actId, number, numberExplanation) {
        var needFix = false;
        var studentRoles = Context.getStudentRoles(actId);
        //Puede haber varios roles en cada fase. Para cada uno de ellos el número de instacias será el mismo
        for (var sr = 0; sr < studentRoles.length; sr++){
            var groupId = studentRoles[sr].id;
            var clfp = DesignInstance.clfpRole(studentRoles[sr]);
            var instances = ClfpsCommon.getInstances(clfp, groupId, LearningFlow);
            //if (instances.length != result.proposal.getNumber()){
            //Comprobar que el número de instancias del grupo coincide con el especificado
            if (instances.length != number){
                needFix = true;
            }
        }
            
            
        //if (number == result.proposal.getNumber()) {
        if (needFix == false){
            this.appendExplanation(result, numberExplanation);
        } else {
            this.fixGroupNumber(result, previousProposal, number, numberExplanation);
        }
    },

    compareMinGroupNumber : function(instanceId, result, previousProposal, actId, min, minExplanation) {
        if (result.proposal.getNumber() < min) {
            var text = i18n.getReplaced2("groupPattern.general.lessthan", minExplanation, min);
            this.fixGroupNumber(result, previousProposal, min, text);
        } else {
            this.appendExplanation(result, minExplanation);
        }
    },
    compareMinMaxPrefGroupNumber : function(instanceId, result, previousProposal, actId, min, max, preferred, rangeExplanation, prefExplanation) {
        if (previousProposal) {
            var expl = null;
            if (result.proposal.getNumber() > max) {
                expl = i18n.getReplaced2("groupPattern.general.morethan", rangeExplanation, max);
            } else if (result.proposal.getNumber() < min) {
                expl = i18n.getReplaced2("groupPattern.general.lessthan", rangeExplanation, min);
            }

            if (expl) {
                result.ok = false;
                result.fixable = false;
                this.addAlert(result, expl);
            } else {
                this.appendExplanation(result, rangeExplanation);
            }
        } else {
            this.compareExactGroupNumber(instanceId, result, previousProposal, actId, preferred, prefExplanation);
        }
    },

    /***********************************/

    appendExplanation : function(result, explanation) {
        result.why = (result.why.length > 0) ? (result.why + "<br/>" + explanation) : explanation;
    },

    addAlert : function(result, alert, help) {
        this.addAlertNoExplanation(result, alert, help);
        this.appendExplanation(result, alert);
    },
    addAlertNoExplanation : function(result, alert, help) {
        result.alerts.push({
            text : alert,
            help : help ? help : ""
        });
    }
};
