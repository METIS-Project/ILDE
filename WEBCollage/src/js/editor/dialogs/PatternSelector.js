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

var PatternSelector = {

    patterns : new Array(),

    blankphase : {
        id : "blankphase"
    },
    blankphase2 : {
        id : "blankphase2"
    },

    categories : new Array(),

    callback : null,

    init : function() {
        this.dlg = dijit.byId("SelectorDialog");
        this.queryData();
    },
    showHelp : function(patternid) {
        window.open("patterns/help.php?clfp=" + patternid, patternid);
    },
    open : function(callback) {
        this.callback = callback;
        this.updateClfpList();
        this.dlg.show();
    },
    queryData : function() {
        var bindArgs = {
            url : "patterns/categories.php",
            handleAs : "json",
            contentType : "application/json; charset=utf-8",
            load : function(data, args) {
                PatternSelector.loadCategories(data, args);
            }
        };
        dojo.xhrGet(bindArgs);
        bindArgs = {
            url : "patterns/clfps.php",
            handleAs : "json",
            contentType : "application/json; charset=utf-8",
            load : function(data, args) {
                PatternSelector.loadPatternList(data, args);
            }
        };
        dojo.xhrGet(bindArgs);
    },
    loadCategories : function(data) {
        this.data = data;
        var element = dojo.byId("SelectorActitudinalContent");
        for(var i in data.actobjs) {
            var id = "SelectorActitudinalContent_" + i;
            PatternSelector.createCheckBox(id, element, data.actobjs[i].name, data.actobjs[i].id);
        }
        element = dojo.byId("SelectorProceduralContent");
        for(i in data.procobjs) {
            id = "SelectorProceduralContent_" + i;
            PatternSelector.createCheckBox(id, element, data.procobjs[i].name, data.procobjs[i].id);
        }
        element = dojo.byId("SelectorProblemContent");
        for(i in data.problems) {
            id = "SelectorProblemContent_" + i;
            PatternSelector.createCheckBox(id, element, data.problems[i].name, data.problems[i].id);
        }

        var complexityButtons = ["selectorcomplexity_any", "selectorcomplexity_low", "selectorcomplexity_medium", "selectorcomplexity_high"];

        for(i in complexityButtons) {
            var radio = dijit.byId(complexityButtons[i]);
            radio.onClick = function() {
                PatternSelector.difficulty = this.getValue();
                PatternSelector.updateClfpList();
            };
        }
    },
    createCheckBox : function(id, where, label, category) {
        var checkbox = CheckBoxManager.createCheckBox(id, where, label, function(state) {
            PatternSelector.changeCategoryStatus(this.category, state);
        });

        checkbox.category = category;
    },
    loadPatternList : function(data, args) {
        this.patterns = data.patterns;
    },
    updateClfpList : function() {
        var style = this.search();

        dojo.query("#SelectorPatternList > table").orphan();
        var container = document.getElementById("SelectorPatternList");
        var table = container.appendChild(document.createElement("table"));
        dojo.addClass(table, "patternList");

        var selectLabel = i18n.get("common.select");

        for(var i = 0; i < this.patterns.length; i++) {
            var row = table.appendChild(document.createElement("tr"));
            dojo.addClass(row, "patternListRow");

            var cell = row.appendChild(document.createElement("td"));
            dojo.addClass(cell, "patternListCell");

            dojo.style(cell, {
                fontSize : style.size[this.patterns[i].id] + "px",
                opacity : style.transparency[this.patterns[i].id]
            });

            cell.innerHTML = this.patterns[i].name;

            var helpCell = row.appendChild(document.createElement("td"));
            dojo.addClass(helpCell, "patternListCellSelectable");
            var img = helpCell.appendChild(document.createElement("img"));
            img.src = "images/icons/help.png";
            dojo.connect(helpCell, "onclick", {
                patternid : this.patterns[i].id
            }, function() {
                PatternSelector.showHelp(this.patternid);
            });
            var buttonCell = row.appendChild(document.createElement("td"));
            dojo.addClass(buttonCell, "patternListCell");

            var selectButton = new dijit.form.Button({
                label : selectLabel
            });
            buttonCell.appendChild(selectButton.domNode);
            dojo.connect(selectButton, "onClick", {
                patternIndex : i
            }, function() {
                PatternSelector.select(this.patternIndex);
            });
        }
    },
    changeCategoryStatus : function(category, state) {
        this.categories[category] = state;
        this.updateClfpList();
    },
    search : function() {
        var points = new Array();
        for(var i in this.patterns) {
            var pattern = this.patterns[i];
            points[pattern.id] = 5;
            for(var j in pattern.cat) {
                if(this.categories[pattern.cat[j]]) {
                    points[pattern.id]++;
                }
            }

            if(this.difficulty == pattern.cat[0]) {
                points[pattern.id] += 3;
            }
        }

        var total = 0;
        var max = 0;
        for(i in points) {
            total += points[i];
            if(points[i] > max) {
                max = points[i];
            }
        }

        var k = 12. * this.patterns.length / total;
        var tk = 1.0 / max;
        var transparency = new Array();

        for(i in points) {
            transparency[i] = points[i] * tk;
            points[i] = points[i] * k;
        }

        return {
            size : points,
            transparency : transparency
        };
    },
    cancel : function() {
        this.close();
    },
    selectBlankPhase : function() {
        this.close();
        this.callback(this.blankphase);
    },
    selectBlank2Phase : function() {
        this.close();
        this.callback(this.blankphase2);
    },
    select : function(patternIndex) {
        this.close();
        this.callback(this.patterns[patternIndex]);
    },
    close : function() {
        dijit.byId("SelectorDialog").hide();
    }
};
