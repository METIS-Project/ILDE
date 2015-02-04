/**
 * @author Eloy
 */
var Inheritance = {

    /**
     * pattern definitions
     */
    patterns : {},
    
    registerPattern: function(id, definition) {
        this.patterns[id] = definition;
    },

    inheritMethods : function(object, baseClass) {
        for(var i in baseClass.prototype) {
            object[i] = baseClass.prototype[i];
        }
    },
    inheritPatternMethods : function(object) {
        this.inheritMethods(object, this.patterns[object.patternid]);
    }
};
