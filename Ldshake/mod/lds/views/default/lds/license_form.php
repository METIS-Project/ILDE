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

<div id="license_popup" class="lds_popup">
    <a class="lds_close_popup" id="license_popup_close" href="#"><?php echo T("Done") ?></a>
    <h2><?php echo T("Select your preferred license") ?></h2>
    <input type="hidden" size="60" name="license_type" />
    <br />
    <div class="license_cc_select_item" license_id="0"><span style="margin-left: 98px;"><?php echo T("No license")?>.</span></div>
    <div class="license_cc_select_item" license_id="<?php echo LDS_LICENSE_CC_BY ?>"><img src="<?php echo $url."mod/lds/images/license/cc/by_88x31.png" ?>" /><?php echo T("Creative Commons Attribution 3.0 Unported")?></div>
    <div class="license_cc_select_item" license_id="<?php echo LDS_LICENSE_CC_BY_ND ?>"><img src="<?php echo $url."mod/lds/images/license/cc/by_nd_88x31.png" ?>" /><?php echo T("Creative Commons Attribution-NoDerivs 3.0 Unported")?></div>
    <div class="license_cc_select_item" license_id="<?php echo LDS_LICENSE_CC_BY_SA ?>"><img src="<?php echo $url."mod/lds/images/license/cc/by_sa_88x31.png" ?>" /><?php echo T("Creative Commons Attribution-ShareAlike 3.0 Unported")?></div>
    <div class="license_cc_select_item" license_id="<?php echo LDS_LICENSE_CC_BY_NC ?>"><img src="<?php echo $url."mod/lds/images/license/cc/by_nc_88x31.png" ?>" /><?php echo T("Creative Commons Attribution-NonCommercial 3.0 Unported")?></div>
    <div class="license_cc_select_item" license_id="<?php echo LDS_LICENSE_CC_BY_NC_ND ?>"><img src="<?php echo $url."mod/lds/images/license/cc/by_nc_nd_88x31.png" ?>" /><?php echo T("Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported")?></div>
    <div class="license_cc_select_item" license_id="<?php echo LDS_LICENSE_CC_BY_NC_NA ?>"><img src="<?php echo $url."mod/lds/images/license/cc/by_nc_na_88x31.png" ?>" /><?php echo T("Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported")?></div>

    <div style="margin-top: 20px">
        <div id="license_item_cc" class="cc_info_icon"><img src="<?php echo $url."mod/lds/images/license/cc/chooser_cc.png" ?>" /></div>
        <div id="license_item_cc_by" class="cc_info_icon"><img src="<?php echo $url."mod/lds/images/license/cc/chooser_by.png" ?>" /><div class="cc_icon_title"><?php echo T("Attribution")?></div><?php echo T("You must attribute the work in the manner specified by the author or licensor.")?></div>
        <div id="license_item_cc_nc" class="cc_info_icon"><img src="<?php echo $url."mod/lds/images/license/cc/chooser_nc.png" ?>" /><div class="cc_icon_title"><?php echo T("Noncommercial")?></div><?php echo T("The licensor permits others to copy, distribute and transmit the work. In return, licensees may not use the work for commercial purposes — unless they get the licensor's permission.")?></div>
        <div id="license_item_cc_nd" class="cc_info_icon"><img src="<?php echo $url."mod/lds/images/license/cc/chooser_nd.png" ?>" /><div class="cc_icon_title"><?php echo T("No Derivative Works")?></div><?php echo T("The licensor permits others to copy, distribute and transmit only unaltered copies of the work — not derivative works based on it.")?></div>
        <div id="license_item_cc_sa" class="cc_info_icon"><img src="<?php echo $url."mod/lds/images/license/cc/chooser_sa.png" ?>" /><div class="cc_icon_title"><?php echo T("Share Alike")?></div><?php echo T("The licensor permits others to distribute derivative works only under the same license or one compatible with the one that governs the licensor's work.")?></div>
    </div>
</div>