/**
 * @class Un grupo en la instanciación del diseño
 * @param roleid Identificador del grupo
 */
var Group = function(roleid) {
	this.type = "group";
	/**
	 * Indentificador del grupo
	 */
	this.roleid = roleid;
	/**
	 * Instancias del grupo
	 */
	this.instances = new Array();
	IDPool.registerNewObject(this);
};
/**
 * Añade una instancia al grupo
 * @param instance Identificador de la instancia a añadir al grupo
 */
Group.prototype.addInstance = function(instance) {
	this.instances.push(instance)
};
