package trxsh.ontop.theUltimateSMPLib.data;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.enchant.ExampleEnchantment;
import trxsh.ontop.theUltimateSMPLib.sql.SQL;
import trxsh.ontop.theUltimateSMPLib.yaml.YamlHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Player Data container. Saves all data in the map per unique player to either SQL or disk.
 */
public class PlayerData extends DataHolder {
    private UUID uuid;

    private PlayerData(UUID uuid) {
        super();
        this.uuid = uuid;
    }

    public PlayerData() {
        super();
    }

    /*
    Creates player data object for a unique bukkit player
     */
    public static PlayerData Create(Player player) {
        return new PlayerData(player.getUniqueId());
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public Player getPlayer() {
        return getOfflinePlayer().getPlayer();
    }

    // getter/setters for yaml
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void saveToSql() {
        String yaml = YamlHelper.objectToYaml(this);
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
            fs.write(YamlHelper.objectToYaml(this).getBytes(StandardCharsets.UTF_8));
        }catch(IOException e) {
            throw new RuntimeException("failed to save player data to disk", e);
        }
    }
}
