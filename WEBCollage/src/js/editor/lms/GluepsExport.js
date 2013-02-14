/**
 * Exportación de scripts a Glue!PS desde WIC
 */

var GluepsExport = {
    
    exportImsld : function(url, user, pass) {
        var content = {
            id : LearningDesign.ldid
        };
        
        var bindArgs = {
            url : "manager/export.php",
            handleAs: "json",
            content : content,
            contentType : "application/json; charset=utf-8",
            load : function(data) {
                if (data.ok){
                    var title = LearningDesign.data.title;
                    var zipurl = data.url;
                    var imsldType = "IMS LD";
                    //Crear diseño
                    GluepsExport.createGluePSDesignDeploy(url, title, zipurl, imsldType, user, pass);
                }
                else{
                    GluepsExport.showDeployError();
                }
            },
            error : function(error) {
                GluepsExport.showDeployError();
            }
        };
        dojo.xhrGet(bindArgs);
    },
    
    exportImsldPrevious: function() {
        var idlms_installation = DesignInstance.data.instObj.id;
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            sync : true,
            content: {
                task: "obtener_installation",
                idlms_installation: idlms_installation
            },
            load: function(data){
                if (data.ok){
                    var url = data.installation.instance_identifier;
                    var user = data.installation.user;
                    var pass = data.installation.pass;
                    GluepsExport.exportImsld(url, user, pass);
                }
                else
                {
                    GluepsExport.showDeployError();
                }
            },
            error : function(error) {
                GluepsExport.showDeployError();
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    
    createGluePSDesign: function(title, zipurl, imsldType){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                title: title,
                zip_url: zipurl,
                imsld_type: imsldType,
                task: "createGluePSDesign"
            },
            load: function(data){
                if (data.ok){
                    var designId=data.designid;
                    var deployTitle = LearningDesign.data.title;
                    var pos = zipurl.lastIndexOf('/');
                    var fileUrl = zipurl.substring(0,pos+1);
                    fileUrl = fileUrl + "icmanifest.xml";
                    var vleSelect = GluepsExport.buildGlueldId(DesignInstance.data.lmsObj.id);
                    var teacherNames = GluepsExport.buildTeachersString();
                    var themeSelect=1;     
                    //Crear despliegue
                    GluepsExport.createGluePSDeploy(designId, deployTitle, fileUrl, vleSelect, teacherNames, themeSelect);
                }
                else{
                    GluepsExport.showDeployError();
                }
            },
            error : function(error) {
                GluepsExport.showDeployError();
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Devuelve un string que contiene los identificadores de los profesores asociados al VLE de GLUE!PS separados por el carácter ';'
     * @return Identificadores de los profesores separados por ';'
     */
    buildTeachersString: function()
    {
        var teachers = "";   
        for (var i=0; i<DesignInstance.data.participants.length;i++)
        {
            if (DesignInstance.data.participants[i].participantType=="teacher")
            {
                teachers = teachers + DesignInstance.data.participants[i].participantId + ";";
            }
        }
        return teachers;
    },
    
    /**
     * Obtiene el id del recurso VLE
     * @param vleurl URL asociada al diseño de la que se obtiene el id del recurso
     * @return Identificador del recurso VLE 
     */
    buildGlueldId: function(vleurl)
    {
        var pos = vleurl.lastIndexOf('/');
        return vleurl.substring(pos+1);        
    },
    
        
    createGluePSDeploy: function(designId, deployTitle, fileUrl, vleSelect, teacherNames, themeSelect){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                design_id: designId,
                deploy_title: deployTitle,
                file_url: fileUrl,
                vle_select: vleSelect,
                teacher_names: teacherNames,
                theme_select: themeSelect,
                task: "createGluePSDeploy"
            },
            load: function(data){
                if (data.ok){
                    window.open("http://www.gsic.uva.es/GLUEPSManager/gui/glueps/index.html?deployId=" + data.deployid, data.deployid);
                }
                else{
                    GluepsExport.showDeployError();
                }
            },
            error : function(error) {
                GluepsExport.showDeployError();
            }
        };
        dojo.xhrPost(bindArgs);
    },    
    
    createGluePSDesignDeploy: function(url, title, zipurl, imsldType, user, pass){
        var deployTitle = LearningDesign.data.title;
        var pos = zipurl.lastIndexOf('/');
        var fileUrl = zipurl.substring(0,pos+1);
        fileUrl = fileUrl + "icmanifest.xml";
        var vleSelect = GluepsExport.buildGlueldId(DesignInstance.data.lmsObj.id);
        var teacherNames = GluepsExport.buildTeachersString();
        var themeSelect=1;   
                    
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                url: url,
                title: title,
                zip_url: zipurl,
                imsld_type: imsldType,
                
                deploy_title: deployTitle,
                file_url: fileUrl,
                vle_select: vleSelect,
                teacher_names: teacherNames,
                theme_select: themeSelect,
                user: user,
                pass: pass,
                task: "createGluePSDesignDeploy"
            },
            load: function(data){
                if (data.ok){
                    //window.open("http://www.gsic.uva.es/GLUEPSManager/gui/glueps/index.html?deployId=" + data.deployid, data.deployid);
                    window.open(url + "/gui/glueps/deploy.html?deployId=" + data.deployid, data.deployid);
                }
                else{
                    GluepsExport.showDeployError();
                }
            },
            error : function(error) {
                GluepsExport.showDeployError();
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    showDeployError: function() {
        dijit.byId("deployDialog").show();
        dojo.byId("DeployContent").innerHTML = i18n.get("deployGluePS.error");
    },
    
    close: function() {
        dijit.byId("deployDialog").hide();
    }
}


