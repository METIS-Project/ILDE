
var TreeManager = {

    trees: new Array(),
	
    customClasses: new Array(),
	
    
    createTree: function(treeId, selectionCallback, root, rootId, rootIconClass){
        var tree = {};
		
        this.customClasses[rootId] = rootIconClass;
        
        tree.rootId = rootId;
        tree.rebuild = 0;
        tree.realIds = new Array();
        tree.falseIds = new Array();
        
        var data = {
            identifier: "id",
            label: "label",
            childrenAttr: "children",
            items: [{
                top: false,
                id: rootId,
                className: "schemas.Folder",
                label: root
            }]
        };
        
        tree.store = new dojo.data.ItemFileWriteStore({
            data: data
        });
        
        tree.tree = new dijit.Tree({
            labelAttr: 'label',
            store: tree.store,
            id: treeId,
            getIconClass: function(item) {
                customIconClass = item == null ? null : TreeManager.customClasses[item.id[0]];
                return customIconClass;
            },
            onClick: function(item, node){
                selectionCallback(TreeManager.trees[this.id].realIds[item.id]);
            }
        }, dojo.byId(treeId));
        
        this.trees[treeId] = tree;
    },
    
    addItem: function(treeId, label, itemId, parentId, customIconClass){
        var tree = this.trees[treeId];
        if (typeof parentId == "undefined") {
            parentId = tree.rootId;
        } else if (parentId != tree.rootId) {
            parentId = tree.falseIds[parentId]; /* we want the id used to build the tree */
        }
		
		
        var falseId = itemId + "_" + tree.rebuild;
        tree.falseIds[itemId] = falseId;
        tree.realIds[falseId] = itemId; /* looks like shit, I know, snff... */

        this.customClasses[falseId] = customIconClass;
		
        tree.store.fetchItemByIdentity({
            identity: parentId,
            onItem: function(item){
                var newItem = tree.store.newItem({
                    id: falseId,
                    className: "schemas.Folder",
                    label: label
                }, {
                    parent: item,
                    attribute: "children"
                });
            //newItem.customIconClass = customIconClass;
            }
        });
		
    },
    
    expand: function(treeId, itemId) {
        var tree = this.trees[treeId];
        var id = tree.falseIds[itemId];
        //tree.tree._expandNode(tree.tree._itemNodeMap[id]);
        tree.tree._expandNode(tree.tree._itemNodesMap[id][0]);
    },
	
    emptyParent: function(tree, parentId) {
        var ids = new Array();
        tree.store.fetchItemByIdentity({
            identity: parentId,
            onItem: function(item){
                for (var i in item.children) {
                    ids.push(item.children[i].id);
                }
            }
        });
		
        for (var i in ids) {
            delete this.customClasses[ids[i]];
			
            tree.store.fetchItemByIdentity({
                identity: ids[i],
                onItem: function(item){
                    if (item.children && item.children.length > 0) {
                        TreeManager.emptyParent(tree, ids[i]);
                    }
                    tree.store.deleteItem(item);
					
                }
            });
        }
    },
	
    emptyTree: function(treeId){
        var tree = this.trees[treeId];
        this.emptyParent(tree, tree.rootId);
        tree.rebuild ++;
    }
};
