package trxsh.ontop.theUltimateSMPLib.event.custom.runnable;

import trxsh.ontop.theUltimateSMPLib.event.custom.CustomEvent;

/**
 * Custom event wrapper class
 */
public abstract class EventRunnable {
    public String listeningId;

    /**
     * creates a new event runnable that runs when a custom event with listeningId is called
     * @param listeningId
     */
    public EventRunnable(String listeningId) {
        this.listeningId = listeningId;
    }

    public String getListeningId() {
        return listeningId;
    }

    public abstract void run(CustomEvent event) ;
}
