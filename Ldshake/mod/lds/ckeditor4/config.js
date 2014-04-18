/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	config.skin = "kama";

    config.contentsCss = baseurl+'mod/lds/ckeditor4/contents.css?t='+ldshake_cache.lds;
    config.docType = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">';
    config.extraPlugins = 'mediaembed';
    config.removePlugins = 'magicline';

    config.language = language;
    config.uiColor = '#C7D4A3';

    //config.extraPlugins = 'MediaEmbed';
    config.toolbar = 'Medium';


    config.allowedContent = true;
    config.pasteFromWordRemoveFontStyles = false;
    config.pasteFromWordRemoveStyles = false;

    //Font sizes in pt (for print)
    config.fontSize_sizes = '8/8pt;9/9pt;10/10pt;11/11pt;12/12pt;14/14pt;16/16pt;18/18pt;20/20pt;22/22pt;24/24pt;26/26pt;28/28pt;36/36pt;48/48pt;72/72pt;';

    config.keystrokes =
        [
            [ CKEDITOR.ALT + 121 /*F10*/, 'toolbarFocus' ],
            [ CKEDITOR.ALT + 122 /*F11*/, 'elementsPathFocus' ],

            [ CKEDITOR.SHIFT + 121 /*F10*/, 'contextMenu' ],

            [ CKEDITOR.CTRL + 90 /*Z*/, 'undo' ],
            [ CKEDITOR.CTRL + 89 /*Y*/, 'redo' ],
            [ CKEDITOR.CTRL + CKEDITOR.SHIFT + 90 /*Z*/, 'redo' ],

            [ CKEDITOR.CTRL + 76 /*L*/, 'link' ],

            [ CKEDITOR.CTRL + 66 /*B*/, 'bold' ],
            [ CKEDITOR.CTRL + 73 /*I*/, 'italic' ],
            [ CKEDITOR.CTRL + 85 /*U*/, 'underline' ],

            [ CKEDITOR.ALT + 109 /*-*/, 'toolbarCollapse' ]
        ];

    config.toolbar_Medium =
        [
            ['Source'],
            ['Cut','Copy','Paste','PasteText','PasteFromWord'],
            ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
            ['BidiLtr', 'BidiRtl'],
            ['Link','Unlink'/*,'Anchor'*/],
            ['Image','MediaEmbed','Table','HorizontalRule','PageBreak'],
            ['Maximize', 'ShowBlocks'],
            '/',
            ['Styles','Format','Font','FontSize'],
            ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
            ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
            ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
            ['TextColor','BGColor']
        ];

};
