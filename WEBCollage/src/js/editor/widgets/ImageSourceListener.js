
function ImageSourceListener(target, source, emphasis, shape) {
    this.target = target;
    this.source = source;
    this.emphasis = emphasis;
    this.shape = shape != null ? shape : target;
    
    target.connect("onmouseover", this, "setEmphasisSource");
    target.connect("onmouseout", this, "setNormalSource");
}


ImageSourceListener.prototype.setNormalSource = function(){
    this.shape.setShape({
        src: this.source
    });
};

ImageSourceListener.prototype.setEmphasisSource = function(){
    this.shape.setShape({
        src: this.emphasis
    });
};



