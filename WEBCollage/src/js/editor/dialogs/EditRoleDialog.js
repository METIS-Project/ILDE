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

var EditRoleDialog = {

    role : null,

    dlg : null,

    init : function() {
        this.dlg = dijit.byId("LDRoleEditDialog");
    },

    open : function(role) {
        this.role = role;
        this.load();
        this.dlg.show();
    },

    ok : function() {
        this.save();
        this.close();
    },

    cancel : function() {
        this.close();
    },

    close : function() {
        this.dlg.hide();
    },

    load : function() {
        dijit.byId("LDRoleTitle").setValue(this.role.title);
        dijit.byId("LDRoleEditMinPersons").setValue(this.role.persons.min);
        dijit.byId("LDRoleEditMaxPersons").setValue(this.role.persons.max);
        dijit.byId("LDRoleEditDescription").setValue(this.role.description);
    },

    save : function() {
        this.role.title = dijit.byId("LDRoleTitle").getValue();
        this.role.persons.min = dijit.byId("LDRoleEditMinPersons").getValue();
        this.role.persons.max = dijit.byId("LDRoleEditMaxPersons").getValue();
        this.role.description = dijit.byId("LDRoleEditDescription").getValue();
        
        /* Actualizar el nombre asociado a las instancias*/
        var instances = DesignInstance.instanciasGrupo(this.role.id);
        for (var i=0; i<instances.length;i++)
        {
            instances[i].name=this.role.title;
        }

        ChangeManager.roleEdited(this.role);
    }
};
