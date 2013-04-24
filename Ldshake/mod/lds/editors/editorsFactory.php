<?php
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
        $lds->granularity = 0;
        $lds->completeness = 0;
        $lds->cloned = 1;
        $lds->implementable = $this->_lds->implementable;
        $lds->parent = $this->_lds->guid;
        $lds->editor_type = $this->_lds->editor_type;
        $lds->external_editor = $this->_lds->external_editor;

        $tagFields = array ('discipline', 'pedagogical_approach', 'tags');
        foreach ($tagFields as $field)
            $lds->$field = $this->_lds->$field;

        $lds->save();
        create_annotation($lds->guid, 'revised_docs', '', 'text', get_loggedin_userid(), 1);
        $revision = $lds->getAnnotations('revised_docs', 1, 0, 'desc');
        $revision = $revision[0];

        foreach($this->_document as $d) {
            $newdoc = new DocumentObject($lds->guid);
            $newdoc->description = $d->description;
            $newdoc->title = $d->title;
            $newdoc->lds_revision_id = $revision->id;
            $newdoc->save();
        }

        if($editordocument = get_entities_from_metadata('lds_guid',$this->_lds->guid,'object','LdS_document_editor', 0, 100)) {
            $em = EditorsFactory::getInstance($editordocument[0]);
            $em->cloneDocument($lds->guid);
        }

        return $lds;
    }

    public function __construct($document = array(), $lds = null)
    {
        $this->_lds = $lds;
        $this->_document = get_entities_from_metadata('lds_guid',$lds->guid,'object','LdS_document');
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
        if($document->editorType == 'webcollagerest')
            return new RestEditor($document);
	}

    public static function getManager($lds)
    {
        $documents = get_entities_from_metadata('lds_guid',$lds->guid,'object','LdS_document_editor', 0, 100);
        $document = $documents[0];
        if($document->editorType == 'webcollage')
            return new WebCollageEditor($document);
        if($document->editorType == 'exe')
            return new exeLearningEditor($document);
        if($document->editorType == 'webcollagerest')
            return new RestEditor($document);
    }

	public static function getTempInstance($editorType)
	{
		if($editorType == 'webcollage')
			return new WebCollageEditor(null);
		if($editorType == 'exe')
			return new exeLearningEditor(null);
        if($editorType == 'webcollagerest')
            return new RestEditor(null);
	}
}

class LdSFactory
{
    public static function buildLdS($ldsparams = null) {

        //We're creating it from scratch. Construct a new obj.
        $lds = new LdSObject();
        $lds->owner_guid = get_loggedin_userid();
        $lds->editor_type = $ldsparams['type'];

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

        $lds->save();
        lds_contTools::markLdSAsViewed ($lds->guid);
        create_annotation($lds->guid, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);

        $build = array(
            'lds' => $lds,
            'file' => $ldsparams['doc']['file']
        );

        if(isset($ldsparams['doc']['file_imsld']))
            $build['file_imsld'] = $ldsparams['doc']['file_imsld'];

        OpenglmEditor::buildDocument($build);

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

        $lds->save();
        lds_contTools::markLdSAsViewed ($lds->guid);
        create_annotation($lds->guid, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);

        $update = array(
            'lds' => $lds,
            'file' => $ldsparams['doc']['file']
        );

        if(isset($ldsparams['doc']['file_imsld']))
            $update['file_imsld'] = $ldsparams['doc']['file_imsld'];

        OpenglmEditor::updateDocument($update);

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

        $filestorename = $params['lds']->guid.'_'.rand_str(64);
        $file = Editor::getNewFile($filestorename);
        if(isset($params['file_imsld']))
            copy($params['file_imsld'], $file->getFilenameOnFilestore());

        $document->ims_ld = $file->guid;

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

        $fullfilepath = Editor::getFullFilePath($document->ims_ld);
        copy($params['file_imsld'], $fullfilepath);

        //TODO: update revision data

        $document->save();

        return $document;
    }
}

class RestEditor extends Editor
{
    public $document_url;
    private $_rest_id;

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
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        //copy($CONFIG->path.'vendors/exelearning/sample.elp', $filename_editor);
        //file('http://127.0.0.1/exelearning/?load='.$rand_id);
        //unlink($filename_editor);

        $post = array(
            'lang' => 'en',
            'sectoken' => $rand_id,
        );

        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->basicAuth('ldshake_default_user','LdS@k$1#')
            ->sendIt();

        //$this->_document->url = $response;
        //$this->_document->save();

        $vars['editor_id'] = $rand_id;
        //http://appserver.ldshake.edu/designapp/?document_id=value&sectoken=value
        $doc_url = parse_url($response->raw_body);
        $url_path = explode('/', $doc_url['path']);
        $url_path_filtered = array();
        foreach ($url_path as $up)
            if(strlen($up))
                $url_path_filtered[] = $up;
        $doc_id = $url_path_filtered[count($url_path_filtered) -1];
        $vars['document_url'] = "{$response->raw_body}";
        $vars['document_iframe_url'] = "{$CONFIG->webcollagerest_url}?document_id={$doc_id}&sectoken={$rand_id}";
        $vars['editor'] = 'webcollagerest';

        return $vars;
    }

    //load the document into the exelearning server
    public function editDocument()
    {
        global $CONFIG;
        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $rand_id = rand_str(64);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        $post = array(
            'lang' => 'en',
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds};type=application/json; charset=UTF-8"
        );

        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
        $uri = "{$CONFIG->webcollagerest_url}ldshake/ldsdoc/";
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->basicAuth('ldshake_default_user','LdS@k$1#')
            ->sendIt();

        $vars['editor'] = 'webcollagerest';
        $doc_url = parse_url($response->raw_body);
        $url_path = explode('/', $doc_url['path']);
        $url_path_filtered = array();
        foreach ($url_path as $up)
            if(strlen($up))
                $url_path_filtered[] = $up;
        $doc_id = $url_path_filtered[count($url_path_filtered) -1];
        $this->_rest_id = $doc_id;
        $vars['document_url'] = "{$response->raw_body}";
        $vars['document_iframe_url'] = "{$CONFIG->webcollagerest_url}?document_id={$doc_id}&sectoken={$rand_id}";
        $vars['editor_id'] = $rand_id;

        return $vars;
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
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->basicAuth('ldshake_default_user','LdS@k$1#')
            ->sendIt();

        $vars['editor'] = 'webcollagerest';
        $doc_url = parse_url($response->raw_body);
        $url_path = explode('/', $doc_url['path']);
        $url_path_filtered = array();
        foreach ($url_path as $up)
            if(strlen($up))
                $url_path_filtered[] = $up;
        $doc_id = $url_path_filtered[count($url_path_filtered) -1];
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

    public function saveNewDocument($params = null)
    {
        global $CONFIG;

        //save the contents
        $docSession = $params['editor_id'];

        $resultIds = new stdClass();
        $user = get_loggedin_user();

        $uri = $params['url'];
        $response = \Httpful\Request::get($uri)
            ->basicAuth('ldshake_default_user','LdS@k$1#')
            ->addHeader('Accept', 'application/json; charset=UTF-8')
            ->sendIt();

        //create a new file to store the document
        $rand_id = mt_rand(400,9000000);
        $filestorename = (string)$rand_id;
        $file = $this->getNewFile($filestorename);
        file_put_contents($file->getFilenameOnFilestore(), $response->raw_body);
        $this->_document->file_guid = $file->guid;
        $this->_document->save();


        $uri = $params['url'].'.imsld';
        $response = \Httpful\Request::get($uri)
            ->basicAuth('ldshake_default_user','LdS@k$1#')
            ->addHeader('Accept', 'application/json; charset=UTF-8')
            ->sendIt();

        //create a new file to store the document
        $rand_id = mt_rand(400,9000000);
        $filestorename = (string)$rand_id.'.zip';
        $file = $this->getNewFile($filestorename);
        file_put_contents($file->getFilenameOnFilestore(), $response->raw_body);
        $this->_document->file_imsld_guid = $file->guid;
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

    public function unload($docSession)
    {
        //file('http://127.0.0.1/exelearning/?unload='. $docSession);
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

        $uri = $params['url'];
        $response = \Httpful\Request::get($uri)
            ->basicAuth('ldshake_default_user','LdS@k$1#')
            ->addHeader('Accept', 'application/json; charset=UTF-8')
            ->sendIt();

        //create a new file to store the document
        $rand_id = mt_rand(400,9000000);
        $filestorename = (string)$rand_id;
        file_put_contents($this->getFullFilePath($this->_document->file_guid), $response->raw_body);

        $uri = $params['url'].'.imsld';
        $response = \Httpful\Request::get($uri)
            ->basicAuth('ldshake_default_user','LdS@k$1#')
            ->addHeader('Accept', 'application/json; charset=UTF-8')
            ->sendIt();

        //create a new file to store the document
        $rand_id = mt_rand(400,9000000);
        $filestorename = (string)$rand_id;
        file_put_contents($this->getFullFilePath($this->_document->file_imsld_guid), $response->raw_body);


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

    public function __construct($document=null)
    {
        $this->_document = $document;
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
        $this->_implementation = $implementation;
        $this->_document = $glueps_document;
        $this->_vle = $vle;
    }

    public function cloneImplementation($title = null) {
        $implementation = new ElggObject();
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

    public function getVleInfo() {
        global $CONFIG;

        if(!$this->validateVle())
            return false;

        $url = $CONFIG->glueps_url;

        /*
        $vle_url = "http://glue-test.cloud.gsic.tel.uva.es/moodle/";
        $type = 'Moodle';
        $username = 'metis';
        $password = 'M3t1$project';
        */

        $get = array(
            'type' => $this->_vle->vle_type,
            'accessLocation' => $this->_vle->vle_url,
            'creduser' => $this->_vle->username,
            'credsecret' => $this->_vle->password
        );

        $uri = "{$url}courses?"
            ."type=".urlencode($get['type'])."&"
            ."accessLocation=".urlencode($get['accessLocation'])."&"
            ."creduser=".urlencode($get['creduser'])."&"
            ."credsecret=".urlencode($get['credsecret']);

        $response = \Httpful\Request::get($uri)
            ->addHeader('Accept', 'application/json')
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

        return $response->body;
    }

    public function getCourseInfo($course_id) {
        global $CONFIG;
        $url = $CONFIG->glueps_url;
        /*
        $vle_url = "http://glue-test.cloud.gsic.tel.uva.es/moodle/";
        $type = 'Moodle';
        $username = 'metis';
        $password = 'M3t1$project';
        $course = '3';
        */

        $get = array(
            'type' => $this->_vle->vle_type,
            'accessLocation' => $this->_vle->vle_url,
            'creduser' => $this->_vle->username,
            'credsecret' => $this->_vle->password,
            'course' => $course_id
        );

        $uri = "{$url}courses?"
            ."type=".urlencode($get['type'])."&"
            ."accessLocation=".urlencode($get['accessLocation'])."&"
            ."creduser=".urlencode($get['creduser'])."&"
            ."credsecret=".urlencode($get['credsecret'])."&"
            ."course=".urlencode($get['course']);

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
        $wic_vle_data = array(
            'learningEnvironment' => $vle_info,
            'course' => $course_info,
            'name' => $vle_info->name,
            'type' => $this->_vle->vle_type,
            'creduser' => $this->_vle->username,
            'credsecret' => $this->_vle->password,
            'participants' => $course_info->participants
        );
        //unset($vle_info->courses);
        //$vle_info->course = $course_info;
        //$vle_info->participants = $course_info->participants;

        $json_wic_vle_data = json_encode($wic_vle_data);

        $putData = tmpfile();
        fwrite($putData, $json_wic_vle_data);
        fseek($putData, 0);
        $m_fd = stream_get_meta_data($putData);

        $lds = $params['lds'];
        $ldsm = EditorsFactory::getManager($lds);
        //$document = $ldsm->getDocument();
        $document = $params['document'];
        $filename_lds = Editor::getFullFilePath($document->file_imsld_guid);
        $sectoken = rand_str(32);

        $post = array(
            'NewDeployTitleName' => $lds->title,
            'instType' => 'IMS LD',
            'sectoken' => $sectoken,
            'archiveWic' => "@{$filename_lds}",
            'vleData' => "@{$m_fd['uri']};type=application/json; charset=UTF-8"
        );

        $uri = "{$url}deploys";
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

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

        $vars = array();
        $vars['editor_id'] = $sectoken;
        $vars['document_url'] = "{$url}deploys/{$deploy_id}";
        $vars['document_iframe_url'] = "{$url}gui/glueps/deploy.html?deployId={$deploy_id}&sectoken={$sectoken}";
        $vars['editor'] = 'gluepsrest';

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

        $uri = "{$params['url']}";

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
        $vars['editor'] = 'webcollagerest';
        $vars['document_url'] = $response->raw_body;
        $vars['editor_id'] = $rand_id;

        return $vars;
    }

    public function editDocument($params) {
        global $CONFIG;
        $filename_lds = Editor::getFullFilePath($this->_document->file_guid);
        $rand_id = mt_rand(400,5000000);

        $url = $CONFIG->glueps_url;
        $course = $params['course'];

        $vle_info = $this->getVleInfo();
        $course_info = $this->getCourseInfo($course);
        $vle_info->id = $this->_vle->guid;
        $vle_info->name = $this->_vle->name;
        $wic_vle_data = array(
            'learningEnvironment' => $vle_info,
            'course' => $course_info,
            'name' => $vle_info->name,
            'type' => $this->_vle->vle_type,
            'creduser' => $this->_vle->username,
            'credsecret' => $this->_vle->password,
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

        $post = array(
            'NewDeployTitleName' => $params['title'],
            'instType' => 'GLUEPS',
            'sectoken' => $sectoken,
            'archiveWic' => "@{$filename_lds};type=application/xml",
            'vleData' => "@{$m_fd['uri']};type=application/json; charset=UTF-8"
        );

        $uri = "{$url}deploys";
        $response = \Httpful\Request::post($uri)
            ->registerPayloadSerializer('multipart/form-data', $CONFIG->rest_serializer)
            ->body($post, 'multipart/form-data')
            ->basicAuth('ldshake','Ld$haK3')
            ->sendIt();

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

        $vars = array();
        $vars['editor_id'] = $sectoken;
        $vars['document_url'] = "{$url}deploys/{$deploy_id}";
        $vars['document_iframe_url'] = "{$url}gui/glueps/deploy.html?deployId={$deploy_id}&ldshakeToken={$sectoken}";
        $vars['editor'] = 'gluepsrest';

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

    public function deployDocument() {
        global $CONFIG;

        $load_vars = $this->loadDocument();

        $filename_lds = $this->getFullFilePath($this->_document->file_guid);
        $design_contents = file_get_contents($filename_lds);
        $rand_id = mt_rand(400,5000000);
        //$filename_editor = $CONFIG->exedata.'export/'.$rand_id.'.elp';

        /*
        $post = array(
            'lang' => 'en',
            'sectoken' => $rand_id,
            'document' => "@{$filename_lds}"
        );
        */

        $uri = "{$CONFIG->glueps_url}ldshake/ldsdoc/?XDEBUG_SESSION_START=16713";
        $uri = "{$load_vars['document_url']}/static";
        $response = \Httpful\Request::put($uri, $design_contents)
            ->sendIt();

        //copy($filename_lds, $filename_editor);

        //file('http://127.0.0.1/exelearning/?load='.$rand_id);
        //unlink($filename_editor);
        $vars['editor'] = 'webcollagerest';
        $vars['document_url'] = $response->raw_body;
        $vars['editor_id'] = $rand_id;

        return $vars;
    }

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
        $vars['document_url'] = $response->raw_body;
        $vars['editor_id'] = $rand_id;

        return $vars;
    }


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
        $file = $this->getNewFile($filestorename);
        file_put_contents($this->getFullFilePath($this->_document->file_guid), $glueps_xmlcontent);

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
}