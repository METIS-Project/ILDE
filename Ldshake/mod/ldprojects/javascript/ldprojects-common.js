/**
 * Created by cliente on 10/03/14.
 */


$(function() {
    $( ".draggable, #draggable-nonvalid" ).draggable();
    $( "#droppable_conteptualize, #droppable_author, #droppable_implement" ).droppable({
        accept: ".draggable",
        activeClass: "ui-state-hover",
        hoverClass: "ui-state-active",
        drop: function( event, ui ) { //dropEvent
            console.log(this); //cómo el syste.out de java
            $( this )
            .addClass( "ui-state-highlight" )
            .find( "p" )
            .html( $(event.srcElement).attr("tooltype") );

            if( !$(event.srcElement).attr("tooltype_added") )
            {
                $(event.srcElement).attr("tooltype_added", "true");
                var tool = new Object();
                tool.editor_type=$(event.srcElement).attr("tooltype");
                var subtype =$(event.srcElement).attr("subtype");
                if (subtype)
                    tool.editor_subtype=subtype;
                ldproject.push(tool);
            }
        }
    });
});


$(function() {
    $( "#tabs" ).tabs();
});
