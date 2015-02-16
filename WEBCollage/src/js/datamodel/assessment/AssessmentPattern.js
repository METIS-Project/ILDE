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

/*global IDPool, AssessmentPatternSelector */

var AssessmentPattern = function(data) {
    this.type = "assessmentpattern";
    IDPool.registerNewObject(this);
    this.data = data;
};

var AssessmentPatternManager = {

    createByName : function(name) {
        for(var i = 0; i < AssessmentPatternSelector.patterns.length; i++) {
            if(AssessmentPatternSelector.patterns[i].name == name) {
                return new AssessmentPattern(AssessmentPatternSelector.patterns[i]);
            }
        }
        return null;
    },
    getName : function(data) {
        return data.name;
    },
    getTitle : function(data) {
        return data.title;
    },
    getDescription : function(data) {
        return data.description;
    },
    definesAssessedActivity : function(data) {
        return data.assessedActivity !== undefined;
    },
    getAssessedActivityTitle : function(data) {
        return data.assessedActivity;
    },
    definesAssessmentActivity : function(data) {
        return data.assessmentActivity !== undefined;
    },
    getAssessmentActivityTitle : function(data) {
        return data.assessmentActivity;
    },
    isGeneral : function(data) {
        return data.generaltype !== undefined;
    },
    getType : function(data) {
        return data.generaltype;
    },
    isFunctionPattern : function(data) {
        return data.functiontype !== undefined;
    },
    getFunctionType : function(data) {
        return data.functiontype;
    },
    getAction : function(data, key) {
        return data.actions[key];
    }
};


AssessmentPattern.prototype.getName = function() {
    return AssessmentPatternManager.getName(this.data);
};

AssessmentPattern.prototype.getTitle = function() {
    return AssessmentPatternManager.getTitle(this.data);
};

AssessmentPattern.prototype.getDescription = function() {
    return AssessmentPatternManager.getDescription(this.data);
};

AssessmentPattern.prototype.definesAssessedActivity = function() {
    return AssessmentPatternManager.definesAssessedActivity(this.data);
};

AssessmentPattern.prototype.getAssessedActivityTitle = function() {
    return AssessmentPatternManager.getAssessedActivityTitle(this.data);
};

AssessmentPattern.prototype.definesAssessmentActivity = function() {
    return AssessmentPatternManager.definesAssessmentActivity(this.data);
};

AssessmentPattern.prototype.getAssessmentActivityTitle = function() {
    return AssessmentPatternManager.getAssessmentActivityTitle(this.data);
};

AssessmentPattern.prototype.isGeneral = function() {
    return AssessmentPatternManager.isGeneral(this.data);
};

AssessmentPattern.prototype.getType = function() {
    return AssessmentPatternManager.getType(this.data);
};

AssessmentPattern.prototype.isFunctionPattern = function() {
    return AssessmentPatternManager.isFunctionPattern(this.data);
};

AssessmentPattern.prototype.getFunctionType = function() {
    return AssessmentPatternManager.getFunctionType(this.data);
};

AssessmentPattern.prototype.getAction = function(key) {
    return AssessmentPatternManager.getAction(this.data, key);
};
