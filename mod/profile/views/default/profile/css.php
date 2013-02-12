<?php

?>

/* ***************************************
	AVATAR CONTEXTUAL MENU
*************************************** */	
#profile_icon_wrapper {
	float:left;
}
	
.usericon {
	position:relative;
}

.avatar_menu_button {
	width:15px;
	height:15px;
	position:absolute;
	cursor:pointer;
	display:none;
	right:0;
	bottom:0;
}
.avatar_menu_arrow {
	background: url(<?php echo $vars['url']; ?>_graphics/avatar_menu_arrows.gif) no-repeat left top;
	width:15px;
	height:15px;
}
.avatar_menu_arrow_on {
	background: url(<?php echo $vars['url']; ?>_graphics/avatar_menu_arrows.gif) no-repeat left -16px;
	width:15px;
	height:15px;
}
.avatar_menu_arrow_hover {
	background: url(<?php echo $vars['url']; ?>_graphics/avatar_menu_arrows.gif) no-repeat left -32px;
	width:15px;
	height:15px;
}
.usericon div.sub_menu { 
	display:none; 
	position:absolute; 
	padding:2px; 
	margin:0; 
	border-top:solid 1px #E5E5E5; 
	border-left:solid 1px #E5E5E5; 
	border-right:solid 1px #999999; 
	border-bottom:solid 1px #999999;  
	width:160px; 
	background:#FFFFFF; 
	text-align:left;
}
div.usericon a.icon img {
	z-index:10;
}

.usericon div.sub_menu a {margin:0;padding:2px;}
.usericon div.sub_menu a:link, 
.usericon div.sub_menu a:visited, 
.usericon div.sub_menu a:hover{ display:block;}	
.usericon div.sub_menu a:hover{ background:#cccccc; text-decoration:none;}

.usericon div.sub_menu h3 {
	font-size:1.2em;
	padding-bottom:3px;
	border-bottom:solid 1px #dddddd;
	color: #4690d6;
	margin:0 !important;
	background:#ffffff !important;
}
.usericon div.sub_menu h3:hover {
	background:#cccccc !important;
}

.user_menu_addfriend,
.user_menu_removefriend,
.user_menu_profile,
.user_menu_friends,
.user_menu_friends_of,
.user_menu_blog,
.user_menu_file,
.user_menu_messages,
.user_menu_admin,
.user_menu_pages {
	margin:0;
	padding:0;
}
.user_menu_admin {
	border-top:solid 1px #dddddd;
}
.user_menu_admin a {
	color:#cc0033;
}