package cn.howardliu.miner;

/**
 * <br/>create at 15-11-17
 *
 * @author liuxh
 * @since 1.0.0
 */
public class CharacterUtils {
    private CharacterUtils() {
    }

    public static String sbc2Dbc(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        return new String(c);
    }
}
