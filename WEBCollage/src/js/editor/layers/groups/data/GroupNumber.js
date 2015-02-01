
var GroupNumber = function(roleid, number) {
    this.type = "number";
    this.subtype = "singlenumber";
    this.number = number;
    this.roleid = roleid;
};


GroupNumber.prototype.setNumber = function(n) {
    this.number = n;
    this.subtype = "singlenumber";
};

GroupNumber.prototype.getNumber = function(n) {
    return this.number;
};

