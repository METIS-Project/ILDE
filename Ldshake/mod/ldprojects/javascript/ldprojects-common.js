/**
 * Created by cliente on 10/03/14.
 */


$(function() {
    $( ".draggable, #draggable-nonvalid" ).draggable();
    $( "#droppable_conteptualize, #droppable_author, #ldproject_toolBar" ).droppable({
        accept: ".draggable",
        activeClass: "ui-state-hover",
        hoverClass: "ui-state-active",
        drop: function( event, ui ) { //dropEvent
            console.log(this); //cómo el syste.out de java

           if( !$(event.srcElement).attr("tooltype_added") || $(event.srcElement).attr("tooltype_added") == "false"  )
           {
                $(event.srcElement).attr("tooltype_added", "true");
                var tool = new Object();
                tool.editor_type=$(event.srcElement).attr("tooltype");
                tool.toolName=$(event.srcElement).attr("toolname");
                var subtype =$(event.srcElement).attr("subtype");
                if (subtype)
                    tool.editor_subtype=subtype;
                ldproject.push(tool);
           }

           if( this.id == "ldproject_toolBar" )
           {
               var arrayLength = ldproject.length;
               for (var i=0; i < arrayLength; i++)
               {
                   tool = ldproject[i];
                   if(tool.toolName ==  $(event.srcElement).attr("toolname") )
                   {
                       ldproject.splice(i, 1);
                       $(event.srcElement).attr("tooltype_added", "false");
                   }
               }
           }
        }
    });
});

    $(function() {
        $( "#tabs" ).tabs();
})


