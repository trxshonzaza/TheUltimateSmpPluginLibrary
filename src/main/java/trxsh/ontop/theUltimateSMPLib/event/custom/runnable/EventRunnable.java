package trxsh.ontop.theUltimateSMPLib.event.custom.runnable;

import trxsh.ontop.theUltimateSMPLib.event.custom.CustomEvent;

public abstract class EventRunnable {
    public String listeningId;

    public EventRunnable(String listeningId) {
        this.listeningId = listeningId;
    }

    public String getListeningId() {
        return listeningId;
    }

    public abstract void run(CustomEvent event) ;
}
