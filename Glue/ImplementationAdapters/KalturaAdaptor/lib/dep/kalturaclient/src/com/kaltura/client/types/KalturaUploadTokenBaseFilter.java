package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaUploadTokenStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaUploadTokenBaseFilter extends KalturaFilter {
    public String idEqual;
    public String idIn;
    public String userIdEqual;
    public KalturaUploadTokenStatus statusEqual;
    public String statusIn;

    public KalturaUploadTokenBaseFilter() {
    }

    public KalturaUploadTokenBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("idEqual")) {
                this.idEqual = txt;
                continue;
            } else if (nodeName.equals("idIn")) {
                this.idIn = txt;
                continue;
            } else if (nodeName.equals("userIdEqual")) {
                this.userIdEqual = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaUploadTokenStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaUploadTokenBaseFilter");
        kparams.addStringIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        kparams.addStringIfNotNull("userIdEqual", this.userIdEqual);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        return kparams;
    }
}

