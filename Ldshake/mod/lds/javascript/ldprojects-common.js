/**
 * Created by cliente on 10/03/14.
 */

$(document).ready(function() {

    jsPlumb.Defaults.Container = $("#ldproject_toolBar");
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
    var dropgrid = $("#droppable_grid").get(0);

    function isInsideDropGrid(toolElem) {
        /*
        If the element has not been added to the grid (never before)
         or if the element was added and then removed.
         So, the element is inside the toolBar
         */
        var gridLocation = dropgrid.getBoundingClientRect();
        var toolLocation = toolElem.getBoundingClientRect();

        //If  the top position is between grid's top and left
        //If the left position is between grid's left and right
        //Then, the tool is inside the grid
        if(gridLocation.top < toolLocation.top && toolLocation.top < gridLocation.bottom)
        {
            if(gridLocation.left < toolLocation.left && toolLocation.left < gridLocation.right)
            {
                return true;
            }
        }
        return false;
    }

    function addSubToolElem(toolElem) {
        //var src     = baseurl + 'mod/ldprojects/images/plus-icon.png';
        var width   = 50;
        var height  = 50;
        var margin  = 40;
        var toolLocation = toolElem.getBoundingClientRect();
        var left    = $(toolElem).find(".subtool").length * (width + margin);
        var bottom  = -(toolLocation.height + margin);
        var id = "b";

        var item = '<div id="' + id + '" style="border: 1px solid red; width:' + width + 'px' + '; height:' + height + 'px' + '; left:' + left + 'px' + '; bottom:' + bottom + 'px' + '; display:block; position:absolute; z-index: 9999" />';

        console.log(item);
        $(toolElem).append(item);
        var $addedItem = $("#"+id);

        /*
        $addedItem.on("click", function(event) {
            event.preventDefault();
            event.stopPropagation();
            addSubToolElem(toolElem);
        });
*/
    }

    function addPlusIcon(toolElem) {
        var src     = baseurl + 'mod/ldprojects/images/plus-icon.png';
        var width   = 20;
        var height  = 20;
        var toolLocation = toolElem.getBoundingClientRect();
        var left    = (toolLocation.width - width)/2;
        var bottom  = -height/2;
        var id = "a";

        var item = '<img id="' + id + '" src="' + src + '" style="width:' + width + 'px' + '; height:' + height + 'px' + '; left:' + left + 'px' + '; bottom:' + bottom + 'px' + '; display:block; position:absolute; z-index: 9999" />';

        console.log(item);
        console.log(toolLocation);
        $(toolElem).append(item);
        var $addedItem = $("#"+id);

        $addedItem.on("click", function(event) {
            event.preventDefault();
            event.stopPropagation();
            addSubToolElem(toolElem);
        });

    }

    $(".draggable" ).each(function(){
        jsPlumb.draggable(this);
        $(this).on("mouseup", function(event) {
            //event.preventDefault();
            //event.stopPropagation();
            console.log("up");
            console.log(event);
            console.log(this);

            if(isInsideDropGrid(this)) {
                if( !$(this).attr("tooltype_added") || $(this).attr("tooltype_added") == "false"  ) {
                    console.log("add");
                    storeLdSTool(this);
                    addPlusIcon(this);

                        /*
                         TODO: Quiero añadir la imagen del (+) que tengo aquí al dibujo de la herramienta en la posición left:(toolLocation.right+toolLocation.left)/2 bottom:tool.bottom
                         Para colocarlo justo en medio de la parte inferior del icono de la herramienta.
                         TODO: añadir evento al (+) si se pulsa que cree un nuevo elemento a +50right del último hijo de este elemento y lo linke con la herramenta
                         TODO: crear un nuevo elemento a +10 bottom con el icono general de un LdS
                         TODO: añadir la imagen del (-) al LdS que he creado por si quiero eliminarlo (puedes usar la imagen del + de momento o buscar una)
                         TODO: añadir evento al (-) por si se clica en ese botón elimine este LdS, si sólo qeuda este recolocar la tool en la grid
                         TODO: link o jplumb entre este elemento y la tool
                         */
                } else {
                    console.log("update");
                    updateLdSTool(this);
                }
            }

        });
     });

    /*$( ".draggable" ).on("mouseup", function(event){
     pos = $(event.toElement).position();
     }
     );*/

    //


//TODO: Cambiar el evento mouseup. No he sabido cuadrarlo....
//    $( ".draggable, #draggable-nonvalid" ).draggable();
//
//    $( "#droppable_grid, #ldproject_toolBar" ).droppable({
//        accept: ".draggable",
//        activeClass: "ui-state-hover",
//        hoverClass: "ui-state-active",
//        drop: function( event, ui ) { //dropEvent //Lo primero que probé fué poner aquí que cogier evento mouseup. Pero no me ha funcionado
//            console.log(this); //cómo el syste.out de java
//
//            if( this.id == "ldproject_toolBar" ) //If we move the element from the grid to the toolBar
//            {
//                console.log("elimino");
//                deleteLdSTool(event.toElement);
//                return;
//            }
//
//            /*If the element has not been added to the grid (never before)
//             or if the element was added and then removed.
//             So, the element is inside the toolBar*/
//            var gridLocation = this.getBoundingClientRect();
//            var toolLocation = event.toElement.getBoundingClientRect();
//            console.log(gridLocation);
//
//            //If  the top position is between grid's top and left
//            //If the left position is between grid's left and right
//            //Then, the tool is inside the grid
//            if(gridLocation.top < toolLocation.top && toolLocation.top < gridLocation.bottom)
//            {
//                if(gridLocation.left < toolLocation.left && toolLocation.left < gridLocation.right)
//                {
//                    if( !$(event.toElement).attr("tooltype_added") || $(event.toElement).attr("tooltype_added") == "false"  )
//                    {
//                        console.log("agrego");
//                        storeLdSTool(event.toElement) ;
//                        /*
//                         TODO: Quiero añadir la imagen del (+) que tengo aquí al dibujo de la herramienta en la posición left:(toolLocation.right+toolLocation.left)/2 bottom:tool.bottom
//                         Para colocarlo justo en medio de la parte inferior del icono de la herramienta.
//                         TODO: añadir evento al (+) si se pulsa que cree un nuevo elemento a +50right del último hijo de este elemento y lo linke con la herramenta
//                         TODO: crear un nuevo elemento a +10 bottom con el icono general de un LdS
//                         TODO: añadir la imagen del (-) al LdS que he creado por si quiero eliminarlo (puedes usar la imagen del + de momento o buscar una)
//                         TODO: añadir evento al (-) por si se clica en ese botón elimine este LdS, si sólo qeuda este recolocar la tool en la grid
//                         TODO: link o jplumb entre este elemento y la tool
//                         */
//                    }else //We move the element over the grid
//                    {
//                        console.log("actualizo");
//                        updateLdSTool(event.toElement);
//                    }
//                }
//            }
//
//            //TODO: en el caso que se suelte fuera del grid recolocar en gridLocation.top y gridLocation.left
//        }
//    });


/*
$(function() {
    $( "#tabs" ).tabs();
})
*/

});

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
