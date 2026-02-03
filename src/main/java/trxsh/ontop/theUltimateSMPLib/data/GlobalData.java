package trxsh.ontop.theUltimateSMPLib.data;

import org.bukkit.Bukkit;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.config.ConfigManager;
import trxsh.ontop.theUltimateSMPLib.sql.SQL;
import trxsh.ontop.theUltimateSMPLib.yaml.YamlHelper;

import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Data container. Saves all data in the map to either SQL or disk.
 */
public class GlobalData extends DataHolder {
    private static GlobalData instance = null;

    public GlobalData() {
        super();
        instance = this;
    }

    public static GlobalData getInstance() {
        return instance;
    }

    public void saveToSql() {
        ConfigManager manager = new ConfigManager(Main.getInstance());
        String globalDataTable = manager.getGlobalDataTable();

        String yaml = YamlHelper.objectToYaml(this);

        if(SQL.rowExists(globalDataTable, null)) {
            SQL.update(globalDataTable, null, Map.of(
                    "yaml", yaml
            ));
        } else {
            SQL.insert(globalDataTable, yaml);
        }
    }

    public void saveToDisk() {
        ConfigManager manager = new ConfigManager(Main.getInstance());
        String path = manager.getGlobalDataBackupPath();

        File playerDataFolder = new File(path);
        if(!playerDataFolder.exists()) playerDataFolder.mkdirs();

        try(FileOutputStream fs = new FileOutputStream(path + "/globaldata.yml")) {
            fs.write(YamlHelper.objectToYaml(this).getBytes(StandardCharsets.UTF_8));
        }catch(IOException e) {
            throw new RuntimeException("failed to save player data to disk", e);
        }
    }

    public void loadFromSQL() throws SQLException {
        ConfigManager manager = new ConfigManager(Main.getInstance());
        String globalDataTable = manager.getGlobalDataTable();

        dataList.clear();
        CachedRowSet rws = SQL.select(globalDataTable, "*", null);

        if(rws == null)
            throw new SQLException("global data has either no entries or the table does not exist");

        while(rws.next()) {
            String yaml = rws.getString("yaml");

            try {
                this.setDataList(YamlHelper.yamlToObject(yaml, GlobalData.class).getDataList());
            }catch(Exception e) {
                Bukkit.getLogger().warning("Failed to load global data: " + e.getMessage());
                e.printStackTrace();
            }
        }

        Bukkit.getLogger().info("Loaded global data from SQL.");
    }

    public void loadFromDisk() throws IOException {
        ConfigManager manager = new ConfigManager(Main.getInstance());
        String path = manager.getGlobalDataBackupPath();

        dataList.clear();

        File globalDataFolder = new File(path);
        if(!globalDataFolder.exists()) throw new IOException("path does not exist");

        File file = new File(path + "/globaldata.yml");
        if(!file.exists()) throw new IOException("no global data yml in the specified directory. the global data file must end in '.yml' path:" + path);

        try(FileInputStream fs = new FileInputStream(file)) {
            String yaml = new String(fs.readAllBytes());

            this.setDataList(YamlHelper.yamlToObject(yaml, GlobalData.class).getDataList());
        }
    }
}
