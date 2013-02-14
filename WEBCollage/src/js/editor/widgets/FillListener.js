
function FillListener(target, fill, emphasis){
    this.target = target;
    this.fill = fill;
    this.emphasis = emphasis;
    
    //	LearningFlowAnim.cacheHandler(target.connect("onmouseover", this, "doEmphasisFill"));
    //	LearningFlowAnim.cacheHandler(target.connect("onmouseout", this, "doFill"));
    target.connect("onmouseover", this, "doEmphasisFill");
    target.connect("onmouseout", this, "doFill");
}

FillListener.prototype.doFill = function(){
    this.target.setFill(this.fill);
};

FillListener.prototype.doEmphasisFill = function(){
    this.target.setFill(this.emphasis);
};


