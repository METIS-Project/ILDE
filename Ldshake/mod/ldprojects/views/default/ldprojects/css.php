<?php
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
?>
/****************************/
/*       USER LISTING       */
/****************************/

#ldshakers_add_group_popup {
	z-index: 100000;
	position: fixed;
	width: 300px;
	background-color: #fff;
	padding: 10px;
	top: 50%;
	left: 50%;
	margin-top: -150px;
	margin-left: -160px;
	border: 10px solid rgba(120,120,120,0.42);
	-moz-border-radius: 5px 5px 5px 5px;
	-webkit-border-radius: 5px 5px 5px 5px;
	border-radius: 5px 5px 5px 5px;
	-webkit-background-clip: padding-box;
    -moz-background-clip: padding;
    background-clip: padding-box;
	display: none;
}

.ldshakers_add_group_popup_fieldname {
	color: #777;
	font-size: 13px;
	margin-bottom: 3px;
}

.ldshakers_add_group_popup_section {
	margin-bottom: 15px;
}

#ldshakers_list_header {
	background-color: #C7D4A3;
	padding: 2px 2px 3px 2px;
	/*border-bottom: 1px solid #aaa;*/
}

#ldshakers_list {
	padding: 0;
	margin: 0;
    border-left: 1px solid #ccc;
    border-right: 1px solid #ccc;
    border-top: 1px solid #ccc;
}

#ldshakers_list>li.noresults {
	padding: 0;
	margin: 0;
    border-left: 1px solid #ccc;
    border-right: 1px solid #ccc;
}


#ldshakers_list>li {
	padding: 2px;
	margin: 0;
	background-color: #eee;
	border-bottom: 1px solid #ccc;
}

#ldshakers_list .profilepic {
	display: block;
	float: left;
	margin: 8px;
}

.ldshakers_select {
	display: block;
	float: left;
	margin-top: 20px;
}

.ldshakers_user_name {
	font-weight: bold;
	display: block;
	float: left;
	margin-top: 10px;
}

#ldshakers_add_to_list {
	position: absolute;
	top: 100px;
	left: 100px;
	border: 1px solid;
	border-color: #eee #ccc #ccc #eee;
	background-color: #fff;
	display: none;
}

#ldshakers_add_to_list>ul {
	padding: 0;
	margin: 0;
}

#ldshakers_add_to_list>ul a {
	display: block;
	text-decoration: none;
	color: #000;
	padding: 5px;
}

#ldshakers_add_to_list>ul a:hover {
	color: #fff;
	background-color: #005301;
}

.ldshakers_popup_info,.ldshakers_popup_unavailable {
	padding: 5px;
	color: #777;
}

.starring_in {
	float: right;
	margin: 18px 5px 0 0;
}

.starring_in a {
	display: inline-block;
	padding: 3px 5px;
	background-color: #fff;
	color: #000;
	font-size: 11px;
	border: 1px solid #999;
	text-decoration: none;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
}

/****************************/
/*       COMMON             */
/****************************/

#profile_actions {
	padding: 0;
	margin: 20px 0;
}

/****************************/
/*       COMMON             */
/****************************/

.popup_header {
	font-weight: bold;
	margin-bottom: 10px;
}

.close_popup {
	float: right;
	font-size: 12px;
}

#shade {
	position: fixed;
	z-index: 100;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: #444;
	opacity: 0.5;
	filter: alpha(opacity=50);
	display: none;
}

.errnote {
	color: #c00;
	display: none;
}

.groupicon {
    float: left;
    margin: 0 15px 0 0;
}