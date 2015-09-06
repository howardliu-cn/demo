package com.wfj.platform.util.signature.json;

import com.wfj.platform.util.signature.RsaSigner;
import net.sf.json.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.UUID;

/**
 * 对JSON数据进行签名的工具类
 * <p>create at 15-7-12</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public final class JsonSigner {
    static Logger LOGGER = LoggerFactory.getLogger(JsonSigner.class);

    private JsonSigner() {
    }

    /**
     * 将要发送的JSON内容签名包裹
     *
     * @param json       要签名的JSON内容，可以是 {@link net.sf.json.JSONObject} 、 {@link net.sf.json.JSONArray} 、 {@link net.sf.json.JSONNull}
     * @param privateKey 加密用的私钥
     * @param caller     调用方声明
     * @return 包裹后的Json字符串，请勿在进行JSON转换，否则会导致签名失效、无法验证
     */
    public static String wrapSignature(JSON json, PrivateKey privateKey, String caller) {
        String message = json.toString();
        String stamp = UUID.randomUUID().toString();
        StringBuilder builder = new StringBuilder();
        String dataString = message + stamp;
        builder.append("{\"messageBody\":").append(message).append(",\"stamp\":\"").append(stamp)
                .append("\",\"caller\":\"").append(caller).append("\"");
        try {
            String signature = RsaSigner.sign(dataString.getBytes(), privateKey);
            builder.append(",\"signature\":\"").append(signature).append('\"');
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            // won't happen, since all jvm provide SHA256withRSA
            LOGGER.error("签名失败", e);
        }
        builder.append('}');
        return builder.toString();
    }

    /**
     * 将要发送的JSON内容签名包裹
     *
     * @param json       要签名的JSON内容，可以是 {@link net.sf.json.JSONObject} 、 {@link net.sf.json.JSONArray} 、 {@link net.sf.json.JSONNull}
     * @param privateKey 加密用的私钥
     * @param caller     调用方声明
     * @param username   调用方登录用户名，可为null。
     * @return 包裹后的Json字符串，请勿在进行JSON转换，否则会导致签名失效、无法验证
     */
    public static String wrapSignature(JSON json, PrivateKey privateKey, String caller, String username) {
        String message = json.toString();
        String stamp = UUID.randomUUID().toString();
        StringBuilder builder = new StringBuilder();
        String dataString = message + stamp;
        builder.append("{\"messageBody\":").append(message).append(",\"stamp\":\"").append(stamp)
                .append("\",\"caller\":\"").append(caller).append("\",\"username\":\"").append(username).append("\"");
        try {
            String signature = RsaSigner.sign(dataString.getBytes(), privateKey);
            builder.append(",\"signature\":\"").append(signature).append('\"');
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            // won't happen, since all jvm provide SHA256withRSA
            LOGGER.error("签名失败", e);
        }
        builder.append('}');
        return builder.toString();
    }
}
