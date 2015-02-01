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
