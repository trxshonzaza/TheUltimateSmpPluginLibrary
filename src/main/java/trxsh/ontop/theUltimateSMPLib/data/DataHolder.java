package trxsh.ontop.theUltimateSMPLib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataHolder {
    Map<String, Object> dataList;

    public DataHolder() {
        dataList = new HashMap<>();
    }

    public void add(String key, Object data) {
        dataList.put(key, data);
    }

    public void addOrReplace(String key, Object data) {
        if(hasKey(key)) {
            dataList.replace(key, data);
        } else {
            add(key, data);
        }
    }

    public void addIfNotExists(String key, Object data) {
        if(!hasKey(key)) {
            add(key, data);
        }
    }

    public <T> T get(String itemKey, Class<T> clazz) {
        Object obj = dataList.get(itemKey);

        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getOrCreateList(String key, Class<T> type) {
        List<?> raw = (List<?>) dataList.get(key);
        if (raw != null && !raw.isEmpty() && !type.isInstance(raw.get(0))) {
            throw new IllegalStateException("Invalid type in list for key: " + key);
        }

        if (raw != null) return (List<T>) raw;

        List<T> list = new ArrayList<>();
        dataList.put(key, list);
        return list;
    }

    public void remove(String itemKey) {
        dataList.remove(itemKey);
    }

    public boolean hasKey(String key) {
        return dataList.containsKey(key);
    }

    public void setDataList(Map<String, Object> data) {
        this.dataList = data;
    }

    public Map<String, Object> getDataList() {
        return dataList;
    }
}
