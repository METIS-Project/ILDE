/* -------------------------------------------------- *
 * Collapsorz 1.1
 * Updated: 04/21/09
 * -------------------------------------------------- *
 * Author: Aaron Kuzemchak
 * URL: http://aaronkuzemchak.com/
 * Copyright: 2008-2009 Aaron Kuzemchak
 * License: MIT License
** -------------------------------------------------- */

(function($) {
	$.fn.collapsorz = function(options) {
		// default settings
		var defaults = {
			toggle: "> *", // elements inside the object to toggle
			minimum: 5, // number to show in collapsed form
			showText: "Show", // text for the expand link
			hideText: "Hide", // text for the collapse link
			linkLocation: "after", // use "after" or "before" to determine where link displays
			defaultState: "collapsed", // use "collapsed" or "expanded" to show or hide items by default
			wrapLink: null // specify HTML code to wrap around the link
		};
		var options = $.extend(defaults, options);
		
		return this.each(function() {
			// only execute if there are more than minimum items
			if($(options.toggle, this).length > options.minimum) {
				// setup variables
				var $obj = $(this);
				var $targets = $(options.toggle, this);
			
				// hide the items if necessary
				if(options.defaultState == "collapsed") {
					$targets.filter(":gt("+(options.minimum-1)+")").hide();
				}
				
				// append/prepend the toggle link to the object
				var $toggler = $('<a href="#" class="toggler"></a>');
				if(options.linkLocation == "before") {
					$obj.before($toggler);
				}
				else {
					$obj.after($toggler);
				}
				if(options.wrapLink) {
					$toggler.wrap(options.wrapLink);
				}
				
				// set data, link class, and link text
				if(options.defaultState == "expanded") {
					$obj.data("status", "expanded");
					$toggler.addClass("expanded");
					$toggler.html(options.hideText);
				}
				else {
					$obj.data("status", "collapsed");
					$toggler.addClass("collapsed");
					$toggler.html(options.showText);
				}
				
				// click actions
				$toggler.click(function() {
					if($obj.data("status") == "collapsed") {
						$targets.filter(":hidden").show();
						$toggler.html(options.hideText);
						$obj.data("status", "expanded");
					}
					else if($obj.data("status") == "expanded") {
						$targets.filter(":gt("+(options.minimum-1)+")").hide();
						$toggler.html(options.showText);
						$obj.data("status", "collapsed");
					}
					$(this).toggleClass("collapsed").toggleClass("expanded");
					return false;
				});
			}
		});
	}
})(jQuery);