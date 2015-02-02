package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaReportInputFilter extends KalturaObjectBase {
    public int fromDate = Integer.MIN_VALUE;
    public int toDate = Integer.MIN_VALUE;
    public String keywords;
    public boolean searchInTags;
    public boolean searchInAdminTags;
    public String categories;
    public int timeZoneOffset = Integer.MIN_VALUE;

    public KalturaReportInputFilter() {
    }

    public KalturaReportInputFilter(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("fromDate")) {
                try {
                    if (!txt.equals("")) this.fromDate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("toDate")) {
                try {
                    if (!txt.equals("")) this.toDate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("keywords")) {
                this.keywords = txt;
                continue;
            } else if (nodeName.equals("searchInTags")) {
                if (!txt.equals("")) this.searchInTags = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("searchInAdminTags")) {
                if (!txt.equals("")) this.searchInAdminTags = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("categories")) {
                this.categories = txt;
                continue;
            } else if (nodeName.equals("timeZoneOffset")) {
                try {
                    if (!txt.equals("")) this.timeZoneOffset = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaReportInputFilter");
        kparams.addIntIfNotNull("fromDate", this.fromDate);
        kparams.addIntIfNotNull("toDate", this.toDate);
        kparams.addStringIfNotNull("keywords", this.keywords);
        kparams.addBoolIfNotNull("searchInTags", this.searchInTags);
        kparams.addBoolIfNotNull("searchInAdminTags", this.searchInAdminTags);
        kparams.addStringIfNotNull("categories", this.categories);
        kparams.addIntIfNotNull("timeZoneOffset", this.timeZoneOffset);
        return kparams;
    }
}

