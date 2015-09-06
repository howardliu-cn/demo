package com.wfj.platform.util.signature.json;

import com.wfj.platform.util.signature.RsaSignatureVerifier;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

/**
 * 签名的JSON校验工具
 * <p>create at 15-7-12</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public final class SignedJsonVerifier {
    static Logger LOGGER = LoggerFactory.getLogger(SignedJsonVerifier.class);

    private SignedJsonVerifier() {
    }

    /**
     * 检验接收到的签名JSON
     * @param jsonString 要验证的JSON字符串
     * @param publicKeyProvider 公钥提供器
     * @return 签名对应的caller。如果返回 {@code null} 表示没有验证通过
     * @throws SignedJsonVerifyException
     */
    public static String verify(String jsonString, PublicKeyProvider publicKeyProvider)
            throws SignedJsonVerifyException {
        JSONObject jsonObject;
        String message;
        String stamp;
        String signature;
        String caller;
        try {
            jsonObject = JSONObject.fromObject(jsonString);
            message = jsonObject.getString("messageBody");
            stamp = jsonObject.getString("stamp");
            signature = jsonObject.getString("signature");
            caller = jsonObject.getString("caller");
        } catch (Exception e) {
            LOGGER.error("JSON字符串格式不对应。", e);
            throw new SignedJsonVerifyException("JSON格式非法", e);
        }
        PublicKey publicKey;
        try {
            publicKey = publicKeyProvider.lookUpPublicKey(caller);
        } catch (Exception e) {
            LOGGER.error("无法找到用户或用户公钥", e);
            throw new SignedJsonVerifyException("查找caller失败",e);
        }
        if (publicKey == null) {
            LOGGER.error("无法找到用户公钥");
            throw new SignedJsonVerifyException("caller未配置公钥");
        }
        String dataString = message + stamp;
        try {
            return RsaSignatureVerifier.verify(dataString.getBytes(), publicKey, signature) ? caller : null;
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            // won't happen, since all jvm provide SHA256withRSA
            LOGGER.error("校验失败", e);
            throw new SignedJsonVerifyException("检验失败", e);
        }
    }

    /**
     * 公钥提供器接口
     */
    public static interface PublicKeyProvider {
        /**
         * 提供调用方对应的公钥
         * @param caller 调用方标识
         * @return 如果可以找到，提供其公钥
         * @throws Exception 如果找不到对应调用方或其公钥。
         */
        PublicKey lookUpPublicKey(String caller) throws Exception;
    }
}
