package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaPlaylistType;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaPlaylist extends KalturaBaseEntry {
    public String playlistContent;
    public ArrayList<KalturaMediaEntryFilterForPlaylist> filters;
    public int totalResults = Integer.MIN_VALUE;
    public KalturaPlaylistType playlistType;
    public int plays = Integer.MIN_VALUE;
    public int views = Integer.MIN_VALUE;
    public int duration = Integer.MIN_VALUE;

    public KalturaPlaylist() {
    }

    public KalturaPlaylist(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("playlistContent")) {
                this.playlistContent = txt;
                continue;
            } else if (nodeName.equals("filters")) {
                this.filters = new ArrayList<KalturaMediaEntryFilterForPlaylist>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.filters.add((KalturaMediaEntryFilterForPlaylist)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } else if (nodeName.equals("totalResults")) {
                try {
                    if (!txt.equals("")) this.totalResults = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("playlistType")) {
                try {
                    if (!txt.equals("")) this.playlistType = KalturaPlaylistType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("plays")) {
                try {
                    if (!txt.equals("")) this.plays = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("views")) {
                try {
                    if (!txt.equals("")) this.views = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("duration")) {
                try {
                    if (!txt.equals("")) this.duration = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaPlaylist");
        kparams.addStringIfNotNull("playlistContent", this.playlistContent);
        kparams.addObjectIfNotNull("filters", this.filters);
        kparams.addIntIfNotNull("totalResults", this.totalResults);
        if (playlistType != null) kparams.addIntIfNotNull("playlistType", this.playlistType.getHashCode());
        return kparams;
    }
}

