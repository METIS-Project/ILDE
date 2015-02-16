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


var SelectLOsDialog = {

    iDontNeedThese: null,
    
    selectedLOs: null,
    
    open: function(iDontNeedThese, id){
        this.iDontNeedThese = iDontNeedThese;
        this.selectedLOs = new Array();
        this.load(id);
        DialogModaler.pushPopups();
    },
    
    load: function(id){
        var fullId = "SelectLOsList_" + id;
        dijit.byId(fullId).destroyDescendants();
        
        var content = dojo.byId(fullId);
        content.innerHTML = "";
        
        var callback = function(state){
            SelectLOsDialog.change(this.loid, state);
        };
        
        for (var i in LearningDesign.data.learningObjectives) {
            var lo = LearningDesign.data.learningObjectives[i];
            if (this.iDontNeedThese.indexOf(lo.id) < 0) {
                var checkbox = CheckBoxManager.createCheckBox(fullId + lo.id, content, lo.title, callback);
                checkbox.loid = lo.id;
            }
        }
    },
    
    change: function(loid, state){
        if (state) {
            if (this.selectedLOs.indexOf(loid) < 0) {
                this.selectedLOs.push(loid);
            }
        }
        else {
            var index = this.selectedLOs.indexOf(loid);
            if (index >= 0) {
                this.selectedLOs.splice(index, 1);
            }
        }
    }
};
