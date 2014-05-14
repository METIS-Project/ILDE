/**
 * Created by cliente on 10/03/14.
 */

$(function() {

//Código de pruebas     jsPlumb
    /*jsPlumb.connect({ source: "container0",
     target: "container1" });
     jsPlumb.draggable("container0");
     jsPlumb.draggable("container1");
     });*/

    ////$d = $(".draggable");
    //jsPlumb.connect({source: $d.get(0), target: $d.get(1)});

    /*crear elemento +
     Meterle un evento""
     */

    /*$( ".draggable" ).each(function(){
     jsPlumb.draggable(this);
     }
     );*/

    /*$( ".draggable" ).on("mouseup", function(event){
     pos = $(event.toElement).position();
     }
     );*/

    //

//TODO: Cambiar el evento mouseup. No he sabido cuadrarlo....
    $( ".draggable, #draggable-nonvalid" ).draggable();

    $( "#droppable_grid, #ldproject_toolBar" ).droppable({
        accept: ".draggable",
        activeClass: "ui-state-hover",
        hoverClass: "ui-state-active",
        drop: function( event, ui ) { //dropEvent //Lo primero que probé fué poner aquí que cogier evento mouseup. Pero no me ha funcionado
            console.log(this); //cómo el syste.out de java

            if( this.id == "ldproject_toolBar" ) //If we move the element from the grid to the toolBar
            {
                console.log("elimino");
                deleteLdSTool(event.toElement);
                return;
            }

            /*If the element has not been added to the grid (never before)
             or if the element was added and then removed.
             So, the element is inside the toolBar*/
            var gridLocation = this.getBoundingClientRect();
            var toolLocation = event.toElement.getBoundingClientRect();
            console.log(gridLocation);

            //If  the top position is between grid's top and left
            //If the left position is between grid's left and right
            //Then, the tool is inside the grid
            if(gridLocation.top < toolLocation.top && toolLocation.top < gridLocation.bottom)
            {
                if(gridLocation.left < toolLocation.left && toolLocation.left < gridLocation.right)
                {
                    if( !$(event.toElement).attr("tooltype_added") || $(event.toElement).attr("tooltype_added") == "false"  )
                    {
                        console.log("agrego");
                        storeLdSTool(event.toElement) ;
                        /*
                         TODO: Quiero añadir la imagen del (+) que tengo aquí al dibujo de la herramienta en la posición left:(toolLocation.right+toolLocation.left)/2 bottom:tool.bottom
                         Para colocarlo justo en medio de la parte inferior del icono de la herramienta.
                         TODO: añadir evento al (+) si se pulsa que cree un nuevo elemento a +50right del último hijo de este elemento y lo linke con la herramenta
                         TODO: crear un nuevo elemento a +10 bottom con el icono general de un LdS
                         TODO: añadir la imagen del (-) al LdS que he creado por si quiero eliminarlo (puedes usar la imagen del + de momento o buscar una)
                         TODO: añadir evento al (-) por si se clica en ese botón elimine este LdS, si sólo qeuda este recolocar la tool en la grid
                         TODO: link o jplumb entre este elemento y la tool
                         */
                    }else //We move the element over the grid
                    {
                        console.log("actualizo");
                        updateLdSTool(event.toElement);
                    }
                }
            }

            //TODO: en el caso que se suelte fuera del grid recolocar en gridLocation.top y gridLocation.left
        }
    });
});

$(function() {
    $( "#tabs" ).tabs();
})

function storeLdSTool( myElement )
{
    var tool = new Object();
    tool.editor_type=$(myElement).attr("tooltype");
    tool.toolName=$(myElement).attr("toolname");
    //If check if the tool has sybtype...
    var subtype =$(myElement).attr("subtype");
    if (subtype)
        tool.editor_subtype=subtype;

    //Save locations
    var location = myElement.getBoundingClientRect();
    //alternativa al código anterior
    //var location = document.querySelector("[toolname='"+tool.toolName+"']").getBoundingClientRect();
    //console.log(this.getBoundingClientRect()); Esto muestra la posición del rectangulo general

    tool.left = location.left;
    tool.bottom = location.bottom;
    tool.top = location.top;
    tool.right = location.right;

    $(myElement).attr("tooltype_added", "true"); //we update the added flag to true (is on the grid)
    ldproject.push(tool);
}

function updateLdSTool (myElement) //TODO: mirar si se puede implementar Hashmap en Javascript (seria automático el update)
{
    var arrayLength = ldproject.length;
    var location = myElement.getBoundingClientRect();
    for (var i=0; i < arrayLength; i++)
    {
        tool = ldproject[i];
        if(tool.toolName ==  $(myElement).attr("toolname") )
        {
            ldproject[i].left = location.left;
            ldproject[i].bottom = location.bottom;
            ldproject[i].top = location.top;
            ldproject[i].right = location.right;
        }
    }
}

function deleteLdSTool(myElement)
{
    var arrayLength = ldproject.length;
    var tool;
    for (var i=0; i < arrayLength; i++)
    {
        tool = ldproject[i];
        if(tool.toolName ==  $(myElement).attr("toolname") )
        {
            ldproject.splice(i, 1);
            $(myElement).attr("tooltype_added", "false");
        }
    }
}
