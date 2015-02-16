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

/**
 * @author Eloy
 */
var FlowTree = {

    buildStructureTree: function(treeId, rootId, how){
        TreeManager.emptyTree(treeId);
        this.buildStructureTreeAct(treeId, LearningDesign.data.flow, rootId, how);
    },
    
    buildStructureTreeAct: function(treeId, act, parent, how){
        if (act.type == "act") {
            TreeManager.addItem(treeId, act.title, act.id, parent, "actIcon");
            if (how.learningActivities || how.learners) {
                for (var i in act.learners) {
                    var learner = IDPool.getObject(act.learners[i]);
                    var learnerId = learner.id + ":" + act.id;
                    
                    TreeManager.addItem(treeId, learner.title, learnerId, act.id, "learnerIcon");
                    if (how.learningActivities) {
                        var las = act.getActivitiesForRoleId(learner.id);
                        for (var j in las) {
                            TreeManager.addItem(treeId, las[j].title, las[j].id, learnerId, "learningActivityIcon");
                        }
                    }
                }
            }
            if (how.supportActivities || how.staff) {
                for (i in act.staff) {
                    var staff = IDPool.getObject(act.staff[i]);
                    var staffId = staff.id + ":" + act.id;
                    
                    TreeManager.addItem(treeId, staff.title, staffId, act.id, "teacherIcon");
                    if (how.supportActivities) {
                        var sas = act.getActivitiesForRoleId(staff.id);
                        for (j in sas) {
                            TreeManager.addItem(treeId, sas[j].title, sas[j].id, staffId, "supportActivityIcon");
                        }
                    }
                }
            }
            
            return;
        } else { /* Now it must be a clfpAct */
            if (act.title && act.clfps.length == 1) {
                var label = act.title + " (" + act.clfps[0].title + ")";
                TreeManager.addItem(treeId, label, act.clfps[0].id, parent, act.clfps[0].patternid + "ClfpIcon");
                parent = act.clfps[0].id;
                var flow = act.clfps[0].getFlow();
                for (i in flow) {
                    this.buildStructureTreeAct(treeId, flow[i], parent, how);
                }
            } else {
                if (act.title) {
                    TreeManager.addItem(treeId, act.title, act.id, parent, "actIcon");
                    parent = act.id;
                }
                
                for (i in act.clfps) {
                    var clfp = act.clfps[i];
                    TreeManager.addItem(treeId, clfp.title, clfp.id, parent, clfp.patternid + "ClfpIcon");
                    flow = clfp.getFlow();
                    for (j in flow) {
                        this.buildStructureTreeAct(treeId, flow[j], clfp.id, how);
                    }
                }
            }
        }
    }
};
