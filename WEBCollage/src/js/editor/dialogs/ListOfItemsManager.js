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

var ListOfItemsManager = function(listContainer) {
	this.listContainer = listContainer;
	/* LDActivityLOsList */
	this.listOfItems = null;
};

ListOfItemsManager.prototype.buildList = function() {/* extension point */
	var table = dojo.byId(this.listContainer);

	while(table.childNodes.length >= 1) {
		table.removeChild(table.firstChild);
	}

	for(var i in this.listOfItems) {
		var item = IDPool.getObject(this.listOfItems[i]);
		var li = table.appendChild(document.createElement("li"));
		li.className = "loListItem";

		var name = document.createElement("span");
		name.innerHTML = " " + item.title + " ";
		name.setAttribute("title", item.description);
		this.addItem(item.id, li, name);
	}
};

ListOfItemsManager.prototype.addItem = function(id, where, item) {
	where.appendChild(item);
	var byebye = where.appendChild(document.createElement("img"));
	byebye.setAttribute("src", "images/icons/delete.png");
	byebye.className = "generalButton";
	var listener = new DeleteActivityItemListener(id, this);
	dojo.connect(byebye, "onclick", listener, "click");
};

ListOfItemsManager.prototype.removeItem = function(id) {
	var index = this.listOfItems.indexOf(id);
	if(index >= 0) {
		this.listOfItems.splice(index, 1);
	}
	this.buildList();
};
DeleteActivityItemListener = function(id, manager) {
	this.id = id;
	this.manager = manager;
};

DeleteActivityItemListener.prototype.click = function() {
	this.manager.removeItem(this.id);
};
