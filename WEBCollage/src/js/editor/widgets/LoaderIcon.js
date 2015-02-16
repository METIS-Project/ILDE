/**
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Web Collage.

Web Collage is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Web Collage is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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


