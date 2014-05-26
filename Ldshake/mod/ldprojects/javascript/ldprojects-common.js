/**
 * Created by cliente on 10/03/14.
 */

function saveProjectN(){
    $(".draggable").each(function(){

        if( $(this).attr("tooltype_added") ){
            var tool = new Object();
            tool.tooltype=$(this).find('[tooltype]').attr("tooltype");
            tool.toolName=$(this).find('[tooltype]').attr("toolname");
            //If check if the tool has sybtype...
            var subtype =$(this).find('[tooltype]').attr("subtype");
            if (subtype)
                tool.editor_subtype=subtype;

            //Save locations
            var location = this.getBoundingClientRect();

            tool.left = location.left;
            tool.bottom = location.bottom;
            tool.top = location.top;
            tool.right = location.right;

            tool.associatedLdS = {};

            $(this).find(".subtool._jsPlumb_endpoint_anchor_").each(function(){
                tool.associatedLdS.guid=parseInt($(this).attr("associatedLdS"), 10);
            })

            ldproject.push(tool);
        }

        return ldproject;
    });
}


$(document).ready(function() {

    jsPlumb.Defaults.Container = $("#ldproject_toolBar");

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

    /**
     * Given a
     * @param toolElem
     */
    function addSubToolElem(toolElem) {
        //var src     = baseurl + 'mod/ldprojects/images/plus-icon.png';
        var width   = 50;
        var height  = 50;
        var margin  = 20;
        var toolLocation = toolElem.getBoundingClientRect();
        var left    = $(toolElem).find(".subtool").length * (width + margin);
        var bottom  = -(toolLocation.height); //+ margin (delete it, because it was too much space)
        var ldsimg     = baseurl + 'mod/lds/images/lds-doc-icon-64.png';

        //generate new unique id
        var id = $(toolElem).find('[tooltype]').attr("tooltype") + "_"
            + $(toolElem).find('[subtype]').attr("subtype") + "_"
            + toolElem.subToolElemCount++;

        toolElem.associatedLdS[toolElem.subToolElemCount-1]=null; //añade Jon para ver si puedo guardar aquí los IDs de los asociados

       //add button id
        var id_add = $(toolElem).find('[tooltype]').attr("tooltype") + "_"
            + $(toolElem).find('[subtype]').attr("subtype") + "_add";

        var item = '<div id="' + id + '" class="subtool" tooltype="'+$(toolElem).find('[tooltype]').attr("tooltype")+'" subtype="'+$(toolElem).find('[subtype]').attr("subtype")+'" style="width:' + width + 'px' + '; height:' + height + 'px' + '; left:' + left + 'px' + '; bottom:' + bottom + 'px' + '; display:block; position:absolute; z-index: 9999; background-image: url(\'' + ldsimg + '\'); background-size: contain;" />';

        //console.log(item);Semantic mechanisms supporting management and re-use of learning designs in teacher communities
        $(toolElem).append(item);

        var $addedItem = $("#"+id);
        addRemoveIcon($addedItem.get(0));
        addAttacthIcon($addedItem.get(0));

        //link the elements
        $addedItem.get(0).jsPlumbConn = toolElem.jsPlumb.connect({
            source: id_add,
            target: id,
            connector:"StateMachine", //For defining straight lines
            anchor: ["Bottom", "Top"], //Botton source to top target)
            paintStyle:{lineWidth:3,strokeStyle:"#056"},
            endpoint: "Blank",
            overlays:[ ["PlainArrow", {location:1, width:10, length:12} ]]
        });

        return $addedItem;
    }

    function addRemoveIcon(subToolElem) {
        var src     = baseurl + 'mod/ldprojects/images/minus-icon.png';
        var width   = 20;
        var height  = 20;
        var toolLocation = subToolElem.getBoundingClientRect();
        var left    = toolLocation.width - width/2;
        var bottom  = toolLocation.height - height/2;
        var id = subToolElem.id + "_remove";

        var item = '<img id="' + id + '" src="' + src + '" style="width:' + width + 'px' + '; height:' + height + 'px' + '; left:' + left + 'px' + '; bottom:' + bottom + 'px' + '; display:block; position:absolute; z-index: 9999; cursor: pointer;" />';

        //console.log(item);
        //console.log(toolLocation);
        $(subToolElem).append(item);
        var $addedItem = $("#"+id);

        $addedItem.on("click", function(event) {
            event.preventDefault();
            event.stopPropagation();
            var tool = subToolElem.parentElement;
            tool.jsPlumb.detach(subToolElem.jsPlumbConn);
            $(subToolElem).remove();
            //TODO: añadir que quite el LdS eliminado de los associatedLdS
            if(!$(tool).find(".subtool").length) {
                $(tool).find(".addsubtool-icon").remove();
                $(tool).css("top", "");
                $(tool).css("left", "");
                deleteLdSTool(tool);
            }
        });
    }

    function addPlusIcon(toolElem) {
        var src     = baseurl + 'mod/ldprojects/images/plus-icon.png';
        var width   = 20;
        var height  = 20;
        var toolLocation = toolElem.getBoundingClientRect();
        var left    = (toolLocation.width - width)/2;
        var bottom  = -height/2;
        var id = $(toolElem).find('[tooltype]').attr("tooltype") + "_"
            + $(toolElem).find('[subtype]').attr("subtype") + "_add";

        var item = '<img id="' + id + '" class="addsubtool-icon" src="' + src + '" style="width:' + width + 'px' + '; height:' + height + 'px' + '; left:' + left + 'px' + '; bottom:' + bottom + 'px' + '; display:block; position:absolute; z-index: 9999; cursor: pointer;"/>';

        //console.log(item);
        //console.log(toolLocation);
        $(toolElem).append(item);
        var $addedItem = $("#"+id);

        $addedItem.on("click", function(event) {
            event.preventDefault();
            event.stopPropagation();
            addSubToolElem(toolElem);
        });
    }

    function addAttacthIcon(subToolElem) {
        var src     = baseurl + 'mod/ldprojects/images/attach-icon.png';
        var width   = 20;
        var height  = 20;
        var toolLocation = subToolElem.getBoundingClientRect();
        var left    = toolLocation.width - width/2;
        var bottom  = -height/2;
        var id = subToolElem.id + "_attatch";

        var item = '<img id="' + id + '" src="' + src + '" style="width:' + width + 'px' + '; height:' + height + 'px' + '; left:' + left + 'px' + '; bottom:' + bottom + 'px' + '; display:block; position:absolute; z-index: 9999; cursor: pointer;" />';

        $(subToolElem).append(item);
        var $addedItem = $("#"+id);

        $addedItem.on("click", function(event) {
            $('#lds_attachment_popup').toggle();
            $('#shade').toggle();
            //TODO: añadir código para asociar un ldS existente
            var item ='<form action=""  name="myldSform">';

            ldsToBeListed.forEach(function(entry){
                if($(subToolElem).attr("tooltype") == entry.lds.editor_type)
                    item = item + '<input type="radio" name="lds_selection" value="'+entry.lds.guid+'">'+entry.lds.title+'</br>';
            });
            item = item + '<input type="submit" value="Submit">'
            item = item + "</form>";
            $('#lds_attachment_popup').append(item);
            $('[name="myldSform"]').on("submit", function(event){
                event.preventDefault();
                var lds_id = document.myldSform.lds_selection.value;
                $(subToolElem).attr("associatedLdS", lds_id);
                $('#lds_attachment_popup').toggle();
                $('#shade').toggle();
                $('#lds_attachment_popup').empty();
            });
            //Ventana emergente con mi div y
            //Div ponerlo en relative z index que 9999
        });
    }

    //Buscaba una forma de que el contenedor de herramientas siempre sea un poco mayor que e
    // $("#ldproject_toolBar").css("bottom", '"'+document.querySelector("[toolname='CADMOS']").getBoundingClientRect().bottom+600+"'");

    $(".draggable" ).each(function(){
        //create a jsPlumb instance for each draggable
        this.jsPlumb = jsPlumb.getInstance();
        this.jsPlumb.Defaults.Container = this;
        this.subToolElemCount = 0;
        this.associatedLdS = new Array();
        jsPlumb.draggable(this);

        //EDIT PART
        if(ldproject.length > 0 && toolLoaded < totalToLoad)
        {
            var tool = ldproject[toolLoaded]; //obtenemos el objeto
            if(tool.toolName == $(this).find('[tooltype]').attr("toolName"))
            {
                $(this).attr("tooltype_added", "true");
                $(this).css("top", tool.top + "px"); //lo posicionamos
                $(this).css("left", -tool.left + "px");
                addPlusIcon(this); //Añado el icono de + y el primer LdS
                if(tool.associatedLdS){
                    for(var i = 0; i < tool.associatedLdS.length; i++)
                    {
                        var $addedElement = addSubToolElem(this);
                        var addedElement = $addedElement.get(0);
                        addedElement.associatedLdS = tool.associatedLdS[i];
                    }
                }
                toolLoaded++;
            }
        }

        $(this).on("mouseup", function(event) {
            if(isInsideDropGrid(this)) {
                if( !$(this).attr("tooltype_added") || $(this).attr("tooltype_added") == "false"  ) {
                    //console.log("add");
                    $(this).attr("tooltype_added", "true"); //we update the added flag to true (is on the grid);
                    addPlusIcon(this);
                    addSubToolElem(this);
                }
            }

        });
    });
});







