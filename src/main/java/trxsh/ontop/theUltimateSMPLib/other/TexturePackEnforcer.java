package trxsh.ontop.theUltimateSMPLib.other;

import net.kyori.adventure.text.TextComponent;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class TexturePackEnforcer {
    private static boolean enforcePacks = false;

    // K = hash as a string. V = resource pack URL
    private static Map<String, String> packs = new HashMap<>();
    private static TextComponent prompt;

    public static Map<String, String> getPacks() {
        return packs;
    }

    public static TextComponent getPrompt() {
        return prompt;
    }

    public static void setPrompt(TextComponent prompt) {
        TexturePackEnforcer.prompt = prompt;
    }

    public static void setEnforcePacks(boolean enforcePacks) {
        TexturePackEnforcer.enforcePacks = enforcePacks;
    }

    public static void setPacks(Map<String, String> packs) {
        TexturePackEnforcer.packs = packs;
    }

    public static boolean isEnforcePacks() {
        return enforcePacks;
    }
}
