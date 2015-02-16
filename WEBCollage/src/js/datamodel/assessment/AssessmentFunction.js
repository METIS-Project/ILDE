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

/*global i18n, IDPool */

var AssessmentFunction = function(type) {
    this.type = "assessmentfunction";
    this.subtype = type;
    IDPool.registerNewObject(this);
};

AssessmentFunction.prototype.getTitle = function() {
    switch(this.subtype) {
        case "summative":
            return i18n.get("assessment.edit.functions.summative");
        case "formative":
            return i18n.get("assessment.edit.functions.formative");
        case "diagnosis":
            return i18n.get("assessment.edit.functions.diagnosis");
        default:
            return "";
    }
};

AssessmentFunction.prototype.getLinkedActivity = function() {
    return IDPool.getObject(this.link);
};