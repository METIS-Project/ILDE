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
require_once __DIR__.'/../../lds_contTools.php';

global $CONFIG;

if ($doc = get_entity(get_input('doc')))
{
	if ($doc->subtype == get_subtype_id('object', 'LdS_document_editor'))
	{
		//Check if I am the owner of the document
		if ($doc->owner_guid == get_loggedin_userid())
		{
			if (get_input('action') == 'publish')
			{
				$doc->published = '1';
				$editor = EditorsFactory::getInstance($doc);
				$editor->updatePublish();
				$doc->save();
				
				//Get all the revisions of this document
				$revisions = get_entities_from_metadata('document_guid',$doc->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
				//Remove any mark of published on the previous revisions (for the republish action)
				if (is_array($revisions) && count($revisions))
					foreach ($revisions as $rev)
						$rev->published = '0';
				
				echo 'ok:'.$CONFIG->url.'ve/'.lds_contTools::encodeId($doc->guid);
			}
			else
			{
				$doc->published = '0';
				
				//Get all the revisions of this document
				$revisions = get_entities_from_metadata('document_guid',$doc->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
				//Remove any mark of published on the previous revisions
				if (is_array($revisions) && count($revisions))
					foreach ($revisions as $rev)
						$rev->published = '0';
				
				echo 'ok';
			}
		}
		else echo 'error:1:invalid_owner';
	}
	else echo 'error:2:invalid_type';
}
else echo 'error:3:invalid_lds';