package trxsh.ontop.theUltimateSMPLib;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import trxsh.ontop.theUltimateSMPLib.command.ItemRegistryCommand;
import trxsh.ontop.theUltimateSMPLib.command.SaveCommand;
import trxsh.ontop.theUltimateSMPLib.config.ConfigManager;
import trxsh.ontop.theUltimateSMPLib.data.GlobalData;
import trxsh.ontop.theUltimateSMPLib.data.PlayerData;
import trxsh.ontop.theUltimateSMPLib.event.GuiChecker;
import trxsh.ontop.theUltimateSMPLib.event.Join;
import trxsh.ontop.theUltimateSMPLib.event.SimpleEventHandler;
import trxsh.ontop.theUltimateSMPLib.manager.PlayerDataManager;
import trxsh.ontop.theUltimateSMPLib.other.Async;
import trxsh.ontop.theUltimateSMPLib.other.Loops;
import trxsh.ontop.theUltimateSMPLib.other.TexturePackEnforcer;
import trxsh.ontop.theUltimateSMPLib.sql.SQL;
import trxsh.ontop.theUltimateSMPLib.sql.SqlConstants;

import javax.sql.rowset.CachedRowSet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getScheduler;

public final class Main extends JavaPlugin {
    private static Main instance = null;
    private static GlobalData globalData = null;
    private static ConfigManager manager = null;

    @Override
    public void onEnable() {
        createFolder();
        saveDefaultConfig();

        instance = this;
        globalData = new GlobalData();
        manager = new ConfigManager(this);

        //commands
        getCommand("checkdata").setExecutor(new SaveCommand());
        getCommand("items").setExecutor(new ItemRegistryCommand());

        //events
        getPluginManager().registerEvents(new Join(), this);
        getPluginManager().registerEvents(new GuiChecker(), this);
        getPluginManager().registerEvents(new SimpleEventHandler(), this);

        // keep SQL async if possible.
        Async.run(() -> {
            try {
                if(manager.useSql()) {
                    SQL.Initialize(manager.getSqlHost(), manager.getSqlPort(), manager.getSqlUser(), manager.getSqlPassword(), manager.getSqlDatabase());
                    createTables();

                    PlayerDataManager.loadFromSQL();
                    globalData.loadFromSQL();
                } else {
                    PlayerDataManager.loadFromDisk();
                    globalData.loadFromDisk();
                }
            } catch (SQLException | IOException | ClassNotFoundException e) {
                throw new RuntimeException("encountered an error when loading data", e);
            }
        });

        TexturePackEnforcer.setEnforcePacks(manager.getTexturePackEnforceEnabled());
        TexturePackEnforcer.setPacks(manager.getEnabledTexturePacks());
        TexturePackEnforcer.setPrompt(Component.text(manager.getTexturePackPrompt()));
    }

    @Override
    public void onDisable() {
        // save everything here typically.
        Async.run(() -> {
            Collection<PlayerData> playerData = PlayerDataManager.getDataMap().values();

            if(manager.useSql()) {
                playerData.forEach(PlayerData::saveToSql);
                globalData.saveToSql();
            } else {
                playerData.forEach(PlayerData::saveToDisk);
                globalData.saveToDisk();
            }
        });
    }

    public static Main getInstance() {
        return instance;
    }

    private void createTables() {
        if(!SQL.tableExists(manager.getPlayerDataTable())) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();

            map.put("uuid", SqlConstants.UUID);
            map.put("yaml", SqlConstants.LONGTEXT);

            SQL.createTable(manager.getPlayerDataTable(), map);
        }

        if(!SQL.tableExists(manager.getGlobalDataTable())) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("yaml", SqlConstants.LONGTEXT);

            SQL.createTable(manager.getGlobalDataTable(), map);
        }
    }

    private void createFolder() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
    }

    public ConfigManager getConfigManager() {
        return manager;
    }
}
