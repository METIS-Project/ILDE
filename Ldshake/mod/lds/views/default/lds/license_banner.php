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
<div class="license_banner" license_id="<?php echo LDS_LICENSE_CC_BY ?>" <?php if($lds->license == LDS_LICENSE_CC_BY):?>style="display:inline"<?php endif;?>><a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by/3.0/80x15.png" /></a> <?php echo T("This work is licensed under a %1 license.",'<a rel="license" href="http://creativecommons.org/licenses/by/3.0/">'.T("Creative Commons Attribution 3.0 Unported"))?></a></div>
<div class="license_banner" license_id="<?php echo LDS_LICENSE_CC_BY_ND ?>" <?php if($lds->license == LDS_LICENSE_CC_BY_ND):?>style="display:inline"<?php endif;?>><a rel="license" href="http://creativecommons.org/licenses/by-nd/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-nd/3.0/80x15.png" /></a> <?php echo T("This work is licensed under a %1 license.",'<a rel="license" href="http://creativecommons.org/licenses/by-nd/3.0/">'.T("Creative Commons Attribution-NoDerivs 3.0 Unported"))?></a></div>
<div class="license_banner" license_id="<?php echo LDS_LICENSE_CC_BY_SA ?>" <?php if($lds->license == LDS_LICENSE_CC_BY_SA):?>style="display:inline"<?php endif;?>><a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-sa/3.0/80x15.png" /></a> <?php echo T("This work is licensed under a %1 license.",'<a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/">'.T("Creative Commons Attribution-ShareAlike 3.0 Unported"))?></a></div>
<div class="license_banner" license_id="<?php echo LDS_LICENSE_CC_BY_NC ?>" <?php if($lds->license == LDS_LICENSE_CC_BY_NC):?>style="display:inline"<?php endif;?>><a rel="license" href="http://creativecommons.org/licenses/by-nc/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-nc/3.0/80x15.png" /></a> <?php echo T("This work is licensed under a %1 license.",'<a rel="license" href="http://creativecommons.org/licenses/by-nc/3.0/">'.T("Creative Commons Attribution-NonCommercial 3.0 Unported"))?></a></div>
<div class="license_banner" license_id="<?php echo LDS_LICENSE_CC_BY_NC_ND ?>" <?php if($lds->license == LDS_LICENSE_CC_BY_NC_ND):?>style="display:inline"<?php endif;?>><a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-nd/3.0/80x15.png" /></a> <?php echo T("This work is licensed under a %1 license.",'<a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/3.0/">'.T("Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported"))?></a></div>
<div class="license_banner" license_id="<?php echo LDS_LICENSE_CC_BY_NC_NA ?>" <?php if($lds->license == LDS_LICENSE_CC_BY_NC_NA):?>style="display:inline"<?php endif;?>><a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/3.0/80x15.png" /></a> <?php echo T("This work is licensed under a %1 license.",'<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">'.T("Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported"))?></a></div>
<div class="license_banner" license_id="0" <?php if(!$lds->license):?>style="display:inline"<?php endif;?>><?php echo T("There is no license") ?>.</div>
