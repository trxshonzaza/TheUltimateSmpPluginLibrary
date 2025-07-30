package trxsh.ontop.theUltimateSMPLib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import trxsh.ontop.theUltimateSMPLib.command.SaveCommand;
import trxsh.ontop.theUltimateSMPLib.event.GuiChecker;
import trxsh.ontop.theUltimateSMPLib.event.Join;
import trxsh.ontop.theUltimateSMPLib.manager.PlayerDataManager;
import trxsh.ontop.theUltimateSMPLib.sql.SQL;
import trxsh.ontop.theUltimateSMPLib.sql.SqlConstants;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.Map;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getScheduler;

public final class Main extends JavaPlugin {
    private static Main instance = null;

    @Override
    public void onEnable() {
        instance = this;

        //commands
        getCommand("save").setExecutor(new SaveCommand());

        //events
        getPluginManager().registerEvents(new Join(), this);
        getPluginManager().registerEvents(new GuiChecker(), this);

        try {
            SQL.Initialize("localhost", "osas", "r5t7hgrt", "testdatabase");
            createTables();

            PlayerDataManager.loadFromSQL();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }

    private void createTables() {
        if(!SQL.tableExists("playerData")) {
            SQL.createTable("playerData", Map.of(
                    "uuid", SqlConstants.UUID,
                    "yaml", SqlConstants.LONGTEXT
            ));
        }

        if(!SQL.tableExists("globalData")) {
            SQL.createTable("globalData", Map.of(
                    "yaml", SqlConstants.LONGTEXT
            ));
        }
    }

    private void createFolder() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
    }
}
