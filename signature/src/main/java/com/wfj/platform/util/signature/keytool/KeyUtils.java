package com.wfj.platform.util.signature.keytool;

import com.wfj.platform.util.signature.jdk8.Base64;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.Key;

/**
 * Key工具类，提供一些格式转换能力
 * <p>create at 15-7-12</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class KeyUtils {
    /**
     * 将Key的字节内容转换为Base64编码后的字符串
     *
     * @param key 公钥/密钥
     * @return Base64编码后的字符串
     */
    public static String toBase64String(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 输出公/私钥。输出的内容为Base64编码后的字符串
     *
     * @param out 输出IO流
     * @param key 要保存的公/私钥。
     * @throws java.io.IOException 如果发生了IO错误
     */
    public static void outKey(OutputStream out, Key key) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        writer.write(toBase64String(key));
        writer.flush();
        out.flush();
    }
}
