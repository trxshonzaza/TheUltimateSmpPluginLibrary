package trxsh.ontop.theUltimateSMPLib.manager;

import org.bukkit.Bukkit;
import trxsh.ontop.theUltimateSMPLib.data.PlayerData;
import trxsh.ontop.theUltimateSMPLib.sql.SQL;
import trxsh.ontop.theUltimateSMPLib.util.YamlUtil;

import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private static Map<UUID, PlayerData> dataMap = new HashMap<>();

    public static void add(PlayerData data) {
        dataMap.put(data.getUuid(), data);
    }

    public static PlayerData get(UUID uuid) {
        return dataMap.get(uuid);
    }

    public static void loadFromSQL() throws SQLException {
        dataMap.clear();
        CachedRowSet rws = SQL.select("playerData", "*", null);

        if(rws == null)
            throw new SQLException("player data has either no entries or the table does not exist");

        while(rws.next()) {
            UUID uuid = UUID.fromString(rws.getString("uuid"));
            String yaml = rws.getString("yaml");

            try {
                PlayerData data = YamlUtil.yamlToObject(yaml, PlayerData.class);
                dataMap.put(uuid, data);
            }catch(Exception e) {
                Bukkit.getLogger().warning("Failed to load data for UUID: " + uuid + " - " + e.getMessage());
                e.printStackTrace();
            }
        }

        Bukkit.getLogger().info("Loaded " + dataMap.size() + " player data entries from SQL.");
    }

    public static void loadFromDisk(String path) throws IOException {
        dataMap.clear();

        File playerDataFolder = new File(path);
        if(!playerDataFolder.exists()) throw new IOException("path does not exist");

        File[] files = playerDataFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if(files == null) throw new IOException("no player data in specified directory. all player data must end with '.yml' " + path);

        for(File file : files) {
            try(FileInputStream fs = new FileInputStream(file)) {
                String yaml = new String(fs.readAllBytes());
                PlayerData data = YamlUtil.yamlToObject(yaml, PlayerData.class);

                dataMap.put(data.getUuid(), data);
            }
        }
    }
}
