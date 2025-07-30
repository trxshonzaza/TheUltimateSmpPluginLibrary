package trxsh.ontop.theUltimateSMPLib.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import trxsh.ontop.theUltimateSMPLib.Main;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final JavaPlugin plugin;
    private final FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    // ===== SQL CONFIGURATION =====

    public boolean useSql() {
        return config.getBoolean("sql.useSql", true);
    }

    public String getSqlHost() {
        return config.getString("sql.host", "localhost");
    }

    public int getSqlPort() {
        return config.getInt("sql.port", 3306);
    }

    public String getSqlDatabase() {
        return config.getString("sql.database", "minecraft");
    }

    public String getSqlUser() {
        return config.getString("sql.user", "root");
    }

    public String getSqlPassword() {
        return config.getString("sql.password", "password");
    }

    // ===== TABLE NAMES =====

    public String getPlayerDataTable() {
        return config.getString("sql.tables.player_data", "playerdata");
    }

    public String getGlobalDataTable() {
        return config.getString("sql.tables.global_data", "globaldata");
    }

    // ===== PATHS =====

    public String getPlayerDataBackupPath() {
        return config.getString("paths.player_data_backup", "plugins/theUltimateSMPLib/data/player_data.yml");
    }

    public String getGlobalDataBackupPath() {
        return config.getString("paths.global_data_backup", "plugins/theUltimateSMPLib/data/global_data.yml");
    }

    public String getLogFilePath() {
        return config.getString("paths.log_file", "plugins/theUltimateSMPLib/logs/sql.log");
    }

    // ==== TEXTURE PACKS ====

    public String getTexturePackPrompt() {
        return config.getString("texture_pack.prompt", "§6Please accept the custom resource pack to play on the server.");
    }

    public boolean getTexturePackEnforceEnabled() {
        return config.getBoolean("texture_pack.enabled", false);
    }

    public Map<String, String> getEnabledTexturePacks() {
        Map<String, String> enabledPacks = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("texture_pack.packs");

        if (section != null) {
            for (String hash : section.getKeys(false)) {
                ConfigurationSection packSection = section.getConfigurationSection(hash);

                if (packSection != null && packSection.getBoolean("enabled")) {
                    String url = packSection.getString("url");

                    if (url != null && !url.isEmpty()) {
                        enabledPacks.put(hash, url);
                    }
                }
            }
        }
        return enabledPacks;
    }
}
