/**
* @class Un rol
* @param title Nombre del rol
* @param type Tipo de rol
*/
var Role = function(title, type) {
    this.type = "role";
    this.subtype = type;
    this.title = title;
    this.description = "";
    this.persons = {
        min : "",
        max : ""
    };
    IDPool.registerNewObject(this);
    this.children = new Array();
};

/**
* Añade un rol hijo al rol
* @param role Rol hijo a añadir
*/
Role.prototype.addChild = function(role) {
    this.children.push(role);
};

/**
* Elimina un rol hijo del rol
* @param role Rol hijo a eliminar
*/
Role.prototype.removeChild = function(role) {
    this.children.splice(this.children.indexOf(role), 1);
};

/**
* Obtiene el nombre del rol
* @return Nombre del rol
*/
Role.prototype.getTitle = function() {
    return this.title;
};

Role.prototype.setTitle = function(title) {
    this.title = title;
};


