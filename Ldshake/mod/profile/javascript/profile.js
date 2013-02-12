//function to display a preview of the users cropped section
function preview(img, selection) {
    var origWidth = $("#user_avatar").width(); //get the width of the users master photo
    var origHeight = $("#user_avatar").height(); //get the height of the users master photo
    var scaleX = 100 / selection.width; 
    var scaleY = 100 / selection.height; 
    $('#user_avatar_preview > img').css({ 
        width: Math.round(scaleX * origWidth) + 'px', 
        height: Math.round(scaleY * origHeight) + 'px', 
        marginLeft: '-' + Math.round(scaleX * selection.x1) + 'px', 
        marginTop: '-' + Math.round(scaleY * selection.y1) + 'px' 
     }); 
} 
    
//variables for the newly cropped avatar
var $x1, $y1, $x2, $y2, $w, $h;
    
function selectChange(img, selection){
   
   //populate the form with the correct coordinates once a user has cropped their image
   document.getElementById('x_1').value = selection.x1;
   document.getElementById('x_2').value = selection.x2;
   document.getElementById('y_1').value = selection.y1;
   document.getElementById('y_2').value = selection.y2;
   
 }     
 
$(document).ready(function () {
    
    //get and set the coordinates
    $x1 = $('#x1');
    $y1 = $('#y1');
    $x2 = $('#x2');
    $y2 = $('#y2');
    $w = $('#w');
    $h = $('#h');
    
    masterimage = (typeof masterimage === 'undefined')? '' : masterimage;
    $('<div id="user_avatar_preview"><img src="'+masterimage+'" /></div>') 
    .insertAfter($('#user_avatar'));
    
    $('<div id="user_avatar_preview_title"><label>Preview</label></div>').insertBefore($('#user_avatar_preview'));
    
}); 

$(window).load(function () { 
    
    //this produces the coordinates
    $('#user_avatar').imgAreaSelect({ selectionOpacity: 0, onSelectEnd: selectChange });
    //show the preview
    $('#user_avatar').imgAreaSelect({ aspectRatio: '1:1', onSelectChange: preview });
  
});