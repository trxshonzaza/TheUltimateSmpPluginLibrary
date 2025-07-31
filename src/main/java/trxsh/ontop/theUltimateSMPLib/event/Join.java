package trxsh.ontop.theUltimateSMPLib.event;

import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import trxsh.ontop.theUltimateSMPLib.manager.PlayerDataManager;
import trxsh.ontop.theUltimateSMPLib.data.PlayerData;
import trxsh.ontop.theUltimateSMPLib.other.Messenger;
import trxsh.ontop.theUltimateSMPLib.other.Text;
import trxsh.ontop.theUltimateSMPLib.other.TexturePackEnforcer;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Join implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = PlayerDataManager.get(player.getUniqueId());

        if(data == null) {
            data = PlayerDataManager.add(PlayerData.Create(player));
        }

        data.addOrReplace("ip", player.getAddress().getAddress().getHostAddress());
        data.addOrReplace("lastLogin", LocalDateTime.now().toString());

        data.addIfNotExists("firstLogin", LocalDateTime.now().toString());

        Messenger.sendMessage(player, Component.text("Hello!"));
        Messenger.sendErrorMessage(player, Component.text("This is an error message!"));
        Messenger.sendSuccessMessage(player, Component.text("This is a success messsage!"));
        Messenger.sendColorMessage(player, NamedTextColor.DARK_AQUA, Component.text("This is a colored text messsage!"));
        Messenger.sendMessage(player, Text.gradientText("And this is gradient text!! :)",
                Text.textColorToBukkitColor(NamedTextColor.AQUA),
                Text.textColorToBukkitColor(NamedTextColor.GREEN)));

        if(TexturePackEnforcer.isEnforcePacks()) {
            try {
                List<ResourcePackInfo> packs = new ArrayList<>();

                for(Map.Entry<String, String> entry : TexturePackEnforcer.getPacks().entrySet()) {
                    packs.add(ResourcePackInfo.resourcePackInfo(
                            UUID.randomUUID(),
                            new URI(entry.getValue()),
                            entry.getKey()));
                }

                ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
                        .prompt(TexturePackEnforcer.getPrompt())
                        .required(true)
                        .packs(packs)
                        .build();

                player.sendResourcePacks(request);
            } catch (Exception e) {
                throw new RuntimeException("encountered an exception when loading packs", e);
            }
        }
    }
}
