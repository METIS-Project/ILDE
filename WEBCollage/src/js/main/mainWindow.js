dojo.addOnLoad(function() {
    DojoPatch.patch();

    dojo.parser.parse();

    General.init();
    LearningFlow.init();
    AssessmentManager.init();
    Loader.init();
    TableGenerator.init();
    MenuManager.init();
    GFXTooltip.init();
    PatternSelector.init();
    AssessmentPatternSelector.init();
    EditActivityDialog.init();
    EditRoleDialog.init();
    EditAssessmentDialog.init();
    EditDocumentDialog.init();
    EditToolDialog.init();  
    DialogManager.init();
    ClipDisplay.init();
    ExportManager.init();
    LearningDesign.ldid = dojo.body().attributes.ldid.value;
    LearningDesign.clear();
    ClipManager.clear();
    UndoManager.init();
    LoadingPane.init();
    EditToolVLEDialog.init();
    OntoolsearchDialog.init(); 
    EditToolKeywordDialog.init();   
    ResourceManager.init();
    RenameElementDialog.init();
    GroupPatternsDialog.init();
    
    if (dojo.body().attributes.document_id.value.length > 0 && dojo.body().attributes.sectoken.value.length > 0){
        LearningDesign.ldid = dojo.body().attributes.document_id.value;
        Loader.ldShakeMode = true;
        Loader.ldShakeInfo.document_id = dojo.body().attributes.document_id.value;
        Loader.ldShakeInfo.sectoken = dojo.body().attributes.sectoken.value;        
        Loader.load();
    }
    else{

        if (dojo.body().attributes.ldid.value.length > 0) {
            Loader.load();
        } else {
                //Informar de que no tiene acceso al diseño
                alert(i18n.get("loader.load.noAccess"));
                //Resetar datos
                LearningDesign.clear();
                ClipManager.clear();
                //Recargar la página de la que procede
                if(window.opener) {
                    window.opener.location.reload();
                } else {
                    window.location.assign("index.php");
                }
                //Cerramos el diseño
                window.close();

        }
    }
    ParticipantManagement.init();
    ParticipantSelection.init();
    LmsManagement.init();   
    //Hide the webinstancecollage elements which can't be shown when it is contained in a iframe of ldshake
    if (Loader.ldShakeMode){
        dijit.byId("toolbar.exportmenu").domNode.style.display = "none";
        dijit.byId("createGluePSDesign").domNode.style.display = "none";
        dijit.byId("toolbar.exit").domNode.style.display = "none";
        dijit.byId("elegirInstancia").domNode.style.display = "none";
        dijit.byId("actualizarParticipantes").domNode.style.display = "none"; 
        dijit.byId("LDTitle").domNode.style.display = "none";
    }
});



