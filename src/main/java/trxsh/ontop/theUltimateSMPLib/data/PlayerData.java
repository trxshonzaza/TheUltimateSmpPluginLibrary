package trxsh.ontop.theUltimateSMPLib.data;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.sql.SQL;
import trxsh.ontop.theUltimateSMPLib.util.YamlUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private Map<String, Object> persistentData = new HashMap<>();
    private UUID uuid;

    private PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public PlayerData() {

    }

    public static PlayerData Create(Player player) {
        return new PlayerData(player.getUniqueId());
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public Player getPlayer() {
        return getOfflinePlayer().getPlayer();
    }

    public <T> T get(String itemKey, Class<T> clazz) {
        Object obj = persistentData.get(itemKey);

        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        }

        return null;
    }

    public void add(String key, Object data) {
        persistentData.put(key, data);
    }

    public void addOrReplace(String key, Object data) {
        if(hasKey(key)) {
            persistentData.replace(key, data);
        } else {
            add(key, data);
        }
    }

    public void addIfNotExists(String key, Object data) {
        if(!hasKey(key)) {
            add(key, data);
        }
    }

    public boolean hasKey(String key) {
        return persistentData.containsKey(key);
    }

    // getter/setters for yaml
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Map<String, Object> getPersistentData() {
        return persistentData;
    }

    public void setPersistentData(Map<String, Object> persistentData) {
        this.persistentData = persistentData;
    }

    public void saveToSql() {
        String yaml = YamlUtil.objectToYaml(this);
        String uuid = getUuid().toString();

        if(SQL.rowExists("playerData", "uuid = " + SQL.convertString(uuid))) {
            SQL.update("playerData", "uuid = " + SQL.convertString(uuid), Map.of(
                    "yaml", yaml
            ));
        } else {
            SQL.insert("playerData", getUuid().toString(), yaml);
        }
    }

    public void saveToDisk() {
        String path = Main.getInstance().getConfigManager().getPlayerDataBackupPath();

        File playerDataFolder = new File(path);
        if(!playerDataFolder.exists()) playerDataFolder.mkdirs();

        try(FileOutputStream fs = new FileOutputStream(path + "/" + uuid.toString() + ".yml")) {
            fs.write(YamlUtil.objectToYaml(this).getBytes(StandardCharsets.UTF_8));
        }catch(IOException e) {
            throw new RuntimeException("failed to save player data to disk", e);
        }
    }
}
