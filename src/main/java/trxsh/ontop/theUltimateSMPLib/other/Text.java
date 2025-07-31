package trxsh.ontop.theUltimateSMPLib.other;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;

public class Text {
    private static final String[] chars = new String[] {
            "ᴀ","ʙ","ᴄ","ᴅ","ᴇ","ғ","ɢ","ʜ","ɪ","ᴊ","ᴋ","ʟ","ᴍ","ɴ","ᴏ","ᴘ","ǫ","ʀ","s","ᴛ","ᴜ","ᴠ","ᴡ","x","ʏ","ᴢ","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0"
    };

    public static String toSmallText(String text) {
        String in = text.toLowerCase();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < in.length(); i++) {
            char currentChar = in.charAt(i);

            if (Character.isLetterOrDigit(currentChar)) {
                int index;

                if (Character.isLetter(currentChar)) {
                    if (Character.isUpperCase(currentChar)) {
                        char lowerChar = Character.toLowerCase(currentChar);

                        index = lowerChar - 'a';

                        result.append(chars[index].toUpperCase());
                    } else {
                        char lowerChar = Character.toLowerCase(currentChar);

                        index = lowerChar - 'a';

                        result.append(chars[index]);
                    }
                } else {
                    if(currentChar == '0') {
                        result.append('0');
                    }else {
                        index = currentChar - '0' + 51;

                        result.append(chars[index]);
                    }
                }
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    public static TextComponent gradientText(String text, Color start, Color end) {
        if (text == null || text.isEmpty()) return Component.empty();
        int length = text.length();

        TextComponent.Builder componentBuilder = Component.text();

        for (int i = 0; i < length; i++) {
            double ratio = (double) i / (length - 1);
            Color color = lerpColor(start, end, ratio);
            TextColor adventureColor = TextColor.color(color.getRed(), color.getGreen(), color.getBlue());

            componentBuilder.append(Component.text(String.valueOf(text.charAt(i))).color(adventureColor));
        }

        return componentBuilder.build();
    }

    public static Color textColorToBukkitColor(TextColor color) {
        return Color.fromRGB(color.red(), color.green(), color.blue());
    }

    private static Color lerpColor(Color start, Color end, double ratio) {
        int red = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
        int green = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
        int blue = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));
        return Color.fromRGB(red, green, blue);
    }
}

