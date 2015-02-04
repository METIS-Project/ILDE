
var LoaderIcon = {
    
    src: "images/loading2.gif",
    
    show: function(id){
        var div = dojo.byId(id);
        if ( div.hasChildNodes() )
        {
            while ( div.childNodes.length >= 1 )
            {
                div.removeChild( div.firstChild );       
            } 
        }
        var image = document.createElement("img");
        image.setAttribute("src", this.src);
        image.setAttribute("name", "loadingGif");
        image.setAttribute("height", "24");
        image.setAttribute("width", "24");
        div.appendChild(image);
        dojo.style(id,"display","");
    },
    
    hide: function(id){
        dojo.style(id,"display","none");
        
    }
};


