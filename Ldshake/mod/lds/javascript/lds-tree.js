var margin = {top: 20, right: 0, bottom: 20, left: 0},
    width = 960 - margin.right - margin.left,
    height = 500 - margin.top - margin.bottom;

var i = 0,
    duration = 750,
    root;
var nodeWidth = 160,
    nodeHeight = 110,
    nodeSeparation = 1.3;
    nodeHeightSeparation = 200;

var tree = d3.layout.tree()
    .size([width, height]).nodeSize([nodeWidth,nodeHeight]).separation(function(a,b) {return a.parent == b.parent ? nodeSeparation : 2.8;});//.nodeSize([20,90]);

var diagonal = d3.svg.diagonal();

var svg = d3.select(".tree").append("svg")
    .attr("width", width + margin.right + margin.left)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("id", "g_duplicate_tree")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

var maxDepth = 3,
    minDepth = 0;

var scaleFactor = 1.0;
var zoom = 1.0;
var zoomX = width / 2;
var zoomY = height / 2;
var advanceX = 0.0;
var advanceY = 0.0;
var panX = 0;
var panY = 0;
var panXLast = 0;
var panYLast = 0;
var treeWidth;
var treeXpos;

var z_popup=1000;

var exclusivemode = false;
/*
d3.json(d3_data, function(error, flare) {
    root = flare;
    root.x0 = height / 2;
    root.y0 = 0;

    function collapse(d) {
        if (d.children) {
            d._children = d.children;
            d._children.forEach(collapse);
            d.children = null;
        }
    }

    root.children.forEach(collapse);
    update(root);
});
*/

root = d3_data;
root.x0 = width / 2;//height / 2;
root.y0 = 0;//0;
var lds_depth=[0,0,0];

function get_tree_level(d) {
    if (d.depth == 0)
        lds_depth[d.depth]++;

    if (d.depth == 1)
        lds_depth[d.depth]++;

    if (d.depth == 2)
        lds_depth[d.depth]++;

    if (d.lds_guid == lds_guid)
        lds_guid_depth= d.depth;

    if(d.children)
        d.children.forEach(get_tree_level);
}

function set_siblings_buttons() {
    if(lds_depth[lds_guid_depth] > 1) {
        $("#lds_tree_siblings_button")
            .show()
            .click(show_parents);
    }
}


function collapse(d) {

    if ((d.children && d.depth > 2 && d.lds_guid == lds_guid) || (d.children && d.depth > 1 && d.lds_guid != lds_guid)) {
        d._children = d.children;
        d._children.forEach(collapse);
        d.children = null;
        //update(d);
    } else if(d.children){
        if(d.target) {
            d._childrenAlt = [];
            d._childrenTarget = [];
            d.children.forEach(function(children){
                if(children.lds_guid == d.target)
                    d._childrenTarget.push(children);
                else
                    d._childrenAlt.push(children);
            });
            d._childrenAlt.forEach(collapse);
            d.children = d._childrenTarget;
            d._children = [];
        }
        d.children.forEach(collapse);
    }
}

function restore_parents(d){
    if (d.target && d._childrenAlt && d.depth < 2) {
        d.children = d._childrenTarget.concat(d._childrenAlt);
        d.children.forEach(restore_parents);
    }
}

function show_parents(){
    $(this)
        .css("cursor","default")
        .css("color","grey")
        .css("border-color", "grey")
        .css("background", "transparent")
        .unbind("click");
    restore_parents(root);
    update(root);
}



tree.nodes(root).reverse();
get_tree_level(root);
set_siblings_buttons();

//if(root.children)
//    root.children.forEach(collapse);
collapse(root);
update(root);

d3.select(self.frameElement).style("height", height+"px");


if(!(navigator.userAgent.indexOf("Gecko")>0 && navigator.userAgent.indexOf("WebKit")<0)) {

$("svg")
    .get(0).addEventListener("mousewheel",function(e){
        console.log(e.deltaY);
        var zoomBoxX = $("svg").get(0).getBoundingClientRect().left;
        var zoomBoxY = $("svg").get(0).getBoundingClientRect().top;
        var zoomBoxWidth = $("svg").get(0).getBoundingClientRect().width;
        var zoomBoxHeight = $("svg").get(0).getBoundingClientRect().height;
        var tree_mouseX = e.clientX - zoomBoxX;
        var tree_mouseY = e.clientY - zoomBoxY;
        var preZoom = zoom;

        var tree_mouseXDiff = (tree_mouseX)/(scaleFactor*zoom) + advanceX/zoom;
        var tree_mouseYDiff = (tree_mouseY)/(scaleFactor*zoom) + advanceY/zoom;


            zoom += e.wheelDelta*0.0002;

        var zoomDiff = (zoom - preZoom);
        advanceX += tree_mouseXDiff*zoomDiff;
        advanceY += tree_mouseYDiff*zoomDiff;

        d3
            .select("#g_duplicate_tree")
            .attr("transform", "scale("+(zoom*scaleFactor)+") translate(" + (-panX - advanceX/zoom) + "," + (-advanceY/zoom - panY) + ")");
    }, false);
} else {

$("svg")
    .get(0).addEventListener("wheel",function(e){
        console.log(e.deltaY);
        var zoomBoxX = $("div.tree").get(0).getBoundingClientRect().left;
        var zoomBoxY = $("div.tree").get(0).getBoundingClientRect().top;
        var zoomBoxWidth = $("div.tree").outerWidth();//$("svg").get(0).getBoundingClientRect().width;
        var zoomBoxHeight = $("div.tree").outerHeight();//$("svg").get(0).getBoundingClientRect().height;
        var tree_mouseX = e.clientX - zoomBoxX;
        var tree_mouseY = e.clientY - zoomBoxY;
        var preZoom = zoom;

        var tree_mouseXDiff = (tree_mouseX)/(scaleFactor*zoom) + advanceX/zoom;
        var tree_mouseYDiff = (tree_mouseY)/(scaleFactor*zoom) + advanceY/zoom;

        if(!!navigator.userAgent.match(/Trident\/[789]\./))
            zoom -= e.deltaY*0.0004;
        else
            zoom -= e.deltaY*0.008;

        var zoomDiff = (zoom - preZoom);
        advanceX += tree_mouseXDiff*zoomDiff;
        advanceY += tree_mouseYDiff*zoomDiff;

        d3
            .select("#g_duplicate_tree")
            .attr("transform", "scale("+(zoom*scaleFactor)+") translate(" + (-panX - advanceX/zoom) + "," + (-advanceY/zoom - panY) + ")");
    }, false);
}

$("svg").bind("mousedown", function(e){
    panXLast = 0;
    panYLast = 0;
    console.log("down");
});

$("svg").on("mousedown", function(e){
    $(this).css("cursor","move");
}).on("mouseup", function(e){
        $(this).css("cursor","default");
});

var gecko_svg_mousedown = 0;
$("svg").on("mousedown", function(e){
    gecko_svg_mousedown = 1;
});

$("svg").on("mouseup", function(e){
    gecko_svg_mousedown = 0;
});

$("svg").on("mouseout", function(e){
    gecko_svg_mousedown = 0;
});

$("svg").bind("mousemove", function(e){
    var button = 0;
    //console.log("X: "+e.pageX+" Y: "+e.pageY+" buttons: "+ e.button);
    //if(event)
//        button += event.button;
//    else

    if(navigator.userAgent.indexOf("Gecko")>0 && navigator.userAgent.indexOf("WebKit")<0)
        button += gecko_svg_mousedown;
    else if(navigator.userAgent.indexOf("MSIE")>0)
        button += event.button;
    else
        button += e.which;

    if(button){
        console.log("button pressed");
        if(panXLast != 0 && panYLast != 0){
            panX += (panXLast - e.pageX)/(zoom*scaleFactor);
            panY += (panYLast - e.pageY)/(zoom*scaleFactor);

            panXLast = e.pageX;
            panYLast = e.pageY;

            d3
                .select("#g_duplicate_tree")
                .attr("transform", "scale("+(zoom*scaleFactor)+") translate(" + (-panX - advanceX/zoom) + "," + (-advanceY/zoom - panY) + ")");
                //.attr("transform", "scale("+(zoom*scaleFactor)+") translate(" + (-panX + margin.left + (width)/(2*zoom*scaleFactor) - treeWidth/2 + treeXpos - advanceX/zoom) + "," + (-advanceY/zoom - panY) + ")");

        } else {
            panXLast = e.pageX;
            panYLast = e.pageY;
        }
        //console.log("X: "+e.pageX+" Y: "+e.pageY+" buttons: "+ e.which);
        //console.log("X: "+panX+" Y: "+panY);
        //console.log("Xl: "+panXLast+" Yl: "+panYLast);
    }
});

$("html").bind("mousedown", function(e){
    console.log("X: "+e.pageX+" Y: "+e.pageY+" buttons: "+ e.button);

});

//$("html").addClass("htmlnonselect");

    function update(source) {

    //tree.separation(function(a,b) {return 4});

    // Compute the new tree layout.
    var nodes = tree.nodes(root).reverse(),
        links = tree.links(nodes);


    /*var depth1 = nodes.filter(function(element, index, array){
        var il;
        if(il = (element.depth < 2)){
            if(element.children) {
                element._children= element.children;
                element.children = null;
            }
        }
        return il;
    });*/


    depth1 = nodes;
    links = tree.links(depth1);


    // Normalize for fixed-depth.
    depth1.forEach(function(d) { d.y = d.depth * nodeHeightSeparation; });

    // Update the nodes…
    /*
    var node = svg.selectAll("g.node")
        .data(nodes, function(d) { return d.id || (d.id = ++i); });
        */

    var node = svg.selectAll("g.node")
        .data(depth1, function(d) { return d.id || (d.id = ++i); });

    // Enter any new nodes at the parent's previous position.
    var nodeEnter = node
        .enter()
        .append("g")
        .filter(function (d) {
            return d.depth <= maxDepth && d.depth >= minDepth;
        })
        .filter(function (d) {
                if(exclusivemode)
                    return d.lds_guid == lds_guid;
                return true;
        });


    var max_x = 0;
    var min_x = 0;
    var max_depth = 0;
    node.each(function(d) {
        if(max_depth < d.depth)
            max_depth = d.depth;

        if(max_x < d.x)
        max_x = d.x;

        if(min_x > d.x)
            min_x = d.x;
    });

    if(panX) {
        panX -= -(width)/(2*zoom*scaleFactor);
        panX -= treeWidth/2;
        panX -= -treeXpos;
        panY -= -(nodeHeight)/(2*zoom*scaleFactor);
    }

    treeWidth = max_x + Math.abs(min_x);
    treeXpos = Math.abs(min_x);
    var scaleFactorX = 1,
        scaleFactorY = 1;

    if((max_depth+1)*nodeHeightSeparation > height)
        scaleFactorY = height/((max_depth+1)*nodeHeightSeparation);

    if(treeWidth + nodeWidth > width)
        scaleFactorX = width/(treeWidth + nodeWidth);

    scaleFactor = (scaleFactorX < scaleFactorY) ? scaleFactorX : scaleFactorY;

    if(!panX) {
        panX += -(width)/(2*zoom*scaleFactor);
        panX += treeWidth/2;
        panX += -treeXpos;
        panY += -(nodeHeight)/(2*zoom*scaleFactor);
    } else{
        panX += -(width)/(2*zoom*scaleFactor);
        panX += treeWidth/2;
        panX += -treeXpos;
        panY += -(nodeHeight)/(2*zoom*scaleFactor);
    }

    d3.select("#g_duplicate_tree")
        .transition()
        .duration(duration)
        //.attr("transform", "scale("+(zoom*scaleFactor)+") translate(" + (-panX + margin.left + (width)/(2*zoom*scaleFactor) - treeWidth/2 + treeXpos - advanceX/zoom) + "," + (-advanceY/zoom - panY) + ")");
        .attr("transform", "scale("+(zoom*scaleFactor)+") translate(" + (-panX - advanceX/zoom) + "," + (-advanceY/zoom - panY) + ")");

    var zoomBox = "svg";


    //    .bind("mousewheel",function(event,delta){
            //d3.select(this).attr("transform", "scale(0.1)");
//            alert(e.wheelDelta);
    //    });
        //.attr("transform", "scale(" + scaleFactor + ") translate(" + (treeXpos + nodeWidth/2 + (width - (treeWidth + nodeWidth)*scaleFactor)/(2*scaleFactor)) + "," + (70 - minDepth * 200) + ")");

    /*
    console.log(max_x + nodeWidth*nodeSeparation);
    console.log(min_x);
    console.log(treeWidth = d3.select("#g_duplicate_tree")[0][0].getBBox().width);
    console.log(treeXpos = d3.select("#g_duplicate_tree")[0][0].getBBox().x);
    */


    nodeEnter.attr("class", "node")
        .attr("transform", function(d) { return "translate(" + (source.x0 - nodeWidth/2) + "," + (source.y0 - nodeHeight/2) + ")"; })
        ;//.on("click", click);

    if ( navigator.userAgent.indexOf("MSIE")>0 || navigator.userAgent.indexOf("Gecko")>0 )
    {
        var box = nodeEnter.append("g");

        box.append("rect").attr("width",nodeWidth).attr("height",nodeHeight).
            attr("class", "tree-node-box").
            attr("rx",3).attr("ry",3).
            attr("fill", function(d){
                if(d.enabled)
                    return d.lds_guid != lds_guid ? "#739c00":"#EEE626";
                else
                    return "#82A38E";
            }).
            attr("stroke", "olive").
            attr("stroke-width", "2").
            filter(function (d) {
                return d.enabled;
            }).
            classed("enabled", true).
            style("cursor" ,"pointer").
            on("click", tree_popup_show);

        box.append("rect").attr("width",nodeWidth-10).attr("height",30).
            attr("x",5).attr("y",5).
            attr("rx",3).attr("ry",3).
            attr("fill", "#83BE66").
            attr("stroke", "olive").
            attr("stroke-width", "2").
            filter(function (d) {
                return d.enabled;
            }).
            attr("pointer-events","none").
            attr("class","svg-tree-box-title");
            /*.
            style("cursor" ,"pointer").
            on("click", function(d){
                window.location = baseurl + 'pg/lds/view/' + d.lds_guid;
            });
            */

        box.append("text").text(function(d) {
            if(d.title.length > 14)
                return d.title.substring(0,12)+"...";
            else
                return d.title.substring(0,14);
        })
            .attr("x",10).attr("y",25)
            .attr("class","svg-tree-box-title")
            //.style("display", "block")
            .style("background-color", "#83BE66")
            //.style("cursor" ,"pointer")

            //.style("border-radius", "3px")
            //.style("padding", "3px")
            .style("font-size", "20px")
            .style("color", "#474747")
            .attr("pointer-events","none");

        box.append("image")
            .attr("x",10).attr("y",60)
            .attr("xlink:href",function(d) {return d.owner_icon;})
            .attr("width",40).attr("height",40)
            .attr("pointer-events","none");

        box.append("text").text(function(d) {
            if(d.name.length > 11)
                return d.name.substring(0,10)+"...";
            else
                return d.name.substring(0,11);
        })
            .attr("x",60).attr("y",80)
            .attr("class","svg-box-username")
            .style("font-size", "14px")
            .style("font-weight", "bold")
            .style("margin-left", "4px")
            //.style("cursor" ,"pointer")
            .attr("fill", function(d){return d.lds_guid != lds_guid ? "#EBEBEB":"#666";})
            .attr("pointer-events","none");

            /*
            .on("click", function(d){
                window.location = baseurl + 'pg/ldshakers/' + d.username;
            });
            */

    }
    else {
        var box = nodeEnter.append("g").append("foreignObject").attr("width",nodeWidth+20).attr("height",nodeHeight)
            //.append("xhtml:body")   .style("width", "inherit")
    //                                .style("height", "inherit")
            .append("xhtml:body")   .style("width", "300px")
            .style("height", "300px")
                                    .style("overflow", "hidden")
                                    .style("background-color", "rgba(0, 0, 0, 0)")
            .append("div")  .style("width", (nodeWidth - 2*5 - 2*2) + "px").style("height", (nodeHeight - 2*5 - 2*2) + "px")
                            //.style("box-sizing", "border-box")
                            .style("border-radius", "3px")
                            .style("padding", "5px")
                            .style("background-color", function(d){return d.lds_guid != lds_guid ? "#739c00":"#EEE626";})
                            .style("border", "2px solid olive");

            box.append("div")   .text(function(d) {return d.title.substring(0,12)})
                                .attr("class","title")
                                .style("display", "block")
                                .style("background-color", "#83BE66")
                                .style("border-radius", "3px")
                                .style("padding", "3px")
                                .style("font-size", "18px")
                                .style("color", "#474747")
                                .on("click", function(d){
                                        window.location = baseurl + 'pg/lds/view/' + d.lds_guid;
                                    });


        box.append("div")
                            .style("margin-top", "3px")
                            .style("overflow", "hidden")
                            .style("width", "145px")
                            .style("height", "20px")
            .append("div").html(function(d) {return d.tags_html})
            .style("width", "1800px")
            .style("height", "5000px");

        var UserBox = box.append("div")
                            //.style("height", "20px")
                //.style("background-color", "#83BE66")
                            .style("border-radius-bottom-left", "3px")
                            .style("border-radius-bottom-right", "3px");


        UserBox.append("img")   .attr("src",function(d) {return d.owner_icon})
                                .style("display", "block")
                                .style("border-radius", "3px")
                                .style("float", "left");

        UserBox.append("div")   .text(function(d) {return d.name.substring(0,10)})
                                .attr("class","username")
                                .style("display", "block")
                                //.style("width", "40px")
                                .style("float", "left")
                                //.style("background-color", "#83BE66")
                                //.style("border-radius", "3px")
                                //.style("padding", "3px")
                                .style("font-size", "14px")
                                .style("font-weight", "bold")
                                .style("margin-left", "4px")
                                .style("color", "#EBEBEB")
                                .on("click", function(d){
                                    window.location = baseurl + 'pg/ldshakers/' + d.username;
                                });

        UserBox.append("div")   .style("clear", "both");

    }

    node
        .filter(function (d) {
            return d.lds_guid == lds_guid;
        })
        .append("text").attr("transform", function(d) { return "translate(" + (-22) + ", "+(nodeHeight/2 + 7)+")"; })
        .style("font-size", "15px")
        .style("font-weight", "bold")
        .on("click", function(e){
            //e.stopPropagation();
            exclusivemode = !exclusivemode;
            update(root);
        });

    node
        .filter(function (d) {
            return d.lds_guid == lds_guid;
        })
        .append("text").attr("transform", function(d) { return "translate(" + (nodeWidth + 5) + ", "+(nodeHeight/2 + 7)+")"; })
        .style("font-size", "15px")
        .style("font-weight", "bold")
        .on("click", function(e){
            //e.stopPropagation();
            exclusivemode = !exclusivemode;
            update(root);
        });

    var childIcon = nodeEnter.append("g")
        //.attr("class", "node")
        .filter(function (d) {
            return d.children || d._children;
        })
        .attr("class", "nodeState")
        .style("cursor", "pointer")
        .attr("transform", function(d) { return "translate(" + nodeWidth/2 + ", " + nodeHeight + ")"; })
        .on("click", click);

    childIcon.append("circle")
        .attr("r", 10)
        //.style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });
        .attr("class", function(d) { return d._children ? "has-children" : "no-children"; });

    // Transition nodes to their new position.
    var nodeUpdate = node.transition()
        .duration(duration)
        .attr("transform", function(d) {
            /*if(exclusivemode)
                return "translate(" + (width/2 - nodeWidth/2) + "," + (height/2 - nodeHeight/2) + ")";
                */
            return "translate(" + (d.x - nodeWidth/2) + "," + (d.y - nodeHeight/2) + ")";
        });

//    nodeUpdate=node;
    nodeUpdate.select("circle")
        .attr("r", 10)
        //.style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });
        .attr("class", function(d) { return d._children ? "has-children" : "no-children"; });

    //update the node state
    node
        .selectAll("g.nodeState line")
        .remove();

    node
        .selectAll("g.nodeState")
        .filter(function (d) {
            return d.children != null;
        })
        .append("line")
        .attr("pointer-events","none")
        .attr("x1",-2.5).attr("y1",0)
        .attr("x2",2.5).attr("y2",0)
        .attr("style","stroke:rgb(0,0,0);stroke-width:2");

    //nodeUpdate.select("text.state")
    var expand_buttons = node
        .selectAll("g.nodeState")
        .filter(function (d) {
            return d._children;
        })
        .filter(function (d) {
            return d.children == null;
        });

    expand_buttons
        .append("line")
        .attr("pointer-events","none")
        .attr("x1",-3.5).attr("y1",0)
        .attr("x2",3.5).attr("y2",0)
        .attr("style","stroke:rgb(0,0,0);stroke-width:2");

    expand_buttons
        .append("line")
        .attr("pointer-events","none")
        .attr("x1",0).attr("y1",-3.5)
        .attr("x2",0).attr("y2",3.5)
        .attr("style","stroke:rgb(0,0,0);stroke-width:2");

    nodeUpdate.select("text")
        .style("fill-opacity", 1);

    // Transition exiting nodes to the parent's new position.
    var nodeExit = node.exit().transition()
        .duration(duration)
        .attr("transform", function(d) { return "translate(" + (source.x - nodeWidth/2) + "," + (source.y  - nodeHeight/2) + ")"; })
        .remove();

    // Update the links…
    var link = svg.selectAll("path.link")
        .data(links, function(d) {
            return d.target.id;
        });

    // Enter any new links at the parent's previous position.
    link.enter().insert("path", "g")
        .filter(function (d) {
            return  d.source.depth <= maxDepth && d.source.depth >= minDepth
                    && d.target.depth <= maxDepth && d.target.depth >= minDepth;
        })
        .filter(function (d) {
            if(exclusivemode)
                return false;
            return true;
        })
        .attr("class", "link")
        .attr("d", function(d) {
            var o = {x: (0 - nodeWidth/2), y: (source.y0 - nodeHeight/2)};
            return diagonal({source: o, target: o});
        });

    // Transition links to their new position.
    /*
    link.transition()
        .duration(duration)
        .attr("d", diagonal);
        */

    link.transition()
        .filter(function (d) {
            return  d.source.depth <= maxDepth && d.source.depth >= minDepth
                && d.target.depth <= maxDepth && d.target.depth >= minDepth;
        })
        .filter(function (d) {
            if(exclusivemode)
                return false;
            return true;
        })
        .duration(duration)
        .attr("d", function(d) {
            var source = {x: d.source.x, y: d.source.y + nodeHeight/2};
            var target = {x: d.target.x, y: d.target.y - nodeHeight/2};
            return diagonal({source: source, target: target});
        });/*.each("end",function(){
            var treeWidth = d3.select("#g_duplicate_tree")[0][0].getBBox().width;
            var treeXpos = d3.select("#g_duplicate_tree")[0][0].getBBox().x;
            d3.select("#g_duplicate_tree").attr("transform", "translate(" + (margin.left + width/2 - treeWidth/2 - treeXpos) + "," + (70 - minDepth * 200) + ")");

        });*/

    /*
    setTimeout(function(){
        var treeWidth = d3.select("#g_duplicate_tree")[0][0].getBBox().width;
        var treeXpos = d3.select("#g_duplicate_tree")[0][0].getBBox().x;
        d3.select("#g_duplicate_tree").attr("transform", "translate(" + (margin.left + width/2 - treeWidth/2 - treeXpos) + "," + (70 - minDepth * 200) + ")");

    },duration+1000);
    */



    // Transition exiting nodes to the parent's new position.
    link.exit().transition()
        .duration(duration)
        .attr("d", function(d) {
            var o = {x: source.x, y: source.y};
            return diagonal({source: o, target: o});
        })
        .remove();

    // Stash the old positions for transition.
    nodes.forEach(function(d) {
        d.x0 = d.x;
        d.y0 = d.y;
    });


}

function tree_popup_show(d){
    //console.log(this);
    var popup_id = d.lds_guid;
    var tree_popup_random = Math.floor(Math.random()*1000000000);
    var popup_id_selector = "#tree_info_popup_shell_"+ popup_id + "_" + tree_popup_random + " ";
    var $popup, $popup_move;
    var tree_box_origin=this;
    var node = this.getBoundingClientRect();
    var X = this.getBoundingClientRect().left;
    var Y = this.getBoundingClientRect().top;
    var height = this.getBoundingClientRect().height - 32;
    var width = this.getBoundingClientRect().width - 32;
    var popup_width = 600;
    var popup_height = 400;
    var fadein_timing = 1;
    var popupX = X-100,
        popupY = Y+100;

    var transition_settings = "width "+fadein_timing+"s, height "+fadein_timing+"s, opacity "+fadein_timing+"s, top "+fadein_timing+"s, left "+fadein_timing+"s";
    transition_settings = "all 2.0s ease";

    $("#tree_lds_popup")
        //.empty()
        .append(tree_lds_box.html);

    $popup = $("#tree_info_popup_shell_empty")
        .attr("id", "tree_info_popup_shell_"+ popup_id + "_"+ tree_popup_random)
        .css("z-index", z_popup++);

    $popup_move = $("#tree_info_popup_move_empty")
        .attr("id", "tree_info_popup_move_"+ popup_id + "_"+ tree_popup_random);

    if($(window).height() < Y + popup_height + 50)
        popupY = $(window).height() - popup_height - 50;

    if($(window).width() < X + popup_width + 50)
        popupX = $(window).height() - popup_width - 50;

    if(lds_guid == d.lds_guid)
        $popup.find(".tree_info_popup_control_button.diff").hide();


    //$popup = $(popup_id_selector);
    //$popup_move= $(popup_id_selector + ".tree_info_popup_move");
    //$popup_move.show();

    $.post(
        baseurl+'action/lds/tree_lds_view',
        {
            guid: d.lds_guid,
            ref_id: lds_guid
        },
        function(data) {
            var $interal_viewer;
            var maximized= 0,
                minimized=0;
            var iframe_ready=false,
                iframe_diff=false;
            var tree_popup_zoom_level = 100*popup_width/957;
            var iframe_xsize,
                iframe_ysize;


            $popup.show();
            $(popup_id_selector+"#tree_info_popup").html(data.html);
            $interal_viewer = $(popup_id_selector+"#internal_iviewer");


            $popup.click(function(e) {
                    $popup.css("z-index", z_popup++);
                }
            );

            if(d.tags_html.length > 5)
                $interal_viewer.attr("height","79%");

            var zoom_update = function(popup_frame_width) {
                tree_popup_zoom_level = 100*popup_frame_width/957;
                popup_box_zoom();
                if(iframe_ready)
                    popup_iframe_zoom();
            }

            var popup_iframe_zoom = function() {
                var iframe_tree_contents= $(popup_id_selector+"#internal_iviewer").contents().contents();
                var iframe_width = 957*tree_popup_zoom_level/100;

                if(!iframe_ready) {
                    //iframe_xsize = $(iframe_tree_contents).filter("html").outerWidth();
                    iframe_xsize = 880;
                    $(iframe_tree_contents).filter("html").css("width", iframe_xsize+"px");
                    iframe_ysize = $(iframe_tree_contents).filter("html").find("body").outerHeight();
                    $(iframe_tree_contents).filter("html").css("position", "relative");
                    $(iframe_tree_contents).css("transition","opacity "+fadein_timing+"s");
                    $(popup_id_selector+"#internal_iviewer").css("transition","opacity "+fadein_timing+"s");
                    setTimeout(function(){
                        $(iframe_tree_contents).css("transition",transition_settings);
                        $(popup_id_selector+"#internal_iviewer").css("transition",transition_settings);
                        $(iframe_tree_contents).filter("html").css("overflow-y","auto");
                    },fadein_timing*1000);

                    $(iframe_tree_contents).filter("html").find("body").click(function(e){
                        e.preventDefault();
                        $popup.click();
                    });

                    iframe_ready=true;
                }

                //$(popup_id_selector+"#internal_iviewer")
                //    .css("width", iframe_width+"px");

                //$(iframe_tree_contents).css("zoom",tree_popup_zoom_level+"%");
                var iframe_xmargin = iframe_xsize*(tree_popup_zoom_level/100 - 1)/2;
                var iframe_ymargin = iframe_ysize*(tree_popup_zoom_level/100 - 1)/2;

                $(iframe_tree_contents).css("left",iframe_xmargin+"px");
                $(iframe_tree_contents).css("top",iframe_ymargin+"px");
                $(iframe_tree_contents).filter("html").css("transform", "scale("+tree_popup_zoom_level/100+")");
                $(iframe_tree_contents).filter("html").css("-webkit-transform", "scale("+tree_popup_zoom_level/100+")");

                //$interal_viewer.css("transition","opacity 1s");
                //$interal_viewer.css("transition",transition_settings);
                $interal_viewer.css("opacity","1.0");

            }

            var popup_box_zoom = function() {
                $(popup_id_selector + "#tree_info_popup #content_area_user_title").css("zoom",tree_popup_zoom_level+"%");
                $(popup_id_selector + "#tree_info_popup > *").css("-webkit-user-select", "none");
                $(popup_id_selector + "#tree_info_popup > *").css("-moz-user-select", "none");
            }

            zoom_update(popup_width);
            $interal_viewer.load(popup_iframe_zoom);

            var popupXlast = 0;
            var popupYlast = 0;

            $(popup_id_selector + ".tree_info_popup_control_button.diff").click(function(e){
                iframe_ready=false;
                $interal_viewer.css("transition","opacity "+fadein_timing/3+"s");
                $interal_viewer.css("opacity","0.0");

                setTimeout(function(){
                    if(!iframe_diff) {
                        $interal_viewer.attr("src", baseurl+"pg/lds/view_iframe/"+data.doc+"/"+data.ref_doc+"/");
                    } else {
                        $interal_viewer.attr("src", baseurl+"pg/lds/view_iframe/"+data.doc+"/");
                    }
                    iframe_diff = !iframe_diff;
                },1000*fadein_timing/3);
            });

            $(popup_id_selector + ".tree_info_popup_control_button.maximize").click(function(e){
                var $tree = $(".tree");
                var tree_offset= $tree.offset();
                $popup.css("transition",transition_settings);

                if(!maximized) {
                    $popup.css("top",tree_offset.top+"px");
                    $popup.css("left",tree_offset.left+"px");
                    $popup.css("width",$tree.outerWidth()+"px");
                    $popup.css("height",$tree.outerHeight()+"px");
                    zoom_update($tree.outerWidth());
                    $(popup_id_selector + ".tree_info_popup_control_button.move, "+popup_id_selector+".tree_info_popup_control").unbind("mousedown");
                } else {
                    $popup.css("top",popupY+"px");
                    $popup.css("left",popupX+"px");
                    $popup.css("width",popup_width+"px");
                    $popup.css("height",popup_height+"px");
                    zoom_update(popup_width);
                    $(popup_id_selector + ".tree_info_popup_control_button.move, "+popup_id_selector+".tree_info_popup_control").on("mousedown", popup_move_enable);
                }

                minimized = false;
                maximized = !maximized;
            });

            $(popup_id_selector + ".tree_info_popup_control_button").on("mousedown", function(e){
                e.stopPropagation();
            });

            $(popup_id_selector + ".tree_info_popup_control_button.minimize").on("click", function(e){
                e.stopPropagation();
                var $tree = $(".tree");
                var tree_offset= $tree.offset();
                $popup.css("transition",transition_settings);

                if(!minimized) {
                    if(maximized) {
                        $popup.css("top",popupY+"px");
                        $popup.css("left",popupX+"px");
                        $popup.css("width",popup_width+"px");
                        $popup.css("height",popup_height+"px");
                        zoom_update(popup_width);
                        $(popup_id_selector + ".tree_info_popup_control_button.move, "+popup_id_selector+".tree_info_popup_control").on("mousedown", popup_move_enable);
                    } else {
                        $popup.css("top",popupY+"px");
                        $popup.css("left",popupX+"px");
                        $popup.css("width",popup_width*0.75+"px");
                        $popup.css("height",popup_height*0.75+"px");
                        zoom_update(popup_width*0.75);
                        minimized = true;
                    }

                } else {
                    $popup.css("top",tree_box_origin.getBoundingClientRect().top+"px");
                    $popup.css("left",tree_box_origin.getBoundingClientRect().left+"px");
                    $popup.css("width",width+"px");
                    $popup.css("height",height+"px");
                    $popup.css("opacity","0.0");
                    setTimeout(function(){$popup.remove()}, 2000);
                }

                maximized = !maximized;
            });

            $(popup_id_selector + ".tree_info_popup_control_button.close").on("click", function(e){
                e.stopPropagation();
                $popup.css("transition",transition_settings);
                $popup.css("top",tree_box_origin.getBoundingClientRect().top+"px");
                $popup.css("left",tree_box_origin.getBoundingClientRect().left+"px");
                $popup.css("width",width+"px");
                $popup.css("height",height+"px");
                $popup.css("opacity","0.0");
                setTimeout(function(){$popup.remove()}, 2000);
            });

            var popup_move_enable = function(e){
                //$(popup_id_selector + ".tree_info_popup_control_button.move").bind("mousedown", function(e){
                popupXlast = e.pageX;
                popupYlast = e.pageY;
                $popup_move.show();
                gecko_move_popup_mousedown = 1;
                console.log("FFshow ");
            }

            $(popup_id_selector + ".tree_info_popup_control_button.move, "+popup_id_selector+".tree_info_popup_control").on("mousedown", popup_move_enable);

            var gecko_move_popup_mousedown = 0;


            $popup_move.on("mouseup", function(e){
                gecko_move_popup_mousedown = 0;
            }).on("mouseout", function(e){
                gecko_move_popup_mousedown = 0;
                    console.log("FFout ");
            });

            $popup_move.bind("mousemove", function(e){
                //e.stopPropagation();
                var button = 0;

                if(navigator.userAgent.indexOf("Gecko")>0 && navigator.userAgent.indexOf("WebKit")<0)
                    button += gecko_move_popup_mousedown;
                else if(navigator.userAgent.indexOf("MSIE")>0)
                    button += event.button;
                else
                    button += e.which;

                console.log("move "+button);

                if(button && popupXlast && popupYlast){

                    popupX -= popupXlast - e.pageX;
                    popupY -= popupYlast - e.pageY;

                    popupXlast = e.pageX;
                    popupYlast = e.pageY;

                    $popup.css("transition","all 0s");
                    $popup.css("left",popupX+"px");
                    $popup_move.css("left",popupX+"px");
                    $popup.css("top",popupY+"px");
                    $popup_move.css("top",popupY+"px");

                    zoom_update($popup.outerWidth());

                    //console.log("X: "+e.pageX+" Y: "+e.pageY+" buttons: "+ e.which);
                    //console.log("X: "+panX+" Y: "+panY);
                    //console.log("Xl: "+panXLast+" Yl: "+panYLast);
                }
                if(!button) {
                    $popup_move.hide();
                }
            });

        });

    $popup.css("width",width+"px");
    $popup.css("height",height+"px");
    $popup.css("top",Y+"px");
    $popup_move.css("top",Y+"px");
    $popup.css("left",X+"px");
    $popup_move.css("left",X+"px");
    $popup.css("opacity",0.4);

    $popup.show();
    $popup.css("transition",transition_settings);
    $popup.css("width",popup_width+"px");
    $popup.css("height",popup_height+"px");
    $popup.css("top",popupY+"px");
    $popup_move.css("top",popupY+"px");
    $popup.css("left",popupX+"px");
    $popup_move.css("left",popupX+"px");
    $popup.css("opacity",1.0);

    //$popup.hide();


    //console.log(node);
    //console.log(d);
}

// Toggle children on click.
function click(d) {

    if (d.children) {
        d._children = d.children;
        d.children = null;
    } else {
        d.children = d._children;
        d._children = null;

        d.children.forEach(function(d) {
            if (d.children) {
                d._children = d.children;
                d.children = null;
            }
        });

        if(maxDepth < d.depth + 1)
            maxDepth = d.depth + 1;
    }
    update(d);

}

