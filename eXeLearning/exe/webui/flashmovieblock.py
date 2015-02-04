# ===========================================================================
# eXe 
# Copyright 2004-2006, University of Auckland
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
# ===========================================================================
"""
FlashWithTextBlock can render and process FlashWithTextIdevices as XHTML
"""

import logging
from exe.webui.block   import Block
from exe.webui.element import TextAreaElement, FlashMovieElement
from exe.webui         import common

log = logging.getLogger(__name__)


# ===========================================================================
class FlashMovieBlock(Block):
    """
    FlashMovieBlock can render and process FlashMovieIdevices as XHTML
    """

    name = 'flashMovie'

    def __init__(self, parent, idevice):
        """
        Initialize
        """
        Block.__init__(self, parent, idevice)
        self.flashMovieElement = FlashMovieElement(idevice.flash)

        # to compensate for the strange unpickling timing when objects are 
        # loaded from an elp, ensure that proper idevices are set:
        # (only applies to the image-embeddable ones, not FlashMovieElement)
        if idevice.text.idevice is None: 
            idevice.text.idevice = idevice
        self.textElement  = TextAreaElement(idevice.text)


    def process(self, request):
        """
        Process the request arguments from the web server to see if any
        apply to this block
        """
        log.debug("process " + repr(request.args))
        Block.process(self, request)

        if (u"action" not in request.args or
            request.args[u"action"][0] != u"delete"):
            self.flashMovieElement.process(request)
            self.textElement.process(request)
            
        if "float"+self.id in request.args:
            self.idevice.float = request.args["float"+self.id][0]
            
        if "caption"+self.id in request.args:
            self.idevice.caption = request.args["caption"+self.id][0]


    def renderEdit(self, style):
        """
        Returns an XHTML string with the form elements for editing this block
        """
        log.debug("renderEdit")
        html  = u"<div class=\"iDevice\">\n"
        html += self.flashMovieElement.renderEdit()       
        floatArr    = [[_(u'Left'), 'left'],
                      [_(u'Right'), 'right'],
                      [_(u'None'),  'none']]
        this_package = None
        if self.idevice is not None and self.idevice.parentNode is not None:
            this_package = self.idevice.parentNode.package
        html += common.formField('select', this_package, _("Align:"),
                                 "float" + self.id,
                                 options = floatArr,
                                 selection = self.idevice.float)
        html += u'<div class="block">'
        html += u"<b>%s </b>" % _(u"Caption:")
        html += common.elementInstruc(self.idevice.captionInstruc)
        html += u'</div>\n'
        html += u'<div class="block">'
        html += common.textInput("caption" + self.id, self.idevice.caption)
        html += u'</div>\n'
        html += u'<div class="block">'
        html += self.textElement.renderEdit()
        html += self.renderEditButtons()
        html += u'</div>\n'
        html += u"</div>\n"
        return html


    def renderPreview(self, style):
        """
        Returns an XHTML string for previewing this block
        """
        log.debug("renderPreview")
        html  = u"\n<!-- flash with text iDevice -->\n"
        html  = u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\" "
        html += "ondblclick=\"submitLink('edit',"+self.id+", 0);\">\n"
        html += u"<div class=\"flash_text\" style=\""
        html += u"width:" + str(self.idevice.flash.width) + "px; "
        html += u"float:%s;\">\n" % self.idevice.float
        html += u"<div class=\"flash\">\n"
        html += self.flashMovieElement.renderPreview()
        html += u"" + self.idevice.caption + "</div>"
        html += u"</div>\n"
        html += self.textElement.renderPreview()
        html += u"<br/>\n"        
        html += u"<div style=\"clear:both;\">"
        html += u"</div>\n"
        html += self.renderViewButtons()
        html += u"</div>\n"
        return html
    

    def renderView(self, style):
        """
        Returns an XHTML string for viewing this block
        """        
        log.debug("renderView")
        html  = u"\n<!-- Flash with text iDevice -->\n"
        html += u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\">\n"
        html += u"<div class=\"flash_text\" style=\""
        html += u"width:" + str(self.idevice.flash.width) + "px; "
        html += u"float:%s;\">\n" % self.idevice.float
        html += u"<div class=\"flash\">\n"
        html += self.flashMovieElement.renderView()
        html += u"<br/>" + self.idevice.caption + "</div>"
        html += u"</div>\n"
        html += self.textElement.renderView()
        html += u"<div style=\"clear:both;\">"
        html += u"</div>\n"
        html += u"</div><br/>\n"
        return html
    
# ===========================================================================
"""Register this block with the BlockFactory"""
from exe.engine.flashmovieidevice import FlashMovieIdevice
from exe.webui.blockfactory       import g_blockFactory
g_blockFactory.registerBlockType(FlashMovieBlock, FlashMovieIdevice)    

# ===========================================================================
