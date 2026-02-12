package trxsh.ontop.theUltimateSMPLib.data;

import trxsh.ontop.theUltimateSMPLib.item.CustomItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A persistent data holder.
 * each instance is added to a list corresponding to its UUID in order to retrieve the respective data holder instance.
 */
public class PersistentDataHolder extends DataHolder {
    private String holderKey;
    private static final Map<String, PersistentDataHolder> dataHolders = new HashMap<>();

    public static PersistentDataHolder get(String key) {
        return dataHolders.get(key);
    }

    /**
     * creates a persistent data holder
     * @param id the id to assign to the data holder
     */
    public PersistentDataHolder(String id) {
        super();
        this.holderId = id;

        dataHolders.put(id, this);
    }

    /**
     * clears all data, removing from the list as well.
     */
    public void removeInstance() {
        dataList.clear();
        dataHolders.remove(getHolderKey());

        holderId = null;
    }

    public String getHolderKey() {
        return holderId;
    }
}
