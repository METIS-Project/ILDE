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


class DocumentEditorRevisionObject extends ElggObject
{
 
	protected function initialise_attributes()
	{	
		parent::initialise_attributes();
		$this->attributes['subtype'] = 'LdS_document_editor_revision';
	}
	
	//We should not instanitate revisions. We should use createRevisionFromDocumentEditor instead.
	public function __construct($document_guid, $guid = null)
	{
		parent::__construct($guid);
		$this->document_guid = $document_guid;
		$this->access_id = 2;
		$this->write_access_id = 2;
	}

	public static function createRevisionFromDocumentEditor ($document)
	{
		$revision = new DocumentEditorRevisionObject($document->guid);
		$editor = EditorsFactory::getInstance($document);
		
		$readfile = $editor->getFullFilePath($document->file_guid);
		$filestorename = 'rev_'.$document->guid.'_'.$document->lds_revision_id;
		$file = $editor->getNewFile($filestorename);
		copy($readfile, $file->getFilenameOnFilestore());
	
		$revision->file_guid = $file->guid;
		$revision->editorType = $document->editorType;
		$revision->lds_revision_id = $document->lds_revision_id;
		$revision->lds_guid = $document->lds_guid;
        $revision->container_guid = $document->lds_guid;
		$revision->previewDir = $document->revisionDir.'_'.$revision->lds_revision_id;
		$revision->id = $document->revisionDir.lds_revision_id;
		
		$editor->revisionPreview($revision);
		
		//If the document from which we crate the revision is published, the revision will be marked as published
		//and the document itserl will be unmarked (so the published version stays the same until a republish happens)
		$revision->published = $document->published;
		if($revision->published)
		{
			$editor->publishedRevision($revision);
		}
		
		$document->published = '0';
		
		$revision->save ();
	}
}
 
function DocumentEditorRevisionObject_init() {
   register_entity_type('object', 'LdS_document_editor_revision');
   // This operation only affects the db on the first call for this subtype
   // If you change the class name, you'll have to hand-edit the db
   add_subtype('object', 'LdS_document_editor_revision', 'DocumentEditorRevisionObject');
}
 
register_elgg_event_handler('init', 'system', 'DocumentEditorRevisionObject_init');
