package com.kaltura.client.services;

import org.w3c.dom.Element;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaServiceBase;
import com.kaltura.client.utils.XmlUtils;
import com.kaltura.client.enums.*;
import com.kaltura.client.types.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import com.kaltura.client.KalturaFiles;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaReportService extends KalturaServiceBase {
    public KalturaReportService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public List<KalturaReportGraph> getGraphs(KalturaReportType reportType, KalturaReportInputFilter reportInputFilter) throws KalturaApiException {
        return this.getGraphs(reportType, reportInputFilter, null);
    }

    public List<KalturaReportGraph> getGraphs(KalturaReportType reportType, KalturaReportInputFilter reportInputFilter, String dimension) throws KalturaApiException {
        return this.getGraphs(reportType, reportInputFilter, dimension, null);
    }

    public List<KalturaReportGraph> getGraphs(KalturaReportType reportType, KalturaReportInputFilter reportInputFilter, String dimension, String objectIds) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("reportType", reportType.getHashCode());
        if (reportInputFilter != null) kparams.add("reportInputFilter", reportInputFilter.toParams());
        kparams.addStringIfNotNull("dimension", dimension);
        kparams.addStringIfNotNull("objectIds", objectIds);
        this.kalturaClient.queueServiceCall("report", "getGraphs", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaReportGraph> list = new ArrayList<KalturaReportGraph>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaReportGraph)KalturaObjectFactory.create(node));
        }
        return list;
    }

    public KalturaReportTotal getTotal(KalturaReportType reportType, KalturaReportInputFilter reportInputFilter) throws KalturaApiException {
        return this.getTotal(reportType, reportInputFilter, null);
    }

    public KalturaReportTotal getTotal(KalturaReportType reportType, KalturaReportInputFilter reportInputFilter, String objectIds) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("reportType", reportType.getHashCode());
        if (reportInputFilter != null) kparams.add("reportInputFilter", reportInputFilter.toParams());
        kparams.addStringIfNotNull("objectIds", objectIds);
        this.kalturaClient.queueServiceCall("report", "getTotal", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaReportTotal)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaReportTable getTable(KalturaReportType reportType, KalturaReportInputFilter reportInputFilter, KalturaFilterPager pager) throws KalturaApiException {
        return this.getTable(reportType, reportInputFilter, pager, null);
    }

    public KalturaReportTable getTable(KalturaReportType reportType, KalturaReportInputFilter reportInputFilter, KalturaFilterPager pager, String order) throws KalturaApiException {
        return this.getTable(reportType, reportInputFilter, pager, order, null);
    }

    public KalturaReportTable getTable(KalturaReportType reportType, KalturaReportInputFilter reportInputFilter, KalturaFilterPager pager, String order, String objectIds) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("reportType", reportType.getHashCode());
        if (reportInputFilter != null) kparams.add("reportInputFilter", reportInputFilter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        kparams.addStringIfNotNull("order", order);
        kparams.addStringIfNotNull("objectIds", objectIds);
        this.kalturaClient.queueServiceCall("report", "getTable", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaReportTable)KalturaObjectFactory.create(resultXmlElement);
    }

    public String getUrlForReportAsCsv(String reportTitle, String reportText, String headers, KalturaReportType reportType, KalturaReportInputFilter reportInputFilter) throws KalturaApiException {
        return this.getUrlForReportAsCsv(reportTitle, reportText, headers, reportType, reportInputFilter, null);
    }

    public String getUrlForReportAsCsv(String reportTitle, String reportText, String headers, KalturaReportType reportType, KalturaReportInputFilter reportInputFilter, String dimension) throws KalturaApiException {
        return this.getUrlForReportAsCsv(reportTitle, reportText, headers, reportType, reportInputFilter, dimension, null);
    }

    public String getUrlForReportAsCsv(String reportTitle, String reportText, String headers, KalturaReportType reportType, KalturaReportInputFilter reportInputFilter, String dimension, KalturaFilterPager pager) throws KalturaApiException {
        return this.getUrlForReportAsCsv(reportTitle, reportText, headers, reportType, reportInputFilter, dimension, pager, null);
    }

    public String getUrlForReportAsCsv(String reportTitle, String reportText, String headers, KalturaReportType reportType, KalturaReportInputFilter reportInputFilter, String dimension, KalturaFilterPager pager, String order) throws KalturaApiException {
        return this.getUrlForReportAsCsv(reportTitle, reportText, headers, reportType, reportInputFilter, dimension, pager, order, null);
    }

    public String getUrlForReportAsCsv(String reportTitle, String reportText, String headers, KalturaReportType reportType, KalturaReportInputFilter reportInputFilter, String dimension, KalturaFilterPager pager, String order, String objectIds) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("reportTitle", reportTitle);
        kparams.addStringIfNotNull("reportText", reportText);
        kparams.addStringIfNotNull("headers", headers);
        kparams.addIntIfNotNull("reportType", reportType.getHashCode());
        if (reportInputFilter != null) kparams.add("reportInputFilter", reportInputFilter.toParams());
        kparams.addStringIfNotNull("dimension", dimension);
        if (pager != null) kparams.add("pager", pager.toParams());
        kparams.addStringIfNotNull("order", order);
        kparams.addStringIfNotNull("objectIds", objectIds);
        this.kalturaClient.queueServiceCall("report", "getUrlForReportAsCsv", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }
}
