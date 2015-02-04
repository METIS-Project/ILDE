// ===========================================================================
// eXe
// Copyright 2012, Pedro Peña Pérez, Open Phoenix IT
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//===========================================================================

Ext.define('eXe.view.forms.LangContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.langcontainer',
    
    getLangInputId: function() {
        var item = this;

        while (!item.inputId)
            item = item.item;
        return item.inputId + "_language";
    },

    initComponent: function() {
        var me = this,
            items;

        this.item.flex = this.flex !== undefined? this.flex : 1;
        
        items = [
            {
                xtype: 'container',
                layout: 'hbox',
                anchor: '100%',
                defaults: {
                    flex: 0
                },
                items: [
                    this.item,
                    {
                        xtype: 'combobox',
                        inputId: this.getLangInputId(),
                        fieldLabel: _('Language'),
                        labelWidth: 60,
                        tooltip: _('Language of the field'),
                        store: langsStore,
                        validateOnBlur: false,
                        validateOnChange: false,
                        dirtyCls: 'property-form-dirty',
                        margin: '0 0 0 4'
                    }
                ]
            }
        ];

        Ext.applyIf(me, {
            xtype: 'container',
            layout: 'anchor',
            items: items
        });
        
        me.callParent(arguments);
    }
});

