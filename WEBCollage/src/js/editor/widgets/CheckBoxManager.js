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
var CheckBoxManager = {
	createCheckBox : function(id, where, label, whatToDoOnChange) {
		var newElement = where.appendChild(document.createElement("div"));
		var checkbox = new dijit.form.CheckBox( {
			id : id,
			name : id,
			onChange : whatToDoOnChange
		}, newElement);
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", id);
		labelElement.innerHTML = " " + label;
		where.appendChild(labelElement);
		where.appendChild(document.createElement("br"));

		return checkbox;
	},

	createRadioButton : function(value, name, where, label, whatToDoOnChange) {
		var newElement = where.appendChild(document.createElement("div"));
		var checkbox = new dijit.form.RadioButton( {
			value : value,
			name : name,
			onChange : whatToDoOnChange
		}, newElement);
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", checkbox.id);
		labelElement.innerHTML = " " + label;
		where.appendChild(labelElement);
		where.appendChild(document.createElement("br"));

		return checkbox;
	}
};
