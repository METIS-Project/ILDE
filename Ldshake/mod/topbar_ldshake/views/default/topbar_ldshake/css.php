#ldshake_topbar {
	height: 40px;
	/*background: #002E02;*/
	background: url("<?php echo $vars['url']; ?>mod/topbar_ldshake/graphics/bg-header.png") repeat-x scroll left top #002E02;
	font-size: 13px;
}

#ldshake_topbar_wrapper {
	margin: 0 auto;
	width: 997px;
	padding: 3px 0;
}

#ldshake_topbar_logo {
	float: left;
	margin-right: 30px;
}

#ldshake_topbar_container_left {
	float: left;
	width: 580px;
	position: relative;
}

#ldshake_topbar_container_right {
	float: right;
}

#ldshake_topbar #toolbarlinks #toolbar_options {
	margin: 0;
	margin-top: 9px;
}

#ldshake_topbar #toolbarlinks #toolbar_options li {
	float: left;
	margin-right: 20px;
}

#ldshake_topbar a {
	color: #bbb;
}

#ldshake_topbar a:hover {
	color: #fff;
	text-decoration: none;
}

#ldshake_topbar_avatar {
	margin: 3px 10px 0 0;
	float: left;
}

#ldshake_topbar_avatar img {
	border: 1px solid #777;
}


#ldshake_topbar_user_options {
	display: block;
	margin: 9px 5px 0 0;
	padding-right: 15px;
	background: url("<?php echo $vars['url']; ?>mod/topbar_ldshake/graphics/down-arrow.png") no-repeat scroll right 7px;
	max-width: 150px;
	overflow: hidden;
	white-space: nowrap;
	float: left;
}

.menu_active {
	color: #fff !important;
}

#ldshake_topbar_user_menu {
	display: none;
	position: absolute;
	top: 40px;
	z-index: 10000;
	background-color: #19371A;
	padding: 15px 10px 10px 10px;
	width: 120px;
	margin-top: 0;
	-moz-border-radius: 0 0 5px 5px;
	-webkit-border-radius: 0 0 5px 5px;
	border-radius: 0 0 5px 5px;
	-webkit-box-shadow: 0 1px 4px #666;
	-moz-box-shadow: 0 1px 4px #666;
	box-shadow: 0 1px 4px #666;
}

#ldshake_topbar_user_menu ul {
	margin: 0;
	padding: 0;
}

#ldshake_topbar_user_menu li {
	margin-bottom: 5px;
}

#ldshake_topbar_serach {
	float: left;
	margin: 4px 35px 0 0;
}

#ldshake_topbar_search_input {
	-webkit-border-radius: 4px; 
	-moz-border-radius: 4px;
	border-radius: 4px;
	background-color: #eaeaea;
	border-top: 1px solid #999;
	border-left: 1px solid #999;
	border-right: 1px solid #fff;
	border-bottom: 1px solid #fff;
	color: #777;
	font-size: 13px;
	margin: 0;
	padding: 2px;
	width: 200px;
	height: 19px;
	text-shadow: 0px 1px 0px #fff;
}

#ldshake_topbar_search_input:focus {
	-webkit-box-shadow: 0 0 10px #46B800;
	-moz-box-shadow: 0 0 10px #46B800;
	box-shadow: 0 0 10px #46B800;
}

#ldshake_topbar_search_input.written {
	color: #000;
	text-shadow: none;
}

#ldshake_topbar_search_submit {
	position: relative;
	left: 205px;
	top: 3px;
}

#ldshake_topbar a.privatemessages {
    background:transparent url(<?php echo $vars['url']; ?>_graphics/toolbar_messages_icon.gif) no-repeat left 2px;
    padding:0 0 4px 16px;
    cursor:pointer;
}

#ldshake_topbar a.privatemessages:hover {
    text-decoration: none;
    background:transparent url(<?php echo $vars['url']; ?>_graphics/toolbar_messages_icon.gif) no-repeat left -36px;
}

#ldshake_topbar a.privatemessages_new {
    background:transparent url(<?php echo $vars['url']; ?>_graphics/toolbar_messages_icon.gif) no-repeat left -17px;
    padding:0 0 0 20px;
    color:white;
}

#toolbar_lds_types {
	position: absolute;
	display: none;
	background-color: #19371A;
	padding: 15px;
	background-color: #19371A;
    border-radius: 0 0 5px 5px;
    box-shadow: 0 1px 4px #666666;
    top: 37px;
    left: 110px;
}

#toolbar_lds_types ul {
	margin: 0;
	padding: 0;
}

#toolbar_lds_types li {
	margin-bottom: 5px;
}