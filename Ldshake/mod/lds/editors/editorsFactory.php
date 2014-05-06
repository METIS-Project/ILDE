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


include_once(__DIR__.'/../rand.php');
class Editor
{
	public $_document;
    private $_lds;

    public function getDocument() {
        return $this->_document;
    }
	//return the filesystem path of an ElggFile object
	public static function getFullFilePath($file_guid)
	{
		$file = get_entity($file_guid);
		$readfile = new ElggFile();
		$readfile->owner_guid = $file->owner_guid;
		$readfile->setFilename($file->originalfilename);
		return $readfile->getFilenameOnFilestore();
	}

	//create an ElggFile, object and return it
	public static function getNewFile($filename)
	{
		$user = get_loggedin_user();
		$file = new ElggFile();
		$file->setFilename($filename);
		$file->owner_guid = $user->guid;
		$file->subtype = "lds_editor_file";
		$file->originalfilename = $filename;
		$file->access_id = 2;
		$file->open("write");
		//write a zero bytes size string to force Elgg to create the working directory for the present user
		$file->write("");
		$file->close();
		$file->save();
		return $file;
	}
}

class ManagerFactory {
    public static function getManager($lds) {

        if($lds->getSubtype() == 'LdS_implementation')
            return new GluepsManager($lds);
        return new richTextEditor(array(), $lds);
    }
}

class richTextEditor extends Editor
{
    public function cloneLdS($title = null)
    {
        $lds = new LdSObject();
        if($title)
            $lds->title = $title;
        $lds->subtype = $this->_lds->getSubtype();
        $lds->access_id = 2;
        $lds->granularity = 0;
        $lds->completeness = 0;
        $lds->cloned = 1;
        $lds->all_can_view = $this->_lds->all_can_view;
        $lds->implementable = $this->_lds->implementable;
        $lds->parent = $this->_lds->guid;
        $lds->editor_type = $this->_lds->editor_type;
        if($this->_lds->editor_subtype)
            $lds->editor_subtype = $this->_lds->editor_subtype;

        //implementation
        $lds->vle_id = $this->_lds->vle_id;
        $lds->course_id = $this->_lds->course_id;
        $lds->lds_id = $this->_lds->lds_id;

        $lds->external_editor = $this->_lds->external_editor;

        $tagFields = array ('discipline', 'pedagogical_approach', 'tags');
        foreach ($tagFields as $field)
            $lds->$field = $this->_lds->$field;

        $lds->save();
        create_annotation($lds->guid, 'revised_docs', '', 'text', get_loggedin_userid(), 1);
        $revision = $lds->getAnnotations('revised_docs', 1, 0, 'desc');
        $revision = $revision[0];

        if(is_array($this->_document))
        foreach($this->_document as $d) {
            $newdoc = new DocumentObject($lds->guid);
            $newdoc->description = $d->description;
            $newdoc->title = $d->title;
            $newdoc->lds_revision_id = $revision->id;
            $newdoc->save();
        }

        //TODO: fix non existing subtype on empty database
        if($editordocument = get_entities_from_metadata('lds_guid',$this->_lds->guid,'object','LdS_document_editor', 0, 100)) {
            foreach($editordocument as $e_d) {
                $em = EditorsFactory::getInstance($e_d);
                $em->cloneDocument($lds->guid);
            }
        }

        return $lds;
    }

    public function __construct($document = array(), $lds = null)
    {
        $this->_lds = $lds;
        $this->_document = get_entities_from_metadata('lds_guid',$lds->guid,'object','LdS_document',0,9999,0,'e.guid asc');
    }
}

class exeLearningEditor extends Editor
{
	public function getDocumentId()
	{
		return $this->_document->guid;

	}

	//load an empty data file to start a new document
	public function newEditor()
	{
		global $CONFIG;
		$user = get_loggedin_user();
		$rand_id = mt_rand(1000000,5000000);
		$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

		copy($CONFIG->path.'vendors/exelearning/sample.elp', $filename_editor);
		file('http://127.0.0.1/exelearning/?load='.$rand_id);
		unlink($filename_editor);

		$vars['editor_id'] = $rand_id;
		$vars['editor'] = 'exe';

		return $vars;
	}

	//load the document into the exelearning server
	public function editDocument()
	{
		global $CONFIG;
		$filename_lds = $this->getFullFilePath($this->_document->file_guid);
		$rand_id = mt_rand(400,5000000);
		$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

		copy($filename_lds, $filename_editor);

		file('http://127.0.0.1/exelearning/?load='.$rand_id);
		unlink($filename_editor);
		$vars['editor'] = 'exe';
        $vars['editor_label'] = 'eXeLearning';
		$vars['editor_id'] = $rand_id;

		return $vars;
	}

	//function to export a document to a given format and return the file guid
	public function saveNewExportDocument($docSession, $format)
	{
		global $CONFIG;
		$filestorename = rand_str(32).'.zip';
		$file = $this->getNewFile($filestorename);
		file('http://127.0.0.1/exelearning/?export='.$docSession.'&type='.$format.'&filename='.$docSession);
		copy($CONFIG->exedata.'export/'.$docSession.'.zip', $file->getFilenameOnFilestore());
		exec('rm --interactive=never '.$CONFIG->exedata.'export/'.$docSession.'.zip');
		return $file->guid;
	}

	//update the exported documents
	public function updateExportDocument($file_guid, $docSession, $format)
	{
		global $CONFIG;
		file('http://127.0.0.1/exelearning/?export='.$docSession.'&type='.$format.'&filename='.$docSession);
		copy($CONFIG->exedata.'export/'.$docSession.'.zip', $this->getFullFilePath($file_guid));
		exec('rm --interactive=never '.$CONFIG->exedata.'export/'.$docSession.'.zip');
		return true;
	}

	public function calcHash($docSession)
	{
		global $CONFIG;
		file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=hashPage');
		$hash = sha1_file($CONFIG->exedata.'export/hashPage/'.$docSession.'/index.html');
		if(strlen((string)$docSession) > 0)
			exec('rm -r --interactive=never '.$CONFIG->exedata.'export/hashPage/'.$docSession);
		return $hash;
	}

	//update the published documents
	public function updatePublish()
	{
		global $CONFIG;
		copy($this->getFullFilePath($this->_document->ims_ld), $this->getFullFilePath($this->_document->pub_ims_ld));
		copy($this->getFullFilePath($this->_document->scorm), $this->getFullFilePath($this->_document->pub_scorm));
		copy($this->getFullFilePath($this->_document->scorm2004), $this->getFullFilePath($this->_document->pub_scorm2004));
		copy($this->getFullFilePath($this->_document->webZip), $this->getFullFilePath($this->_document->pub_webZip));
		if(strlen((string)$this->_document->pub_previewDir) > 0)
			exec('rm -r --interactive=never '.$CONFIG->editors_content.'content/exe/'.$this->_document->pub_previewDir);
		exec('cp -r '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->pub_previewDir);
	}

	//copy the published documents to the new revision
	public function publishedRevision($revision)
	{
		$revision->pub_ims_ld = $this->_document->pub_ims_ld;
		$revision->pub_scorm = $this->_document->pub_scorm;
		$revision->pub_scorm2004 = $this->_document->pub_scorm2004;
		$revision->pub_webZip = $this->_document->pub_webZip;
		$revision->pub_previewDir = $this->_document->pub_previewDir;
	}

	//create a directory with the preview for the new revision
	public function revisionPreview($revision)
	{
		global $CONFIG;
		exec('cp -r '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.' '.$CONFIG->editors_content.'content/exe/'.$revision->previewDir);

		return true;
	}

	//TODO (Pau) Fer que totes les crides portin l'editor_id (mai s'agafi de request
	//aquí dins.
	public function saveNewDocument($editor_id = null)
	{
		global $CONFIG;

		//save the contents
		if (is_null($editor_id))
			$docSession = get_input('editor_id');
		else
			$docSession = $editor_id;

		$resultIds = new stdClass();
		$user = get_loggedin_user();
		$filename_editor = $CONFIG->exedata.'export/'.$docSession.'.elp';
		file('http://127.0.0.1/exelearning/?save='. $docSession);

		$rand_id = mt_rand(400,9000000);

		//create a new file to store the document
		$filestorename = $lds->guid.(string)$rand_id;
		$file = $this->getNewFile($filestorename);
		copy($filename_editor, $file->getFilenameOnFilestore());
		unlink($filename_editor);

		$this->_document->file_guid = $file->guid;
		$this->_document->save();

		//export the contents to the most common formats supported by exelarning
		$this->_document->ims_ld = $this->saveNewExportDocument($docSession, 'IMS');
		$this->_document->scorm = $this->saveNewExportDocument($docSession, 'scorm');
		$this->_document->scorm2004 = $this->saveNewExportDocument($docSession, 'scorm2004');
		$this->_document->webZip = $this->saveNewExportDocument($docSession, 'zipFile');

		//create the published files
		$this->_document->pub_ims_ld = $this->saveNewExportDocument($docSession, 'IMS');
		$this->_document->pub_scorm = $this->saveNewExportDocument($docSession, 'scorm');
		$this->_document->pub_scorm2004 = $this->saveNewExportDocument($docSession, 'scorm2004');
		$this->_document->pub_webZip = $this->saveNewExportDocument($docSession, 'zipFile');

		//assign a random string to each directory
		$this->_document->previewDir = rand_str(64);
		$this->_document->pub_previewDir = rand_str(64);
		$this->_document->revisionDir = rand_str(64);

		$this->_document->rev_last = 0;
		$this->_document->lds_revision_id = 0;

		//create the preview page with a single page html document
		file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=singlePage');
		exec('cp -r '.$CONFIG->exedata.'export/singlePage/'.$docSession.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir);
		exec('cp -r '.$CONFIG->exedata.'export/singlePage/'.$docSession.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->pub_previewDir);		
		if(strlen((string)$docSession) > 0)
			exec('rm -r --interactive=never '.$CONFIG->exedata.'export/singlePage/'.$docSession);

		$resultIds->guid = $this->_document->lds_guid;
		$resultIds->file_guid = $file->guid;

		$this->_document->save();

		return array($this->_document, $resultIds);
	}

	public function unload($docSession)
	{
		file('http://127.0.0.1/exelearning/?unload='. $docSession);
	}

	public function saveDocument()
	{
		if($this->_document->file_guid)
			$this->saveExistingDocument();
		else
			$this->saveNewDocument();
	}

	//update the previous contents
	public function saveExistingDocument()
	{
		global $CONFIG;
		$user = get_loggedin_user();
		$resultIds = new stdClass();
		$docSession = get_input('editor_id');
		$filename_editor = $CONFIG->exedata.'export/'.$docSession.'.elp';
		file('http://127.0.0.1/exelearning/?save='. $docSession);

		$filename_lds = $this->getFullFilePath($this->_document->file_guid);

		copy($filename_editor, $filename_lds);
		unlink($filename_editor);

		$old_previewDir = $this->_document->previewDir;
		$this->_document->previewDir = rand_str(64);


		file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=singlePage');
		exec('cp -r '.$CONFIG->exedata.'export/singlePage/'.$docSession.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir);
		if(strlen((string)$docSession) > 0)
			exec('rm -r --interactive=never '.$CONFIG->exedata.'export/singlePage/'.$docSession);
		if(strlen((string)$old_previewDir) > 0)
			exec('rm -r --interactive=never '.$CONFIG->editors_content.'content/exe/'.$old_previewDir);

		$this->updateExportDocument($this->_document->ims_ld, $docSession, 'IMS');
		$this->updateExportDocument($this->_document->scorm, $docSession, 'scorm');
		$this->updateExportDocument($this->_document->scorm2004, $docSession, 'scorm2004');
		$this->updateExportDocument($this->_document->webZip, $docSession, 'zipFile');

		$revisions = get_entities_from_metadata('document_guid',$this->_document->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
		$resultIds->count = count($revisions);

		//create the diff content against the last saved revision
		if(count($revisions) > 0)
		{
			$output = array();
			exec ("{$CONFIG->pythonpath} {$CONFIG->path}mod/lds/ext/diff.py".' '.$CONFIG->editors_content.'content/exe/'.$revisions[count($revisions)-1]->previewDir.'/index.html'.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/index.html', $output);
			$diff = implode('', $output);
			//insert an inline style definition to highlight the differences
			$diff = str_replace("<del", "<del style=\"background-color: #fcc;display: inline-block;text-decoration: none;\" ", $diff);
			$diff = str_replace("<ins", "<ins style=\"background-color: #cfc;display: inline-block;text-decoration: none;\" ", $diff);
			$handle = fopen($CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/diff.html', "w");
			fwrite($handle, $diff);
			fclose($handle);
		}

		$this->_document->save();

		return array($this->_document, $resultIds);
	}

	public function __construct($document=null)
	{
		$this->_document = $document;
	}
}

class WebCollageEditor extends Editor
{
    public function getDocumentId()
    {
        return $document->guid;
    }

    public function __construct($document)
    {
        $this->_document = $document;
    }

    //copy the contents of the published document to the newly created revision
    public function publishedRevision($revision)
    {
        $revision->pub_ims_ld = $this->_document->pub_ims_ld;
        $revision->pub_webZip = $this->_document->pub_webZip;

        $revision->pub_previewDir = $this->_document->pub_previewDir;
    }

    public function newEditor()
    {
        global $CONFIG;
        $user = get_loggedin_user();
        $rand_id = mt_rand(1000000,9000000); //session id

        //empty data file to start a new webcollage document
        $xmlstr = <<<XML
<?xml version='1.0' standalone='yes'?>
<webcollage>
 <lds>
  <id>0</id>
  <data></data>
  <todo></todo>
  <actions></actions>
 </lds>
</webcollage>
XML;

        $filename_editor = $CONFIG->webcollage_data.$rand_id.'.xml';

        $xml = new SimpleXMLElement($xmlstr);
        $xml->lds[0]->id = $rand_id;
        $xml_text = $xml->asXML();

        $handle = fopen($filename_editor, "w");
        $contents = fwrite($handle, $xml_text);
        fclose($handle);

        //load the file into webcollage database
        file('http://127.0.0.1/webcollage/manager/loadXML.php?id='.$rand_id);
        unlink($filename_editor);

        //variables for the edition window php
        $vars['editor'] = 'webcollage';
        $vars['editor_label'] = 'WebCollage';
        $vars['file_editor'] = $filename_editor;
        $vars['editor_id'] = $rand_id;

        return $vars;
    }

    public function unload($docSession)
    {
        global $CONFIG;
        file('http://127.0.0.1/webcollage/manager/unloadXML.php?id='.$docSession);
    }

    public function calcHash($docSession)
    {
        global $CONFIG;
        $hash = sha1_file($CONFIG->webcollage_data.$docSession.'.xml');
        return $hash;
    }

    public function saveDocument()
    {
        //check if this is the first time that we save the document
        if($this->_document->file_guid)
            $this->saveExistingDocument();
        else
            $this->saveNewDocument();
    }

    public function saveExistingDocument()
    {
        global $CONFIG;
        $user = get_loggedin_user();
        $resultIds = new stdClass();
        $docSession = get_input('editor_id');

        //output file
        $filename_editor = $CONFIG->webcollage_data.$docSession.'.xml';

        //destination file
        $filename_lds = $this->getFullFilePath($this->_document->file_guid);

        $handle_editor = fopen($filename_editor, "r");
        $contents = fread($handle_editor, filesize($filename_editor));
        $xml = new SimpleXMLElement($contents);

        //update the changelog
        $this->_document->changelog = (string)$xml->lds[0]->actions;
        fclose($handle_editor);

        copy($filename_editor, $filename_lds);
        unlink($filename_editor);

        //directory to put the preview webpage
        $content_dir = $CONFIG->editors_content.'content/webcollage/';
        $handle_file_dest = fopen($CONFIG->editors_content.'content/webcollage/'.$this->_document->previewDir.'.html', "w");

        //build the html file with the html file headers plus the content received throught ajax
        $handle_file = fopen($CONFIG->path.'vendors/webcollage/base', r);
        $content = fread($handle_file, filesize($CONFIG->path.'vendors/webcollage/base'));
        $content = $content.urldecode(get_input('summary')).'</div></body></html>';
        fwrite($handle_file_dest, $content);
        fclose($handle_file_dest);
        fclose($handle_file);

        //export the document in ims-ld format and put the output zip file into the elggfile object
        file("http://127.0.0.1/webcollage/manager/ldshakeExport.php?id=".$docSession);
        copy($CONFIG->webcollage_data.$docSession.'.zip', $this->getFullFilePath($this->_document->ims_ld));

        //build a zip with the html summary
        copy($CONFIG->path.'vendors/webcollage/base.zip', $CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip');
        exec('cd '.$CONFIG->editors_content.'content/webcollage &&'.'zip '.$CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip '.$this->_document->previewDir.'.html');
        copy($CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip', $this->getFullFilePath($this->_document->webZip));
        unlink($CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip');

        //take a screenshot from the previous html with wkhtmltoimage
        exec($CONFIG->screenshot_generator.'wkhtmltoimage --width 900 '.$content_dir.$this->_document->previewDir.'.html'.' '.$content_dir.'rev_'.$this->_document->lds_guid.'_'.$this->_document->guid.'.jpg');

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function saveNewDocument()
    {
        global $CONFIG;
        $docSession = get_input('editor_id');
        $resultIds = new stdClass();
        $user = get_loggedin_user();

        //output file
        $filename_editor = $CONFIG->webcollage_data.$docSession.'.xml';

        //we need to create a random name for our document files
        $rand_id = mt_rand(100000,9000000);
        $filestorename = $lds->guid.(string)$rand_id;
        $file = $this->getNewFile($filestorename);
        //copy the content file into the elggfile object
        copy($filename_editor, $file->getFilenameOnFilestore());

        $handle_editor = fopen($filename_editor, "r");
        $contents = fread($handle_editor, filesize($filename_editor));
        $xml = new SimpleXMLElement($contents);
        //update the changelog
        $this->_document->changelog = (string)$xml->lds[0]->actions;
        fclose($handle_editor);
        unlink($filename_editor);

        $this->_document->file_guid = $file->guid;
        $this->_document->previewDir = rand_str(24);
        $this->_document->pub_previewDir = rand_str(64);
        $this->_document->save();

        $content_dir = $CONFIG->editors_content.'content/webcollage/';
        $handle_file_dest = fopen($CONFIG->editors_content.'content/webcollage/'.$this->_document->previewDir.'.html', 'w');

        $handle_file = fopen($CONFIG->path.'vendors/webcollage/base', 'r');
        $content = fread($handle_file, filesize($CONFIG->path.'vendors/webcollage/base'));
        $content = $content.urldecode(get_input('summary')).'</div></body></html>';
        fwrite($handle_file_dest, $content);
        fclose($handle_file_dest);
        fclose($handle_file);

        $this->_document->revisionDir = rand_str(16);

        //file to store the ims-ld file
        $filestorename = 'webcollage_ims_ld'.rand_str(16).'.zip';
        $file = $this->getNewFile($filestorename);

        //export the ims-ld file
        file("http://127.0.0.1/webcollage/manager/ldshakeExport.php?id=".$docSession);
        $this->_document->ims_ld = $file->guid;
        copy($CONFIG->webcollage_data.$docSession.'.zip', $file->getFilenameOnFilestore());

        //file to store the published ims-ld file
        $filestorename = 'pub_webcollage_ims_'.rand_str(16).'.zip';
        $file = $this->getNewFile($filestorename);

        $this->_document->pub_ims_ld = $file->guid;
        copy($CONFIG->webcollage_data.$docSession.'.zip', $file->getFilenameOnFilestore());

        //export the web file
        $filestorename = 'webcollage_webZip'.rand_str(16).'.zip';
        $file = $this->getNewFile($filestorename);

        copy($CONFIG->path.'vendors/webcollage/base.zip', $CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip');
        exec('cd '.$CONFIG->editors_content.'content/webcollage &&'.'zip '.$CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip '.$this->_document->previewDir.'.html');
        copy($CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip', $file->getFilenameOnFilestore());
        unlink($CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip');
        $this->_document->webZip = $file->guid;

        //stored the published web file
        $filestorename = 'pub_webcollage_webZip'.rand_str(16).'.zip';

        $file = $this->getNewFile($filestorename);

        copy($CONFIG->path.'vendors/webcollage/base.zip', $CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip');
        exec('cd '.$CONFIG->editors_content.'content/webcollage &&'.'zip '.$CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip '.$this->_document->previewDir.'.html');
        copy($CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip', $file->getFilenameOnFilestore());
        unlink($CONFIG->webcollage_data.$this->_document->lds_guid.'base.zip');
        $this->_document->pub_webZip = $file->guid;
        $this->_document->rev_last = 0;
        $this->_document->lds_revision_id = 0;

        //take a screenshot
        exec($CONFIG->screenshot_generator.'wkhtmltoimage --width 900 '.$content_dir.$this->_document->previewDir.'.html'.' '.$content_dir.'rev_'.$this->_document->lds_guid.'_'.$this->_document->guid.'.jpg');

        $this->_document->save();

        //return the obtained lds_guid to the ajax output
        $resultIds->guid = $this->_document->lds_guid;
        //$resultIds->file_guid = $this->_document->file_guid;

        return array($this->_document, $resultIds);
    }

    public function revisionPreview($revision)
    {
        global $CONFIG;

        //copy the published previews to the new revision
        $content_dir = $CONFIG->editors_content.'content/webcollage/';
        copy($content_dir.'rev_'.$this->_document->lds_guid.'_'.$this->_document->guid.'.jpg', $content_dir.'rev_'.$this->_document->lds_guid.'_'.$this->_document->guid.'_'.$this->_document->lds_revision_id.'.jpg');
        $revision->changelog = $this->_document->changelog;
        return true;
    }

    //load the document we want to edit to webcollage
    public function editDocument()
    {
        global $CONFIG;

        //create a new xml file with the document data and load it to the database
        $rand_id = mt_rand(400000,9000000);
        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $filename_editor = $CONFIG->webcollage_data.$rand_id.'.xml';
        $handle_lds = fopen($filename_lds, "r");
        $handle_editor = fopen($filename_editor, "w+");
        $contents = fread($handle_lds, filesize($filename_lds));

        $xml = new SimpleXMLElement($contents);
        //empty the changelog actions
        $xml->lds[0]->actions = "";
        $contents = $xml->asXML();
        fwrite($handle_editor, $contents);

        fclose($handle_lds);
        fclose($handle_editor);
        file('http://127.0.0.1/webcollage/manager/loadXML.php?id='.$rand_id);
        unlink($filename_editor);
        $vars['editor'] = 'webcollage';
        $vars['editor_label'] = 'WebCollage';
        $vars['editor_id'] = $rand_id;

        return $vars;
    }

    //update the published files
    public function updatePublish()
    {
        global $CONFIG;
        copy($this->getFullFilePath($this->_document->ims_ld), $this->getFullFilePath($this->_document->pub_ims_ld));
        copy($this->getFullFilePath($this->_document->webZip), $this->getFullFilePath($this->_document->pub_webZip));

        copy($CONFIG->editors_content.'content/webcollage/'.$this->_document->previewDir.'.html', $CONFIG->editors_content.'content/webcollage/'.$this->_document->pub_previewDir.'.html');
    }
}

class EditorsFactory
{
	public static function getInstance($document)
	{
		if($document->editorType == 'webcollage')
			return new WebCollageEditor($document);
		if($document->editorType == 'exe')
			return new exeLearningEditor($document);
        if(RestEditor::rest_enabled($document->editorType))
            return new RestEditor($document);
        if($document->editorType == 'google_docs')
            return new GoogleEditor($document);
        if($document->editorType == 'openglm')
            return new UploadEditor($document);
        if($document->editorType == 'cadmos')
            return new UploadEditor($document);
        if($document->editorType == 'cld')
            return new UploadEditor($document);
        if($document->editorType == 'image')
            return new UploadEditor($document);
        if($document->editorType == 'gluepsrest')
            return new GluepsManager(null, null, $document);
	}

    public static function getManager($lds)
    {
        $documents = get_entities_from_metadata('lds_guid',$lds->guid,'object','LdS_document_editor', 0, 100);
        $document = $documents[0];
        if($document->editorType == 'webcollage')
            return new WebCollageEditor($document);
        if($document->editorType == 'exe')
            return new exeLearningEditor($document);
        if(RestEditor::rest_enabled($document->editorType))
            return new RestEditor($document);
        if($document->editorType == 'google_docs')
            return new GoogleEditor($document);
    }

	public static function getTempInstance($editorType)
	{
		if($editorType == 'webcollage')
			return new WebCollageEditor(null);
		if($editorType == 'exe')
			return new exeLearningEditor(null);
        if(RestEditor::rest_enabled($editorType))
            return new RestEditor(null, $editorType);
        if($editorType == 'openglm')
            return new UploadEditor(null, $editorType);
        if($editorType == 'cadmos')
            return new UploadEditor(null, $editorType);
        if($editorType == 'cld')
            return new UploadEditor(null, $editorType);
        if($editorType == 'image')
            return new UploadEditor(null, $editorType);
        if($editorType == 'google_docs')
            return new GoogleEditor(null, $editorType);
    }
}

class LdSFactory
{
    public static function buildLdS($ldsparams = null) {

        //We're creating it from scratch. Construct a new obj.
        $lds = new LdSObject();
        $lds->owner_guid = get_loggedin_userid();
        $lds->external_editor = true;
        $lds->editor_type = $ldsparams['type'];
        $lds->implementable = 1;
        $lds->all_can_view = "no";

        $lds->title = $ldsparams['title'];
        $lds->granularity = 0;
        $lds->completeness = 0;
        if(isset($ldsparams['granularity'])) $lds->granularity = $ldsparams['granularity'];
        if(isset($ldsparams['completeness'])) $lds->completeness = $ldsparams['completeness'];

        $lds->access_id = 2;

//Now the tags. We'll delete the existing ones to save them again
        $tagFields = array ('discipline', 'pedagogical_approach', 'tags');
        foreach ($tagFields as $field)
        {
            if(isset($ldsparams['tags'][$field])) {
                $newTags = explode(',', $ldsparams['tags'][$field]);
                foreach ($newTags as $k=>$v) if(empty($v)) unset($newTags[$k]);
                $lds->$field = $newTags;
            }
        }

        if($ldsparams['license'] !== null)
            $lds->license = $ldsparams['license'];
        else
            $lds->license = 0;

        if($ldsparams['parent'] !== null)
            $lds->parent = $ldsparams['parent'];

        $lds->save();
        //lds_contTools::markLdSAsViewed ($lds->guid);
        create_annotation($lds->guid, 'revised_docs', '', 'text', get_loggedin_userid(), 1);
        $revision = $lds->getAnnotations('revised_docs', 1, 0, 'desc');
        $revision = $revision[0];

        $build = array(
            'lds' => $lds,
            'file' => $ldsparams['doc']['file'],
            'filename' => $ldsparams['doc']['filename'],
        );

        if(isset($ldsparams['doc']['file_imsld'])) {
            $build['file_imsld'] = $ldsparams['doc']['file_imsld'];
            $build['filename_imsld'] = $ldsparams['doc']['filename_imsld'];
        }

        OpenglmEditor::buildDocument($build);

        $docObj = new DocumentObject($lds->guid);
        $docObj->title = T('Support document');

        if($ldsparams['description'] !== null)
            $docObj->description = $ldsparams['description'];
        else
            $docObj->description = "";

        $docObj->lds_revision_id = $revision->id;
        $docObj->save();

        return $lds;

    }

    public static function updateLdS($ldsparams = null) {

        //We're creating it from scratch. Construct a new obj.
        $lds = get_entity($ldsparams['id']);
        //$lds->owner_guid = get_loggedin_userid();
        //$lds->editor_type = $ldsparams['type'];

        $lds->title = $ldsparams['title'];
        //$lds->granularity = 0;
        //$lds->completeness = 0;
        if(isset($ldsparams['granularity'])) $lds->granularity = $ldsparams['granularity'];
        if(isset($ldsparams['completeness'])) $lds->completeness = $ldsparams['completeness'];

        //$lds->access_id = 2;

//Now the tags. We'll delete the existing ones to save them again
        $tagFields = array ('discipline', 'pedagogical_approach', 'tags');
        foreach ($tagFields as $field)
        {
            if(isset($ldsparams['tags'][$field])) {
                $newTags = explode(',', $ldsparams['tags'][$field]);
                foreach ($newTags as $k=>$v) if(empty($v)) unset($newTags[$k]);
                $lds->$field = $newTags;
            }
        }

        if($ldsparams['license'] !== null)
            $lds->license = $ldsparams['license'];

        $lds->save();
        //lds_contTools::markLdSAsViewed ($lds->guid);
        create_annotation($lds->guid, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);
        $revision = $lds->getAnnotations('revised_docs_editor', 1, 0, 'desc');
        $revision = $revision[0];

        $update = array(
            'lds' => $lds,
            'file' => $ldsparams['doc']['file'],
            'filename' => $ldsparams['doc']['filename']
        );

        if(isset($ldsparams['doc']['file_imsld'])) {
            $update['file_imsld'] = $ldsparams['doc']['file_imsld'];
            $update['filename_imsld'] = $ldsparams['doc']['filename_imsld'];
        }

        OpenglmEditor::updateDocument($update);

        if($ldsparams['description'] !== null) {
            if($documents = get_entities_from_metadata('lds_guid',$lds->guid,'object','LdS_document', 0, 1)) {
                $docObj = $documents[0];
                $docObj->description = $ldsparams['description'];
                $docObj->lds_revision_id = $revision->id;
                $docObj->save();
            }
        }

        return $lds;
    }
}

class OpenglmEditor extends Editor {
    public static function buildDocument($params = null) {

        global $CONFIG;

        //create a new file to store the document
        $filestorename = $params['lds']->guid.'_'.rand_str(64);
        $file = Editor::getNewFile($filestorename);
        copy($params['file'], $file->getFilenameOnFilestore());

        $document = new DocumentEditorObject($params['lds']->guid);
        $document->file_guid = $file->guid;
        $document->upload_filename = $params['filename'];

        $document->editorType = $params['lds']->editor_type;

        $filestorename = $params['lds']->guid.'_'.rand_str(12).'.zip';
        $file = Editor::getNewFile($filestorename);
        if(isset($params['file_imsld'])) {
            copy($params['file_imsld'], $file->getFilenameOnFilestore());
            $document->upload_filename_imsld = $params['filename_imsld'];
        }
        else {
            copy($params['file'], $file->getFilenameOnFilestore());
            $document->upload_filename_imsld = $params['filename'];
        }

        $document->file_imsld_guid = $file->guid;

        //assign a random string to each directory
        $document->previewDir = rand_str(64);
        $document->pub_previewDir = rand_str(64);
        $document->revisionDir = rand_str(64);

        $document->rev_last = 0;
        $document->lds_revision_id = 0;

        $document->save();

        return $document;
    }

    public static function updateDocument($params = null) {

        global $CONFIG;

        //$document = new DocumentEditorObject($params['lds']->guid);
        $editordocument = get_entities_from_metadata('lds_guid',$params['lds']->guid,'object','LdS_document_editor', 0, 100);
        $document = $editordocument[0];

        //$document->file_guid = $file->guid;
        $fullfilepath = Editor::getFullFilePath($document->file_guid);
        copy($params['file'], $fullfilepath);
        $document->upload_filename = $params['filename'];

        if(isset($params['file_imsld'])) {
            if(!$document->file_imsld_guid) {
                $filestorename = $params['lds']->guid.'_'.rand_str(12).'.zip';
                $document->file_imsld_guid = Editor::getNewFile($filestorename);
            }

            $fullfilepath = Editor::getFullFilePath($document->file_imsld_guid);
            copy($params['file_imsld'], $fullfilepath);
            $document->upload_filename_imsld = $params['filename_imsld'];
        } else {
            if(!$document->file_imsld_guid) {
                $filestorename = $params['lds']->guid.'_'.rand_str(12).'.zip';
                $document->file_imsld_guid = Editor::getNewFile($filestorename);
            }

            $fullfilepath = Editor::getFullFilePath($document->file_imsld_guid);
            copy($params['file'], $fullfilepath);
            $document->upload_filename_imsld = $params['filename'];
        }

        //TODO: update revision data

        $document->save();

        return $document;
    }
}

class RestEditor extends Editor
{
    public $document_url;
    private $_editorType;
    private $_rest_id;

    /*also useful to check if a given editor supports the REST api*/
    public static function rest_enabled($editor_type) {
        global $CONFIG;

        if(!$CONFIG->rest_editor_list) {
            if($user_editor = get_entity(get_loggedin_user()->editor)) {
                $CONFIG->rest_editor_list[$user_editor->internalname] = array(
                    'name' => $user_editor->name,
                    'url_rest' => $user_editor->resturl,
                    'url_gui' => $user_editor->guiurl,
                    'password' => $user_editor->restpass,
                    'imsld' => $user_editor->imsld,
                    'preview' => $user_editor->preview,
                    'icon' => false
                );
            }

            $CONFIG->rest_editor_list['webcollagerest'] = array(
                'name' => 'WebCollage',
                'url_rest' => "http://pandora.tel.uva.es/~wic/wic2Ldshake/",
                'url_gui' => "http://pandora.tel.uva.es/~wic/wic2Ldshake/indexLdShake.php",
                'preview' => true,
                'imsld' => true,
                'password' => 'LdS@k$1#',
                'icon' => true
            );

            $CONFIG->rest_editor_list['exelearningrest'] = array(
                'name' => 'eXeLearning',
                'url_rest' => "http://ldshake2.upf.edu:8080/",
                'url_gui' => "http://ldshake2.upf.edu:8080/ldshakegui/",
                //'url_rest' => "http://ilde:443/",
                //'url_gui' => "http://ilde:443/ldshakegui/",
                //'url_rest' => "http://192.168.1.219:8080/",
                //'url_gui' => "http://192.168.1.219:8080/ldshakegui/",
                'preview' => true,
                'imsld' => false,
                'password' => 'LdS@k$1#',
                'icon' => false
            );
        }

        return $CONFIG->rest_editor_list[$editor_type];
    }

    public function getDocumentId()
    {
        return $this->_document->guid;
    }

    //load an empty data file to start a new document
    public function newEditor()
    {
        global $CONFIG;
        $rand_id = rand_str(64);
        $editorType = $this->_editorType;

        $ldshake_url = parse_url($CONFIG->url);
        $ldshake_frame_origin = $ldshake_url['scheme'].'://'.$ldshake_url['host'];
        if(isset($ldshake_url['port']))
            $ldshake_frame_origin .= ':'. $ldshake_url['port'];

        $url_gui_parsed = parse_url($CONFIG->rest_editor_list[$editorType]['url_gui']);

        if(isset($url_gui_parsed['port']))
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'].':'.$url_gui_parsed['port'];
        else
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'];

        $vars['restapi_remote_domain'] = $gui_frame_origin;

        $vars['editor'] = $editorType;
        $vars['editor_label'] = $CONFIG->rest_editor_list[$editorType]['name'];
        $vars['restapi'] = true;

        $lang = lds_contTools::tool_lang($vars['editor'],$CONFIG->language);

        $post = array(
            'lang' => $lang,
            'sectoken' => $rand_id,
            'name' => T('Untitled LdS'),
            'ldshake_frame_origin' => $ldshake_frame_origin
        );

        //$uri = "http://web.dev/ilde/services/dummy?XDEBUG_SESSION_START=16713";
        $uri = "{$CONFIG->rest_editor_list[$editorType]['url_rest']}ldshake/ldsdoc/";

        try {
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->basicAuth('ldshake_default_user', $CONFIG->rest_editor_list[$editorType]['password'])
            ->sendIt();

        if($response->code > 299)
            throw new Exception("Error code {$response->code}");

        } catch (Exception $e) {
            register_error(htmlentities($e->getMessage()));
            return false;
            forward($CONFIG->url . 'pg/lds/');
        }

        $vars['editor_id'] = $rand_id;
        $doc_url = parse_url($response->raw_body);
        $url_path = explode('/', $doc_url['path'].$doc_url['query']);
        $url_path_filtered = array();
        foreach ($url_path as $up)
            if(strlen($up))
                $url_path_filtered[] = $up;
        $doc_id = $url_path_filtered[count($url_path_filtered) -1];
        $vars['document_url'] = "{$response->raw_body}";
        $vars['document_iframe_url'] = "{$CONFIG->rest_editor_list[$editorType]['url_gui']}?document_id={$doc_id}&lang={$lang}";
        //$vars['document_url'] = "{$response->raw_body}";

        $gui_url = parse_url($response->raw_body);

        return $vars;
    }

    //load the document into the exelearning server
    public function editDocument()
    {
        global $CONFIG;
        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = rand_str(64);
        $editorType = $this->_editorType;
        $lds = get_entity($this->_document->lds_guid);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $ldshake_url = parse_url($CONFIG->url);
        $ldshake_frame_origin = $ldshake_url['scheme'].'://'.$ldshake_url['host'];

        $url_gui_parsed = parse_url($CONFIG->rest_editor_list[$editorType]['url_gui']);

        if(isset($url_gui_parsed['port']))
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'].':'.$url_gui_parsed['port'];
        else
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'];

        $vars['restapi_remote_domain'] = $gui_frame_origin;

        $vars['editor'] = $this->_document->editorType;
        $vars['editor_label'] = $CONFIG->rest_editor_list[$this->_document->editorType]['name'];
        $vars['restapi'] = true;

        $lang = lds_contTools::tool_lang($vars['editor'],$CONFIG->language);

        $post = array(
            'lang' => $lang,
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds};type=application/json; charset=UTF-8",
            'name' => $lds->title,
            'ldshake_frame_origin' => $ldshake_frame_origin
        );

        //$uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        $uri = "{$CONFIG->rest_editor_list[$this->_document->editorType]['url_rest']}ldshake/ldsdoc/";

        try {

        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->basicAuth('ldshake_default_user', $CONFIG->rest_editor_list[$this->_document->editorType]['password'])
            ->sendIt();

        if($response->code > 299)
            throw new Exception("Error code {$response->code}");

        } catch (Exception $e) {
            register_error(htmlentities($e->getMessage()));
            return false;
            forward($CONFIG->url . 'pg/lds/');
        }

        $doc_url = parse_url($response->raw_body);
        $url_path = explode('/', $doc_url['path'].$doc_url['query']);
        $url_path_filtered = array();
        foreach ($url_path as $up)
            if(strlen($up))
                $url_path_filtered[] = $up;
        $doc_id = $url_path_filtered[count($url_path_filtered) -1];
        $this->_rest_id = $doc_id;
        $vars['document_url'] = "{$response->raw_body}";
        $vars['document_iframe_url'] = "{$CONFIG->rest_editor_list[$this->_document->editorType]['url_gui']}?document_id={$doc_id}&lang={$lang}";
        $vars['editor_id'] = $rand_id;

        return $vars;
    }

    public function putImplementation($params)
    {
        global $CONFIG;
        $lds = $params['lds'];
        $vle = $params['vle'];
        $gluepsm = new GluepsManager($vle);
        $vle_info = $gluepsm->getVleInfo();
        $course_info = $gluepsm->getCourseInfo($params['course_id']);

        $vle_info->id = $vle->guid;
        $vle_info->name = $vle->name;
        if($vle->admin_id) {
            $admin_vle = get_entity($vle->admin_id);
            $vle_info->version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $vle_info->wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }

        $wic_vle_data = array(
            'learningEnvironment' => $vle_info,
            'course' => $course_info
        );

        $json_wic_vle_data = json_encode($wic_vle_data);

        $putData = tmpfile();
        fwrite($putData, $json_wic_vle_data);
        fseek($putData, 0);
        $m_fd = stream_get_meta_data($putData);

        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = rand_str(64);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $ldshake_url = parse_url($CONFIG->url);
        $ldshake_frame_origin = $ldshake_url['scheme'].'://'.$ldshake_url['host'];

        $vars['editor'] = 'webcollagerest';
        $vars['restapi'] = true;

        $url_gui_parsed = parse_url($CONFIG->rest_editor_list["webcollagerest"]['url_gui']);

        if(isset($url_gui_parsed['port']))
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'].':'.$url_gui_parsed['port'];
        else
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'];

        $vars['restapi_remote_domain'] = $gui_frame_origin;

        $lang = lds_contTools::tool_lang($vars['editor'],$CONFIG->language);

        $post = array(
            'lang' => $lang,
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds};type=application/json; charset=UTF-8",
            'vle_info' => "@{$m_fd['uri']};type=application/json; charset=UTF-8",
            'name' => $params['name'],
            'ldshake_frame_origin' => $ldshake_frame_origin,
        );

        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        try {
            $response = \Httpful\Request::post($uri)
                ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
                ->body($post, 'multipart/form-data')
                ->basicAuth('ldshake_default_user','LdS@k$1#')
                ->sendIt();

            $vars['editor_label'] = 'WebCollage';
            $doc_url = parse_url($response->raw_body);
            if(!isset($doc_url['scheme']) || !isset($doc_url['host']) || !isset($doc_url['path']))
                throw new Exception("Invalid document URL: {$response->raw_body}");

            $url_path = explode('/', $doc_url['path'].$doc_url['query']);
            $url_path_filtered = array();
            foreach ($url_path as $up)
                if(strlen($up))
                    $url_path_filtered[] = $up;
            $doc_id = $url_path_filtered[count($url_path_filtered) -1];
        } catch (Exception $e) {
            register_error(htmlentities($e->getMessage()));
            forward($CONFIG->url . 'pg/lds/');
        }

        $this->_rest_id = $doc_id;
        $vars['document_url'] = "{$response->raw_body}";
        //$vars['document_iframe_url'] = "{$CONFIG->webcollagerest_url}?document_id={$doc_id}&sectoken={$rand_id}";
        $vars['document_iframe_url'] = "http://pandora.tel.uva.es/~wic/wic2Ldshake/indexLdShake.php?document_id={$doc_id}&lang={$lang}";
        $vars['editor_id'] = $rand_id;

        return $vars;
    }

    //function to export a document to a given format and return the file guid
    public function saveNewExportDocument($docSession, $format)
    {
        global $CONFIG;
        $filestorename = rand_str(32).'.zip';
        $file = $this->getNewFile($filestorename);
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type='.$format.'&filename='.$docSession);
        copy($CONFIG->exedata.'export/'.$docSession.'.zip', $file->getFilenameOnFilestore());
        exec('rm --interactive=never '.$CONFIG->exedata.'export/'.$docSession.'.zip');
        return $file->guid;
    }

    //update the exported documents
    public function updateExportDocument($file_guid, $docSession, $format)
    {
        global $CONFIG;
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type='.$format.'&filename='.$docSession);
        copy($CONFIG->exedata.'export/'.$docSession.'.zip', $this->getFullFilePath($file_guid));
        exec('rm --interactive=never '.$CONFIG->exedata.'export/'.$docSession.'.zip');
        return true;
    }

    public function calcHash($docSession)
    {
        global $CONFIG;
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=hashPage');
        $hash = sha1_file($CONFIG->exedata.'export/hashPage/'.$docSession.'/index.html');
        if(strlen((string)$docSession) > 0)
            exec('rm -r --interactive=never '.$CONFIG->exedata.'export/hashPage/'.$docSession);
        return $hash;
    }

    //update the published documents
    public function updatePublish()
    {
        /*
        global $CONFIG;
        copy($this->getFullFilePath($this->_document->ims_ld), $this->getFullFilePath($this->_document->pub_ims_ld));
        copy($this->getFullFilePath($this->_document->scorm), $this->getFullFilePath($this->_document->pub_scorm));
        copy($this->getFullFilePath($this->_document->scorm2004), $this->getFullFilePath($this->_document->pub_scorm2004));
        copy($this->getFullFilePath($this->_document->webZip), $this->getFullFilePath($this->_document->pub_webZip));
        if(strlen((string)$this->_document->pub_previewDir) > 0)
            exec('rm -r --interactive=never '.$CONFIG->editors_content.'content/exe/'.$this->_document->pub_previewDir);
        exec('cp -r '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->pub_previewDir);
        */
    }

    //copy the published documents to the new revision
    public function publishedRevision($revision)
    {
        $revision->pub_ims_ld = $this->_document->pub_ims_ld;
        $revision->pub_scorm = $this->_document->pub_scorm;
        $revision->pub_scorm2004 = $this->_document->pub_scorm2004;
        $revision->pub_webZip = $this->_document->pub_webZip;
        $revision->pub_previewDir = $this->_document->pub_previewDir;
    }

    //create a directory with the preview for the new revision
    public function revisionPreview($revision)
    {
        global $CONFIG;
        //exec('cp -r '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.' '.$CONFIG->editors_content.'content/exe/'.$revision->previewDir);

        return true;
    }

    public function saveNewDocument($params = null)
    {
        global $CONFIG;

        //save the contents
        $docSession = $params['editor_id'];

        $resultIds = new stdClass();
        $user = get_loggedin_user();

        $url_path = explode('/', $params['document_url']);
        $url_path_filtered = array();
        foreach ($url_path as $up)
            if(strlen($up))
                $url_path_filtered[] = $up;
        $doc_id = $url_path_filtered[count($url_path_filtered) -1];

        $uri = "{$CONFIG->rest_editor_list[$this->_document->editorType]['url_rest']}ldshake/ldsdoc/{$doc_id}";

        try {
        $response = \Httpful\Request::get($uri)
            ->basicAuth('ldshake_default_user', $CONFIG->rest_editor_list[$this->_document->editorType]['password'])
            //->addHeader('Accept', 'application/json; charset=UTF-8')
            ->sendIt();

            if($response->code > 299)
                throw new Exception("Error code {$response->code}");

        } catch (Exception $e) {
            register_error(htmlentities($e->getMessage()));
            return false;
            forward($CONFIG->url . 'pg/lds/');
        }

        //create a new file to store the document
        $rand_id = mt_rand(400,9000000);
        $filestorename = (string)$rand_id;
        $file = $this->getNewFile($filestorename);
        file_put_contents($file->getFilenameOnFilestore(), $response->raw_body);
        $this->_document->file_guid = $file->guid;
        $this->_document->save();

        if($CONFIG->rest_editor_list[$this->_document->editorType]['imsld']) {
            $uri = "{$CONFIG->rest_editor_list[$this->_document->editorType]['url_rest']}ldshake/ldsdoc/{$doc_id}".'.imsld';

            try {
            $response = \Httpful\Request::get($uri)
                ->basicAuth('ldshake_default_user', $CONFIG->rest_editor_list[$this->_document->editorType]['password'])
                //->addHeader('Accept', 'application/json; charset=UTF-8')
                ->sendIt();

                if($response->code > 299)
                    throw new Exception("Error code {$response->code}");

            } catch (Exception $e) {
                register_error(htmlentities($e->getMessage()));
                return false;
                forward($CONFIG->url . 'pg/lds/');
            }

            //create a new file to store the document
            $rand_id = mt_rand(400,9000000);
            $filestorename = (string)$rand_id.'.zip';
            $file = $this->getNewFile($filestorename);
            file_put_contents($file->getFilenameOnFilestore(), $response->raw_body);
            $this->_document->file_imsld_guid = $file->guid;
            $this->_document->save();
        }

        //assign a random string to each directory
        $this->_document->previewDir = rand_str(64);
        $this->_document->pub_previewDir = rand_str(64);
        $this->_document->revisionDir = rand_str(64);

        if($CONFIG->rest_editor_list[$this->_document->editorType]['preview']) {
            $uri = "{$CONFIG->rest_editor_list[$this->_document->editorType]['url_rest']}ldshake/ldsdoc/{$doc_id}".'/summary';

            try {
            $response = \Httpful\Request::get($uri)
                ->basicAuth('ldshake_default_user', $CONFIG->rest_editor_list[$this->_document->editorType]['password'])
                ->sendIt();

                if($response->code > 299)
                    throw new Exception("Error code {$response->code}");

            } catch (Exception $e) {
                register_error(htmlentities($e->getMessage()));
                return false;
                forward($CONFIG->url . 'pg/lds/');
            }

            $zip_file = $CONFIG->tmppath . $this->_document->guid . time() . '.zip';
            file_put_contents($zip_file, $response->raw_body, FILE_BINARY);
            $preview_path = $CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$this->_document->previewDir;
            mkdir($preview_path);

            $zip = new ZipArchive();
            if($zip->open($zip_file)) {
                $zres = $zip->extractTo($preview_path);
                $zip->close();
            }
            unlink($zip_file);
        }

        //$this->_document->rev_last = 0;
        //$this->_document->lds_revision_id = 0;

        $resultIds->guid = $this->_document->lds_guid;
        $resultIds->file_guid = $this->_document->file_guid;

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function cloneDocument($lds)
    {
        global $CONFIG;

        $rand_id = mt_rand(400,9000000);

        $clone = new DocumentEditorObject($lds);

        $file_origin = Editor::getFullFilePath($this->_document->file_guid);

        //create a new file to store the document
        $filestorename = (string)$rand_id;
        $file = $this->getNewFile($filestorename);
        copy($file_origin, $file->getFilenameOnFilestore());

        $clone->file_guid = $file->guid;

        if($this->_document->file_imsld_guid) {
            $file_origin = Editor::getFullFilePath($this->_document->file_imsld_guid);

            //create a new file to store the document
            $filestorename = (string)$rand_id.'.zip';
            $file = $this->getNewFile($filestorename);
            copy($file_origin, $file->getFilenameOnFilestore());

            $clone->file_imsld_guid = $file->guid;
        }

        $clone->editorType = $this->_document->editorType;

        $clone->save();

        //assign a random string to each directory
        $clone->previewDir = rand_str(64);
        $clone->pub_previewDir = rand_str(64);
        $clone->revisionDir = rand_str(64);

        if(file_exists($CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$this->_document->previewDir)) {
            $src_preview_path = $CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$this->_document->previewDir;
            $preview_path = $CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$clone->previewDir;
            mkdir($preview_path);
            shell_exec("cp -r {$src_preview_path}/. {$preview_path}");
        }

        $clone->rev_last = 0;
        $clone->lds_revision_id = 0;

        $clone->save();

        create_annotation($lds, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);

        return array($clone);
    }

    public function unload($docSession)
    {
        //file('http://127.0.0.1/exelearning/?unload='. $docSession);
    }

    public function saveDocument($params=null)
    {
        if($this->_document->file_guid)
            return $this->saveExistingDocument($params);
        else
            return $this->saveNewDocument($params);
    }

    //update the previous contents
    public function saveExistingDocument($params=null)
    {
        global $CONFIG;
        $user = get_loggedin_user();
        $resultIds = new stdClass();
        $docSession = $params['editor_id'];

        $url_path = explode('/', $params['document_url']);
        $url_path_filtered = array();
        foreach ($url_path as $up)
            if(strlen($up))
                $url_path_filtered[] = $up;
        $doc_id = $url_path_filtered[count($url_path_filtered) -1];

        $uri = "{$CONFIG->rest_editor_list[$this->_document->editorType]['url_rest']}ldshake/ldsdoc/{$doc_id}";

        try {
        $response = \Httpful\Request::get($uri)
            ->basicAuth('ldshake_default_user', $CONFIG->rest_editor_list[$this->_document->editorType]['password'])
            //->addHeader('Accept', 'application/json; charset=UTF-8')
            ->sendIt();

            if($response->code > 299)
                throw new Exception("Error code {$response->code}");

        } catch (Exception $e) {
            register_error(htmlentities($e->getMessage()));
            return false;
            forward($CONFIG->url . 'pg/lds/');
        }

        //create a new file to store the document
        $rand_id = mt_rand(400,9000000);
        file_put_contents($this->getFullFilePath($this->_document->file_guid), $response->raw_body);

        if($CONFIG->rest_editor_list[$this->_document->editorType]['imsld']) {
            $uri = "{$CONFIG->rest_editor_list[$this->_document->editorType]['url_rest']}ldshake/ldsdoc/{$doc_id}".'.imsld';

            try {
            $response = \Httpful\Request::get($uri)
                ->basicAuth('ldshake_default_user', $CONFIG->rest_editor_list[$this->_document->editorType]['password'])
                //->addHeader('Accept', 'application/json; charset=UTF-8')
                ->sendIt();

                if($response->code > 299)
                    throw new Exception("Error code {$response->code}");

            } catch (Exception $e) {
                register_error(htmlentities($e->getMessage()));
                return false;
                forward($CONFIG->url . 'pg/lds/');
            }

            //create a new file to store the document
            if(!$this->_document->file_imsld_guid) {
                $rand_id = mt_rand(400,9000000);
                $filestorename = (string)$rand_id.'.zip';
                $file = $this->getNewFile($filestorename);
                $this->_document->file_imsld_guid = $file->guid;
            }
            file_put_contents($this->getFullFilePath($this->_document->file_imsld_guid), $response->raw_body);
        }

        $old_previewDir = $this->_document->previewDir;
        $this->_document->previewDir = rand_str(64);

        //preview
        if($CONFIG->rest_editor_list[$this->_document->editorType]['preview']) {
            $uri = "{$CONFIG->rest_editor_list[$this->_document->editorType]['url_rest']}ldshake/ldsdoc/{$doc_id}".'/summary';

            try {
            $response = \Httpful\Request::get($uri)
                ->basicAuth('ldshake_default_user', $CONFIG->rest_editor_list[$this->_document->editorType]['password'])
                ->sendIt();
                if($response->code > 299)
                    throw new Exception("Error code {$response->code}");

            } catch (Exception $e) {
                register_error(htmlentities($e->getMessage()));
                return false;
                forward($CONFIG->url . 'pg/lds/');
            }

            $zip_file = $CONFIG->tmppath . $this->_document->guid . time() . '.zip';
            file_put_contents($zip_file, $response->raw_body, FILE_BINARY);
            $preview_path = $CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$this->_document->previewDir;
            mkdir($preview_path);

            $zip = new ZipArchive();
            if($zip->open($zip_file)) {
                $zres = $zip->extractTo($preview_path);
                $zip->close();
            }
            unlink($zip_file);
        }

        /*
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=singlePage');
        exec('cp -r '.$CONFIG->exedata.'export/singlePage/'.$docSession.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir);
        if(strlen((string)$docSession) > 0)
            exec('rm -r --interactive=never '.$CONFIG->exedata.'export/singlePage/'.$docSession);
        if(strlen((string)$old_previewDir) > 0)
            exec('rm -r --interactive=never '.$CONFIG->editors_content.'content/exe/'.$old_previewDir);


        $this->updateExportDocument($this->_document->ims_ld, $docSession, 'IMS');
        $this->updateExportDocument($this->_document->scorm, $docSession, 'scorm');
        $this->updateExportDocument($this->_document->scorm2004, $docSession, 'scorm2004');
        $this->updateExportDocument($this->_document->webZip, $docSession, 'zipFile');

        */
        $revisions = get_entities_from_metadata('document_guid',$this->_document->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
        $resultIds->count = count($revisions);

        //create the diff content against the last saved revision
        if(count($revisions) > 0)
        {
            /*
            $output = array();
            exec ("{$CONFIG->pythonpath} {$CONFIG->path}mod/lds/ext/diff.py".' '.$CONFIG->editors_content.'content/exe/'.$revisions[count($revisions)-1]->previewDir.'/index.html'.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/index.html', $output);
            $diff = implode('', $output);
            //insert an inline style definition to highlight the differences
            $diff = str_replace("<del", "<del style=\"background-color: #fcc;display: inline-block;text-decoration: none;\" ", $diff);
            $diff = str_replace("<ins", "<ins style=\"background-color: #cfc;display: inline-block;text-decoration: none;\" ", $diff);
            $handle = fopen($CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/diff.html', "w");
            fwrite($handle, $diff);
            fclose($handle);
            */
        }

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function __construct($document=null, $editorType = 'webcollagerest')
    {
        require_once(__DIR__ . '/../../../vendors/httpful/bootstrap.php');
        $this->_document = $document;

        if($document)
            $this->_editorType = $document->editorType;
        else
            $this->_editorType = $editorType;
    }
}

class GoogleEditor extends Editor
{
    public $document_url;
    private $_editorType;
    private $_rest_id;
    private $_service;

    public function getDocumentId()
    {
        return $this->_document->guid;
    }

    private function google_auth() {
        global $CONFIG;

        set_include_path(get_include_path() . PATH_SEPARATOR . __DIR__.'/../../../vendors/');
        require_once 'Google/Client.php';
        require_once 'Google/Service/Drive.php';

        $client_id = $CONFIG->google_drive['client_id'];
        $service_account_name = $CONFIG->google_drive['service_account_name'];
        $key_file_location = $CONFIG->google_drive['key_file_location'];
        $application_name = $CONFIG->google_drive['application_name'];

        $key = file_get_contents($key_file_location);
        $cred = new Google_Auth_AssertionCredentials(
            $service_account_name,
            array('https://www.googleapis.com/auth/drive'),
            $key
        );

        $client = new Google_Client();
        $client->setApplicationName($application_name);
        $client->setAssertionCredentials($cred);

        if($client->getAuth()->isAccessTokenExpired()) {
            $client->getAuth()->refreshTokenWithAssertion($cred);
        }

        $service = new Google_Service_Drive($client);

        return $service;
    }

    private function get_created_document($template, $mimetype) {
        $hash = hash('sha256', $template);
        $thn = get_metastring_id("gdoc_hash");
        $thv = get_metastring_id($hash);

        if($thn && $thv) {
            $query = <<<SQL
SELECT entity_guid AS guid
FROM metadata m
JOIN entities e ON e.guid = m.entity_guid
JOIN objects_entity o ON e.guid = m.entity_guid
WHERE
name_id = {$thn}
AND value_id = {$thv}
AND m.enabled = 'yes'
AND e.enabled = 'yes'
LIMIT 50
SQL;

            $guid = 0;
            if($gdocs = get_data($query, "ldshake_guid_callback")) {
                while(!$guid && count($gdocs)) {
                    $try_guid = mt_rand(0, count($gdocs)-1);

                    $query = <<<SQL
UPDATE metadata
SET enabled = 'no'
WHERE
name_id = {$thn}
AND value_id = {$thv}
AND enabled = 'yes'
AND entity_guid = {$gdocs[$try_guid]}
LIMIT 1
SQL;

                    if(update_data_affected($query))
                        $guid = $gdocs[$try_guid];
                    else
                        unset($gdocs[$try_guid]);
                }
                if($guid) {
                    //build one more document
                    $this->schedule_cache_remote_gdoc(array(
                        'number' => 1,
                        'data' => $template,
                        'mimeType' => $mimetype,
                        'uploadType' => 'media',
                        'convert' => true,
                        'hash' => $hash,
                        'title' => 'template',
                        'description' => 'desc1'
                    ));
                    return $guid;
                }
            }
        }

       //build more documents
        $this->schedule_cache_remote_gdoc(array(
            'number' => 10,
            'data' => $template,
            'mimeType' => $mimetype,
            'uploadType' => 'media',
            'convert' => true,
            'hash' => $hash,
            'title' => 'template',
            'description' => 'desc1'
        ));
        return false;
    }

    private function create_remote_document($data) {
        $title = $data['title'];
        $description = $data['description'];
        $mimeType = $data['mimeType'];
        $data = $data['data'];

        $file = new Google_Service_Drive_DriveFile();
        $file->setTitle($title);
        $file->setDescription($description);
        $file->setMimeType($mimeType);
        $file->setWritersCanShare(false);
        $file->setFileSize(strlen($data));
        try {
            $createdFile = $this->_service->files->insert($file, array(
                'data' => $data,
                'mimeType' => $mimeType,
                'uploadType' => 'media',
                'convert' => true,
            ));
        } catch (Exception $e) {
            return false;
        }

        $value = 'me';
        $type = 'anyone';
        $role = 'writer';
        $fileId = $createdFile->id;
        $newPermission = new Google_Service_Drive_Permission();
        $newPermission->setValue($value);
        $newPermission->setType($type);
        $newPermission->setRole($role);
        $newPermission->setWithLink(true);
        try {
            $this->_service->permissions->insert($fileId, $newPermission);
        } catch (Exception $e) {
            return false;
        }
        return $createdFile;
    }

    private function schedule_cache_remote_gdoc($data) {
        global $CONFIG;

        $cached_gdoc                = new ElggObject();
        $cached_gdoc->subtype       = 'cached_gdoc';
        $cached_gdoc->description   = serialize($data);

        if(!$cache_id = $cached_gdoc->save())
            return false;

        $cmd = 'php ' .$CONFIG->path . 'mod/lds/background/gdocs_cache.php ' . $CONFIG->currentEnv . ' ' . $cache_id . ' > /dev/null &';
        exec($cmd);
    }

    public function cache_remote_gdoc($data) {
        for($i=0; $i < $data['number']; $i++) {
            if($gdoc = $this->create_remote_document($data)) {
                $cached_gdoc                = new ElggObject();
                $cached_gdoc->subtype       = 'cached_gdoc';
                $cached_gdoc->access_id     = ACCESS_PUBLIC;
                $cached_gdoc->gdoc_hash     = $data['hash'];
                $cached_gdoc->title         = $gdoc->id;
                $cached_gdoc->description   = $gdoc->alternateLink;
                $cached_gdoc->save();
            } else {
                return false;
            }
        }
    }

    //load an empty data file to start a new document
    public function newEditor($template_html = null)
    {
        global $CONFIG;
        $editorType = $this->_editorType;
        $ldshake_url = parse_url($CONFIG->url);

        $vars['editor'] = $editorType;
        $vars['editor_label'] = 'google_docs';
        $vars['restapi'] = false;

        if($template_html) {
            if($guid = $this->get_created_document($template_html, 'text/html')) {
                $premade_gdoc = get_entity($guid);
                $vars['editor_id'] = $premade_gdoc->title;
                $doc_url = $premade_gdoc->description;
                $vars['document_url'] = null;
                $vars['document_iframe_url'] = $doc_url;

                return $vars;
            }
        }
        $service = $this->google_auth();

        if(!$template_html) {
            $title="Untitled LdS.rtf";
            $description="desc1";
            $mimeType="application/rtf";
            $filename="/var/www/something.rtf";
            $data = file_get_contents($filename);
        } else {
            $title = 'template';
            $description="desc1";
            $mimeType = 'text/html';
            $data = $template_html;
        }

        $file = new Google_Service_Drive_DriveFile();
        $file->setTitle($title);
        $file->setDescription($description);
        $file->setMimeType($mimeType);
        $file->setWritersCanShare(false);
        $file->setFileSize(strlen($data));
        try {
            $createdFile = $service->files->insert($file, array(
                'data' => $data,
                'mimeType' => $mimeType,
                'uploadType' => 'media',
                'convert' => true,
            ));
        } catch (Exception $e) {
            return false;
        }

        $value = 'me';
        $type = 'anyone';
        $role = 'writer';
        $fileId = $createdFile->id;
        $newPermission = new Google_Service_Drive_Permission();
        $newPermission->setValue($value);
        $newPermission->setType($type);
        $newPermission->setRole($role);
        $newPermission->setWithLink(true);
        try {
            $service->permissions->insert($fileId, $newPermission);
        } catch (Exception $e) {
            return false;
        }

        $vars['editor_id'] = $createdFile->id;
        $doc_url = $createdFile->alternateLink;
        $vars['document_url'] = null;
        $vars['document_iframe_url'] = $doc_url;

        return $vars;
    }

    //load the document into the exelearning server
    public function editDocument()
    {
        global $CONFIG;
        //$filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = rand_str(64);
        $lds = get_entity($this->_document->lds_guid);

        $vars['editor'] = $this->_document->editorType;
        $vars['editor_label'] = 'google_docs';
        $vars['restapi'] = false;

        $doc_id = $this->_document->drive_id;

        $service = $this->google_auth();

        try {
            $google_file = $service->files->get($doc_id);
        } catch (Exception $e) {
            return false;
        }

        $vars['document_url'] = "";
        $vars['document_iframe_url'] = $google_file->alternateLink;
        $vars['editor_id'] = $doc_id;

        return $vars;
    }

    public function putImplementation($params)
    {
        global $CONFIG;
        $lds = $params['lds'];
        $vle = $params['vle'];
        $gluepsm = new GluepsManager($vle);
        $vle_info = $gluepsm->getVleInfo();
        $course_info = $gluepsm->getCourseInfo($params['course_id']);

        $vle_info->id = $vle->guid;
        $vle_info->name = $vle->name;
        if($vle->admin_id) {
            $admin_vle = get_entity($vle->admin_id);
            $vle_info->version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $vle_info->wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }

        $wic_vle_data = array(
            'learningEnvironment' => $vle_info,
            'course' => $course_info
        );

        $json_wic_vle_data = json_encode($wic_vle_data);

        $putData = tmpfile();
        fwrite($putData, $json_wic_vle_data);
        fseek($putData, 0);
        $m_fd = stream_get_meta_data($putData);

        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = rand_str(64);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $ldshake_url = parse_url($CONFIG->url);
        $ldshake_frame_origin = $ldshake_url['scheme'].'://'.$ldshake_url['host'];

        $vars['editor'] = 'webcollagerest';
        $vars['restapi'] = true;

        $lang = lds_contTools::tool_lang($vars['editor'],$CONFIG->language);

        $post = array(
            'lang' => $lang,
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds};type=application/json; charset=UTF-8",
            'vle_info' => "@{$m_fd['uri']};type=application/json; charset=UTF-8",
            'name' => $params['name'],
            'ldshake_frame_origin' => $ldshake_frame_origin,
        );

        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        try {
            $response = \Httpful\Request::post($uri)
                ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
                ->body($post, 'multipart/form-data')
                ->basicAuth('ldshake_default_user','LdS@k$1#')
                ->sendIt();

            $vars['editor_label'] = 'WebCollage';
            $doc_url = parse_url($response->raw_body);
            if(!isset($doc_url['scheme']) || !isset($doc_url['host']) || !isset($doc_url['path']))
                throw new Exception("Invalid document URL: {$response->raw_body}");

            $url_path = explode('/', $doc_url['path'].$doc_url['query']);
            $url_path_filtered = array();
            foreach ($url_path as $up)
                if(strlen($up))
                    $url_path_filtered[] = $up;
            $doc_id = $url_path_filtered[count($url_path_filtered) -1];
        } catch (Exception $e) {
            register_error(htmlentities($e->getMessage()));
            forward($CONFIG->url . 'pg/lds/');
        }

        $this->_rest_id = $doc_id;
        $vars['document_url'] = "{$response->raw_body}";
        //$vars['document_iframe_url'] = "{$CONFIG->webcollagerest_url}?document_id={$doc_id}&sectoken={$rand_id}";
        $vars['document_iframe_url'] = "http://pandora.tel.uva.es/~wic/wic2Ldshake/indexLdShake.php?document_id={$doc_id}&lang={$lang}";
        $vars['editor_id'] = $rand_id;

        return $vars;
    }

    //function to export a document to a given format and return the file guid
    public function saveNewExportDocument($docSession, $format)
    {
        global $CONFIG;
        $filestorename = rand_str(32).'.zip';
        $file = $this->getNewFile($filestorename);
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type='.$format.'&filename='.$docSession);
        copy($CONFIG->exedata.'export/'.$docSession.'.zip', $file->getFilenameOnFilestore());
        exec('rm --interactive=never '.$CONFIG->exedata.'export/'.$docSession.'.zip');
        return $file->guid;
    }

    //update the exported documents
    public function updateExportDocument($file_guid, $docSession, $format)
    {
        global $CONFIG;
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type='.$format.'&filename='.$docSession);
        copy($CONFIG->exedata.'export/'.$docSession.'.zip', $this->getFullFilePath($file_guid));
        exec('rm --interactive=never '.$CONFIG->exedata.'export/'.$docSession.'.zip');
        return true;
    }

    public function calcHash($docSession)
    {
        global $CONFIG;
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=hashPage');
        $hash = sha1_file($CONFIG->exedata.'export/hashPage/'.$docSession.'/index.html');
        if(strlen((string)$docSession) > 0)
            exec('rm -r --interactive=never '.$CONFIG->exedata.'export/hashPage/'.$docSession);
        return $hash;
    }

    //update the published documents
    public function updatePublish()
    {
        /*
        global $CONFIG;
        copy($this->getFullFilePath($this->_document->ims_ld), $this->getFullFilePath($this->_document->pub_ims_ld));
        copy($this->getFullFilePath($this->_document->scorm), $this->getFullFilePath($this->_document->pub_scorm));
        copy($this->getFullFilePath($this->_document->scorm2004), $this->getFullFilePath($this->_document->pub_scorm2004));
        copy($this->getFullFilePath($this->_document->webZip), $this->getFullFilePath($this->_document->pub_webZip));
        if(strlen((string)$this->_document->pub_previewDir) > 0)
            exec('rm -r --interactive=never '.$CONFIG->editors_content.'content/exe/'.$this->_document->pub_previewDir);
        exec('cp -r '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->pub_previewDir);
        */
    }

    //copy the published documents to the new revision
    public function publishedRevision($revision)
    {
        $revision->pub_ims_ld = $this->_document->pub_ims_ld;
        $revision->pub_scorm = $this->_document->pub_scorm;
        $revision->pub_scorm2004 = $this->_document->pub_scorm2004;
        $revision->pub_webZip = $this->_document->pub_webZip;
        $revision->pub_previewDir = $this->_document->pub_previewDir;
    }

    //create a directory with the preview for the new revision
    public function revisionPreview($revision)
    {
        global $CONFIG;
        //exec('cp -r '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.' '.$CONFIG->editors_content.'content/exe/'.$revision->previewDir);

        return true;
    }

    public function saveNewDocument($params = null)
    {
        global $CONFIG;

        //save the contents
        $docSession = $params['editor_id'];
        $title = $params['title'];

        $resultIds = new stdClass();
        $user = get_loggedin_user();

        $service = $this->google_auth();

        try {
            $google_file = $service->files->get($docSession);
        } catch (Exception $e) {
            return false;
        }

        //create a new file to store the document
        $rand_id = mt_rand(400,9000000);
        $filestorename = (string)$rand_id;
        $file = $this->getNewFile($filestorename);
        file_put_contents($file->getFilenameOnFilestore(), "");
        $this->_document->file_guid = $file->guid;

        $this->_document->drive_id = $google_file->id;
        $this->_document->title = $title;
        $this->_document->save();

        //assign a random string to each directory
        $this->_document->previewDir = rand_str(64);
        $this->_document->pub_previewDir = rand_str(64);
        $this->_document->revisionDir = rand_str(64);

        $uri = $google_file->exportLinks['text/html'];

        $preview_path = $CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$this->_document->previewDir;
        mkdir($preview_path);


        try {

            if(!($fd = fopen($preview_path.'/index.html','w')))
                throw new Exception("Cannot create file.");

            $options = array(
                CURLOPT_FILE    => $fd,
                CURLOPT_TIMEOUT =>  60,
                CURLOPT_URL     => $uri,
                CURLOPT_SSL_VERIFYPEER => false
            );

            $ch = curl_init();
            curl_setopt_array($ch, $options);
            $result = curl_exec($ch);

            if($result)
                $this->_document->description = file_get_contents($preview_path.'/index.html');

        } catch (Exception $e) {
            register_error(htmlentities($e->getMessage()));
            return false;
        }

        //$this->_document->rev_last = 0;
        //$this->_document->lds_revision_id = 0;

        $resultIds->guid = $this->_document->lds_guid;
        $resultIds->file_guid = $this->_document->file_guid;

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function cloneDocument($lds)
    {
        global $CONFIG;

        $rand_id = mt_rand(400,9000000);

        $clone = new DocumentEditorObject($lds);
        $lds_entity = get_entity($lds);

        $file_origin = Editor::getFullFilePath($this->_document->file_guid);

        //create a new file to store the document
        /*
        $filestorename = (string)$rand_id;
        $file = $this->getNewFile($filestorename);
        copy($file_origin, $file->getFilenameOnFilestore());

        $clone->file_guid = $file->guid;
        */

        /*
        if($this->_document->file_imsld_guid) {
            $file_origin = Editor::getFullFilePath($this->_document->file_imsld_guid);

            //create a new file to store the document
            $filestorename = (string)$rand_id.'.zip';
            $file = $this->getNewFile($filestorename);
            copy($file_origin, $file->getFilenameOnFilestore());

            $clone->file_imsld_guid = $file->guid;
        }

        */
        $clone->editorType = $this->_document->editorType;

        $clone->save();

        //assign a random string to each directory
        $clone->previewDir = rand_str(64);
        $clone->pub_previewDir = rand_str(64);
        $clone->revisionDir = rand_str(64);

        if(file_exists($CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$this->_document->previewDir . '/index.html')) {
            $src_preview_path = $CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$this->_document->previewDir;
            $preview_path = $CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$clone->previewDir;
            mkdir($preview_path);
            shell_exec("cp -r {$src_preview_path}/. {$preview_path}");
        }

        $clone->rev_last = 0;
        $clone->lds_revision_id = 0;

        $service = $this->google_auth();

        $copiedFile = new Google_Service_Drive_DriveFile();
        $copiedFile->setTitle($lds_entity->title);

        try {
            $copiedFile = $service->files->copy($this->_document->drive_id, $copiedFile);
        } catch (Exception $e) {
            return false;
        }
        $clone->drive_id = $copiedFile->id;
        $clone->support = $this->_document->support;
        $clone->description = $this->_document->description;
        $clone->title = $this->_document->title;
        $clone->save();

        $value = 'me';
        $type = 'anyone';
        $role = 'writer';
        $fileId = $copiedFile->id;
        $newPermission = new Google_Service_Drive_Permission();
        $newPermission->setValue($value);
        $newPermission->setType($type);
        $newPermission->setRole($role);
        $newPermission->setWithLink(true);
        try {
            $service->permissions->insert($fileId, $newPermission);
        } catch (Exception $e) {
            return false;
        }
        create_annotation($lds, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);

        return array($clone);
    }

    public function unload($docSession)
    {
        //file('http://127.0.0.1/exelearning/?unload='. $docSession);
    }

    public function saveDocument($params=null)
    {
        if($this->_document->file_guid)
            return $this->saveExistingDocument($params);
        else
            return $this->saveNewDocument($params);
    }

    //update the previous contents
    public function saveExistingDocument($params=null)
    {
        global $CONFIG;
        $user = get_loggedin_user();
        $resultIds = new stdClass();
        $docSession = $params['editor_id'];

        $service = $this->google_auth();

        try {
            $google_file = $service->files->get($docSession);
        } catch (Exception $e) {
            return false;
        }

        /*
        try {
            $file = $service->files->get($docSession);
        } catch (Exception $e) {
            return false;
        }
        */

        $old_previewDir = $this->_document->previewDir;
        $this->_document->previewDir = rand_str(64);
        $preview_path = $this->_document->previewDir;

        //preview
        $uri = $google_file->exportLinks['text/html'];

        $preview_path = $CONFIG->editors_content.'content/'.'webcollagerest'.'/'.$this->_document->previewDir;
        mkdir($preview_path);

        try {

            if(!($fd = fopen($preview_path.'/index.html','w')))
                throw new Exception("Cannot create file.");

            $options = array(
                CURLOPT_FILE    => $fd,
                CURLOPT_TIMEOUT =>  60,
                CURLOPT_URL     => $uri,
                CURLOPT_SSL_VERIFYPEER => false
            );

            $ch = curl_init();
            curl_setopt_array($ch, $options);
            $result = curl_exec($ch);

            if($result)
                $this->_document->description = file_get_contents($preview_path.'/index.html');

        } catch (Exception $e) {
            register_error(htmlentities($e->getMessage()));
            return false;
        }

        /*
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=singlePage');
        exec('cp -r '.$CONFIG->exedata.'export/singlePage/'.$docSession.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir);
        if(strlen((string)$docSession) > 0)
            exec('rm -r --interactive=never '.$CONFIG->exedata.'export/singlePage/'.$docSession);
        if(strlen((string)$old_previewDir) > 0)
            exec('rm -r --interactive=never '.$CONFIG->editors_content.'content/exe/'.$old_previewDir);


        $this->updateExportDocument($this->_document->ims_ld, $docSession, 'IMS');
        $this->updateExportDocument($this->_document->scorm, $docSession, 'scorm');
        $this->updateExportDocument($this->_document->scorm2004, $docSession, 'scorm2004');
        $this->updateExportDocument($this->_document->webZip, $docSession, 'zipFile');

        */
        $revisions = get_entities_from_metadata('document_guid',$this->_document->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
        $resultIds->count = count($revisions);

        //create the diff content against the last saved revision
        if(count($revisions) > 0)
        {
            /*
            $output = array();
            exec ("{$CONFIG->pythonpath} {$CONFIG->path}mod/lds/ext/diff.py".' '.$CONFIG->editors_content.'content/exe/'.$revisions[count($revisions)-1]->previewDir.'/index.html'.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/index.html', $output);
            $diff = implode('', $output);
            //insert an inline style definition to highlight the differences
            $diff = str_replace("<del", "<del style=\"background-color: #fcc;display: inline-block;text-decoration: none;\" ", $diff);
            $diff = str_replace("<ins", "<ins style=\"background-color: #cfc;display: inline-block;text-decoration: none;\" ", $diff);
            $handle = fopen($CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/diff.html', "w");
            fwrite($handle, $diff);
            fclose($handle);
            */
        }

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function __construct($document=null, $editorType = 'webcollagerest')
    {
        $this->_document = $document;

        if($document)
            $this->_editorType = $document->editorType;
        else
            $this->_editorType = $editorType;

        $this->_service = $this->google_auth();
    }
}

class UploadEditor extends Editor
{
    public $document_url;
    private $_editorType;

    public function getDocumentId()
    {
        return $this->_document->guid;
    }

    //load an empty data file to start a new document
    public function newEditor()
    {
        global $CONFIG;
        $user = get_loggedin_user();
        $rand_id = rand_str(64);//mt_rand(1000000,5000000);

        $vars['editor_id'] = 0;

        $vars['document_url'] = null;
        $vars['document_iframe_url'] = null;
        $vars['editor'] = $this->_editorType;
        $vars['editor_label'] = 'File Upload';

        return $vars;
    }

    //load the document into the exelearning server
    public function editDocument()
    {
        global $CONFIG;
        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = rand_str(64);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $vars['editor'] = $this->_document->editorType;
        $vars['editor_label'] = '';
        $vars['document_url'] = null;
        $vars['document_iframe_url'] = null;
        $vars['editor_id'] = 0;
        $vars['upload'] = true;

        return $vars;
    }

    public function cloneDocument($lds)
    {
        global $CONFIG;

        $rand_id = mt_rand(400,9000000);

        $clone = new DocumentEditorObject($lds);

        $file_origin = Editor::getFullFilePath($this->_document->file_guid);

        //create a new file to store the document
        $filestorename = (string)$rand_id;
        $file = $this->getNewFile($filestorename);
        copy($file_origin, $file->getFilenameOnFilestore());
        $clone->file_guid = $file->guid;
        $clone->upload_filename = $this->_document->upload_filename;

        if($this->_document->file_imsld_guid) {
            $filestorename = (string)$rand_id.'.zip';
            $file = $this->getNewFile($filestorename);
            $file_origin = Editor::getFullFilePath($this->_document->file_imsld_guid);
            copy($file_origin, $file->getFilenameOnFilestore());
            $clone->file_imsld_guid = $file->guid;
            $clone->upload_filename_imsld = $this->_document->upload_filename_imsld;
        }

        $clone->editorType = $this->_document->editorType;
        $clone->save();

        //assign a random string to each directory
        $clone->previewDir = rand_str(64);
        $clone->pub_previewDir = rand_str(64);
        $clone->revisionDir = rand_str(64);

        $clone->rev_last = 0;
        $clone->lds_revision_id = 0;

        $clone->save();

        create_annotation($lds, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);

        return array($clone);
    }

    public function putImplementation($params)
    {
        global $CONFIG;
        $vle = $params['vle'];
        $gluepsm = new GluepsManager($vle);
        $vle_info = $gluepsm->getVleInfo();
        $course_info = $gluepsm->getCourseInfo($params['course_id']);
        $vle_info->id = $vle->guid;
        $vle_info->name = $vle->name;

        if($this->_vle->admin_id) {
            $admin_vle = get_entity($this->_vle->admin_id);
            $vle_info->version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $vle_info->wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }

        $wic_vle_data = array(
            'learningEnvironment' => $vle_info,
            'course' => $course_info
        );

        $json_wic_vle_data = json_encode($wic_vle_data);

        $putData = tmpfile();
        fwrite($putData, $json_wic_vle_data);
        fseek($putData, 0);
        $m_fd = stream_get_meta_data($putData);

        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = rand_str(64);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $post = array(
            'lang' => 'en',
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds};type=application/json; charset=UTF-8",
            'vle_info' => "@{$m_fd['uri']};type=application/json; charset=UTF-8"
        );

        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        try {
            $response = \Httpful\Request::post($uri)
                ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
                ->body($post, 'multipart/form-data')
                ->basicAuth('ldshake_default_user','LdS@k$1#')
                ->sendIt();

            $vars['restapi'] = !!RestEditor::rest_enabled($this->_document->editorType);
            $vars['editor'] = $this->_document->editorType;
            $vars['editor_label'] = $CONFIG->rest_editor_list[$this->_document->editorType]->name;
            $doc_url = parse_url($response->raw_body);
            $url_path = explode('/', $doc_url['path'].$doc_url['query']);
            $url_path_filtered = array();
            foreach ($url_path as $up)
                if(strlen($up))
                    $url_path_filtered[] = $up;
            $doc_id = $url_path_filtered[count($url_path_filtered) -1];
        } catch (Exception $e) {
            echo 'Caught exception: ',  $e->getMessage(), "\n";
        }

        $this->_rest_id = $doc_id;
        $vars['document_url'] = "{$response->raw_body}";
        $vars['document_iframe_url'] = "{$CONFIG->webcollagerest_url}?document_id={$doc_id}&sectoken={$rand_id}";
        $vars['editor_id'] = $rand_id;

        return $vars;
    }

    //function to export a document to a given format and return the file guid
    public function saveNewExportDocument($docSession, $format)
    {
        global $CONFIG;
        $filestorename = rand_str(32).'.zip';
        $file = $this->getNewFile($filestorename);
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type='.$format.'&filename='.$docSession);
        copy($CONFIG->exedata.'export/'.$docSession.'.zip', $file->getFilenameOnFilestore());
        exec('rm --interactive=never '.$CONFIG->exedata.'export/'.$docSession.'.zip');
        return $file->guid;
    }

    //update the exported documents
    public function updateExportDocument($file_guid, $docSession, $format)
    {
        global $CONFIG;
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type='.$format.'&filename='.$docSession);
        copy($CONFIG->exedata.'export/'.$docSession.'.zip', $this->getFullFilePath($file_guid));
        exec('rm --interactive=never '.$CONFIG->exedata.'export/'.$docSession.'.zip');
        return true;
    }

    public function calcHash($docSession)
    {
        global $CONFIG;
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=hashPage');
        $hash = sha1_file($CONFIG->exedata.'export/hashPage/'.$docSession.'/index.html');
        if(strlen((string)$docSession) > 0)
            exec('rm -r --interactive=never '.$CONFIG->exedata.'export/hashPage/'.$docSession);
        return $hash;
    }

    //update the published documents
    public function updatePublish()
    {
        /*
        global $CONFIG;
        copy($this->getFullFilePath($this->_document->ims_ld), $this->getFullFilePath($this->_document->pub_ims_ld));
        copy($this->getFullFilePath($this->_document->scorm), $this->getFullFilePath($this->_document->pub_scorm));
        copy($this->getFullFilePath($this->_document->scorm2004), $this->getFullFilePath($this->_document->pub_scorm2004));
        copy($this->getFullFilePath($this->_document->webZip), $this->getFullFilePath($this->_document->pub_webZip));
        if(strlen((string)$this->_document->pub_previewDir) > 0)
            exec('rm -r --interactive=never '.$CONFIG->editors_content.'content/exe/'.$this->_document->pub_previewDir);
        exec('cp -r '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->pub_previewDir);
        */
    }

    //copy the published documents to the new revision
    public function publishedRevision($revision)
    {
        $revision->pub_ims_ld = $this->_document->pub_ims_ld;
        $revision->pub_scorm = $this->_document->pub_scorm;
        $revision->pub_scorm2004 = $this->_document->pub_scorm2004;
        $revision->pub_webZip = $this->_document->pub_webZip;
        $revision->pub_previewDir = $this->_document->pub_previewDir;
    }

    //create a directory with the preview for the new revision
    public function revisionPreview($revision)
    {
        global $CONFIG;
        exec('cp -r '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.' '.$CONFIG->editors_content.'content/exe/'.$revision->previewDir);

        return true;
    }

    public function saveNewDocument($params = null)
    {
        global $CONFIG;

        $resultIds = new stdClass();
        $user = get_loggedin_user();

        //create a new file to store the document
        $rand_id = mt_rand(400,9000000);
        $filestorename = (string)$rand_id;
        $file = $this->getNewFile($filestorename);
        $this->_document->file_guid = $file->guid;

        //save the contents
        if($docSession = $params['editor_id']) {
            $doc_file = get_entity($docSession);
            $this->_document->upload_filename = $doc_file->upload_filename;
            $file_origin = Editor::getFullFilePath($docSession);
            copy($file_origin, $file->getFilenameOnFilestore());
        } elseif(isset($CONFIG->editor_templates[$this->_document->editorType])) {
            $this->_document->upload_filename = $CONFIG->editor_templates[$this->_document->editorType]['filename'];
            $file_origin = $CONFIG->path.$CONFIG->editor_templates[$this->_document->editorType]['path'];
            copy($file_origin, $file->getFilenameOnFilestore());
        }

        $this->_document->save();

        //create a new file to store the document
        $rand_id = mt_rand(400,9000000);
        $filestorename = (string)$rand_id.'.zip';

        if($docSession) {
            $file = $this->getNewFile($filestorename);
            $file_origin = Editor::getFullFilePath($docSession);
            copy($file_origin, $file->getFilenameOnFilestore());
            $this->_document->upload_filename_imsld = $doc_file->upload_filename;
            $this->_document->file_imsld_guid = $file->guid;
        } elseif(isset($CONFIG->editor_templates[$this->_document->editorType]['imsld'])) {
            $file = $this->getNewFile($filestorename);
            $this->_document->upload_filename_imsld = $CONFIG->editor_templates[$this->_document->editorType]['imsld']['filename'];
            $file_origin = $CONFIG->path.$CONFIG->editor_templates[$this->_document->editorType]['imsld']['path'];
            copy($file_origin, $file->getFilenameOnFilestore());
            $this->_document->file_imsld_guid = $file->guid;
        }

        $this->_document->save();

        //assign a random string to each directory
        $this->_document->previewDir = rand_str(64);
        $this->_document->pub_previewDir = rand_str(64);
        $this->_document->revisionDir = rand_str(64);

        $this->_document->rev_last = 0;
        $this->_document->lds_revision_id = 0;

        $resultIds->guid = $this->_document->lds_guid;
        $resultIds->file_guid = $this->_document->file_guid;

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function unload($docSession)
    {
        //file('http://127.0.0.1/exelearning/?unload='. $docSession);
    }

    public function saveDocument($params=null)
    {
        if($this->_document->file_guid)
            return $this->saveExistingDocument($params);
        else
            return $this->saveNewDocument($params);
    }

    //update the previous contents
    public function saveExistingDocument($params=null)
    {
        global $CONFIG;
        $user = get_loggedin_user();
        $resultIds = new stdClass();
        $docSession = $params['editor_id'];
        if($doc_file = get_entity($docSession)) {
            //create a new file to store the document
            $file_origin = Editor::getFullFilePath($docSession);
            copy($file_origin, Editor::getFullFilePath($this->_document->file_guid));
            copy($file_origin, Editor::getFullFilePath($this->_document->file_imsld_guid));
            $this->_document->upload_filename = $doc_file->upload_filename;
        }

        $this->_document->previewDir = rand_str(64);


        /*
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=singlePage');
        exec('cp -r '.$CONFIG->exedata.'export/singlePage/'.$docSession.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir);
        if(strlen((string)$docSession) > 0)
            exec('rm -r --interactive=never '.$CONFIG->exedata.'export/singlePage/'.$docSession);
        if(strlen((string)$old_previewDir) > 0)
            exec('rm -r --interactive=never '.$CONFIG->editors_content.'content/exe/'.$old_previewDir);


        $this->updateExportDocument($this->_document->ims_ld, $docSession, 'IMS');
        $this->updateExportDocument($this->_document->scorm, $docSession, 'scorm');
        $this->updateExportDocument($this->_document->scorm2004, $docSession, 'scorm2004');
        $this->updateExportDocument($this->_document->webZip, $docSession, 'zipFile');

        */
        $revisions = get_entities_from_metadata('document_guid',$this->_document->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
        $resultIds->count = count($revisions);

        //create the diff content against the last saved revision
        if(count($revisions) > 0)
        {
            /*
            $output = array();
            exec ("{$CONFIG->pythonpath} {$CONFIG->path}mod/lds/ext/diff.py".' '.$CONFIG->editors_content.'content/exe/'.$revisions[count($revisions)-1]->previewDir.'/index.html'.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/index.html', $output);
            $diff = implode('', $output);
            //insert an inline style definition to highlight the differences
            $diff = str_replace("<del", "<del style=\"background-color: #fcc;display: inline-block;text-decoration: none;\" ", $diff);
            $diff = str_replace("<ins", "<ins style=\"background-color: #cfc;display: inline-block;text-decoration: none;\" ", $diff);
            $handle = fopen($CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/diff.html', "w");
            fwrite($handle, $diff);
            fclose($handle);
            */
        }

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function __construct($document=null, $editorType = null)
    {
        require_once(__DIR__ . '/../../../vendors/httpful/bootstrap.php');

        $this->_document = $document;
        $this->_editorType = $editorType;
    }
}

class GluepsManager
{
    public $lds;
    public $_document;
    public $document_url;
    public $instance_url;
    public $_implementation;
    public $_glueps_document;
    public $_vle;

    public function __construct($vle = null, $implementation=null, $glueps_document=null)
    {
        require_once(__DIR__ . '/../../../vendors/httpful/bootstrap.php');
        $this->_implementation = $implementation;
        $this->_document = $glueps_document;
        $this->_vle = $vle;
    }

    public function testVle() {

    }

    public function cloneImplementation($title = null) {
        $implementation = new LdS();
        $implementation->subtype = 'LdS_implementation';
        $implementation->access_id = $this->_implementation->access_id;
        $implementation->container_guid = $this->_implementation->container_guid;
        if(!$title)
            $title = $this->_implementation->title;
        $implementation->title = $title;
        $implementation->vle_id = $this->_implementation->vle_id;
        $implementation->course_id = $this->_implementation->course_id;
        $implementation->lds_id = $this->_implementation->lds_id;

        $implementation->granularity = 0;
        $implementation->completeness = 0;
        $implementation->cloned = 1;
        $implementation->parent = $this->_implementation->guid;
        $implementation->editor_type = $this->_implementation->editor_type;

        $tagFields = array ('discipline', 'pedagogical_approach', 'tags');
        foreach ($tagFields as $field)
            $implementation->$field = $this->_implementation->$field;

        $implementation->save();
        create_annotation($implementation->guid, 'revised_docs', '', 'text', get_loggedin_userid(), 1);
        $revision = $implementation->getAnnotations('revised_docs', 1, 0, 'desc');
        $revision = $revision[0];

        foreach($this->_document as $d) {
            $newdoc = new DocumentObject($implementation->guid);
            $newdoc->description = $d->description;
            $newdoc->title = $d->title;
            $newdoc->lds_revision_id = $revision->id;
            $newdoc->save();
        }

        if($editordocument = get_entities_from_metadata('lds_guid',$this->_implementation->guid,'object','LdS_document_editor', 0, 100)) {
            //$em = EditorsFactory::getInstance($editordocument[0]);
            //$em->cloneDocument($lds->guid);

            $rand_id = mt_rand(400,9000000);

            $clone = new DocumentEditorObject($implementation->guid);

            $file_origin = Editor::getFullFilePath($this->_document->file_guid);

            //create a new file to store the document
            $filestorename = (string)$rand_id;
            $file = $this->getNewFile($filestorename);
            file_put_contents($file_origin, $file->getFilenameOnFilestore());

            $clone->file_guid = $file->guid;

            //assign a random string to each directory
            $clone->previewDir = rand_str(64);
            $clone->pub_previewDir = rand_str(64);
            $clone->revisionDir = rand_str(64);

            $clone->rev_last = 0;
            $clone->lds_revision_id = 0;

            $clone->save();

            create_annotation($implementation->guid, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);

        }


        $implementation->save();
        return $implementation;
    }

    public function validateVle() {
        if(
            strlen($this->_vle->username) &&
            strlen($this->_vle->password) &&
            strlen($this->_vle->vle_type) &&
            strlen($this->_vle->vle_url)
        )
            return true;

        return false;
    }

    public function getVleInfo($test = false) {
        global $CONFIG;

        if(!$this->validateVle())
            return false;

        $glueps_url = $CONFIG->glueps_url;
        $vle_password = $this->_vle->encrypted ? lds_contTools::decrypt_password($this->_vle->password): $this->_vle->password;

        $version='';
        $wstoken='';

        if($this->_vle->admin_id) {
            $admin_vle = get_entity($this->_vle->admin_id);
            $version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }

        $get = array(
            'type' => $this->_vle->vle_type,
            'accessLocation' => $this->_vle->vle_url,
            'creduser' => $this->_vle->username,
            'credsecret' => $vle_password,
            'version' => $version,
            'wstoken' => $wstoken
        );

        $uri = "{$glueps_url}courses?"
            ."type=".urlencode($get['type'])."&"
            ."accessLocation=".urlencode($get['accessLocation'])."&"
            ."creduser=".urlencode($get['creduser'])."&"
            ."credsecret=".urlencode($get['credsecret'])."&"
            ."version=".urlencode($get['version'])."&"
            ."wstoken=".urlencode($get['wstoken']);

        try {
            $response = \Httpful\Request::get($uri)
                ->addHeader('Accept', 'application/json')
                ->basicAuth('ldshake','Ld$haK3')
                ->sendIt();


            if($response->code > 399) {
                if($test)
                    return false;
                else
                    throw new Exception("VLE server error, go to \"Register VLE\" and check the configuration.");
            }

            }
        catch (Exception $e) {
            register_error($e->getMessage());
            forward($CONFIG->url . 'pg/lds/');
            return false;
        }

        return $response->body;
    }

    public function getCourseInfo($course_id) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;
        $vle_password = $this->_vle->encrypted ? lds_contTools::decrypt_password($this->_vle->password): $this->_vle->password;

        $version='';
        $wstoken='';

        if($this->_vle->admin_id) {
            $admin_vle = get_entity($this->_vle->admin_id);
            $version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }

        $get = array(
            'type' => $this->_vle->vle_type,
            'accessLocation' => $this->_vle->vle_url,
            'creduser' => $this->_vle->username,
            'credsecret' => $vle_password,
            'course' => $course_id,
            'version' => $version,
            'wstoken' => $wstoken
        );

        $uri = "{$url}courses?"
            ."type=".urlencode($get['type'])."&"
            ."accessLocation=".urlencode($get['accessLocation'])."&"
            ."creduser=".urlencode($get['creduser'])."&"
            ."credsecret=".urlencode($get['credsecret'])."&"
            ."course=".urlencode($get['course'])."&"
            ."version=".urlencode($get['version'])."&"
            ."wstoken=".urlencode($get['wstoken']);

        $response = \Httpful\Request::get($uri)
            ->addHeader('Accept', 'application/json')
            //->expectsXML()
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

        return $response->body;
    }

    public function newImplementation($params = null) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;
        /*
        $vle_url = "http://glue-test.cloud.gsic.tel.uva.es/moodle/";
        $type = 'Moodle';
        $username = 'metis';
        $password = 'M3t1$project';
        */
        $course = $params['course'];

        $vle_info = $this->getVleInfo();
        $course_info = $this->getCourseInfo($course);
        $vle_info->id = $this->_vle->guid;
        $vle_info->name = $this->_vle->name;

        if($this->_vle->admin_id) {
            $admin_vle = get_entity($this->_vle->admin_id);
            $vle_info->version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $vle_info->wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }

        $vle_password = $this->_vle->encrypted ? lds_contTools::decrypt_password($this->_vle->password): $this->_vle->password;

        $wic_vle_data = array(
            'learningEnvironment' => $vle_info,
            'course' => $course_info,
            'name' => $vle_info->name,
            'type' => $this->_vle->vle_type,
            'creduser' => $this->_vle->username,
            'credsecret' => $vle_password,
            'participants' => $course_info->participants,
        );
        //unset($vle_info->courses);
        //$vle_info->course = $course_info;
        //$vle_info->participants = $course_info->participants;

        $json_wic_vle_data = json_encode($wic_vle_data);

        $putData = tmpfile();
        fwrite($putData, $json_wic_vle_data);
        fseek($putData, 0);
        $m_fd = stream_get_meta_data($putData);

        $implementation = $params['implementation'];
        //$ldsm = EditorsFactory::getManager($lds);
        //$document = $ldsm->getDocument();
        $document = $params['document'];
        if($document->file_imsld_guid)
            $filename_lds = Editor::getFullFilePath($document->file_imsld_guid);
        else
            $filename_lds = Editor::getFullFilePath($document->file_guid);

        $sectoken = rand_str(32);

        $ldshake_url = parse_url($CONFIG->url);
        $ldshake_frame_origin = $ldshake_url['scheme'].'://'.$ldshake_url['host'];

        $post = array(
            'NewDeployTitleName' => $implementation->title,
            'instType' => 'IMS LD',
            'sectoken' => $sectoken,
            'archiveWic' => "@{$filename_lds}",
            'vleData' => "@{$m_fd['uri']};type=application/json; charset=UTF-8",
            'ldshake_frame_origin' => $ldshake_frame_origin,
            'lang' => 'en'
        );

        try {
            $uri = "{$url}deploys";
            $response = \Httpful\Request::post($uri)
                ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
                ->body($post, 'multipart/form-data')
                ->basicAuth('ldshake','Ld$haK3')
                ->sendIt();

            if($response->code != 200)
                throw new Exception($response->code. ' ' .$response->body);
            $xmldoc = new DOMDocument();
            $xmldoc->loadXML($response->raw_body);
            $xpathvar = new Domxpath($xmldoc);

            $queryResult = $xpathvar->query('//deploy');
            $doc_url = $queryResult->item(0)->getAttribute('id');

            $url_path = explode('/', $doc_url);
            $url_path_filtered = array();
            foreach ($url_path as $up)
                if(strlen($up))
                    $url_path_filtered[] = $up;
            $deploy_id = $url_path_filtered[count($url_path_filtered) -1];
        } catch (Exception $e) {
            register_error($e->getMessage());
            forward($CONFIG->url);
        }

        $vars = array();
        $vars['editor'] = 'gluepsrest';
        $vars['editor_label'] = 'GLUE!-PS';

        $url_gui_parsed = parse_url($url);

        if(isset($url_gui_parsed['port']))
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'].':'.$url_gui_parsed['port'];
        else
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'];

        $vars['restapi'] = true;
        $vars['restapi_remote_domain'] = $gui_frame_origin;

        $vars['editor_id'] = $sectoken;
        $vars['document_url'] = "{$url}deploys/{$deploy_id}";
        //$vars['document_iframe_url'] = "{$url}gui/glueps/deploy.html?deployId={$deploy_id}&sectoken={$sectoken}&lang=en";
        $lang = lds_contTools::tool_lang($vars['editor'],$CONFIG->language);
        $vars['document_iframe_url'] = "{$url}gui/glueps/deployLdShake.html?deployId={$deploy_id}&lang={$lang}";

        return $vars;
    }

    public function updateImplementation($params = null) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;

        $uri = "{$url}deploys/{$params['id']}";
        $response = \Httpful\Request::put($uri, "@{$params['file']}", 'application/octet-stream')
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

        return $response->body;
    }

    public function deployImplementation($params = null) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;

        $uri = "{$url}deploys/{$params['id']}/static";
        $response = \Httpful\Request::put($uri, "@{$params['file']}", 'application/octet-stream')
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

        return $response->body;
    }

    public static function getImplementation($params = null) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;

        $uri = "{$params['document_url']}";

        $response = \Httpful\Request::get($uri)
            //->addHeader('Accept', 'application/json')
            //->expectsXML()
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

        return $response->raw_body;
    }

    public function loadInstantiation() {
        global $CONFIG;
        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = mt_rand(400,5000000);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $post = array(
            'lang' => 'en',
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds}"
        );

        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->sendIt();

        //copy($filename_lds, $filename_editor);

        //file('http://127.0.0.1/exelearning/?load='.$rand_id);
        //unlink($filename_editor);

        $url_gui_parsed = parse_url($CONFIG->rest_editor_list["webcollagerest"]['url_gui']);

        if(isset($url_gui_parsed['port']))
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'].':'.$url_gui_parsed['port'];
        else
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'];

        $vars['restapi'] = true;
        $vars['restapi_remote_domain'] = $gui_frame_origin;

        $vars['editor'] = 'webcollagerest';
        $vars['editor_label'] = 'WebCollage';
        $vars['document_url'] = $response->raw_body;
        $vars['editor_id'] = $rand_id;

        return $vars;
    }

    public function editDocument($params) {
        global $CONFIG;
        $filename_lds = Editor::getFullFilePath($this->_document->file_guid);

        $lds = get_entity($this->_document->lds_guid);
        $design = get_entity($lds->lds_id);
        $editordocument = get_entities_from_metadata_multi(array(
                'lds_guid' => $lds->guid,
                'editorType' => $design->editor_type
            ),
            'object','LdS_document_editor', 0, 100);

        if($editordocument[0]->time_updated > $this->_document->time_updated)
        {
            $filename_lds = Editor::getFullFilePath($editordocument[0]->file_imsld_guid);
            $instType = 'IMS LD';
            $archiveWic = "@{$filename_lds}";
        } else {
            $filename_lds = Editor::getFullFilePath($this->_document->file_guid);
            $instType = 'GLUEPS';
            $archiveWic = "@{$filename_lds};type=application/xml";
        }

        $rand_id = mt_rand(400,5000000);

        $url = $CONFIG->glueps_url;
        $course = $params['course'];

        $vle_info = $this->getVleInfo();
        $course_info = $this->getCourseInfo($course);
        $vle_info->id = $this->_vle->guid;
        $vle_info->name = $this->_vle->name;

        if($this->_vle->admin_id) {
            $admin_vle = get_entity($this->_vle->admin_id);
            $vle_info->version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $vle_info->wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }

        $vle_password = $this->_vle->encrypted ? lds_contTools::decrypt_password($this->_vle->password): $this->_vle->password;

        $wic_vle_data = array(
            'learningEnvironment' => $vle_info,
            'course' => $course_info,
            'name' => $vle_info->name,
            'type' => $this->_vle->vle_type,
            'creduser' => $this->_vle->username,
            'credsecret' => $vle_password,
            'participants' => $course_info->participants
        );

        $json_wic_vle_data = json_encode($wic_vle_data);

        $putData = tmpfile();
        fwrite($putData, $json_wic_vle_data);
        fseek($putData, 0);
        $m_fd = stream_get_meta_data($putData);

        /*
        $lds = $params['lds'];
        $ldsm = EditorsFactory::getManager($lds);
        $document = $ldsm->getDocument();
        $filename_lds = Editor::getFullFilePath($document->file_imsld_guid);
        */

        $sectoken = rand_str(32);

        $ldshake_url = parse_url($CONFIG->url);
        $ldshake_frame_origin = $ldshake_url['scheme'].'://'.$ldshake_url['host'];


        $post = array(
            'NewDeployTitleName' => $params['title'],
            'instType' => $instType,
            'sectoken' => $sectoken,
            'archiveWic' => $archiveWic,
            'vleData' => "@{$m_fd['uri']};type=application/json; charset=UTF-8",
            'ldshake_frame_origin' => $ldshake_frame_origin,
        );

        try {
            $uri = "{$url}deploys";
            $response = \Httpful\Request::post($uri)
                ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
                ->body($post, 'multipart/form-data')
                ->basicAuth('ldshake','Ld$haK3')
                ->sendIt();

            if($response->code != 200)
                throw new Exception("GLUEPS!: " . $response->code. ' ' .$response->body);

            $xmldoc = new DOMDocument();
            $xmldoc->loadXML($response->raw_body);
            $xpathvar = new Domxpath($xmldoc);

            $queryResult = $xpathvar->query('//deploy');
            $doc_url = $queryResult->item(0)->getAttribute('id');

            $url_path = explode('/', $doc_url);
            $url_path_filtered = array();
            foreach ($url_path as $up)
                if(strlen($up))
                    $url_path_filtered[] = $up;
            $deploy_id = $url_path_filtered[count($url_path_filtered) -1];


        }
        catch (Exception $e) {
            register_error($e->getMessage());
            forward($CONFIG->url . 'pg/lds/');
            return false;
        }
        $vars = array();
        $vars['editor_id'] = $sectoken;
        $vars['document_url'] = "{$url}deploys/{$deploy_id}";
        //$vars['document_iframe_url'] = "{$url}gui/glueps/deploy.html?deployId={$deploy_id}&sectoken={$sectoken}&lang=en";
        $vars['editor'] = 'gluepsrest';
        $vars['editor_label'] = 'GLUE!-PS';

        $url_gui_parsed = parse_url($url);

        if(isset($url_gui_parsed['port']))
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'].':'.$url_gui_parsed['port'];
        else
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'];

        $vars['restapi'] = true;
        $vars['restapi_remote_domain'] = $gui_frame_origin;

        $lang = lds_contTools::tool_lang($vars['editor'],$CONFIG->language);
        $vars['document_iframe_url'] = "{$url}gui/glueps/deployLdShake.html?deployId={$deploy_id}&lang={$lang}";

        return $vars;
    }

/*    public function depsaveDocument() {
        global $CONFIG;
        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = mt_rand(400,5000000);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $post = array(
            'lang' => 'en',
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds}"
        );

        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->sendIt();

        //copy($filename_lds, $filename_editor);

        //file('http://127.0.0.1/exelearning/?load='.$rand_id);
        //unlink($filename_editor);
        $vars['editor'] = 'webcollagerest';
        $vars['document_url'] = $response->raw_body;
        $vars['editor_id'] = $rand_id;

        return $vars;
    }
*/

//    public function deployDocument() {
//        global $CONFIG;
//
//        $load_vars = $this->loadDocument();
//
//        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
//        $design_contents = file_get_contents($filename_lds);
//        $rand_id = mt_rand(400,5000000);
//        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';
//
//        /*
//        $post = array(
//            'lang' => 'en',
//            'sectoken' => $rand_id,
//            'document' => "@{$filename_lds}"
//        );
//        */
//
//        $uri = "{$CONFIG->glueps_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
//        $uri = "{$load_vars['document_url']}/static";
//        $response = \Httpful\Request::put($uri, $design_contents)
//            ->sendIt();
//
//        //copy($filename_lds, $filename_editor);
//
//        //file('http://127.0.0.1/exelearning/?load='.$rand_id);
//        //unlink($filename_editor);
//        $vars['editor'] = 'webcollagerest';
//        $vars['editor_label'] = 'WebCollage';
//        $vars['document_url'] = $response->raw_body;
//        $vars['editor_id'] = $rand_id;
//
//        return $vars;
//    }

    /*
    public function getVle() {
        global $CONFIG;
        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = mt_rand(400,5000000);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $post = array(
            'lang' => 'en',
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds}"
        );

        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->sendIt();

        //copy($filename_lds, $filename_editor);

        //file('http://127.0.0.1/exelearning/?load='.$rand_id);
        //unlink($filename_editor);
        $vars['editor'] = 'webcollagerest';
        $vars['editor_label'] = 'WebCollage';
        $vars['document_url'] = $response->raw_body;
        $vars['editor_id'] = $rand_id;

        return $vars;
    }
    */


    public function saveNewDocument($params = null)
    {
        global $CONFIG;

        //save the contents
        $docSession = $params['editor_id'];

        $resultIds = new stdClass();
        $user = get_loggedin_user();

        $glueps_xmlcontent = GluepsManager::getImplementation($params);

        $rand_id = mt_rand(400,9000000);

        //create a new file to store the document
        $filestorename = (string)$rand_id;
        $file = Editor::getNewFile($filestorename);
        file_put_contents($file->getFilenameOnFilestore(), $glueps_xmlcontent);
        //copy($filename_editor, $file->getFilenameOnFilestore());
        //unlink($filename_editor);

        $this->_document->file_guid = $file->guid;
        $this->_document->save();

        //assign a random string to each directory
        $this->_document->previewDir = rand_str(64);
        $this->_document->pub_previewDir = rand_str(64);
        $this->_document->revisionDir = rand_str(64);

        $this->_document->rev_last = 0;
        $this->_document->lds_revision_id = 0;

        $resultIds->guid = $this->_document->lds_guid;
        $resultIds->file_guid = $file->guid;

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function saveDocument($params=null)
    {
        if($this->_document->file_guid)
            $this->saveExistingDocument($params);
        else
            $this->saveNewDocument($params);
    }

    //update the previous contents
    public function saveExistingDocument($params=null)
    {
        global $CONFIG;
        $user = get_loggedin_user();
        $resultIds = new stdClass();
        $docSession = $params['editor_id'];

        $glueps_xmlcontent = GluepsManager::getImplementation($params);

        //$filename_editor = $CONFIG->exedata.'export/'.$docSession.'.elp';
        //file('http://127.0.0.1/exelearning/?save='. $docSession);

        $rand_id = mt_rand(400,9000000);

        //create a new file to store the document
        $filestorename = (string)$rand_id;
        //$file = $this->getNewFile($filestorename);
        file_put_contents(Editor::getFullFilePath($this->_document->file_guid), $glueps_xmlcontent);

        /*
        $filename_editor = $CONFIG->exedata.'export/'.$docSession.'.elp';
        file('http://127.0.0.1/exelearning/?save='. $docSession);

        $filename_lds = $this->getFullFilePath($this->_document->file_guid);

        copy($filename_editor, $filename_lds);
        unlink($filename_editor);

        */

        $old_previewDir = $this->_document->previewDir;
        $this->_document->previewDir = rand_str(64);


        /*
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=singlePage');
        exec('cp -r '.$CONFIG->exedata.'export/singlePage/'.$docSession.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir);
        if(strlen((string)$docSession) > 0)
            exec('rm -r --interactive=never '.$CONFIG->exedata.'export/singlePage/'.$docSession);
        if(strlen((string)$old_previewDir) > 0)
            exec('rm -r --interactive=never '.$CONFIG->editors_content.'content/exe/'.$old_previewDir);


        $this->updateExportDocument($this->_document->ims_ld, $docSession, 'IMS');
        $this->updateExportDocument($this->_document->scorm, $docSession, 'scorm');
        $this->updateExportDocument($this->_document->scorm2004, $docSession, 'scorm2004');
        $this->updateExportDocument($this->_document->webZip, $docSession, 'zipFile');

        */
        $revisions = get_entities_from_metadata('document_guid',$this->_document->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
        $resultIds->count = count($revisions);

        //create the diff content against the last saved revision
        if(count($revisions) > 0)
        {
            /*
            $output = array();
            exec ("{$CONFIG->pythonpath} {$CONFIG->path}mod/lds/ext/diff.py".' '.$CONFIG->editors_content.'content/exe/'.$revisions[count($revisions)-1]->previewDir.'/index.html'.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/index.html', $output);
            $diff = implode('', $output);
            //insert an inline style definition to highlight the differences
            $diff = str_replace("<del", "<del style=\"background-color: #fcc;display: inline-block;text-decoration: none;\" ", $diff);
            $diff = str_replace("<ins", "<ins style=\"background-color: #cfc;display: inline-block;text-decoration: none;\" ", $diff);
            $handle = fopen($CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/diff.html', "w");
            fwrite($handle, $diff);
            fclose($handle);
            */
        }

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function cloneDocument($lds)
    {
        global $CONFIG;

        $rand_id = mt_rand(400,9000000);

        $clone = new DocumentEditorObject($lds);

        $file_origin = Editor::getFullFilePath($this->_document->file_guid);

        //create a new file to store the document
        $filestorename = (string)$rand_id;
        $file = Editor::getNewFile($filestorename);
        copy($file_origin, $file->getFilenameOnFilestore());

        $clone->file_guid = $file->guid;
        $clone->editorType = $this->_document->editorType;
        $clone->save();

        //assign a random string to each directory
        $clone->previewDir = rand_str(64);
        $clone->pub_previewDir = rand_str(64);
        $clone->revisionDir = rand_str(64);

        $clone->rev_last = 0;
        $clone->lds_revision_id = 0;

        $clone->save();

        create_annotation($lds, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);

        return array($clone);
    }
}

class MoodleManager
{
    public $lds;
    public $_document;
    public $document_url;
    public $instance_url;
    public $_implementation;
    public $_glueps_document;
    public $_vle;

    public function __construct($vle = null, $implementation=null, $glueps_document=null)
    {
        require_once(__DIR__ . '/../../../vendors/httpful/bootstrap.php');
        $this->_implementation = $implementation;
        $this->_document = $glueps_document;
        $this->_vle = $vle;
    }

    public static function testVle() {

    }

    public function cloneImplementation($title = null) {
        $implementation = new LdS();
        $implementation->subtype = 'LdS_implementation';
        $implementation->access_id = $this->_implementation->access_id;
        $implementation->container_guid = $this->_implementation->container_guid;
        if(!$title)
            $title = $this->_implementation->title;
        $implementation->title = $title;
        $implementation->vle_id = $this->_implementation->vle_id;
        $implementation->course_id = $this->_implementation->course_id;
        $implementation->lds_id = $this->_implementation->lds_id;

        $implementation->granularity = 0;
        $implementation->completeness = 0;
        $implementation->cloned = 1;
        $implementation->parent = $this->_implementation->guid;
        $implementation->editor_type = $this->_implementation->editor_type;

        $tagFields = array ('discipline', 'pedagogical_approach', 'tags');
        foreach ($tagFields as $field)
            $implementation->$field = $this->_implementation->$field;

        $implementation->save();
        create_annotation($implementation->guid, 'revised_docs', '', 'text', get_loggedin_userid(), 1);
        $revision = $implementation->getAnnotations('revised_docs', 1, 0, 'desc');
        $revision = $revision[0];

        foreach($this->_document as $d) {
            $newdoc = new DocumentObject($implementation->guid);
            $newdoc->description = $d->description;
            $newdoc->title = $d->title;
            $newdoc->lds_revision_id = $revision->id;
            $newdoc->save();
        }

        if($editordocument = get_entities_from_metadata('lds_guid',$this->_implementation->guid,'object','LdS_document_editor', 0, 100)) {
            //$em = EditorsFactory::getInstance($editordocument[0]);
            //$em->cloneDocument($lds->guid);

            $rand_id = mt_rand(400,9000000);

            $clone = new DocumentEditorObject($implementation->guid);

            $file_origin = Editor::getFullFilePath($this->_document->file_guid);

            //create a new file to store the document
            $filestorename = (string)$rand_id;
            $file = $this->getNewFile($filestorename);
            file_put_contents($file_origin, $file->getFilenameOnFilestore());

            $clone->file_guid = $file->guid;

            //assign a random string to each directory
            $clone->previewDir = rand_str(64);
            $clone->pub_previewDir = rand_str(64);
            $clone->revisionDir = rand_str(64);

            $clone->rev_last = 0;
            $clone->lds_revision_id = 0;

            $clone->save();

            create_annotation($implementation->guid, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);

        }


        $implementation->save();
        return $implementation;
    }

    public function validateVle() {
        if(
            strlen($this->_vle->username) &&
            strlen($this->_vle->password) &&
            strlen($this->_vle->vle_type) &&
            strlen($this->_vle->vle_url)
        )
            return true;

        return false;
    }

    public function getVleInfo($test = false) {
        global $CONFIG;

        //if(!$this->validateVle())
        //    return false;

        $moodle_url = $CONFIG->moodle['url'];
        $wstoken = $CONFIG->moodle['wstoken'];

        /*
        if($this->_vle->admin_id) {
            $admin_vle = get_entity($this->_vle->admin_id);
            $version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }
        */

        $get = array(
            'wsfunction' => 'core_course_get_courses',
            'wstoken' => $wstoken,
            'moodlewsrestformat' => 'json'
        );

        $uri = "{$moodle_url}webservice/rest/server.php?"
            ."&wsfunction=".urlencode($get['wsfunction'])."&"
            ."moodlewsrestformat=".urlencode($get['moodlewsrestformat'])."&"
            ."wstoken=".urlencode($get['wstoken']);

        try {
            $response = \Httpful\Request::get($uri)
                ->addHeader('Accept', 'application/json')
                ->sendIt();

            if($response->code > 399) {
                if($test)
                    return false;
                else
                    throw new Exception("VLE server error, go to \"Register VLE\" and check the configuration.");
            }
        }
        catch (Exception $e) {
            register_error($e->getMessage());
            forward($CONFIG->url . 'pg/lds/');
            return false;
        }

        return $response->body;
    }

    public function getCourseInfo($course_id) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;
        $vle_password = $this->_vle->encrypted ? lds_contTools::decrypt_password($this->_vle->password): $this->_vle->password;

        $version='';
        $wstoken='';

        if($this->_vle->admin_id) {
            $admin_vle = get_entity($this->_vle->admin_id);
            $version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }

        $get = array(
            'type' => $this->_vle->vle_type,
            'accessLocation' => $this->_vle->vle_url,
            'creduser' => $this->_vle->username,
            'credsecret' => $vle_password,
            'course' => $course_id,
            'version' => $version,
            'wstoken' => $wstoken
        );

        $uri = "{$url}courses?"
            ."type=".urlencode($get['type'])."&"
            ."accessLocation=".urlencode($get['accessLocation'])."&"
            ."creduser=".urlencode($get['creduser'])."&"
            ."credsecret=".urlencode($get['credsecret'])."&"
            ."course=".urlencode($get['course'])."&"
            ."version=".urlencode($get['version'])."&"
            ."wstoken=".urlencode($get['wstoken']);

        $response = \Httpful\Request::get($uri)
            ->addHeader('Accept', 'application/json')
            //->expectsXML()
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

        return $response->body;
    }

    public function newImplementation($params = null) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;
        /*
        $vle_url = "http://glue-test.cloud.gsic.tel.uva.es/moodle/";
        $type = 'Moodle';
        $username = 'metis';
        $password = 'M3t1$project';
        */
        $course = $params['course'];

        $vle_info = $this->getVleInfo();
        $course_info = $this->getCourseInfo($course);
        $vle_info->id = $this->_vle->guid;
        $vle_info->name = $this->_vle->name;

        if($this->_vle->admin_id) {
            $admin_vle = get_entity($this->_vle->admin_id);
            $vle_info->version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $vle_info->wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }

        $vle_password = $this->_vle->encrypted ? lds_contTools::decrypt_password($this->_vle->password): $this->_vle->password;

        $wic_vle_data = array(
            'learningEnvironment' => $vle_info,
            'course' => $course_info,
            'name' => $vle_info->name,
            'type' => $this->_vle->vle_type,
            'creduser' => $this->_vle->username,
            'credsecret' => $vle_password,
            'participants' => $course_info->participants,
        );
        //unset($vle_info->courses);
        //$vle_info->course = $course_info;
        //$vle_info->participants = $course_info->participants;

        $json_wic_vle_data = json_encode($wic_vle_data);

        $putData = tmpfile();
        fwrite($putData, $json_wic_vle_data);
        fseek($putData, 0);
        $m_fd = stream_get_meta_data($putData);

        $implementation = $params['implementation'];
        //$ldsm = EditorsFactory::getManager($lds);
        //$document = $ldsm->getDocument();
        $document = $params['document'];
        if($document->file_imsld_guid)
            $filename_lds = Editor::getFullFilePath($document->file_imsld_guid);
        else
            $filename_lds = Editor::getFullFilePath($document->file_guid);

        $sectoken = rand_str(32);

        $ldshake_url = parse_url($CONFIG->url);
        $ldshake_frame_origin = $ldshake_url['scheme'].'://'.$ldshake_url['host'];

        $post = array(
            'NewDeployTitleName' => $implementation->title,
            'instType' => 'IMS LD',
            'sectoken' => $sectoken,
            'archiveWic' => "@{$filename_lds}",
            'vleData' => "@{$m_fd['uri']};type=application/json; charset=UTF-8",
            'ldshake_frame_origin' => $ldshake_frame_origin,
            'lang' => 'en'
        );

        try {
            $uri = "{$url}deploys";
            $response = \Httpful\Request::post($uri)
                ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
                ->body($post, 'multipart/form-data')
                ->basicAuth('ldshake','Ld$haK3')
                ->sendIt();

            if($response->code != 200)
                throw new Exception($response->code. ' ' .$response->body);
            $xmldoc = new DOMDocument();
            $xmldoc->loadXML($response->raw_body);
            $xpathvar = new Domxpath($xmldoc);

            $queryResult = $xpathvar->query('//deploy');
            $doc_url = $queryResult->item(0)->getAttribute('id');

            $url_path = explode('/', $doc_url);
            $url_path_filtered = array();
            foreach ($url_path as $up)
                if(strlen($up))
                    $url_path_filtered[] = $up;
            $deploy_id = $url_path_filtered[count($url_path_filtered) -1];
        } catch (Exception $e) {
            register_error($e->getMessage());
            forward($CONFIG->url);
        }

        $vars = array();
        $vars['editor'] = 'gluepsrest';
        $vars['editor_label'] = 'GLUE!-PS';

        $url_gui_parsed = parse_url($url);

        if(isset($url_gui_parsed['port']))
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'].':'.$url_gui_parsed['port'];
        else
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'];

        $vars['restapi'] = true;
        $vars['restapi_remote_domain'] = $gui_frame_origin;

        $vars['editor_id'] = $sectoken;
        $vars['document_url'] = "{$url}deploys/{$deploy_id}";
        //$vars['document_iframe_url'] = "{$url}gui/glueps/deploy.html?deployId={$deploy_id}&sectoken={$sectoken}&lang=en";
        $lang = lds_contTools::tool_lang($vars['editor'],$CONFIG->language);
        $vars['document_iframe_url'] = "{$url}gui/glueps/deployLdShake.html?deployId={$deploy_id}&lang={$lang}";

        return $vars;
    }

    public function updateImplementation($params = null) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;

        $uri = "{$url}deploys/{$params['id']}";
        $response = \Httpful\Request::put($uri, "@{$params['file']}", 'application/octet-stream')
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

        return $response->body;
    }

    public function deployImplementation($params = null) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;

        $uri = "{$url}deploys/{$params['id']}/static";
        $response = \Httpful\Request::put($uri, "@{$params['file']}", 'application/octet-stream')
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

        return $response->body;
    }

    public static function getImplementation($params = null) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;

        $uri = "{$params['document_url']}";

        $response = \Httpful\Request::get($uri)
            //->addHeader('Accept', 'application/json')
            //->expectsXML()
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

        return $response->raw_body;
    }

    public function loadInstantiation() {
        global $CONFIG;
        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = mt_rand(400,5000000);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $post = array(
            'lang' => 'en',
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds}"
        );

        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->sendIt();

        //copy($filename_lds, $filename_editor);

        //file('http://127.0.0.1/exelearning/?load='.$rand_id);
        //unlink($filename_editor);

        $url_gui_parsed = parse_url($CONFIG->rest_editor_list["webcollagerest"]['url_gui']);

        if(isset($url_gui_parsed['port']))
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'].':'.$url_gui_parsed['port'];
        else
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'];

        $vars['restapi'] = true;
        $vars['restapi_remote_domain'] = $gui_frame_origin;

        $vars['editor'] = 'webcollagerest';
        $vars['editor_label'] = 'WebCollage';
        $vars['document_url'] = $response->raw_body;
        $vars['editor_id'] = $rand_id;

        return $vars;
    }

    public function editDocument($params) {
        global $CONFIG;
        $filename_lds = Editor::getFullFilePath($this->_document->file_guid);

        $lds = get_entity($this->_document->lds_guid);
        $design = get_entity($lds->lds_id);
        $editordocument = get_entities_from_metadata_multi(array(
                'lds_guid' => $lds->guid,
                'editorType' => $design->editor_type
            ),
            'object','LdS_document_editor', 0, 100);

        if($editordocument[0]->time_updated > $this->_document->time_updated)
        {
            $filename_lds = Editor::getFullFilePath($editordocument[0]->file_imsld_guid);
            $instType = 'IMS LD';
            $archiveWic = "@{$filename_lds}";
        } else {
            $filename_lds = Editor::getFullFilePath($this->_document->file_guid);
            $instType = 'GLUEPS';
            $archiveWic = "@{$filename_lds};type=application/xml";
        }

        $rand_id = mt_rand(400,5000000);

        $url = $CONFIG->glueps_url;
        $course = $params['course'];

        $vle_info = $this->getVleInfo();
        $course_info = $this->getCourseInfo($course);
        $vle_info->id = $this->_vle->guid;
        $vle_info->name = $this->_vle->name;

        if($this->_vle->admin_id) {
            $admin_vle = get_entity($this->_vle->admin_id);
            $vle_info->version = ($admin_vle->vle_version) ? $admin_vle->vle_version : '';
            $vle_info->wstoken = ($admin_vle->vle_wstoken) ? $admin_vle->vle_wstoken : '';
        }

        $vle_password = $this->_vle->encrypted ? lds_contTools::decrypt_password($this->_vle->password): $this->_vle->password;

        $wic_vle_data = array(
            'learningEnvironment' => $vle_info,
            'course' => $course_info,
            'name' => $vle_info->name,
            'type' => $this->_vle->vle_type,
            'creduser' => $this->_vle->username,
            'credsecret' => $vle_password,
            'participants' => $course_info->participants
        );

        $json_wic_vle_data = json_encode($wic_vle_data);

        $putData = tmpfile();
        fwrite($putData, $json_wic_vle_data);
        fseek($putData, 0);
        $m_fd = stream_get_meta_data($putData);

        /*
        $lds = $params['lds'];
        $ldsm = EditorsFactory::getManager($lds);
        $document = $ldsm->getDocument();
        $filename_lds = Editor::getFullFilePath($document->file_imsld_guid);
        */

        $sectoken = rand_str(32);

        $ldshake_url = parse_url($CONFIG->url);
        $ldshake_frame_origin = $ldshake_url['scheme'].'://'.$ldshake_url['host'];


        $post = array(
            'NewDeployTitleName' => $params['title'],
            'instType' => $instType,
            'sectoken' => $sectoken,
            'archiveWic' => $archiveWic,
            'vleData' => "@{$m_fd['uri']};type=application/json; charset=UTF-8",
            'ldshake_frame_origin' => $ldshake_frame_origin,
        );

        try {
            $uri = "{$url}deploys";
            $response = \Httpful\Request::post($uri)
                ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
                ->body($post, 'multipart/form-data')
                ->basicAuth('ldshake','Ld$haK3')
                ->sendIt();

            if($response->code != 200)
                throw new Exception("GLUEPS!: " . $response->code. ' ' .$response->body);

            $xmldoc = new DOMDocument();
            $xmldoc->loadXML($response->raw_body);
            $xpathvar = new Domxpath($xmldoc);

            $queryResult = $xpathvar->query('//deploy');
            $doc_url = $queryResult->item(0)->getAttribute('id');

            $url_path = explode('/', $doc_url);
            $url_path_filtered = array();
            foreach ($url_path as $up)
                if(strlen($up))
                    $url_path_filtered[] = $up;
            $deploy_id = $url_path_filtered[count($url_path_filtered) -1];


        }
        catch (Exception $e) {
            register_error($e->getMessage());
            forward($CONFIG->url . 'pg/lds/');
            return false;
        }
        $vars = array();
        $vars['editor_id'] = $sectoken;
        $vars['document_url'] = "{$url}deploys/{$deploy_id}";
        //$vars['document_iframe_url'] = "{$url}gui/glueps/deploy.html?deployId={$deploy_id}&sectoken={$sectoken}&lang=en";
        $vars['editor'] = 'gluepsrest';
        $vars['editor_label'] = 'GLUE!-PS';

        $url_gui_parsed = parse_url($url);

        if(isset($url_gui_parsed['port']))
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'].':'.$url_gui_parsed['port'];
        else
            $gui_frame_origin = $url_gui_parsed['scheme'].'://'.$url_gui_parsed['host'];

        $vars['restapi'] = true;
        $vars['restapi_remote_domain'] = $gui_frame_origin;

        $lang = lds_contTools::tool_lang($vars['editor'],$CONFIG->language);
        $vars['document_iframe_url'] = "{$url}gui/glueps/deployLdShake.html?deployId={$deploy_id}&lang={$lang}";

        return $vars;
    }

    /*    public function depsaveDocument() {
            global $CONFIG;
            $filename_lds = $this->getFullFilePath($this->_document->file_guid);
            $rand_id = mt_rand(400,5000000);
            //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

            $post = array(
                'lang' => 'en',
                'sectoken' => $rand_id,
                'document' => "@{$filename_lds}"
            );

            $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
            $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
            $response = \Httpful\Request::post($uri)
                ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
                ->body($post, 'multipart/form-data')
                ->sendIt();

            //copy($filename_lds, $filename_editor);

            //file('http://127.0.0.1/exelearning/?load='.$rand_id);
            //unlink($filename_editor);
            $vars['editor'] = 'webcollagerest';
            $vars['document_url'] = $response->raw_body;
            $vars['editor_id'] = $rand_id;

            return $vars;
        }
    */

//    public function deployDocument() {
//        global $CONFIG;
//
//        $load_vars = $this->loadDocument();
//
//        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
//        $design_contents = file_get_contents($filename_lds);
//        $rand_id = mt_rand(400,5000000);
//        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';
//
//        /*
//        $post = array(
//            'lang' => 'en',
//            'sectoken' => $rand_id,
//            'document' => "@{$filename_lds}"
//        );
//        */
//
//        $uri = "{$CONFIG->glueps_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
//        $uri = "{$load_vars['document_url']}/static";
//        $response = \Httpful\Request::put($uri, $design_contents)
//            ->sendIt();
//
//        //copy($filename_lds, $filename_editor);
//
//        //file('http://127.0.0.1/exelearning/?load='.$rand_id);
//        //unlink($filename_editor);
//        $vars['editor'] = 'webcollagerest';
//        $vars['editor_label'] = 'WebCollage';
//        $vars['document_url'] = $response->raw_body;
//        $vars['editor_id'] = $rand_id;
//
//        return $vars;
//    }

    /*
    public function getVle() {
        global $CONFIG;
        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = mt_rand(400,5000000);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $post = array(
            'lang' => 'en',
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds}"
        );

        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->sendIt();

        //copy($filename_lds, $filename_editor);

        //file('http://127.0.0.1/exelearning/?load='.$rand_id);
        //unlink($filename_editor);
        $vars['editor'] = 'webcollagerest';
        $vars['editor_label'] = 'WebCollage';
        $vars['document_url'] = $response->raw_body;
        $vars['editor_id'] = $rand_id;

        return $vars;
    }
    */


    public function saveNewDocument($params = null)
    {
        global $CONFIG;

        //save the contents
        $docSession = $params['editor_id'];

        $resultIds = new stdClass();
        $user = get_loggedin_user();

        $glueps_xmlcontent = GluepsManager::getImplementation($params);

        $rand_id = mt_rand(400,9000000);

        //create a new file to store the document
        $filestorename = (string)$rand_id;
        $file = Editor::getNewFile($filestorename);
        file_put_contents($file->getFilenameOnFilestore(), $glueps_xmlcontent);
        //copy($filename_editor, $file->getFilenameOnFilestore());
        //unlink($filename_editor);

        $this->_document->file_guid = $file->guid;
        $this->_document->save();

        //assign a random string to each directory
        $this->_document->previewDir = rand_str(64);
        $this->_document->pub_previewDir = rand_str(64);
        $this->_document->revisionDir = rand_str(64);

        $this->_document->rev_last = 0;
        $this->_document->lds_revision_id = 0;

        $resultIds->guid = $this->_document->lds_guid;
        $resultIds->file_guid = $file->guid;

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function saveDocument($params=null)
    {
        if($this->_document->file_guid)
            $this->saveExistingDocument($params);
        else
            $this->saveNewDocument($params);
    }

    //update the previous contents
    public function saveExistingDocument($params=null)
    {
        global $CONFIG;
        $user = get_loggedin_user();
        $resultIds = new stdClass();
        $docSession = $params['editor_id'];

        $glueps_xmlcontent = GluepsManager::getImplementation($params);

        //$filename_editor = $CONFIG->exedata.'export/'.$docSession.'.elp';
        //file('http://127.0.0.1/exelearning/?save='. $docSession);

        $rand_id = mt_rand(400,9000000);

        //create a new file to store the document
        $filestorename = (string)$rand_id;
        //$file = $this->getNewFile($filestorename);
        file_put_contents(Editor::getFullFilePath($this->_document->file_guid), $glueps_xmlcontent);

        /*
        $filename_editor = $CONFIG->exedata.'export/'.$docSession.'.elp';
        file('http://127.0.0.1/exelearning/?save='. $docSession);

        $filename_lds = $this->getFullFilePath($this->_document->file_guid);

        copy($filename_editor, $filename_lds);
        unlink($filename_editor);

        */

        $old_previewDir = $this->_document->previewDir;
        $this->_document->previewDir = rand_str(64);


        /*
        file('http://127.0.0.1/exelearning/?export='.$docSession.'&type=singlePage&filename=singlePage');
        exec('cp -r '.$CONFIG->exedata.'export/singlePage/'.$docSession.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir);
        if(strlen((string)$docSession) > 0)
            exec('rm -r --interactive=never '.$CONFIG->exedata.'export/singlePage/'.$docSession);
        if(strlen((string)$old_previewDir) > 0)
            exec('rm -r --interactive=never '.$CONFIG->editors_content.'content/exe/'.$old_previewDir);


        $this->updateExportDocument($this->_document->ims_ld, $docSession, 'IMS');
        $this->updateExportDocument($this->_document->scorm, $docSession, 'scorm');
        $this->updateExportDocument($this->_document->scorm2004, $docSession, 'scorm2004');
        $this->updateExportDocument($this->_document->webZip, $docSession, 'zipFile');

        */
        $revisions = get_entities_from_metadata('document_guid',$this->_document->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
        $resultIds->count = count($revisions);

        //create the diff content against the last saved revision
        if(count($revisions) > 0)
        {
            /*
            $output = array();
            exec ("{$CONFIG->pythonpath} {$CONFIG->path}mod/lds/ext/diff.py".' '.$CONFIG->editors_content.'content/exe/'.$revisions[count($revisions)-1]->previewDir.'/index.html'.' '.$CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/index.html', $output);
            $diff = implode('', $output);
            //insert an inline style definition to highlight the differences
            $diff = str_replace("<del", "<del style=\"background-color: #fcc;display: inline-block;text-decoration: none;\" ", $diff);
            $diff = str_replace("<ins", "<ins style=\"background-color: #cfc;display: inline-block;text-decoration: none;\" ", $diff);
            $handle = fopen($CONFIG->editors_content.'content/exe/'.$this->_document->previewDir.'/diff.html', "w");
            fwrite($handle, $diff);
            fclose($handle);
            */
        }

        $this->_document->save();

        return array($this->_document, $resultIds);
    }

    public function cloneDocument($lds)
    {
        global $CONFIG;

        $rand_id = mt_rand(400,9000000);

        $clone = new DocumentEditorObject($lds);

        $file_origin = Editor::getFullFilePath($this->_document->file_guid);

        //create a new file to store the document
        $filestorename = (string)$rand_id;
        $file = Editor::getNewFile($filestorename);
        copy($file_origin, $file->getFilenameOnFilestore());

        $clone->file_guid = $file->guid;
        $clone->editorType = $this->_document->editorType;
        $clone->save();

        //assign a random string to each directory
        $clone->previewDir = rand_str(64);
        $clone->pub_previewDir = rand_str(64);
        $clone->revisionDir = rand_str(64);

        $clone->rev_last = 0;
        $clone->lds_revision_id = 0;

        $clone->save();

        create_annotation($lds, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);

        return array($clone);
    }
}

