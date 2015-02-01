/**
 * 
 */

   var ErrorCodes = {
    /**
	 * Obtiene el mensaje de error correspondiente al código especificado
	 * @param codigo Código de error
	 * @return Mensaje del código de error
	 */
    errores : function(codigo) {
        var message = "";
        switch (codigo) {
            case 1:
                message = i18n.get("ErrorDesign");
                break;
            case 2:
                message = i18n.get("ErrorDeploy");
                break;
            case 400:
                message = i18n.get("Error400");
                break;
            case 401:
                message = i18n.get("Error401");
                break;
            case 402:
                message = i18n.get("Error402");
                break;
            case 403:
                message = i18n.get("Error403");
                break;
            case 404:
                message = i18n.get("Error404");
                break;
            case 407:
                message = i18n.get("Error407");
                break;
            case 500:
                message = i18n.get("Error500");
                break;
            case 501:
                message = i18n.get("Error501");
                break;
            case 502:
                message = i18n.get("Error502");
                break;
            case 503:
                message = i18n.get("Error503");
                break;
            default:
                //Error desconocido
                message = i18n.get("ErrorGeneral");
        }
        return message;
    }
   
   }