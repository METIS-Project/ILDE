/**
 * dojo.connect(EditRoleDialog, "close", this, "reopen")
 * dojo.disconnect(this.reopenListeners[i]);
 * 
 * @author Eloy
 */
var DialogModaler = {
    setZIndex: function(forDialog, previousStackIndex) {
        dojo.query(forDialog.dlg.domNode).style( {
            zIndex : 1125 + 50 * previousStackIndex
        });
    },

    block : function(dialog, forDialog, previousStackIndex) {
        var dialogNode = dojo.query(dialog.dlg.domNode);

        var element = dojo.query(dojo.create('div')).place(dojo.body()).addClass("modaler").style( {
            top : (dialog.dlg.domNode.offsetTop + 1) + "px",
            left : (dialog.dlg.domNode.offsetLeft + 1) + "px",
            width : dialogNode.style("width") + "px",
            height : dialogNode.style("height") + "px",
            zIndex : 1100 + 50 * previousStackIndex
        });

        dojo.query(forDialog.dlg.domNode).style( {
            zIndex : 1125 + 50 * previousStackIndex
        });

        var listener = {
            dialogManager : dialog,
            blocker : element,
            connector : null
        };

        var connector = dojo.connect(forDialog.dlg, "hide", listener,
            DialogModaler.unblock);
        listener.connector = connector;

        return;
    },

    unblock : function() {
        dojo.query(this.blocker).orphan();
        dojo.disconnect(this.connector);
        this.dialogManager.reopen();
        return;
    },

    pushPopups : function() {

        dojo.query(".dijitPopup").style( {
            zIndex : 1900
        });
    }
};
