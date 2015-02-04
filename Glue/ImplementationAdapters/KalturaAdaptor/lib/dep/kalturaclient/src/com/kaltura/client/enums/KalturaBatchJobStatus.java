package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaBatchJobStatus {
    PENDING (0),
    QUEUED (1),
    PROCESSING (2),
    PROCESSED (3),
    MOVEFILE (4),
    FINISHED (5),
    FAILED (6),
    ABORTED (7),
    ALMOST_DONE (8),
    RETRY (9),
    FATAL (10),
    DONT_PROCESS (11);

    int hashCode;

    KalturaBatchJobStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaBatchJobStatus get(int hashCode) {
        switch(hashCode) {
            case 0: return PENDING;
            case 1: return QUEUED;
            case 2: return PROCESSING;
            case 3: return PROCESSED;
            case 4: return MOVEFILE;
            case 5: return FINISHED;
            case 6: return FAILED;
            case 7: return ABORTED;
            case 8: return ALMOST_DONE;
            case 9: return RETRY;
            case 10: return FATAL;
            case 11: return DONT_PROCESS;
            default: return PENDING;
        }
    }
}
