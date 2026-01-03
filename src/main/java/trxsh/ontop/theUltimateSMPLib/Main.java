package trxsh.ontop.theUltimateSMPLib;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import trxsh.ontop.theUltimateSMPLib.command.Credits;
import trxsh.ontop.theUltimateSMPLib.command.ItemRegistryCommand;
import trxsh.ontop.theUltimateSMPLib.command.SaveCommand;
import trxsh.ontop.theUltimateSMPLib.config.ConfigManager;
import trxsh.ontop.theUltimateSMPLib.data.GlobalData;
import trxsh.ontop.theUltimateSMPLib.data.PlayerData;
import trxsh.ontop.theUltimateSMPLib.enchant.CustomEnchantment;
import trxsh.ontop.theUltimateSMPLib.enchant.ExampleEnchantment;
import trxsh.ontop.theUltimateSMPLib.event.Anvil;
import trxsh.ontop.theUltimateSMPLib.event.ChangeItem;
import trxsh.ontop.theUltimateSMPLib.event.gui.GuiChecker;
import trxsh.ontop.theUltimateSMPLib.event.Join;
import trxsh.ontop.theUltimateSMPLib.event.simple.SimpleEventHandler;
import trxsh.ontop.theUltimateSMPLib.item.CustomItemStack;
import trxsh.ontop.theUltimateSMPLib.manager.CustomEnchantmentRegistry;
import trxsh.ontop.theUltimateSMPLib.manager.PlayerDataManager;
import trxsh.ontop.theUltimateSMPLib.other.Async;
import trxsh.ontop.theUltimateSMPLib.other.TexturePackEnforcer;
import trxsh.ontop.theUltimateSMPLib.sql.SQL;
import trxsh.ontop.theUltimateSMPLib.sql.SqlConstants;
import trxsh.ontop.theUltimateSMPLib.util.ChestHelper;
import trxsh.ontop.theUltimateSMPLib.util.CustomItemHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;

import static org.bukkit.Bukkit.getPluginManager;

public final class Main extends JavaPlugin {
    private static Main instance = null;
    private static GlobalData globalData = null;
    private static ConfigManager manager = null;
    public static boolean initalizedSimpleEvent = false;

    private static CustomEnchantment enchant = null;

    @Override
    public void onEnable() {
        createFolder();
        saveDefaultConfig();

        instance = this;
        globalData = new GlobalData();
        manager = new ConfigManager(this);

        ExampleEnchantment ex = new ExampleEnchantment("Example Enchantment", "ex_enchant", 10, 1);
        CustomEnchantmentRegistry.register(ex);

        //commands
        getCommand("checkdata").setExecutor(new SaveCommand());
        getCommand("items").setExecutor(new ItemRegistryCommand());
        getCommand("credits").setExecutor(new Credits());

        getCommand("applyenchant").setExecutor((commandSender, command, s, strings) -> {
            if(commandSender instanceof Player player) {
                CustomEnchantment.applyEnchantment(ex, player.getInventory().getItemInMainHand());
            }

            return true;
        });

        //events
        getPluginManager().registerEvents(new Join(), this);
        getPluginManager().registerEvents(new Anvil(), this);
        getPluginManager().registerEvents(new GuiChecker(), this);
        getPluginManager().registerEvents(new ChangeItem(), this);
        getPluginManager().registerEvents(new ChestHelper(), this);
        new SimpleEventHandler().init(this);

        // keep SQL async if possible!
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
        Collection<PlayerData> playerData = PlayerDataManager.getDataMap().values();

        try {
            if(manager.useSql() && SQL.isValid()) {
                playerData.forEach(PlayerData::saveToSql);
                globalData.saveToSql();
            } else {
                playerData.forEach(PlayerData::saveToDisk);
                globalData.saveToDisk();
            }
        } catch(Exception e) {
            Bukkit.getLogger().warning("An error occured when saving data: " + e.getMessage());
            e.printStackTrace();
        }
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
