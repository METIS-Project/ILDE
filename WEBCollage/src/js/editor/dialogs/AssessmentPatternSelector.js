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


var AssessmentPatternSelector = {

    patterns: new Array(),
    
    categories: new Array(),
    
    callback: null,
    
    dlg: null,
    
   
    init: function(){
        this.dlg = dijit.byId("AssessmentSelectorDialog");
        this.queryData();
    },
    
    selected: function(pattern){
        this.selectedPattern = pattern;
        this.updatePatternList();
    },
    
    showHelp: function(patternname) {
        window.open("patterns/help.php?pattern=" + patternname, patternname);
    },
    
    open: function(callback, reference, whatfor, exclude){
        this.callback = {
            callbackfunction: callback,
            reference: reference
        };
        this.exclude = exclude;
        this.whatfor = whatfor;
        this.updatePatternList();
        this.dlg.show();
    },
    
    queryData: function(){
        var bindArgs = {
            url: "patterns/patterns.php",
            handleAs: "json",
            contentType : "application/json; charset=utf-8",
            load: function(data, args){
                AssessmentPatternSelector.loadPatternList(data, args);
            }
        };
        dojo.xhrGet(bindArgs);
    },
    
    loadPatternList: function(data){
        this.patterns = data.patterns;
    },
    
    updatePatternList: function(){
        dojo.query("#SelectorAssessmentPatternList > table").orphan();
        var container = document.getElementById("SelectorAssessmentPatternList");
        var table = container.appendChild(document.createElement("table"));
        dojo.addClass(table, "patternList");
        
        var selectLabel = i18n.get("common.select");

        for (var i = 0; i < this.patterns.length; i++) {
            var selectable = this.selectable(this.patterns[i]);
            if (selectable > 0) {
                var row = table.appendChild(document.createElement("tr"));
                dojo.addClass(row, "patternListRow");
	        
                var cell = row.appendChild(document.createElement("td"));
                dojo.addClass(cell, "patternListCell");
	        
                cell.innerHTML = this.patterns[i].title;
                var description = cell.appendChild(document.createElement("div"));
                dojo.addClass(description, "patternListDescription");
                var type = description.appendChild(document.createElement("span"));
                dojo.addClass(type, this.patterns[i].generaltype == "analysis" ? "patternListTechique" : "patternListConfiguration");
                type.innerHTML = i18n.get(this.patterns[i].generaltype == "analysis" ? "selector.assessment.technique" : "selector.assessment.configuration");
                description.appendChild(document.createTextNode(this.patterns[i].description));
	        
                var helpCell = row.appendChild(document.createElement("td"));
                dojo.addClass(helpCell, "patternListCellSelectable");
                helpCell.appendChild(document.createElement("img")).src = "images/icons/help.png";
                dojo.connect(helpCell, "onclick", {
                    patternname: AssessmentPatternSelector.patterns[i].name
                }, function() {
                    AssessmentPatternSelector.showHelp(this.patternname);
                });
	        
                var buttonCell = row.appendChild(document.createElement("td"));
                dojo.addClass(buttonCell, "patternListCell");

                if (selectable > 1) {
                    var selectButton = new dijit.form.Button({
                        label: selectLabel
                    });
                    buttonCell.appendChild(selectButton.domNode);
                    dojo.connect(selectButton, "onClick", {
                        patternIndex: i
                    }, function() {
                        AssessmentPatternSelector.select(this.patternIndex);
                    });
                }
            }
        }
    },
    
    selectable: function(pattern) {
        var name = AssessmentPatternManager.getName(pattern);
        var excluded = false;
        for (var i = 0; i < this.exclude.length; i++) {
            if (name == this.exclude[i].getName()) {
                excluded = true;
            }
        }
    	
        if (this.whatfor == "assessment" || this.whatfor == "assessed") {
            return pattern.generaltype == "analysis" ? (excluded ? 1 : 2) : -1;
        } else {
            return pattern.generaltype != "analysis" ? (excluded ? 1 : 2) : -1;
        }
    },
    
    cancel: function(){
        this.close();
    },
    
    select: function(patternIndex){
        this.close();
        this.callback.callbackfunction(this.callback.reference, this.patterns[patternIndex]);
    },
    
    close: function(){
        this.dlg.hide();
    }
};

