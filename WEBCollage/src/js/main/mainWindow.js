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

    if (dojo.body().attributes.ldid.value.length > 0) {
        Loader.load();
    } else {
        ChangeManager.updateDisplays();
    }
    ParticipantManagement.init();
    ParticipantSelection.init();
    LmsManagement.init();
});



