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

extract ($vars) ?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><?php echo $lds->title ?> - LdShake</title>
	<link rel="icon" type="image/png" href="<?php echo $url ?>_graphics/favicon.ico" />
	<style type="text/css">
		body {
			margin:0;
			padding:0;
		}
		
		#doctitle {
			margin: 0;
			padding: 5px;
			background-color: #e7e8e9;
			border-bottom: 1px solid #aaa;
			font-family: Helvetica, Arial, sans-serif;
			font-size: 12px;
			color: #444;
		}

        #doctitle > span {
            font-weight: bold;
            font-size: 16px;
        }
		
		#doc {
			padding: 20px;
			font-family: Helvetica, Arial, sans-serif;
			font-size: 12pt;
			line-height: 1.6em;
		}
		
		iframe {
			min-height:700px;
			border: 0px;
			border-top: 1px solid #aaa;
			border-bottom: 1px solid #aaa;
		}
		
		a.exportbutton {
			display: block;
			float:left;
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
   		    margin-top: 5px;
   		    margin-bottom: 5px;
		    text-decoration: none;
		    font: 80%/1.4 sans-serif;
		    font-size: 13px;
		    padding: 3px 5px;
		}
		
		a.exportbutton:hover {
			text-decoration: none;
			border: 1px solid #999;
		}
		
		a.exportbutton:active {
			border: 1px solid #999;
			background-color: #dadada;
		    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#dfdfdf', endColorstr='#fafafa');
			background: -webkit-gradient(linear, center top, center bottom, from(#dfdfdf), to(#fafafa));
		    background: -moz-linear-gradient(center top , #dfdfdf, #fafafa) repeat scroll 0 0 #f6f6f6;
		}

		#exportcontainer {
		    vertical-align: middle;
    		display: table-cell;
		}

        #license {
            float: right;
            /*width: 800px;*/
            font-family: sans-serif;
        }

        .license_banner {
            display: none;
        }

        #view-ext-bottom-attributes {
            font-family: sans-serif;
            font-size: 70%;
            padding-left: 20px;
            background-color: #E2F1DF;
        }
	</style>	
</head>
<body>
	<div id="doctitle"><span><?php echo $lds->title ?></span>
        <?php if($lds->license): ?>
            <div id="license">
                <?php include('license_banner.php'); ?>
                <div style="clear:both"></div>
            </div>
        <?php endif; ?>
        <div style="clear:both"></div>
    </div>

<div id="exportcontainer">
	<?php if ($doc->file_imsld_guid): ?>
        <a class="exportbutton" href="<?php echo $url ?>ve/<?php echo lds_contTools::encodeId($doc->guid)?>/imsld">Save as IMS-LD</a>
        <!--<a class="exportbutton" href="<?php echo $url ?>ve/<?php echo lds_contTools::encodeId($doc->guid)?>/webZip">Save as zipped web page</a>-->
    <?php elseif($doc->editorType != 'google_docs' && $doc->editorType != 'google_draw' && $lds->editor_type != 'project_design'): ?>
        <a class="exportbutton" href="<?php echo $url ?>ve/<?php echo lds_contTools::encodeId($doc->guid)?>/binary"><?php echo T("Download binary file") ?></a>
    <?php endif; ?>

</div>
    <?php
    $editor = $doc->editorType;
    $currentDoc = $doc;
    ?>
    <?php if (RestEditor::rest_enabled($editor) && file_exists($CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$currentDoc->previewDir)): ?>
    <iframe id="internal_iviewer" src="<?php echo $CONFIG->url ?>content/webcollagerest/<?php echo $currentDoc->previewDir?>/index.html?t=<?php echo rand(0, 1000) ?>" height="100%" width="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;"></iframe>
    <?php elseif ($editor == 'reauthoring' && file_exists($CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$currentDoc->previewDir)): ?>
    <iframe id="internal_iviewer" src="<?php echo $CONFIG->url ?>content/webcollagerest/<?php echo $currentDoc->previewDir?>/index.html?t=<?php echo rand(0, 1000) ?>" height="100%" width="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;"></iframe>
    <?php elseif ($editor == 'cld' || $editor == 'image'): ?>
        <?php echo elgg_view('lds/editor_type/cld', array('entity' => $currentDoc)); ?>
    <?php elseif ($editor == 'google_docs'): ?>
        <iframe id="internal_iviewer" src="<?php echo $url.'pg/lds/view_iframe/'. $currentDoc->guid ?>" height="100%" width="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;"></iframe>
    <?php elseif (strstr($editor, 'google')): ?>
        <iframe id="internal_iviewer" src="<?php echo htmlentities($currentDoc->description);?>" height="860" width="1150" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;display: block; margin-left: auto;margin-right: auto;"></iframe>
    <?php elseif ($editor == 'project_design'): ?>
        <style>
            .draggable, .draggable-nonvalid {
                width: 55px;
                height: 55px;
                padding: 5px;
                position: relative;
                float: left;
                margin: 4px 0px 0px 5px;
                background-color: #FFF;
                color: #FFF !important;
                border: 1px solid #DDD;
                z-index: 10;
            }

            .draggable img[tooltype] {
                width: inherit;
            }

            .draggable[tooltype_added="true"] {
                padding: 0px 5px 10px 5px;
                box-shadow: 2px 2px 0px #F0F0F0;
            }

            #ldproject_view_grid .draggable {
                display: none;
                position: absolute;
            }

            #ldproject_view_grid {
                position: relative;
                height: 750px;
                overflow-y: scroll;
                font-family: "sans-serif";
            }

            textarea {
                font: 13px Arial, Helvetica, sans-serif;
                border: solid 1px #CCC;
                padding: 5px;
                color: #000;
            }

            .projects_tool_caption {
                font-family: Arial;
                position: absolute;
                bottom: 0px;
                left: 3px;
                width: 50px;
                background-color: rgba(235, 235, 235, 0.8);
                color: rgb(73, 73, 73);
                text-align: center;
                font-weight: bold;
                border-radius: 8px;
                font-size: 11px;
                /*word-break: break-all;*/

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
                /*height: 30px;*/
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
                font-size: 10px;
                border-width: 0px;
                padding: 0px;
                margin: 0px;
                resize: none;
                color: darkgrey;
            }

            .subtool_title textarea[newtitle="true"] {
                color: #525252;
            }
        </style>
        <?php
        $jslibs['project'] = true;
        $preview_lds_box = <<<HTML
        <div id="tree_info_popup_shell_empty" class="tooltip_bl_body" style="position:absolute;height:300px;width:400px;background-color: #FFF;overflow:hidden;display:none">
            <div class="tree_info_popup_control">

                <!--
                <div class="tree_info_popup_control_button move">
                    <svg width="28px" height="20px"><g>
                        <line x1="8.0" y1="10" x2="20" y2="10" style="stroke:#FFF;stroke-width:2;stroke-linecap:round"></line>
                        <line x1="14" y1="4" x2="14" y2="16" style="stroke:#FFF;stroke-width:2;stroke-linecap:round"></line>
                    </g></svg>
                </div>
                -->
                <div class="tree_info_popup_control_button close">
                    <svg width="28px" height="20px"><g>
                        <line x1="10" y1="6" x2="18" y2="14" style="stroke:rgb(255,0,0);stroke-width:2;stroke-linecap:round"></line>
                        <line x1="10" y1="14" x2="18" y2="6" style="stroke:rgb(255,0,0);stroke-width:2;stroke-linecap:round"></line>
                    </g></svg>
                </div>
                <div class="tree_info_popup_control_button maximize">
                    <svg width="28px" height="20px"><g><rect rx="3" ry="3" x="4" y="4" width="20" height="12" style="stroke:#FFF;stroke-width:2;fill:transparent"></rect></g></svg>
                </div>
                <div class="tree_info_popup_control_button minimize">
                    <svg width="28px" height="20px"><g><line x1="4.0" y1="16" x2="24" y2="16" style="stroke:#FFF;stroke-width:2;stroke-linecap:round"></line></g></svg>
                </div>
                <div class="tree_info_popup_control_button diff" style="display:none">
                    <svg width="28px" height="20px"><g>
                        <line x1="4.0" y1="4" x2="24" y2="4" style="stroke:rgb(255,0,0);stroke-width:2;stroke-linecap:round"></line>
                        <line x1="4.0" y1="8" x2="24" y2="8" style="stroke:rgb(0,255,0);stroke-width:2;stroke-linecap:round"></line>
                        <line x1="4.0" y1="12" x2="24" y2="12" style="stroke:rgb(255,0,0);stroke-width:2;stroke-linecap:round"></line>
                        <line x1="4.0" y1="16" x2="24" y2="16" style="stroke:rgb(0,255,0);stroke-width:2;stroke-linecap:round"></line>
                    </g></svg>
                </div>
            </div>
            <div id="tree_info_popup" style="width:100%;height:100%"></div>
        </div>
        <div id="tree_info_popup_move_empty" class="tree_info_popup_move"></div>
HTML;
        ?>

        <script>
            ldproject = <?php echo json_encode(ldshake_project_upgrade(json_decode($lds->description)));?>;
            project_lds_box = <?php echo json_encode($preview_lds_box);?>;
            is_implementation = <?php echo (($lds->getSubtype() != 'LdSProject') ? 'true' : 'false');?>;
            is_project_view = true;
            is_project_edit = false;
            var ldsToBeListed = <?php echo json_encode(array());?>;
        </script>
        <div id="ldproject_view_grid">

            <?php foreach($CONFIG->project_templates['full'] as $project_template_key => $project_template): ?>
                <div class="draggable" title="<?php echo htmlspecialchars($project_template['title'])?>">
                    <div style="position:relative;width:inherit;height:inherit;">
                        <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/<?php echo htmlspecialchars((isset($project_template['icon']) ? $project_template['icon'] : $project_template_key))?>.png" toolname="<?php echo htmlspecialchars($project_template['title'])?>" tooltype="<?php echo htmlspecialchars($project_template['type'])?>" <?php if(!empty($project_template['subtype'])):?>subtype="<?php echo htmlspecialchars($project_template['subtype']);?>"<?php endif;?>>
                        <?php if(isset($project_template['icon'])): ?>
                            <div unselectable="on" class="projects_tool_caption"><?php echo htmlspecialchars($project_template['title']);?></div>
                        <?php endif; ?>
                    </div>
                </div>
            <?php endforeach; ?>

        </div>

        <!--[if (!IE)|(gt IE 8)]><!-->
        <script type="text/javascript" src="<?php echo $vars['url']?>vendors/jquery/2.1.1/jquery.min.js"></script>
        <!--<![endif]-->

        <!--[if lte IE 8]>
        <script type="text/javascript" src="<?php echo $vars['url']?>vendors/jquery/1.11.1/jquery.min.js"></script>
        <!--[endif]-->

        <script src="<?php echo $vars['url']?>vendors/jqueryui/1.10.4/jquery-ui.min.js"></script>
        <?php

        ?><script type="text/javascript" src="<?php echo $vars['url']?>vendors/jsPlumb/jquery.jsPlumb-1.6.2-min.js"></script><?php
?>
        <script>
        var baseurl = '<?php echo $vars['url'] ?>';
        var language = '<?php echo $vars['config']->language ?>';
        var isadminloggedin = <?php echo (isadminloggedin() ? 'true' : 'false') ?>;
        </script>
<?php
        echo Utils::getJsDeclaration('lds', 'ldprojects-common');
        ?>

        <?php else:?>
    <div id="the_lds" style="height: 380px;padding: 0px;margin: 0px;width: 100%;">
        <?php echo $currentDoc->description ?>
    </div>
    <?php endif; ?>

    <!--
	<?php if ($doc->editorType == 'webcollage'): ?>
		<iframe src="<?php echo $CONFIG->url ?>content/webcollage/<?php echo $doc->pub_previewDir?>.html?t=<?php echo rand(0, 1000) ?>" width="100%" height="100%"></iframe>
	<?php endif; ?>
	-->
    <?php echo $attributes;?>
</body>
</html>
