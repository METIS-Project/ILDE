/**
 * 
 */
String.prototype.visualLength = function() {
	var ruler = document.getElementById("ruler");
	ruler.innerHTML = this;
	return ruler.offsetWidth;
};

String.prototype.visualHeight = function() {
	var ruler = document.getElementById("ruler");
	ruler.innerHTML = this;
	return ruler.offsetHeight;
};