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

/*global dojo, i18n, dijit, ChangeManager*/

var RenameElementDialog = {
    dlgHandle : null,
    tooltipDialog : null,
    titledComponent : null,

    init : function() {
        this.dlgHandle = dojo.byId("RenameComponentTitleDialogHandle");
        dojo.style(this.dlgHandle, {
            position : "absolute",
            width : "0px",
            height : "0px",
            display : "none"
        });
    },
    open : function(titledComponent, x, y) {
        this.titledComponent = titledComponent;

        var content = '<table><tr><td><input dojoType="dijit.form.TextBox" name="title" value="';
        content += titledComponent.getTitle();
        content += '"></td></tr><tr><td colspan="2" align="center"><button dojoType="dijit.form.Button" label="';
        content += i18n.get("common.ok");
        content += '" type="submit"></button></td></tr></table>';
        //</div>';

        this.tooltipDialog = new dijit.TooltipDialog({
            execute : function(formValues) {
                RenameElementDialog.setTitle(formValues.title);
            },
            style : "position: absolute; top: 0px; left: 0px;",
            content : content/*,
            onMouseLeave : function() {
                RenameElementDialog.tooltipDialog.destroy();
            }*/
        });

        dojo.style(this.dlgHandle, {
            display : "inline",
            left : x + "px",
            top : y + "px"
        });

        dijit.popup.open({
            popup : this.tooltipDialog,
            around : this.dlgHandle
        });
    },
    setTitle : function(title) {
        this.tooltipDialog.destroy();
        if(dojo.trim(title).length > 0 && title != this.titledComponent.getTitle()) {
            this.titledComponent.setTitle(title);
            ChangeManager.titleSet();
        }
    },
    registerElement : function(shape, titledComponent) {
        shape.connect("onclick", titledComponent, function(event) {
            var bodypos = dojo.position(dojo.body());
            RenameElementDialog.open(this, event.clientX - bodypos.x - 12, event.clientY - bodypos.y);
        });
    }
};
