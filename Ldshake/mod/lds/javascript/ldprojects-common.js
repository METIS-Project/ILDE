/*********************************************************************************
 * LdShake is a platform for the social sharing and co-edition of learning designs
 * Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
 *
 * (Contributors, alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by the
 * Free Software Foundation with the addition of the following permission added
 * to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK
 * IN WHICH THE COPYRIGHT IS OWNED BY Universitat Pompeu Fabra (UPF), Barcelona,
 * UPF DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program; if not, see http://www.gnu.org/licenses.
 *
 * You can contact the Interactive Technologies Group (GTI), Universitat Pompeu Fabra, Barcelona.
 * headquarters at c/Roc Boronat 138, Barcelona, or at email address davinia.hernandez@upf.edu
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License version 3,
 * these Appropriate Legal Notices must retain the display of the "Powered by
 * LdShake" logo with a link to the website http://ldshake.upf.edu.
 * If the display of the logo is not reasonably feasible for
 * technical reasons, the Appropriate Legal Notices must display the words
 * "Powered by LdShake" with the link to the website http://ldshake.upf.edu.
 ********************************************************************************/


$(function() {
    $( ".draggable, #draggable-nonvalid" ).draggable();
    $( "#droppable_conteptualize, #droppable_author, #ldproject_toolBar" ).droppable({
        accept: ".draggable",
        activeClass: "ui-state-hover",
        hoverClass: "ui-state-active",
        drop: function( event, ui ) { //dropEvent
            console.log(this); //cómo el syste.out de java

            if( !$(event.srcElement).attr("tooltype_added")  )
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
                       ldproject.splice(i, 1 );
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



