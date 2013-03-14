/*********************************************************************************
 * LdShake is a platform for the social sharing and co-edition of learning designs
 * Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
 *
 * (Contributors, alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by the
 * Free Software Foundation with the addition of the following permission added
 * to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK
 * IN WHICH THE COPYRIGHT IS OWNED BY Universitat Pompeu Fabra (UPF), Barcelona,
 * UPF DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program; if not, see http://www.gnu.org/licenses.
 *
 * You can contact the Interactive Technologies Group (GTI), Universitat Pompeu Fabra, Barcelona.
 * headquarters at c/Roc Boronat 138, Barcelona, or at email address davinia.hernandez@upf.edu
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License version 3,
 * these Appropriate Legal Notices must retain the display of the "Powered by
 * LdShake" logo with a link to the website http://ldshake.upf.edu.
 * If the display of the logo is not reasonably feasible for
 * technical reasons, the Appropriate Legal Notices must display the words
 * "Powered by LdShake" with the link to the website http://ldshake.upf.edu.
 ********************************************************************************/

/*
 * Stores all the revision point positions with all the associated data (pos, link)
 */
var rev_points = new Array();

/*
 * Color constants
 */
var red = "rgb(200, 0, 0)";
var green = "rgb(0, 200, 0)";
var blue = "rgb(0, 0, 200)";
var black = "rgb(0, 0, 0)";
var gray = "rgb(200, 200, 200)";
var lightgreen = "rgb(208, 242, 208)";

/*
 * Metrics constants
 */
var revision_info_offset_y = 25;
var revision_lines_offset = 200;
var revision_radius = 10;
var revision_canvas_left = 0;
var revision_canvas_top = 0;

/*
 * Stores the current point
 */
var curPoint;

function array_find (array, elem)
{
	for (var i in array)
		if (elem == array[i].guid)
			return i;
	
	return -1;
}

function resizeRevisions ()
{
	var wHeight = $(window).height() - $('#revision_graph_wrapper').offset().top;
	wHeight = Math.max (wHeight, 300);
	$('#revision_graph_wrapper').css('height', wHeight);
}

function initLastYs (doc_list)
{
	var arr = new Array();
	
	for (var i in doc_list)
		arr.push (-1);
	
	return arr;
}

function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

function on_click (e)
{
	if (curPoint > -1)
		window.location = rev_points[curPoint].link;
}

function on_mousemove (ev)
{
	var x;
	var y;
	
	if (ev.layerX || ev.layerX == 0) { // Firefox
		x = ev.layerX;// - revision_canvas_left;
		y = ev.layerY;// - revision_canvas_top;
	}
  	else if (ev.offsetX || ev.offsetX == 0) { // Opera
		x = ev.offsetX;// - revision_canvas_left;
		y = ev.offsetY;// - revision_canvas_top;
	}
	
	curPoint = inPoint (x, y);
	//console.log("" + x + "," + y);
	document.body.style.cursor = (curPoint > -1) ? "pointer" : "";
}

function inPoint (x, y)
{
	for (var i in rev_points)
	{
		var dx = x - rev_points[i].x;
        var dy = y - rev_points[i].y;
        var dist = Math.sqrt(dx*dx+dy*dy);
        if (dist < revision_radius)
        	return i;
	}
	
	return -1;
}

$(document).ready(function()
{
	resizeRevisions ();
	
	$(window).resize (function ()
	{
		resizeRevisions ();
	});
	
	revision_canvas_left = $("#revision_graph").offset().left;
	revision_canvas_top = $("#revision_graph").offset().top;
	
	var canvas = document.getElementById("revision_graph");
	
	if (canvas.getContext)
	{
		canvas.addEventListener("click", on_click, false);
		canvas.addEventListener("mousemove", on_mousemove, false);
		
		var ctx = canvas.getContext("2d");
		
		var lastYs = initLastYs (doc_list);
		
		var head_extenders = new Array();

		
		//Draw the revisions (Hz axis)
		ctx.strokeStyle = gray;
		ctx.fillStyle = black;
		for (var i in rev_history)
		{
			ctx.beginPath();

			//Draw the info container
			ctx.moveTo(10.5, 100 * i + revision_info_offset_y + 0.5);
			ctx.lineTo(140.5, 100 * i + revision_info_offset_y + 0.5);
			ctx.quadraticCurveTo(150.5, 100 * i + revision_info_offset_y + 0.5, 150.5, 100 * i + revision_info_offset_y + 10.5);
			ctx.lineTo(150.5, 100 * i + revision_info_offset_y + 40.5);
			ctx.quadraticCurveTo(150.5, 100 * i + revision_info_offset_y + 50.5, 140.5, 100 * i + revision_info_offset_y + 50.5);
			ctx.lineTo(10.5, 100 * i + revision_info_offset_y + 50.5);
			ctx.quadraticCurveTo(0.5, 100 * i + revision_info_offset_y + 50.5, 0.5, 100 * i + revision_info_offset_y + 40.5);
			ctx.lineTo(0.5, 100 * i + revision_info_offset_y + 10.5);
			ctx.quadraticCurveTo(0.5, 100 * i + revision_info_offset_y + 0.5, 10.5, 100 * i + revision_info_offset_y + 0.5);
			ctx.moveTo (150.5, 100 * i + 50.5);
			ctx.lineTo (revision_lines_offset + 70 * doc_list.length, 100 * i + 50.5);

			ctx.closePath();
			ctx.stroke();
			
			ctx.drawImage(document.getElementById('revision_avatar_' + rev_history[i].id), 10, 100 * i + revision_info_offset_y + 5);
			
			ctx.fillText("Revision " + rev_history[i].revision_number, 60, 100 * i + revision_info_offset_y + 17);  
			ctx.fillText(rev_history[i].author, 60, 100 * i + revision_info_offset_y + 29);  
			ctx.fillText(rev_history[i].date, 60, 100 * i + revision_info_offset_y + 41);  
		}
		
		
		//Draw the revision points
		for (var i in rev_history)
		{
			//Am I in the last revision?
			var last_rev = (i == rev_history.length - 1);
			
			//These are the docs associated to this revision
			var rev_docs = rev_history[i].revised_docs;
			
			//For each of the revised docs in this revision (but not the head docs)				
			ctx.fillStyle = blue;
			for (var j in rev_docs)
			{
				//Where shall I draw the circle?
				var pos = array_find (doc_list, rev_docs[j].document_guid);
				
				if (pos > -1)
				{
					ctx.beginPath();
					ctx.arc(revision_lines_offset + pos * 70, 100 * i + 50.5, revision_radius, 0, Math.PI*2, true);
					ctx.closePath();
					ctx.fill();
					
					//Add the point to the revision points
					rev_points.push ({
						x: revision_lines_offset + pos * 70,
						y: 100 * i + 50.5,
						link: baseurl + "pg/lds/viewrevision/" + rev_docs[j].guid + '/'
					});
					
					//ctx.fillText("Id " + rev_docs[j].guid, revision_lines_offset + pos * 70, 100 * i + 70.5);
					
					//Draw the line that goes to the last ball.
					if (lastYs[pos] != -1)
					{
						ctx.strokeStyle = blue;
						ctx.FillStyle = blue;
						
						ctx.lineWidth = 5;
						ctx.beginPath();
						ctx.moveTo(revision_lines_offset + pos * 70, lastYs[pos]);
						ctx.lineTo(revision_lines_offset + pos * 70, 100 * i + 50.5);
						ctx.closePath();
						ctx.stroke();
					}
					lastYs[pos] = 100 * i + 50.5;
				}
			}
			
			//For each of the deleted docs
			for (var j in rev_history[i].deleted_docs)
			{
				if (isNumber(rev_history[i].deleted_docs[j]))
				{
					//Where shall I draw the circle?
					var pos = array_find (doc_list, rev_history[i].deleted_docs[j]);
					
					
					//Draw the line that goes to the previous ball.
					if (lastYs[pos] != -1)
					{
						ctx.strokeStyle = blue;
						ctx.FillStyle = blue;
						
						ctx.lineWidth = 5;
						ctx.beginPath();
						ctx.moveTo(revision_lines_offset + pos * 70, lastYs[pos]);
						ctx.lineTo(revision_lines_offset + pos * 70, 100 * i + 50.5);
						ctx.closePath();
						ctx.stroke();
					}
					lastYs[pos] = 100 * i + 50.5;
					
					ctx.fillStyle = red;
					ctx.beginPath();
					ctx.arc(revision_lines_offset + pos * 70, 100 * i + 50.5, revision_radius, 0, Math.PI*2, true);
					ctx.closePath();
					ctx.fill();
				}
			}
			
			//For each of the head docs we will check if they were created in this revision
			ctx.fillStyle = green;
			for (var j in doc_list)
			{
				if (doc_list[j].lds_revision_id == rev_history[i].id)
				{
					if (doc_list[j].enabled == 'yes')
					{
						//Draw the line that goes to the previous ball.
						if (lastYs[j] != -1)
						{
							ctx.strokeStyle = blue;
							ctx.FillStyle = blue;
							
							ctx.lineWidth = 5;
							ctx.beginPath();
							ctx.moveTo(revision_lines_offset + j * 70, lastYs[j]);
							ctx.lineTo(revision_lines_offset + j * 70, 100 * i + 50.5);
							ctx.closePath();
							ctx.stroke();
						}
						lastYs[j] = 100 * i + 50.5;
	
						ctx.beginPath();
						ctx.arc(revision_lines_offset + j * 70, 100 * i + 50.5, revision_radius, 0, Math.PI*2, true);
						ctx.closePath();
						ctx.fill();
						
						//Add the point to the revision points
						rev_points.push ({
							x: revision_lines_offset + j * 70,
							y: 100 * i + 50.5,
							link: baseurl + "pg/lds/viewrevision/" + doc_list[j].guid + '/'
						});
						
						//ctx.fillText("Id " + doc_list[j].guid, revision_lines_offset + j * 70, 100 * i + 70.5);
						
						if (!last_rev)
						{
							head_extenders.push (j);
						}
					}
				}
			}
		} //End revision loop

		//Draw revision extenders
		ctx.strokeStyle = lightgreen;
		ctx.fillStyle = lightgreen;
		for (var j = 0; j < head_extenders.length; j++)
		{
			//Draw the line that goes to the previous ball.
			if (lastYs[head_extenders[j]] != -1)
			{
				ctx.lineWidth = 5;
				ctx.beginPath();
				ctx.moveTo(revision_lines_offset + head_extenders[j] * 70, lastYs[head_extenders[j]] + revision_radius + 3);
				ctx.lineTo(revision_lines_offset + head_extenders[j] * 70, 100 * i + 50.5);
				ctx.closePath();
				ctx.stroke();
			}
			lastYs[head_extenders[j]] = 100 * i + 50.5;

			ctx.beginPath();
			ctx.arc(revision_lines_offset + head_extenders[j] * 70, 100 * i + 50.5, revision_radius, 0, Math.PI*2, true);
			ctx.closePath();
			ctx.fill();
			
			//Add the point to the revision points
			rev_points.push ({
				x: revision_lines_offset + head_extenders[j] * 70,
				y: 100 * i + 50.5,
				link: baseurl + "pg/lds/view/" + lds_id + "/doc/" + doc_list[head_extenders[j]].guid
			});
		}
	}  
});