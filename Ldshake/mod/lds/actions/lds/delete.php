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

$lds = get_input('lds');

if (is_numeric($lds))
{
	if ($lds = get_entity($lds))
	{
		if ($lds->owner_guid == get_loggedin_userid() or isadminloggedin())
		{
            if($lds->getSubtype() == 'LdSProject_implementation') {
                if($project_list = lds_contTools::getProjectLdSList($lds->guid, true)) {
                    foreach($project_list as $fpl) {
                        if(check_entity_relationship($fpl->guid, 'lds_project_existent', $lds->guid)) {
                            remove_entity_relationship($fpl->guid, 'lds_project_existent', $lds->guid);
                            continue;
                        }

                        $fpl->deleted = '1';
                        $fpl->save_ktu();
                        $fpl->disable();
                    }
                }
            }

            $lds->deleted = '1';
            $lds->save_ktu();
            $lds->disable();
			
			echo 'ok';
		}
		else {
            echo 'error:1:permission_denied';
            register_error(T("The %1 could not be deleted.", ldshake_env_category($lds)));
        }
	}
	else echo 'error:2:invalid_lds';
}
else
{
	//We can delete a series of lds
	$idlist = explode(',',$lds);
	if (is_array($idlist) && count($idlist))
	{
		$numerrors = 0;
		foreach ($idlist as $id)
		{
			if (is_numeric($id))
			{
				if ($lds = get_entity($id))
				{
					if ($lds->owner_guid == get_loggedin_userid() or isadminloggedin())
					{
                        if($lds->getSubtype() == 'LdSProject_implementation') {
                            if($project_list = lds_contTools::getProjectLdSList($lds->guid, true)) {
                                foreach($project_list as $fpl) {
                                    $fpl->deleted = '1';
                                    $fpl->save_ktu();
                                    $fpl->disable();
                                }
                            }
                        }

                        $lds->deleted = '1';
                        $lds->save_ktu();
                        $lds->disable();

						echo 'ok';
					}
					else {
                        $numerrors++;
                        register_error(T("The %1 %2 could not be deleted.", ldshake_env_category($lds), $lds->title));
                    }
				}
				else $numerrors++;
			}
		}
		
		if (!$numerrors)
			echo 'ok';
		else {
			echo "error:3:multiple_errors_$numerrors";
        }
	}
}