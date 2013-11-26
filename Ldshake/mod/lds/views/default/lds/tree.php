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

<?php extract ($vars) ?>

<div id="one_column" style="padding-bottom:0 !important">
    <div id="content_area_user_title">
        <h2><?php echo $lds->title ?></h2>
    </div>
    <div id="lds_view_actions">
        <a class="rightbutton" id="lds_tree_siblings_button" href="#" style="display:none"><?php echo T("View siblings") ?></a>
    </div>
<div class="tree">
</div>
<div id="tree_lds_popup">
</div>

    <?php
$tree_lds_box = <<<HTML
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
            <div class="tree_info_popup_control_button diff">
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
<!--    <div id="tree_info_popup" style="position:absolute;top:200px;left:300px;height:300px;width:400px;background-color: #FFF;overflow:hidden">
    </div>
-->
<script type="text/javascript">
    var d3_data = <?php echo $tree;?>;
    var lds_guid = <?php echo $lds->guid;?>;

    var tree_popup_iframe_content = function() {
        var tree_popup_zoom_level = 100*$("#tree_info_popup").outerWidth()/957;
        var iframe_tree_contents= $("#internal_iviewer").contents().contents();
        $(iframe_tree_contents).css("zoom",tree_popup_zoom_level+"%");
        $("#internal_iviewer").css("transition","opacity 0.5s");
        $("#internal_iviewer").css("opacity","1.0");
    }

    var tree_lds_box = <?php echo json_encode(array("html" => $tree_lds_box));?>;


</script>
</div>