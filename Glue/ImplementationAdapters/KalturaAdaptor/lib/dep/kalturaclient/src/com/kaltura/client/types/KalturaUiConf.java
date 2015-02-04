package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaUiConfObjType;
import com.kaltura.client.enums.KalturaUiConfCreationMode;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaUiConf extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public String name;
    public String description;
    public int partnerId = Integer.MIN_VALUE;
    public KalturaUiConfObjType objType;
    public String objTypeAsString;
    public int width = Integer.MIN_VALUE;
    public int height = Integer.MIN_VALUE;
    public String htmlParams;
    public String swfUrl;
    public String confFilePath;
    public String confFile;
    public String confFileFeatures;
    public String confVars;
    public boolean useCdn;
    public String tags;
    public String swfUrlVersion;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public KalturaUiConfCreationMode creationMode;

    public KalturaUiConf() {
    }

    public KalturaUiConf(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("id")) {
                try {
                    if (!txt.equals("")) this.id = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("objType")) {
                try {
                    if (!txt.equals("")) this.objType = KalturaUiConfObjType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("objTypeAsString")) {
                this.objTypeAsString = txt;
                continue;
            } else if (nodeName.equals("width")) {
                try {
                    if (!txt.equals("")) this.width = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("height")) {
                try {
                    if (!txt.equals("")) this.height = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("htmlParams")) {
                this.htmlParams = txt;
                continue;
            } else if (nodeName.equals("swfUrl")) {
                this.swfUrl = txt;
                continue;
            } else if (nodeName.equals("confFilePath")) {
                this.confFilePath = txt;
                continue;
            } else if (nodeName.equals("confFile")) {
                this.confFile = txt;
                continue;
            } else if (nodeName.equals("confFileFeatures")) {
                this.confFileFeatures = txt;
                continue;
            } else if (nodeName.equals("confVars")) {
                this.confVars = txt;
                continue;
            } else if (nodeName.equals("useCdn")) {
                if (!txt.equals("")) this.useCdn = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = txt;
                continue;
            } else if (nodeName.equals("swfUrlVersion")) {
                this.swfUrlVersion = txt;
                continue;
            } else if (nodeName.equals("createdAt")) {
                try {
                    if (!txt.equals("")) this.createdAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatedAt")) {
                try {
                    if (!txt.equals("")) this.updatedAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("creationMode")) {
                try {
                    if (!txt.equals("")) this.creationMode = KalturaUiConfCreationMode.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaUiConf");
        kparams.addStringIfNotNull("name", this.name);
        kparams.addStringIfNotNull("description", this.description);
        if (objType != null) kparams.addIntIfNotNull("objType", this.objType.getHashCode());
        kparams.addIntIfNotNull("width", this.width);
        kparams.addIntIfNotNull("height", this.height);
        kparams.addStringIfNotNull("htmlParams", this.htmlParams);
        kparams.addStringIfNotNull("swfUrl", this.swfUrl);
        kparams.addStringIfNotNull("confFile", this.confFile);
        kparams.addStringIfNotNull("confFileFeatures", this.confFileFeatures);
        kparams.addStringIfNotNull("confVars", this.confVars);
        kparams.addBoolIfNotNull("useCdn", this.useCdn);
        kparams.addStringIfNotNull("tags", this.tags);
        kparams.addStringIfNotNull("swfUrlVersion", this.swfUrlVersion);
        if (creationMode != null) kparams.addIntIfNotNull("creationMode", this.creationMode.getHashCode());
        return kparams;
    }
}

