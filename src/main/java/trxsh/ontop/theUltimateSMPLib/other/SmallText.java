package trxsh.ontop.theUltimateSMPLib.other;

public class SmallText {
    private static final String[] chars = new String[] {
            "ᴀ","ʙ","ᴄ","ᴅ","ᴇ","ғ","ɢ","ʜ","ɪ","ᴊ","ᴋ","ʟ","ᴍ","ɴ","ᴏ","ᴘ","ǫ","ʀ","s","ᴛ","ᴜ","ᴠ","ᴡ","x","ʏ","ᴢ","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0"
    };

    public static String convert(String text) {
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
}

