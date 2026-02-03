package trxsh.ontop.theUltimateSMPLib.data;

import java.util.HashMap;

/**
 * A generic data holder.
 * can be instantiated to be used to hold data (not persistent!)
 * To hold persistent data, use GlobalData for global, or PlayerData for player-specific data.
 */
public class GenericDataHolder extends DataHolder {
    public GenericDataHolder() {
        super();
    }
}
