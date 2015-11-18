package cn.howardliu.miner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>create at 15-11-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

    //reverse phrase
    public static String reverse(String phrase) {
        StringBuilder reversePhrase = new StringBuilder();
        for (int i = phrase.length() - 1; i >= 0; i--) {
            reversePhrase.append(phrase.charAt(i));
        }
        return reversePhrase.toString();
    }

    //co-prefix length of s1 and s2
    public static int coPrefixLength(String s1, String s2) {
        int coPrefixLength = 0;
        for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                coPrefixLength++;
            } else {
                break;
            }
        }
        return coPrefixLength;
    }
}
