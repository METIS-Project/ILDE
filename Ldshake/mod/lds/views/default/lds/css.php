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
/*       MY LISTING         */
/****************************/

#my_lds_list {
	padding: 0;
	margin: 0;
    border-left: 1px solid #ccc;
    border-right: 1px solid #ccc;
}

#my_lds_list>li {
	padding: 2px 2px 3px 2px;
	margin: 0;
	background-color: #eee;
	border-bottom: 1px solid #ccc;
	height: 22px;
    overflow: hidden;
}

#my_lds_list>li.new {
	padding: 2px 2px 3px 2px;
	margin: 0;
	background-color: #fff;
	border-bottom: 1px solid #ccc;
}

#my_lds_list>li.new .lds_title {
	font-weight: bold;
}

.lds_locked {
	filter: alpha(opacity=60);
	opacity: 0.6;
}

.lds_padded {
	padding-left: 37px;
}

#my_lds_list .lds_select {
	float: left;
	display: block;
	margin: 4px 8px 0 4px;
	padding: 0;
}

#my_lds_list .lds_select_spacer {
	float: left;
	margin: 4px 8px 0 4px;
	padding: 0;
	width: 13px;
	height: 13px;
}

#my_lds_list_header {
	background-color: #C7D4A3;
	padding: 2px 2px 3px 2px;
	border-bottom: 1px solid #aaa;
}

#my_lds_list .lds_list_element .lds_icon {
	display: block;
	float: left;
	padding-top: 1px;
}

#my_lds_list .lds_info {
	padding: 3px 0 0 25px;
}

#my_lds_list .lds_edit_action {
	/*
    display: block;
	float: left;
    padding-top: 2px;
    width: 29px;
    */
	font-size: 11px;
	padding-left: 5px;
    padding-right: 3px;
	visibility: hidden;
}

#my_lds_list .lds_implement_action {
    diaplay: block;
    float: left;
    font-size: 11px;
    padding-left: 5px;
    padding-top: 2px;
    width: 89px;
}

#my_lds_list .lds_title_tags {
	display: block;
	width: 397px;
	float: left;
	white-space: nowrap;
	overflow: hidden;
}

#my_lds_list .lds_title_tags.implementable {
    width: 307px;
}

#my_lds_list .lds_title_tags:hover {
    overflow: visible;
    position: relative;
    z-index: 9999;
}

#my_lds_list .lds_locked .lds_title_tags {
	display: block;
	width: 307px;
	float: left;
	white-space: nowrap;
	overflow: hidden;
}

#my_lds_list .lds_title {
	/*display: block;
	float: left;*/
	margin-right: 5px;
}

#my_lds_list .lds_title {
    background-color: #EEE;
}

#my_lds_list .lds_title.new {
    background-color: #FFF;
}

.lds_small_tag {
	padding: 1px 3px;
	margin-right: 3px;
	text-decoration: none;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
	font-size: 10px;
	display: inline-block;
	vertical-align: 2px;
	/*float: left;*/
}

#my_lds_list .lds_people {
	display: block;
	padding-left: 5px;
	padding-top: 2px;
	white-space: nowrap;
	overflow: hidden;
	width: 199px;
	color: #777;
	float: left;
	font-size: 11px;
}

#my_lds_list .lds_editing_by {
	display: none;
	padding-left: 5px;
	padding-top: 2px;
	white-space: nowrap;
	overflow: hidden;
	width: 229px;
	color: #777;
	float: left;
	font-size: 11px;
}

#my_lds_list .lds_date {
	float: left;
	padding-right: 3px;
	width: 85px;
	overflow: hidden;
	white-space: nowrap;
	text-align: right;
}

.some_selected {
	opacity: 0.5;
	filter: alpha(opacity=50);
}

/****************************/
/*       LISTING            */
/****************************/

#left_filters {
	margin-top: 80px;
}

#left_filters h3 {
	font-size: 14px;
}

#left_filters .tag_selector {
	margin: 10px 0 30px 0;
	padding: 0;
}

#left_filters .tag_selector li {
	margin: 3px 0;
}

#left_filters .tag_selector a {
	display: inline-block;
	max-width: 145px;
	overflow: hidden;
	white-space: nowrap;
}

#left_filters .tag_selector .freq {
	display: block;
	float: right;
	margin-right: 10px;
}

.filters {
	height: 40px;
}

.filters .paging {
	float: right;
}

#lds_list {
	padding: 0;
}

#lds_list .lds_list_element {
	padding: 5px;
	margin-bottom: 10px;
	background-color: #eee;
}

#lds_list .lds_list_element .lds_icon {
	display: block;
	float: left;
	padding-top: 5px;
	margin-right: 10px;
	width: 70px;
}

#lds_list .lds_list_element .lds_info {
	padding-top: 5px;
	float: left;
	width: 650px;
	magin-right: 10px;
}

#lds_list .lds_info .tagarea {
	padding: 0;
	color: #000;
	font-size: 11px;
}

#lds_list .lds_info .tagarea li {
	margin: 8px 0;
}

#lds_list .lds_info .authory {
	color: #777;
	font-size: 11px;
	padding-bottom: 5px;
}

#lds_list .lds_list_element .lds_actions {
	padding-top: 5px;
	margin-right: 5px;
	float: right;
	text-align: right;
}

#lds_list .lds_list_element .lds_actions .indicators {
	padding-top: 10px;
	font-size: 11px;
}

#lds_list .lds_list_element .lds_title {
	font-size: 14px;
	font-weight: bold;
}

.lds_sticker {
	font-size: 11px;
	padding: 2px 4px;
	color: #fff;
	display: inline-block;
	margin: 5px 0 0 10px;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
}

.lds_sticker.mine {
	border: 1px solid #333;
	background-color: #333;
}

.lds_sticker.canedit {
	border: 1px solid #777;
	background-color: #777;
}

/****************************/
/*       FORMS              */
/****************************/

#lds_edit_form {
	font-size: 12px;
}

#lds_edit_buttons {
	float: right;
	padding-right: 25px;
}

#lds_edit_buttons.busy {
	background:url("<?php echo $vars['url'] ?>_graphics/spinner.gif") no-repeat scroll right center transparent;
}

#lds_edit_buttons input {
	margin: 0;
}

#lds_edit_middlebar {
	margin-top: 10px;
}

#lds_edit_contents {
	margin-top: 10px;
}

#lds_edit_title {
	width: 500px;
	font-size: 18px;
	float: left;
}

#lds_edit_share_info {
	font-size: 11px;
	color: #999;
	float: left;
	padding-left: 10px;
}

#lds_edit_tags {
	width: 340px;
	padding: 5px;
	overflow: hidden;
	white-space: nowrap;
	cursor: pointer;
}

#lds_edit_tags .tooltip {
	font-style: italic;
	color: #999;
}

#lds_edit_tags.over {
	background-color: #ffc;
}

#lds_edit_tags_popup {
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

#lds_edit_tags_popup_header {
	font-weight: bold;
	margin-bottom: 10px;
}

#lds_edit_tags_popup_close {
	float: right;
	font-size: 12px;
}

.lds_edit_tags_popup_fieldname {
	color: #777;
	font-size: 13px;
	margin-bottom: 3px;
}

.lds_edit_tags_popup_section {
	margin-bottom: 15px;
}

.slider_wrapper {
	float: right;
	width: 280px;
	margin-left: 10px;
	padding: 5px;
}

.slider_wrapper label {
	font-size: 12px;
	font-weight: normal;
	float: right;
	padding: 0 5px 0 0;
}

.slider_wrapper input {
	padding: 0 5px 0 0;
	font-size: 12px;
	border: 0;
	color: #000;
	font-weight: bold;
	width: 20px;
	float: right;
}

.lds_slider {
	float: right;
	width: 150px;
}

#lds_edit_body {
	height: 400px;
	width: 945px;
}

#lds_edit_tabs {
	background-color: #DFE6CB;
	border-left: 5px solid #fff;
	border-right: 5px solid #fff;
	padding: 0 5px;
	height: 30px;
}

#lds_edit_tabs_scrolling {
	white-space: nowrap;
	position: relative;
}

#lds_edit_tabs ul {
	margin: 0;
	padding: 0;
}

#lds_edit_tabs li {
	background-color: #fff;
	border-bottom: 1px solid #ccc;
	border-right: 1px solid #ccc;
	/*float: left;*/
	margin: 0 2px;
	padding: 5px 8px;
	-moz-border-radius: 0 0 5px 5px;
	-webkit-border-radius: 0 0 5px 5px;
	border-radius: 0 0 5px 5px;
	cursor: pointer;
	display: inline-block;
}

#lds_edit_tabs li.current {
	font-weight: bold;
}

#lds_edit_tabs .lds_tab_options {
	color: #999;
	font-weight: normal;
	font-size: 10px;
}

#lds_edit_tabs_popup {
	position: absolute;
	z-index: 10000;
	background-color: #fff;
	padding: 0;
	border-top: 1px solid #eee;
	border-left: 1px solid #eee;
	border-bottom: 1px solid #ccc;
	border-right: 1px solid #ccc;
	display: none;
}

#lds_edit_tabs_popup ul {
	margin: 0;
	padding: 0;
}

#lds_edit_tabs_popup li {
	margin: 0;
	font-size: 12px;
}

#lds_edit_tabs_popup a {
	padding: 5px;
	display: block;
	color: #000;
	text-decoration: none;
}

#lds_edit_tabs_popup a:hover {
	display: block;
	background-color: #005301;
	color: #fff;
	text-decoration: none;
}

/****************************/
/*       REVISION           */
/****************************/

#revision_graph_wrapper {
	width: 960px;
	height: 500px;
	overflow: scroll;
	clear: left;
}

#revision_graph_titles {
	padding-left: 250px;
	border-bottom: 1px solid #aaa;
	height: 100px;
	margin-bottom: 0;
}

#revision_graph_titles li {
	padding: 0;
	float: left;
	-moz-transform:rotate(-90deg);
	-webkit-transform:rotate(-90deg);
	-o-transform: rotate(-90deg);
	transform:rotate(-90deg);
	width: 70px;
	height: 100px;
	font-size: 11px;
}

#revision_graph_avatars {
	display: none;
}

#iefallback_list {
	padding: 0;
	margin: 10px 0 0 0;
}

.iefallback_revpill {
	font-size: 11px;
	padding: 5px;
	border: 1px solid #ccc;
	width: 140px;
	float: left;
	height: 42px;
	margin: 0 10px 10px 0;
}

.iefallback_box {
	width: 70px;
	height: 64px;
	float: left;
	text-align: center;
	font-size: 40px;
}

.iefallback_box.head {
	color: rgb(0, 200, 0);
}

.iefallback_box.rev {
	color: rgb(0, 0, 200);
}

.iefallback_box.del {
	color: rgb(200, 0, 0);
}

.iefallback_box.ext {
	color: rgb(208, 242, 208)
}

.iefallback_box.lu {
	background:url("<?php echo $vars['url'] ?>mod/lds/images/iefallback_bgu.gif") no-repeat scroll right center transparent;	
}

.iefallback_box.lud {
	background:url("<?php echo $vars['url'] ?>mod/lds/images/iefallback_bgud.gif") no-repeat scroll right center transparent;	
}

.iefallback_box.ld {
	background:url("<?php echo $vars['url'] ?>mod/lds/images/iefallback_bgd.gif") no-repeat scroll right center transparent;	
}

.iefallback_box.l {
	background:url("<?php echo $vars['url'] ?>mod/lds/images/iefallback_bg.gif") no-repeat scroll right center transparent;	
}

.iefallback_revpill .icon {
	float: left;
	margin-right: 5px;
}

ins {
	background-color: #6FFD6F;
	display: inline-block;
	text-decoration: none;
}

ins img, del img {
	/*padding: 5px;*/
	opacity: 0.5;
	filter:alpha(opacity=50);
}

del {
	background-color: #fcc;
	display: inline-block;
	text-decoration: none;
}

#diff_wrapper {
	display: none;
}

#lds_revision_details {
	font-size: 13px;
	background-color: #ffc;
	padding: 5px;
	margin-top: 20px;
}

#lds_revision_details img {
	vertical-align: middle;
}

#previous_revision_details {
	display: none;
}

#lds-rev-toggle-diff {
	vertical-align: middle;
}

/****************************/
/*       VIEW               */
/****************************/

#lds_view_tabs {
	margin: 5px 0 0 0;
	padding: 0 2px;
	background-image: url(<?php echo $vars['url'] ?>mod/lds/images/tabsbg.png);
	background-position: center bottom;
    background-repeat: repeat-x;
    height: 27px;
    white-space: nowrap;
    position: relative;
}

#lds_view_tabs li {
	display: inline-block;
}

#lds_view_tabs a {
	/*display: block;
	float: left;*/
	font-size: 13px;
	padding: 5px;
	margin: 0 2px;
	border-top: 1px solid #cfcfcf;
	border-left: 1px solid #cfcfcf;
	border-right: 1px solid #cfcfcf;
	-moz-border-radius: 5px 5px 0 0;
	-webkit-border-radius: 5px 5px 0 0;
	border-radius: 5px 5px 0 0;
	height: 15px;
	color: #000;
	background-color: #f5f5f5;
}

#lds_view_tabs a:hover {
	text-decoration: none;
	background-color: #fff;
}

#lds_view_tabs li.activetab {
	/*float: left;*/
	font-size: 13px;
	padding: 5px;
	margin: 0 2px;
	border-top: 1px solid #aaa;
	border-left: 1px solid #aaa;
	border-right: 1px solid #aaa;
	height: 16px;
	background-color: #fff;
	font-weight: bold;
	-moz-border-radius: 5px 5px 0 0;
	-webkit-border-radius: 5px 5px 0 0;
	border-radius: 5px 5px 0 0;
}

#lds_view_tabs a.infotab {
	/*float: left;*/
	font-size: 13px;
	padding: 5px;
	margin: 0 2px;
	border-top: 1px solid #cfcfcf;
	border-left: 1px solid #cfcfcf;
	border-right: 1px solid #cfcfcf;
	height: 15px;
	background-color: #DFE6CB;
	-moz-border-radius: 5px 5px 0 0;
	-webkit-border-radius: 5px 5px 0 0;
	border-radius: 5px 5px 0 0;
}

#lds_view_tabs a.infotab:hover {
	background-color: #ECF2DA;
}

#lds_view_tabs li.infotab {
	background-color: #DFE6CB;
}

#the_lds_wrapper {
margin-top: 20px;
border: 1px solid #777;
box-shadow: 2px 2px 1px #ccc;
-webkit-box-shadow: 2px 2px 1px #ccc;
-moz-box-shadow: 2px 2px 1px #ccc;
width: 955px;
}

#the_lds {
	margin: 0px 18px 0px 19px;
	border: 0px;
    width: 918px;
}

#lds_info_wrapper {
	background-color: #DFE6CB;
	padding: 10px;
	font-size: 13px;
}

#lds_info_wrapper ul.paramarea {
	float: right;
	padding: 0;
}

#lds_info_wrapper ul.tagarea {
	padding: 0;
}

#lds_info_wrapper ul.tagarea li {
	margin-bottom: 5px;
}

#lds_view_actions {
	height: 20px;
	font-size: 13px;
}

.lds_view_tab_actions {
	font-size: 13px;
	background-color: #ffc;
	padding: 5px;
	margin-top: 10px;
}

.lds_view_tab_actions input {
	font-size: 13px;
	padding: 3px;
	width: 180px;
}

/****************************/
/*  VIEW - CKEDITOR STYLES  */
/****************************/

#the_lds {
	font-family: Helvetica, Arial, sans-serif;
	font-size: 12pt;
	line-height: 1.5em;
	color: #000;
}

#the_lds h1 {
  display: block;
  font-size: 2em;
  font-weight: bold;
  margin: .67em 0;
}

#the_lds h2 {
  display: block;
  font-size: 1.5em;
  font-weight: bold;
  margin: .83em 0;
}

#the_lds h3 {
  display: block;
  font-size: 1.17em;
  font-weight: bold;
  margin: 1em 0;
}

#the_lds h4 {
  display: block;
  font-weight: bold;
  margin: 1.33em 0;
}

#the_lds h5 {
  display: block;
  font-size: 0.83em;
  font-weight: bold;
  margin: 1.67em 0;
}

#the_lds h6 {
  display: block;
  font-size: 0.67em;
  font-weight: bold;
  margin: 2.33em 0;
}

#the_lds ul, #the_lds menu, #the_lds dir {
  display: block;
  list-style-type: disc;
  margin: 1em 0;
  -moz-padding-start: 40px;
}

#the_lds ol {
  display: block;
  list-style-type: decimal;
  margin: 1em 0;
  -moz-padding-start: 40px;
}

#the_lds li {
  display: list-item;
}

/*
#the_lds table {
	border-width: 1px;
	border-style: outset;
	border-spacing: 1px;
}

#the_lds td {
	border-width: 1px;
	border-style: inset;
}
*/

#the_lds a {
	color: #00E;
	text-decoration: underline;
}

#the_lds p {
	margin: 16px 0;
}

/****************************/
/*       1st STEPS          */
/****************************/

#fs_top_banner {
	background-color: #D0F5A9;
	padding: 20px 10px 5px 20px;
	border-bottom: 1px solid #9cc96d;
	margin: -20px -20px 20px -20px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
	-moz-box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
	-webkit-box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

#fs_top_banner h3 {
	font-size: 36px;
	color: #435b00;
	display: block;
	float: left;
	padding-top: 6px;
}

#fs_top_banner p {
	font-size: 17px;
	font-weight: bold;
	margin: 5px 0 5px 50px;
	width: 360px;
	display: block;
	float: left;
}

#fs_never_again {
	clear: both;
	text-align: right;
	font-size: 11px;
}

#fs_actions li {
	margin: 10px 0;
}

#fs_actions .fs-clearfloat {
	clear: both;
	border-bottom: 1px solid #ccc;
	margin-left: 130px;
	padding-top: 5px;
	margin-right: 20px;
}

#fs_actions li.activestep h4 {
	color: #227722;
}

#fs_actions li.activestep .fs-pill {
	opacity: 0.7;
}

#fs_actions li.activestep .callforaction {
	visibility: visible;
}

#fs_actions li h4 {
	font-size: 22px;
	color: #030;
	padding-top: 35px;
	margin-bottom: 10px;
}

.fs-pill
{
	float: left;
	margin: 15px;
	padding-top: 20px;
	text-align: center;
	font-weight: bold;
	width: 100px;
	height: 80px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2), 0 10px 20px rgba(255, 255, 255, 0.6) inset, 0 1px 1px rgba(0, 0, 0, 0.3) inset;
	-moz-box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2), 0 10px 20px rgba(255, 255, 255, 0.6) inset, 0 1px 1px rgba(0, 0, 0, 0.3) inset;
	-webkit-box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2), 0 10px 20px rgba(255, 255, 255, 0.6) inset, 0 1px 1px rgba(0, 0, 0, 0.3) inset;
	font-size: 14px;
}

.callforaction {
	display: block;
	float: right;
	-moz-border-radius: 5px 5px 5px 5px;
	-webkit-border-radius: 5px 5px 5px 5px;
	border-radius: 5px 5px 5px 5px;
	background-color: #eaeaea;
    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#dfdfdf');
	background: -webkit-gradient(linear, center top, center bottom, from(#fafafa), to(#dfdfdf));
    background: -moz-linear-gradient(center top , #fafafa, #dfdfdf) repeat scroll 0 0 #f6f6f6;
    border: 1px solid #ccc;
    color: #000;
	padding: 5px 7px;
	font-size: 16px;
	margin-top: 45px;
	margin-right: 20px;
	text-decoration: none;
	visibility: hidden;
}

.callforaction:hover {
	color: #000;
	text-decoration: none;
	border: 1px solid #999;
}

.callforaction:active {
	border: 1px solid #999;
	background-color: #dadada;
	color: #000;
    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#dfdfdf', endColorstr='#fafafa');
	background: -webkit-gradient(linear, center top, center bottom, from(#dfdfdf), to(#fafafa));
    background: -moz-linear-gradient(center top , #dfdfdf, #fafafa) repeat scroll 0 0 #f6f6f6;
}

/****************************/
/*       COMMON             */
/****************************/


#lds_single_share_popup {
	width: 500px;
	top: 150px;
	left: 50%;
	margin-left: -260px;
}

#implement_popup {
    width: 500px;
    margin-left: -260px;
}

.lds_popup {
    top: 100px;
    left: 50%;
    z-index: 100000;
    position: fixed;
    background-color: #fff;
    padding: 10px;
    border: 10px solid rgba(120,120,120,0.42);
    -moz-border-radius: 5px 5px 5px 5px;
    -webkit-border-radius: 5px 5px 5px 5px;
    border-radius: 5px 5px 5px 5px;
    -webkit-background-clip: padding-box;
    -moz-background-clip: padding;
    background-clip: padding-box;
    display: none;
    font-size: 13px;
}

.close_popup {
	float: right;
}

.remove_contributor {
	display: block;
	padding: 3px 5px 0 0;
	margin: 0 10px 0 10px;
	float: right;
	font-size: 24px;
}

.remove_contributor:hover {
	text-decoration: none;
}

.can_view_option {
	display: block;
	float: left;
	-moz-border-radius: 3px 0 0 3px;
	-webkit-border-radius: 3px 0 0 3px;
	border-radius: 3px 0 0 3px;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#dfdfdf');
	background: -webkit-gradient(linear, center top, center bottom, from(#fafafa), to(#dfdfdf));
    background: -moz-linear-gradient(center top , #fafafa, #dfdfdf) repeat scroll 0 0 #f6f6f6;
	border: 1px solid #ccc;
	color: #888;
	cursor: pointer;
	font-size: 13px;
	padding: 3px 5px;
	text-align: center;
	min-width: 65px;
}

.can_view_option:hover {
	text-decoration: none;
	color: #000;
	border: 1px solid #999;
}

.can_view_option:hover,.can_edit_option:hover {
	text-decoration: none;
	color: #000;
	border-top: 1px solid #999;
	border-right: 1px solid #999;
	border-bottom: 1px solid #999;
}

.can_view_option.pressed {
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#dfdfdf', endColorstr='#fafafa');
	background: -webkit-gradient(linear, center top, center bottom, from(#dfdfdf), to(#fafafa));
    background: -moz-linear-gradient(center top , #dfdfdf, #fafafa) repeat scroll 0 0 #f6f6f6;
	border: 1px solid #999;
	color: #000;
	font-weight: bold;
}

.can_edit_option {
	display: block;
	float: left;
	-moz-border-radius: 0 3px 3px 0;
	-webkit-border-radius: 0 3px 3px 0;
	border-radius: 0 3px 3px 0;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#dfdfdf');
	background: -webkit-gradient(linear, center top, center bottom, from(#fafafa), to(#dfdfdf));
    background: -moz-linear-gradient(center top , #fafafa, #dfdfdf) repeat scroll 0 0 #f6f6f6;
	border-top: 1px solid #ccc;
	border-right: 1px solid #ccc;
	border-bottom: 1px solid #ccc;
	color: #888;
	cursor: pointer;
	font-size: 13px;
	padding: 3px 5px;
	text-align: center;
	min-width: 65px;
}

.can_edit_option.pressed {
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#dfdfdf', endColorstr='#fafafa');
	background: -webkit-gradient(linear, center top, center bottom, from(#dfdfdf), to(#fafafa));
    background: -moz-linear-gradient(center top , #dfdfdf, #fafafa) repeat scroll 0 0 #f6f6f6;
	border-top: 1px solid #999;
	border-right: 1px solid #999;
	border-bottom: 1px solid #999;
	color: #000;
	font-weight: bold;
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

#added_contacts {
	height: 250px;
	overflow: auto;
	display: block;
	background-color: #DFE6CB;
	border: 1px solid #ccc;
	padding: 0;
}

#added_contacts li {
	margin: 0;
	padding: 3px;
	display: block;
	background-color: #fff;
	border-bottom: 1px solid #ccc;
}

#added_contacts li img {
	margin: 1px 10px 1px 1px;
	vertical-align: middle;
}

.sharing_field {
	margin-bottom: 10px;
}

.floated-field {
	float: left;
	width: 240px;
}

.usersuggestbox {
	width: 230px;
}

.share_type_switch {
	display: block;
	float: right;
	margin: 9px 10px 0 0;
}

.static_label {
	display: block;
	float: right;
	margin: 12px 10px 0 0;
}

.suggest_dropdown {
	margin: 0;
	padding: 0;
	border: 1px solid #999;
	position: fixed;
	background-color: #fff;
	width: 250px;
}

.suggest_dropdown li {
	padding: 3px;
}

.suggest_dropdown li img {
	margin-right: 10px;
	vertical-align: middle;;
}

.dropdown_selected {
	background-color: #aaf;
}

.hidden {
	display: none;
}

a.rightbutton,#lds_view_actions span {
	font-size: 13px;
	padding: 3px 5px;
}

a.leftbutton {
	font-size: 13px;
	padding: 3px 5px;
}

a.rightbutton {
	display: block;
	float: right;
	border-radius: 3px 3px 3px 3px;
	-moz-border-radius: 3px 3px 3px 3px;
	-webkit-border-radius: 3px 3px 3px 3px;
	background-color: #eaeaea;
    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#dfdfdf');
	background: -webkit-gradient(linear, center top, center bottom, from(#fafafa), to(#dfdfdf));
    background: -moz-linear-gradient(center top , #fafafa, #dfdfdf) repeat scroll 0 0 #f6f6f6;
    border: 1px solid #ccc;
    color: #000;
    margin-left: 10px;
}

a.rightbutton:hover {
	text-decoration: none;
	border: 1px solid #999;
}

a.rightbutton:active {
	border: 1px solid #999;
	background-color: #dadada;
    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#dfdfdf', endColorstr='#fafafa');
	background: -webkit-gradient(linear, center top, center bottom, from(#dfdfdf), to(#fafafa));
    background: -moz-linear-gradient(center top , #dfdfdf, #fafafa) repeat scroll 0 0 #f6f6f6;
}

.lds_loading
{
	background-image: url(<?php echo $vars['url'] ?>_graphics/spinner.gif);
	width: 16px;
	height: 16px;
	float: right;
	display: none;
	margin-left: 5px;
}

.lds_tag {
	padding: 2px 5px;
	margin-right: 3px;
	text-decoration: none;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
}

.lds_order_by {
    float: right;
    color: #930;
    margin: 10px 20px 20px 5px;
    font-size: 8pt;
}

#left_filters .tag_selector li.lds-browse-non-used-tags-hide {
    display: none;
}

.lds-browse-show-tags {
    cursor: pointer;
}

.lds_tag.discipline,.lds_small_tag.discipline {
	background-color: #090;
	color: #fff;
}

.lds_tag.pedagogical_approach,.lds_small_tag.pedagogical_approach {
	background-color: #009;
	color: #fff;
}

.lds_tag.tags,.lds_small_tag.tags {
	background-color: #D49100;
	color: #fff;
}

#error_wrapper {
	padding: 50px 0 0 20px;
}

#error_wrapper ul {
	list-style-type: disc;
}

#error_wrapper li {
	margin-bottom: 5px;
}

#lds_side_sections_prj
{
    padding-top: 80px;
}

#lds_side_sections
{
	padding-top: 20px;
}

#lds_side_sections_imp
{
    padding-top: 20px;
}

#lds_side_sections a,
#lds_side_sections_prj a,
#lds_side_sections_imp a
{
	display: block;
	text-decoration: none;
	color: #000;
	padding: 4px;
}

#lds_side_sections a:hover,
#lds_side_sections_prj a:hover,
#lds_side_sections_imp a:hover

{
	display: block;
	text-decoration: none;
	color: #000;
	background-color: #eee;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
}

#lds_side_sections a.current,
#lds_side_sections_prj a.current,
#lds_side_sections_imp a.current
{
	display: block;
	text-decoration: none;
	background-color: #930;
	font-weight: bold;
	color: #fff;
	padding: 4px;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
}

#lds_side_sections a.current:hover,
#lds_side_sections_prj a.current:hover,
#lds_side_sections_imp a.current:hover
{
	display: block;
	text-decoration: none;
	background-color: #930;
}

/* bl stands for Bottom Left */
.tooltip_bl
{
	position: absolute;
	z-index: 10000;
	top: 0;
	left: 0;
	display: none;
}

.tooltip_bl_stem
{
	background: url("<?php echo $vars['url'] ?>mod/lds/images/stem.png") no-repeat scroll center top transparent;
	height: 12px;
}

.tooltip_bl_body
{
	border: 6px solid #458225;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
	background-color: #fff;
	padding: 10px;
}

.tooltip_bl h4
{
	font-size: 14px;
	margin-bottom: 5px;
}

.tooltip_bl p
{
	margin: 10px 0;
}

.tooltip_bl ol
{
	list-style-type: decimal;
}

.browseralert {
	background-color: #ffa;
	padding: 20px;
	font-size: 13px;
}

#lds_loading_contents {
	background-color: #000;
	padding: 5px 7px;
	color: #fff;
	position: fixed;
	top: 0px;
	left: 20px;
	z-index: 101;
	font-weight: bold;
	font-size: 13px;
	-moz-border-radius: 0 0 7px 7px;
	-webkit-border-radius: 0 0 7px 7px;
	border-radius: 0 0 7px 7px;
}

.scrollable{
	position: relative;
	overflow: hidden;
}

.arrow {
	position: absolute;
	top: 8px;
	z-index: 1;
	padding-top: 8px;
	display: none;
	cursor: pointer;
	width: 20px;
	text-align: center;
	padding: 1px;
	background-color: #729C00;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	border-radius: 4px;
	color: #fff;
	border: 1px solid #777;
}

.arrow:hover {
	background-color: #8DBF04;
}

.arrow.right {
	right: 0;
}

.arrow.left {
	float: left;
}

#lds_export {
	display: table-cell;
	padding: 3px;
}

#comment_switcher {
    height: 0px;
	background-color: #DFE6CB;
	padding: 5px 10px;
	font-weight: bold;
    display:none;
}

#fs_xul {
	border-bottom: 1px solid #ccc;
}

#fs_xul>ul {
	margin: 10px 0;
	height: 270px;
}

#fs_xul li {
	float: left;
	margin: 5px;
	padding: 0;
	width: 220px;
	text-align: center;
}

#fs_xul li p {
	margin-top: 10px;
	font-size: 11px;
}

#fs_xul em {
	font-style: italic;
}

#fs_xul h2 {
	margin-bottom: 10px;
}

#fs_xul>p {
	margin: 5px 0;
}

.ldshake-listing-admin {
	color: green;
}

.ldshake-user-nonvalidated {
    color: black;
}

form#editorfileupload {
	float: right;
}

.user_menu_removefriend,
.user_menu_friends,
.user_menu_friends_of
{
	display: none;
}

input[type="submit"]:disabled,input[type="button"]:disabled {
    color: graytext;
    cursor: default;
    background: #F6F6F6;
}

/*** VLE form ***/
#lds_vle_page {
padding: 20px;
}

select[name="vle_name_select"] {
    border-radius: 4px;
    background-color: #FCFCFC;
    width: 75%;
    box-shadow: inset 1px 1px 2px rgba(0, 0, 0, 0.1);
    color: #333;
    border: 1px solid #CCC;
    font: 120% Arial, Helvetica, sans-serif;
}

input[name="vle_type"].readonly,
input[name="vle_url"].readonly {
    background: #EFEFEF !important;
}

.vle_form_input:focus {
    border: 1px solid #92F06D !important;
}

.lds_form_title {
font-size: 17px;
background-color: rgb(215, 223, 255);
padding: 3px;
border-bottom: 2px solid red;
color: rgb(71, 71, 71);
font-size: 19px;
}

#lds_vle_form {
font-size: 17px;
margin-top: 20px;
}

.lds_form_block {
font-weight: bold;
padding: 4px 5px 4px 5px;
background-color: #f4f4f4;
border: 1px solid #fff;
box-shadow: 0 0 10px #e0e0e0;
}

.vle_form_label {
width: 20%;
display: inline-block;
color: #666;
}

.vle_form_input {
    border-radius: 4px;
    background-color: #fcfcfc;
    width: 75%;
    box-shadow: inset 1px 1px 2px rgba(0,0,0,0.1);
}

.vle_info_element {
    padding: 4px 5px 4px 5px;  background-color: #F4F4F4;  border: 1px solid #FFF;
    box-shadow: 0 0 10px #e0e0e0;
}
.vle_info_key_data {
    background-color: #C4C4C4;
    padding: 5px;
    width: 18px;
    display: inline-block;
    text-align: center;
}

#vle_test_box {
    margin-top: 30px;
    margin-left: auto;
    margin-right: auto;
}
.vle_info_working {
    font-size: 17px;
    background-color: rgb(194, 243, 194);
    padding: 5px;
}

#vle_data_box {
    margin-bottom: 30px;
}

.vle_info_name_data {
    display: inline-block;
    /*width: 80px;*/
}

.vle_list_add_item a {
    text-decoration: none;
    color: #464646;
    font-weight: bold;
    padding: 5px;
    display: block;
}

.vle_list_item a {
    text-decoration: none;
    color: #464646;
    padding: 5px;
    display: block;
}


.vle_list_item,
.vle_list_add_item {
    margin-top: 20px;
    text-align: center;
    background-color: #B4FFA6;
    /*padding: 5px;*/
    border-radius: 4px;
    /*margin-left: 20px;*/
}

.vle_list_add_item {
    margin-top: 80px;
}

.ldshake_sidebar_block_title {
    /*margin-left: 20px;*/
    margin-top: 30px;
    font-size: 17px;
    border-bottom: 2px grey solid;
}

.vle_list_item.current {
    border: 1px green solid;
    font-weight: bold;
}

.vle_list_add_item:hover {
    background-color: #8ED581;
}

.vle_list_item:hover {
    background-color: #78CE78;
}

.vle_implement_form_vle_name {
    font-weight: bold;
    margin-top: 5px;
}

.lds_close_popup {
    float: right;
}

#editimplementation_popup {
    left: 20%;
}

#upload_result{
    border-width: 0px;
    width: 0px;
    height: 0px;
}

/* MSO */
/* Style Definitions */
p.MsoNormal, li.MsoNormal, div.MsoNormal
{margin:0cm;
margin-bottom:.0001pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoHeading7, li.MsoHeading7, div.MsoHeading7
{mso-style-link:"Heading 7 Char";
margin-top:12.0pt;
margin-right:0cm;
margin-bottom:3.0pt;
margin-left:64.8pt;
text-indent:-64.8pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoHeading8, li.MsoHeading8, div.MsoHeading8
{mso-style-link:"Heading 8 Char";
margin-top:12.0pt;
margin-right:0cm;
margin-bottom:3.0pt;
margin-left:72.0pt;
text-indent:-72.0pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";
font-style:italic;}

p.MsoHeading9, li.MsoHeading9, div.MsoHeading9
{mso-style-link:"Heading 9 Char";
margin-top:12.0pt;
margin-right:0cm;
margin-bottom:3.0pt;
margin-left:79.2pt;
text-indent:-79.2pt;
font-size:9.0pt;
font-family:"Arial","sans-serif";
font-weight:bold;
font-style:italic;}

p.MsoToc1, li.MsoToc1, div.MsoToc1
{margin-top:0cm;
margin-right:0cm;
margin-bottom:5.0pt;
margin-left:0cm;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoToc2, li.MsoToc2, div.MsoToc2
{margin-top:0cm;
margin-right:0cm;
margin-bottom:5.0pt;
margin-left:11.0pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoCommentText, li.MsoCommentText, div.MsoCommentText
{mso-style-link:"Comment Text Char";
margin:0cm;
margin-bottom:.0001pt;
font-size:10.0pt;
font-family:"Arial","sans-serif";}

p.MsoHeader, li.MsoHeader, div.MsoHeader
{mso-style-link:"Header Char";
margin:0cm;
margin-bottom:.0001pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoFooter, li.MsoFooter, div.MsoFooter
{mso-style-link:"Footer Char";
margin:0cm;
margin-bottom:.0001pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoCaption, li.MsoCaption, div.MsoCaption
{margin-top:0cm;
margin-right:0cm;
margin-bottom:10.0pt;
margin-left:0cm;
font-size:9.0pt;
font-family:"Arial","sans-serif";
color:#4F81BD;
font-weight:bold;}

span.MsoCommentReference
{font-family:"Times New Roman","serif";}

p.MsoTitle, li.MsoTitle, div.MsoTitle
{mso-style-link:"Title Char";
margin-top:12.0pt;
margin-right:0cm;
margin-bottom:3.0pt;
margin-left:0cm;
text-align:center;
font-size:16.0pt;
font-family:"Arial","sans-serif";
font-weight:bold;}

p.MsoSubtitle, li.MsoSubtitle, div.MsoSubtitle
{mso-style-link:"Subtitle Char";
margin-top:0cm;
margin-right:0cm;
margin-bottom:3.0pt;
margin-left:0cm;
text-align:center;
font-size:12.0pt;
font-family:"Arial","sans-serif";}

span.MsoHyperlink
{font-family:"Times New Roman","serif";
color:blue;
text-decoration:underline;}

span.MsoHyperlinkFollowed
{font-family:"Times New Roman","serif";
color:purple;
text-decoration:underline;}

p.MsoCommentSubject, li.MsoCommentSubject, div.MsoCommentSubject
{mso-style-link:"Comment Subject Char";
margin:0cm;
margin-bottom:.0001pt;
font-size:10.0pt;
font-family:"Arial","sans-serif";
font-weight:bold;}

p.MsoAcetate, li.MsoAcetate, div.MsoAcetate
{mso-style-link:"Balloon Text Char";
margin:0cm;
margin-bottom:.0001pt;
font-size:8.0pt;
font-family:"Tahoma","sans-serif";}

p.MsoRMPane, li.MsoRMPane, div.MsoRMPane
{margin:0cm;
margin-bottom:.0001pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoListParagraph, li.MsoListParagraph, div.MsoListParagraph
{margin-top:0cm;
margin-right:0cm;
margin-bottom:0cm;
margin-left:36.0pt;
margin-bottom:.0001pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoListParagraphCxSpFirst, li.MsoListParagraphCxSpFirst, div.MsoListParagraphCxSpFirst
{margin-top:0cm;
margin-right:0cm;
margin-bottom:0cm;
margin-left:36.0pt;
margin-bottom:.0001pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoListParagraphCxSpMiddle, li.MsoListParagraphCxSpMiddle, div.MsoListParagraphCxSpMiddle
{margin-top:0cm;
margin-right:0cm;
margin-bottom:0cm;
margin-left:36.0pt;
margin-bottom:.0001pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoListParagraphCxSpLast, li.MsoListParagraphCxSpLast, div.MsoListParagraphCxSpLast
{margin-top:0cm;
margin-right:0cm;
margin-bottom:0cm;
margin-left:36.0pt;
margin-bottom:.0001pt;
font-size:11.0pt;
font-family:"Arial","sans-serif";}

p.MsoTocHeading, li.MsoTocHeading, div.MsoTocHeading
{margin-top:24.0pt;
margin-right:0cm;
margin-bottom:0cm;
margin-left:0cm;
margin-bottom:.0001pt;
line-height:115%;
page-break-after:avoid;
font-size:14.0pt;
font-family:"Cambria","serif";
color:#365F91;
font-weight:bold;}

span.Heading1Char
{mso-style-name:"Heading 1 Char";
mso-style-link:"Heading 1";
font-family:"Arial","sans-serif";
font-weight:bold;}

span.Heading2Char
{mso-style-name:"Heading 2 Char";
mso-style-link:"Heading 2";
font-family:"Cambria","serif";
font-weight:bold;
font-style:italic;}

span.Heading3Char
{mso-style-name:"Heading 3 Char";
mso-style-link:"Heading 3";
font-family:"Cambria","serif";
font-weight:bold;}

span.Heading4Char
{mso-style-name:"Heading 4 Char";
mso-style-link:"Heading 4";
font-family:"Calibri","sans-serif";
font-weight:bold;}

span.Heading5Char
{mso-style-name:"Heading 5 Char";
mso-style-link:"Heading 5";
font-family:"Calibri","sans-serif";
font-weight:bold;
font-style:italic;}

span.Heading6Char
{mso-style-name:"Heading 6 Char";
mso-style-link:"Heading 6";
font-family:"Calibri","sans-serif";
font-weight:bold;}

span.Heading7Char
{mso-style-name:"Heading 7 Char";
mso-style-link:"Heading 7";
font-family:"Calibri","sans-serif";}

span.Heading8Char
{mso-style-name:"Heading 8 Char";
mso-style-link:"Heading 8";
font-family:"Calibri","sans-serif";
font-style:italic;}

span.Heading9Char
{mso-style-name:"Heading 9 Char";
mso-style-link:"Heading 9";
font-family:"Cambria","serif";}

span.HeaderChar
{mso-style-name:"Header Char";
mso-style-link:Header;
font-family:"Arial","sans-serif";}

span.TitleChar
{mso-style-name:"Title Char";
mso-style-link:Title;
font-family:"Cambria","serif";
font-weight:bold;}

span.SubtitleChar
{mso-style-name:"Subtitle Char";
mso-style-link:Subtitle;
font-family:"Cambria","serif";}

span.FooterChar
{mso-style-name:"Footer Char";
mso-style-link:Footer;
font-family:"Arial","sans-serif";}

span.BalloonTextChar
{mso-style-name:"Balloon Text Char";
mso-style-link:"Balloon Text";}

span.CommentTextChar
{mso-style-name:"Comment Text Char";
mso-style-link:"Comment Text";
font-family:"Arial","sans-serif";}

span.CommentSubjectChar
{mso-style-name:"Comment Subject Char";
mso-style-link:"Comment Subject";
font-family:"Arial","sans-serif";
font-weight:bold;}

span.apple-converted-space
{mso-style-name:apple-converted-space;}

.MsoChpDefault
{font-size:10.0pt;}

/* Page Definitions */
@page WordSection1
{size:595.3pt 841.9pt;
margin:62.35pt 62.35pt 62.35pt 62.35pt;}
div.WordSection1
{page:WordSection1;}
@page WordSection2
{size:595.3pt 841.9pt;
margin:72.0pt 90.0pt 72.0pt 90.0pt;}
div.WordSection2
{page:WordSection2;}


.MsoTitle a,
.MsoSubtitle a,
#the_lds h1 a,
#the_lds h2 a
{
    color: black!important;
    text-decoration:  none!important;
}

p.MsoListParagraphCxSpFirst,
p.MsoListParagraphCxSpMiddle,
p.MsoListParagraphCxSpLast,
p.MsoListParagraph {
    margin-left: 18px!important;
}

/***** tree diagram *****/
.tree .node .title,
.tree .node .expand,
.tree .node .username{

}

.tree-node-box.enabled:hover {
    stroke: #F3AF1D;
}

.node .nodeState > circle.has-children {
    fill: lightsteelblue;
}

.node .nodeState > circle.no-children {
    fill: #fff;
}

.node .nodeState > circle:hover {
    fill: #D6A5E2;
}

a.tree-box-title {
    color: black;
}

/*
.tree:active {
    cursor: move;
}
*/

.htmlnonselect:active {
    cursor: default;
    -webkit-user-select: none;
}

/*
*:active,
*:hover {
cursor: pointer;
-webkit-user-select: none;
}
*/

.tree {
    overflow:hidden;
    width: 960px;
}

.tree .node circle {
    fill: #fff;
    stroke: steelblue;
    stroke-width: 1.5px;
    -webkit-user-select: none;
    -moz-user-select: none;
    user-select: none;
}

.tree .node text {
    font: 10px sans-serif;
    -webkit-user-select: none;
    -moz-user-select: none;
    user-select: none;

}

.tree .link {
    fill: none;
    stroke: #666;
    stroke-width: 1.5px;
}

.tree_info_popup_control {
    float: right;
    width: 182px;
    background-color: #458225;
    margin-top: -10px;
    margin-right: -10px;
    height: 27px;
    border-bottom-left-radius: 9px;
}

.tree_info_popup_control_button {
    width: 28px;
    height: 20px;
    background: #86CC89;
    float: right;
    margin-right: 8px;
    border-radius: 4px;
}

.tree_info_popup_control_button:hover {
    background: #C4D166;
}

.tree_info_popup_move {
    display:none;
    position: absolute;
    margin-left: 100px;
    margin-top: -200px;
    width: 600px;
    height: 500px;
    z-index: 9999;
background-color: rgba(0,0,0,0);
cursor: move;
}

#tree_lds_popup * {
    -webkit-user-select: none;
    -moz-user-select: none;
}

/*rect.svg-tree-box-title:hover {
    stroke: #006180;
}
*/
.svg-box-username:hover {
    fill: #B2C4B1;
}

/***** image viewer *****/
.lds_image_viewer {
    width: 100%;
}

.lds_image_viewer svg,
.lds_image_viewer img {
    margin-left: auto;
    margin-right: auto;
    display: block;
    max-width: 957px;
}

/** ABOUT HELP**/
#metis-about {
    padding: 30px;
}


/** license **/
div.cc_info_icon {
    width: 150px;
    float:left;
    margin-right: 20px;
    display: none;
}

div.cc_info_icon > img {
    display: block;
    margin-right: auto;
    margin-left: auto;
}

div.cc_info_icon > div.cc_icon_title {
    font-family: sans-serif;
    font-size: 19px;
    text-align: center;
    color: grey;
}

.license_cc_select_item {
    border: 2px solid rgba(255,255,255,255);
    font-size: 18px;
    padding: 4px;
    padding-bottom: 0px;
}

.license_cc_select_item:hover {
    border: 2px solid grey;
    border-radius: 6px;
    background-color: lightblue;
    cursor: pointer;
}

.license_cc_select_item > img {
    margin-right: 10px;
}

.license_banner {
    display: none;
}

#license_popup {
    width: 820px;
    margin-left: -410px;
    top: 0px;
    height: 630px;
}


/*** repository search ***/
.repository-links-title {
    font-size: 20px;
}

#repository-links {
    padding: 20px;
}

/*** debug settings ***/
.debug_settings {
    padding: 20px;
}

/*** projects css ***/
#droppable_grid {
    width: 690px;
    height: 661px;
    padding: 0.5em;
    border: 1px solid grey;
    float: left;
    background-color: white !important;
    background-image: none;
    position: relative;
    z-index:0;
}

#ldproject_toolBar {
    position: relative;
    width: 250px;
    height: 675px;
    float:left;
    border-style:
    solid; border-color:
    black; background-color: #F0F0F0 !important;
    background-image: none;
}

.draggable, .draggable-nonvalid {
    width: 75px;
    height: 75px;
    padding: 5px;
    position: relative;
    float: left;
    margin: 8px 0px 0px 8px;
    background-color: white;
    color: white !important;
    border: 1px solid #dddddd;
    z-index: 10;
}

.draggable:not([tooltype_added="true"]):hover {
    border-color: #757575;
}

.ui-widget-content {background-image: none; }

.ui-widget-header {background-image: none; background-color: green !important; border-color: black}

.lds_att_popup {
    top: 100px;
    left: 50%;
    z-index: 100000;
    position: fixed;
    background-color: #fff;
    padding: 10px;
    border: 10px solid rgba(120,120,120,0.42);
    -moz-border-radius: 5px 5px 5px 5px;
    -webkit-border-radius: 5px 5px 5px 5px;
    border-radius: 5px 5px 5px 5px;
    -webkit-background-clip: padding-box;
    -moz-background-clip: padding;
    background-clip: padding-box;
    display: none;
    font-size: 13px;
}

#lds_attachment_popup {
    width: 500px;
    top: 150px;
    left: 50%;
    margin-left: -260px;
}

.draggable img[tooltype] {
    width: inherit;
}

.draggable[tooltype_added="true"] {
    padding: 0px 5px 10px 5px;
    box-shadow: 2px 2px 0px #F0F0F0;
}

.subtool_title {
    color: grey;
}

#ldproject_view_grid .draggable {
    display: none;
    position: absolute;
}

#ldproject_view_grid {
    position: relative;
    height: 100%;
    overflow-y: scroll;
}

.projects_tool_caption {
    position: absolute;
    bottom: 5px;
    left: 3px;
    width: 70px;
    background-color: rgba(235, 235, 235, 0.8);
    color: rgb(73, 73, 73);
    text-align: center;
    font-weight: bold;
    border-radius: 8px;
    font-size: 11px;

    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -khtml-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    cursor: default
}

.workflow_order {
    width: 18px;
    height: 15px;
    left: -5px;
    top: -3px;
    display: block;
    position: absolute;
    z-index: 9999;
    border: 3px solid #00F;
    border-radius: 20px;
    background-color: #FFF;
    padding-bottom: 2px;
    padding-top: 1px;
}

.workflow_order > input {
    padding: 0px;
    width: 100%;
    background-color: rgba(0,0,0,0);
    margin: 0px;
    border-width: 0px;
    text-align: center;
    font-weight: bold;
    color: grey;
    font-size: 12px;
}

.stickynote {
    width: 150px;
    height: 300px;
    display: block;
    position: absolute;
    z-index: 9999;
    border: 3px solid yellow;
    border-top-width: 13px;
    border-top-color: rgb(241, 241, 0);
    background-color: yellow;
    cursor: all-scroll;
}

.stickynotetext {
    width: 100%;
    height: 100%;
    border-width: 0px;
    padding: 0px;
    margin: 0px;
    resize: none;
    background-color: yellow;
    color: grey;
}

#project_add_note {
    float: right;
    width: 100px;
    height: 30px;
    position: relative;
    font-size: 16px;
    font-weight: bold;
    background-color: yellow;
    padding-left: 9px;
    padding-top: 6px;
    cursor: all-scroll;
    color: grey;
}

.stickynoteclose {
    position: absolute;
    right: 3px;
    top: -14px;
    font-size: 12px;
    font-weight: bold;
    cursor: pointer;
    z-index: 10000;
}

.subtool_title textarea {
    width: 100%;
    height: 100%;
    border-width: 0px;
    padding: 0px;
    margin: 0px;
    resize: none;
    color: darkgrey;
}

.subtool_title textarea[newtitle="false"] {
    font-style: italic;
}

.subtool_title textarea[newtitle="true"] {
    color: #525252;
}