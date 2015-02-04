/**
 * @class Un recurso
 * @param title Nombre del recurso
 * @param type Tipo de recurso
 */
var ResourceHelper = {
    getDescriptionForTool : function(data) {
        var key = null;

        switch(data.tooltype) {
            case "ontoolquery":
                return i18n.get("resources.tool.description.ontoolquery");
            case "ontoolurl":
                return i18n.get("resources.tool.description.ontoolurl");
            case "gluepsid":
                var name = data.toolname || i18n.get("resources.tool.description.gluepsid.notset");

                return i18n.getReplaced1("resources.tool.description.gluepsid", name);
            default:
                return i18n.get("resources.tool.description.default");

        }
    }
};

var Resource = function(title, type) {
    /**
     * Indicador de tipo recurso
     */
    this.type = "resource";
    /**
     * Tipo del recurso
     */
    this.subtype = type;
    /**
     * Nombre del recurso
     */
    this.title = title;
    if(this.subtype == "tool") {
        this.url = null;
        this.ontoolquery = null;
        this.gluepsid = null;
        this.tooltype = null;
        this.toolname = null;
    } else if(this.subtype == "doc") {
        this.link = "";
    }
    IDPool.registerNewObject(this);
};
/**
 * Obtiene el título del recurso
 * @return título del recurso
 */
Resource.prototype.getTitle = function() {
    return this.title;
};

Resource.prototype.getDescription = function() {
    if(this.subtype == "doc") {
        return this.link;
    } else if(this.subtype == "tool") {
        return ResourceHelper.getDescriptionForTool(this);
    } else {
        return "";
    }
};
