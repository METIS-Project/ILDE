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
