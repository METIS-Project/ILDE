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

var maxDepth = 2,
    minDepth = 0;

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

function collapse(d) {
    if (d.children && d.depth > 1) {
        d._children = d.children;
        d.children = null;
        //update(d);
    } else if(d.children){
        d.children.forEach(collapse);
    }
}

tree.nodes(root).reverse();
if(root.children)
    root.children.forEach(collapse);
update(root);

d3.select(self.frameElement).style("height", height+"px");

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

    var treeWidth = max_x + Math.abs(min_x);
    var treeXpos = Math.abs(min_x);
    var scaleFactorX = 1,
        scaleFactorY = 1,
        scaleFactor = 1;

    if((max_depth+1)*nodeHeightSeparation > height)
        scaleFactorY = height/((max_depth+1)*nodeHeightSeparation);

    if(treeWidth + nodeWidth > width)
        scaleFactorX = width/(treeWidth + nodeWidth);

    scaleFactor = (scaleFactorX < scaleFactorY) ? scaleFactorX : scaleFactorY;

    d3.select("#g_duplicate_tree")
        .transition()
        .duration(duration)
        .attr("transform", "scale(" + scaleFactor + ") translate(" + (margin.left + width/(2*scaleFactor) - treeWidth/2 + treeXpos) + "," + (70 - minDepth * 200) + ")");
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




    /*
    var nodeFilter = nodeEnter
        .filter(function(d, i) {
            return d.depth == 1;
        });
        */

    /*
    nodeEnter.append("text")
        .attr("x", function(d) { return d.children || d._children ? -10 : 10; })
        .attr("dy", ".35em")
        .attr("text-anchor", function(d) { return d.children || d._children ? "end" : "start"; })
        .text(function(d) { return d.name; })
        .style("fill-opacity", 1e-6)
        .style("font-size", "12px")
        .style("font-weight", function(d) { return (d.lds_guid == window.lds_guid) ? "bold" : "normal"; });
        */

    if ( navigator.userAgent.indexOf("MSIE")>0 )
    {
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
                            .style("background-color", "#739c00")
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

        nodeEnter
            .filter(function (d) {
                return d.lds_guid == lds_guid;
            })
            .append("text").attr("transform", function(d) { return "translate(" + (-22) + ", "+(nodeHeight/2 + 7)+")"; })
            .text("<<")
            .style("font-size", "15px")
            .style("font-weight", "bold")
            .on("click", function(e){
                //e.stopPropagation();
                exclusivemode = !exclusivemode;
                update(root);
            });

        nodeEnter
            .filter(function (d) {
                return d.lds_guid == lds_guid;
            })
            .append("text").attr("transform", function(d) { return "translate(" + (nodeWidth + 5) + ", "+(nodeHeight/2 + 7)+")"; })
            .text(">>")
            .style("font-size", "15px")
            .style("font-weight", "bold")
            .on("click", function(e){
                //e.stopPropagation();
                exclusivemode = !exclusivemode;
                update(root);
            });


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



        /*
        var parentIcon = nodeEnter.append("g")
            //.attr("class", "node")
            .attr("transform", function(d) { return "translate(" + nodeWidth/2 + ", 0)"; })
            .on("click", click);

        parentIcon.append("circle")
            .attr("r", 1e-6)
            .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

    */
        /*
        parentIcon
            .append("foreignObject").attr("width","20").attr("height","20").attr("transform", "translate(-10,-12)")
            .append("xhtml:body").style("width", "20px").style("height", "20px")
            .style("display", "table")
            .style("overflow", "hidden")
            .style("background-color", "rgba(0, 0, 0, 0)")
            .append("div")
            .style("width", "100%").style("height", "100%")
            .style("display", "table-cell")
            .style("text-align","center")
            .style("vertical-align","middle")
            .style("font-size","17px")
            .style("font-weight","bold")
            .text("+");
            */

        var childIcon = nodeEnter.append("g")
            //.attr("class", "node")
            .filter(function (d) {
                return d.children || d._children;
            })
            .attr("transform", function(d) { return "translate(" + nodeWidth/2 + ", " + nodeHeight + ")"; })
            .on("click", click);

        childIcon.append("circle")
            .attr("r", 10)
            .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

        childIcon
            .append("foreignObject").attr("width","20").attr("height","20").attr("transform", "translate(-10,-12)")
            .append("xhtml:body").style("width", "20px").style("height", "20px")
            .style("display", "table")
            .style("overflow", "hidden")
            .style("background-color", "rgba(0, 0, 0, 0)")
            .append("div")
            .attr("class","expand")
            .style("width", "100%").style("height", "100%")
            .style("display", "table-cell")
            .style("text-align","center")
            .style("vertical-align","middle")
            .style("font-size","17px")
            .style("font-weight","bold")
            .text("+");
    }

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
        .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; })
        ;


    nodeUpdate.select("text")
        .style("fill-opacity", 1);

    // Transition exiting nodes to the parent's new position.

    var nodeExit = node.exit().transition()
        .duration(duration)
        .attr("transform", function(d) { return "translate(" + (source.x - nodeWidth/2) + "," + (source.y  - nodeHeight/2) + ")"; })
        .remove();

    /*
    nodeExit.select("circle")
        .attr("r", 1e-6);

    nodeExit.select("text")
        .style("fill-opacity", 1e-6);

        */

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

// Toggle children on click.
function click(d) {
    //window.location = baseurl + 'pg/lds/view/' + d.lds_guid;

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

