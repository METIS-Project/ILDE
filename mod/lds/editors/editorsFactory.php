<?php
include_once(__DIR__.'/../rand.php');
class Editor
{
	private $_document;

	//return the filesystem path of an ElggFile object
	public function getFullFilePath($file_guid)
	{
		$file = get_entity($file_guid);
		$readfile = new ElggFile();
		$readfile->owner_guid = $file->owner_guid;
		$readfile->setFilename($file->originalfilename);
		return $readfile->getFilenameOnFilestore();
	}

	//create an ElggFile, object and return it
	public function getNewFile($filename)
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

class exeLearningEditor extends Editor
{
	public function getDocumentId()
	{
		return $_document->guid;

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
        $vars['file_editor'] = $filename_editor;
        $vars['editor_id'] = $rand_id;
        $vars['editor'] = 'webcollage';

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
	}

	public static function getTempInstance($editorType)
	{
		if($editorType == 'webcollage')
			return new WebCollageEditor(null);
		if($editorType == 'exe')
			return new exeLearningEditor(null);
	}
}


?>
