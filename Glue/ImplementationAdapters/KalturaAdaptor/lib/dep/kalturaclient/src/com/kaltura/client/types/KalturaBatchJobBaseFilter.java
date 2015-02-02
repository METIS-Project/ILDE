package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaBatchJobType;
import com.kaltura.client.enums.KalturaBatchJobStatus;
import com.kaltura.client.enums.KalturaBatchJobErrorTypes;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaBatchJobBaseFilter extends KalturaBaseJobFilter {
    public String entryIdEqual;
    public KalturaBatchJobType jobTypeEqual;
    public String jobTypeIn;
    public String jobTypeNotIn;
    public int jobSubTypeEqual = Integer.MIN_VALUE;
    public String jobSubTypeIn;
    public String jobSubTypeNotIn;
    public int onStressDivertToEqual = Integer.MIN_VALUE;
    public String onStressDivertToIn;
    public String onStressDivertToNotIn;
    public KalturaBatchJobStatus statusEqual;
    public String statusIn;
    public String statusNotIn;
    public int abortEqual = Integer.MIN_VALUE;
    public int checkAgainTimeoutGreaterThanOrEqual = Integer.MIN_VALUE;
    public int checkAgainTimeoutLessThanOrEqual = Integer.MIN_VALUE;
    public int progressGreaterThanOrEqual = Integer.MIN_VALUE;
    public int progressLessThanOrEqual = Integer.MIN_VALUE;
    public int updatesCountGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatesCountLessThanOrEqual = Integer.MIN_VALUE;
    public int priorityGreaterThanOrEqual = Integer.MIN_VALUE;
    public int priorityLessThanOrEqual = Integer.MIN_VALUE;
    public int priorityEqual = Integer.MIN_VALUE;
    public String priorityIn;
    public String priorityNotIn;
    public int twinJobIdEqual = Integer.MIN_VALUE;
    public String twinJobIdIn;
    public String twinJobIdNotIn;
    public int bulkJobIdEqual = Integer.MIN_VALUE;
    public String bulkJobIdIn;
    public String bulkJobIdNotIn;
    public int parentJobIdEqual = Integer.MIN_VALUE;
    public String parentJobIdIn;
    public String parentJobIdNotIn;
    public int rootJobIdEqual = Integer.MIN_VALUE;
    public String rootJobIdIn;
    public String rootJobIdNotIn;
    public int queueTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int queueTimeLessThanOrEqual = Integer.MIN_VALUE;
    public int finishTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int finishTimeLessThanOrEqual = Integer.MIN_VALUE;
    public KalturaBatchJobErrorTypes errTypeEqual;
    public String errTypeIn;
    public String errTypeNotIn;
    public int errNumberEqual = Integer.MIN_VALUE;
    public String errNumberIn;
    public String errNumberNotIn;
    public int fileSizeLessThan = Integer.MIN_VALUE;
    public int fileSizeGreaterThan = Integer.MIN_VALUE;
    public boolean lastWorkerRemoteEqual;
    public int schedulerIdEqual = Integer.MIN_VALUE;
    public String schedulerIdIn;
    public String schedulerIdNotIn;
    public int workerIdEqual = Integer.MIN_VALUE;
    public String workerIdIn;
    public String workerIdNotIn;
    public int batchIndexEqual = Integer.MIN_VALUE;
    public String batchIndexIn;
    public String batchIndexNotIn;
    public int lastSchedulerIdEqual = Integer.MIN_VALUE;
    public String lastSchedulerIdIn;
    public String lastSchedulerIdNotIn;
    public int lastWorkerIdEqual = Integer.MIN_VALUE;
    public String lastWorkerIdIn;
    public String lastWorkerIdNotIn;
    public int dcEqual = Integer.MIN_VALUE;
    public String dcIn;
    public String dcNotIn;

    public KalturaBatchJobBaseFilter() {
    }

    public KalturaBatchJobBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("entryIdEqual")) {
                this.entryIdEqual = txt;
                continue;
            } else if (nodeName.equals("jobTypeEqual")) {
                try {
                    if (!txt.equals("")) this.jobTypeEqual = KalturaBatchJobType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("jobTypeIn")) {
                this.jobTypeIn = txt;
                continue;
            } else if (nodeName.equals("jobTypeNotIn")) {
                this.jobTypeNotIn = txt;
                continue;
            } else if (nodeName.equals("jobSubTypeEqual")) {
                try {
                    if (!txt.equals("")) this.jobSubTypeEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("jobSubTypeIn")) {
                this.jobSubTypeIn = txt;
                continue;
            } else if (nodeName.equals("jobSubTypeNotIn")) {
                this.jobSubTypeNotIn = txt;
                continue;
            } else if (nodeName.equals("onStressDivertToEqual")) {
                try {
                    if (!txt.equals("")) this.onStressDivertToEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("onStressDivertToIn")) {
                this.onStressDivertToIn = txt;
                continue;
            } else if (nodeName.equals("onStressDivertToNotIn")) {
                this.onStressDivertToNotIn = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaBatchJobStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("statusNotIn")) {
                this.statusNotIn = txt;
                continue;
            } else if (nodeName.equals("abortEqual")) {
                try {
                    if (!txt.equals("")) this.abortEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("checkAgainTimeoutGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.checkAgainTimeoutGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("checkAgainTimeoutLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.checkAgainTimeoutLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("progressGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.progressGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("progressLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.progressLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatesCountGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.updatesCountGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatesCountLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.updatesCountLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("priorityGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.priorityGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("priorityLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.priorityLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("priorityEqual")) {
                try {
                    if (!txt.equals("")) this.priorityEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("priorityIn")) {
                this.priorityIn = txt;
                continue;
            } else if (nodeName.equals("priorityNotIn")) {
                this.priorityNotIn = txt;
                continue;
            } else if (nodeName.equals("twinJobIdEqual")) {
                try {
                    if (!txt.equals("")) this.twinJobIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("twinJobIdIn")) {
                this.twinJobIdIn = txt;
                continue;
            } else if (nodeName.equals("twinJobIdNotIn")) {
                this.twinJobIdNotIn = txt;
                continue;
            } else if (nodeName.equals("bulkJobIdEqual")) {
                try {
                    if (!txt.equals("")) this.bulkJobIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("bulkJobIdIn")) {
                this.bulkJobIdIn = txt;
                continue;
            } else if (nodeName.equals("bulkJobIdNotIn")) {
                this.bulkJobIdNotIn = txt;
                continue;
            } else if (nodeName.equals("parentJobIdEqual")) {
                try {
                    if (!txt.equals("")) this.parentJobIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("parentJobIdIn")) {
                this.parentJobIdIn = txt;
                continue;
            } else if (nodeName.equals("parentJobIdNotIn")) {
                this.parentJobIdNotIn = txt;
                continue;
            } else if (nodeName.equals("rootJobIdEqual")) {
                try {
                    if (!txt.equals("")) this.rootJobIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("rootJobIdIn")) {
                this.rootJobIdIn = txt;
                continue;
            } else if (nodeName.equals("rootJobIdNotIn")) {
                this.rootJobIdNotIn = txt;
                continue;
            } else if (nodeName.equals("queueTimeGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.queueTimeGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("queueTimeLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.queueTimeLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("finishTimeGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.finishTimeGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("finishTimeLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.finishTimeLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("errTypeEqual")) {
                try {
                    if (!txt.equals("")) this.errTypeEqual = KalturaBatchJobErrorTypes.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("errTypeIn")) {
                this.errTypeIn = txt;
                continue;
            } else if (nodeName.equals("errTypeNotIn")) {
                this.errTypeNotIn = txt;
                continue;
            } else if (nodeName.equals("errNumberEqual")) {
                try {
                    if (!txt.equals("")) this.errNumberEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("errNumberIn")) {
                this.errNumberIn = txt;
                continue;
            } else if (nodeName.equals("errNumberNotIn")) {
                this.errNumberNotIn = txt;
                continue;
            } else if (nodeName.equals("fileSizeLessThan")) {
                try {
                    if (!txt.equals("")) this.fileSizeLessThan = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("fileSizeGreaterThan")) {
                try {
                    if (!txt.equals("")) this.fileSizeGreaterThan = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("lastWorkerRemoteEqual")) {
                if (!txt.equals("")) this.lastWorkerRemoteEqual = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("schedulerIdEqual")) {
                try {
                    if (!txt.equals("")) this.schedulerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("schedulerIdIn")) {
                this.schedulerIdIn = txt;
                continue;
            } else if (nodeName.equals("schedulerIdNotIn")) {
                this.schedulerIdNotIn = txt;
                continue;
            } else if (nodeName.equals("workerIdEqual")) {
                try {
                    if (!txt.equals("")) this.workerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("workerIdIn")) {
                this.workerIdIn = txt;
                continue;
            } else if (nodeName.equals("workerIdNotIn")) {
                this.workerIdNotIn = txt;
                continue;
            } else if (nodeName.equals("batchIndexEqual")) {
                try {
                    if (!txt.equals("")) this.batchIndexEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("batchIndexIn")) {
                this.batchIndexIn = txt;
                continue;
            } else if (nodeName.equals("batchIndexNotIn")) {
                this.batchIndexNotIn = txt;
                continue;
            } else if (nodeName.equals("lastSchedulerIdEqual")) {
                try {
                    if (!txt.equals("")) this.lastSchedulerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("lastSchedulerIdIn")) {
                this.lastSchedulerIdIn = txt;
                continue;
            } else if (nodeName.equals("lastSchedulerIdNotIn")) {
                this.lastSchedulerIdNotIn = txt;
                continue;
            } else if (nodeName.equals("lastWorkerIdEqual")) {
                try {
                    if (!txt.equals("")) this.lastWorkerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("lastWorkerIdIn")) {
                this.lastWorkerIdIn = txt;
                continue;
            } else if (nodeName.equals("lastWorkerIdNotIn")) {
                this.lastWorkerIdNotIn = txt;
                continue;
            } else if (nodeName.equals("dcEqual")) {
                try {
                    if (!txt.equals("")) this.dcEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("dcIn")) {
                this.dcIn = txt;
                continue;
            } else if (nodeName.equals("dcNotIn")) {
                this.dcNotIn = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaBatchJobBaseFilter");
        kparams.addStringIfNotNull("entryIdEqual", this.entryIdEqual);
        if (jobTypeEqual != null) kparams.addStringIfNotNull("jobTypeEqual", this.jobTypeEqual.getHashCode());
        kparams.addStringIfNotNull("jobTypeIn", this.jobTypeIn);
        kparams.addStringIfNotNull("jobTypeNotIn", this.jobTypeNotIn);
        kparams.addIntIfNotNull("jobSubTypeEqual", this.jobSubTypeEqual);
        kparams.addStringIfNotNull("jobSubTypeIn", this.jobSubTypeIn);
        kparams.addStringIfNotNull("jobSubTypeNotIn", this.jobSubTypeNotIn);
        kparams.addIntIfNotNull("onStressDivertToEqual", this.onStressDivertToEqual);
        kparams.addStringIfNotNull("onStressDivertToIn", this.onStressDivertToIn);
        kparams.addStringIfNotNull("onStressDivertToNotIn", this.onStressDivertToNotIn);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        kparams.addStringIfNotNull("statusNotIn", this.statusNotIn);
        kparams.addIntIfNotNull("abortEqual", this.abortEqual);
        kparams.addIntIfNotNull("checkAgainTimeoutGreaterThanOrEqual", this.checkAgainTimeoutGreaterThanOrEqual);
        kparams.addIntIfNotNull("checkAgainTimeoutLessThanOrEqual", this.checkAgainTimeoutLessThanOrEqual);
        kparams.addIntIfNotNull("progressGreaterThanOrEqual", this.progressGreaterThanOrEqual);
        kparams.addIntIfNotNull("progressLessThanOrEqual", this.progressLessThanOrEqual);
        kparams.addIntIfNotNull("updatesCountGreaterThanOrEqual", this.updatesCountGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatesCountLessThanOrEqual", this.updatesCountLessThanOrEqual);
        kparams.addIntIfNotNull("priorityGreaterThanOrEqual", this.priorityGreaterThanOrEqual);
        kparams.addIntIfNotNull("priorityLessThanOrEqual", this.priorityLessThanOrEqual);
        kparams.addIntIfNotNull("priorityEqual", this.priorityEqual);
        kparams.addStringIfNotNull("priorityIn", this.priorityIn);
        kparams.addStringIfNotNull("priorityNotIn", this.priorityNotIn);
        kparams.addIntIfNotNull("twinJobIdEqual", this.twinJobIdEqual);
        kparams.addStringIfNotNull("twinJobIdIn", this.twinJobIdIn);
        kparams.addStringIfNotNull("twinJobIdNotIn", this.twinJobIdNotIn);
        kparams.addIntIfNotNull("bulkJobIdEqual", this.bulkJobIdEqual);
        kparams.addStringIfNotNull("bulkJobIdIn", this.bulkJobIdIn);
        kparams.addStringIfNotNull("bulkJobIdNotIn", this.bulkJobIdNotIn);
        kparams.addIntIfNotNull("parentJobIdEqual", this.parentJobIdEqual);
        kparams.addStringIfNotNull("parentJobIdIn", this.parentJobIdIn);
        kparams.addStringIfNotNull("parentJobIdNotIn", this.parentJobIdNotIn);
        kparams.addIntIfNotNull("rootJobIdEqual", this.rootJobIdEqual);
        kparams.addStringIfNotNull("rootJobIdIn", this.rootJobIdIn);
        kparams.addStringIfNotNull("rootJobIdNotIn", this.rootJobIdNotIn);
        kparams.addIntIfNotNull("queueTimeGreaterThanOrEqual", this.queueTimeGreaterThanOrEqual);
        kparams.addIntIfNotNull("queueTimeLessThanOrEqual", this.queueTimeLessThanOrEqual);
        kparams.addIntIfNotNull("finishTimeGreaterThanOrEqual", this.finishTimeGreaterThanOrEqual);
        kparams.addIntIfNotNull("finishTimeLessThanOrEqual", this.finishTimeLessThanOrEqual);
        if (errTypeEqual != null) kparams.addIntIfNotNull("errTypeEqual", this.errTypeEqual.getHashCode());
        kparams.addStringIfNotNull("errTypeIn", this.errTypeIn);
        kparams.addStringIfNotNull("errTypeNotIn", this.errTypeNotIn);
        kparams.addIntIfNotNull("errNumberEqual", this.errNumberEqual);
        kparams.addStringIfNotNull("errNumberIn", this.errNumberIn);
        kparams.addStringIfNotNull("errNumberNotIn", this.errNumberNotIn);
        kparams.addIntIfNotNull("fileSizeLessThan", this.fileSizeLessThan);
        kparams.addIntIfNotNull("fileSizeGreaterThan", this.fileSizeGreaterThan);
        kparams.addBoolIfNotNull("lastWorkerRemoteEqual", this.lastWorkerRemoteEqual);
        kparams.addIntIfNotNull("schedulerIdEqual", this.schedulerIdEqual);
        kparams.addStringIfNotNull("schedulerIdIn", this.schedulerIdIn);
        kparams.addStringIfNotNull("schedulerIdNotIn", this.schedulerIdNotIn);
        kparams.addIntIfNotNull("workerIdEqual", this.workerIdEqual);
        kparams.addStringIfNotNull("workerIdIn", this.workerIdIn);
        kparams.addStringIfNotNull("workerIdNotIn", this.workerIdNotIn);
        kparams.addIntIfNotNull("batchIndexEqual", this.batchIndexEqual);
        kparams.addStringIfNotNull("batchIndexIn", this.batchIndexIn);
        kparams.addStringIfNotNull("batchIndexNotIn", this.batchIndexNotIn);
        kparams.addIntIfNotNull("lastSchedulerIdEqual", this.lastSchedulerIdEqual);
        kparams.addStringIfNotNull("lastSchedulerIdIn", this.lastSchedulerIdIn);
        kparams.addStringIfNotNull("lastSchedulerIdNotIn", this.lastSchedulerIdNotIn);
        kparams.addIntIfNotNull("lastWorkerIdEqual", this.lastWorkerIdEqual);
        kparams.addStringIfNotNull("lastWorkerIdIn", this.lastWorkerIdIn);
        kparams.addStringIfNotNull("lastWorkerIdNotIn", this.lastWorkerIdNotIn);
        kparams.addIntIfNotNull("dcEqual", this.dcEqual);
        kparams.addStringIfNotNull("dcIn", this.dcIn);
        kparams.addStringIfNotNull("dcNotIn", this.dcNotIn);
        return kparams;
    }
}

