package com.wfj.platform.util.signature.keytool;

import com.wfj.platform.util.signature.jdk8.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA Key文件加载工具
 * <p>create at 15-7-12</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class RsaKeyLoader {
    /**
     * 使用指定数据恢复公钥
     *
     * @param bytes 指定公钥的二进制编码数据<br/>
     *              <strong>只接受由{@link java.security.interfaces.RSAPublicKey#getEncoded()}产出的数据。</strong>
     * @return 从二进制数据中恢复的公钥
     * @throws java.security.spec.InvalidKeySpecException 数据格式非法。
     */
    public static RSAPublicKey bytes2PubKey(byte[] bytes) throws InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ignored) {
        }
        assert keyFactory != null;
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    /**
     * 使用指定数据恢复公钥
     *
     * @param keyString 指定公钥的Base64文本<br/>
     *                  <strong>只接受由{@link java.security.interfaces.RSAPublicKey#getEncoded()}产出的数据。</strong>
     * @return 从二进制数据中恢复的公钥
     * @throws java.security.spec.InvalidKeySpecException 数据格式非法。
     * @throws IllegalArgumentException                   如果参数不是合法的Base64编码字符串
     */
    public static RSAPublicKey base64String2PubKey(String keyString)
            throws InvalidKeySpecException, IllegalArgumentException {
        return bytes2PubKey(Base64.getDecoder().decode(keyString));
    }

    /**
     * 使用指定数据恢复私钥
     *
     * @param bytes 指定私钥的二进制编码数据<br/>
     *              <strong>只接受由{@link java.security.interfaces.RSAPrivateKey#getEncoded()}产出的数据。</strong>
     * @return 从二进制数据中恢复的私钥
     * @throws InvalidKeySpecException  数据格式非法。
     */
    public static RSAPrivateKey bytes2PriKey(byte[] bytes) throws InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ignored) {
        }
        assert keyFactory != null;
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * 使用指定数据恢复私钥
     *
     * @param keyString 指定私钥的Base64文本<br/>
     *                  <strong>只接受由{@link java.security.interfaces.RSAPrivateKey#getEncoded()}产出的数据。</strong>
     * @return 从二进制数据中恢复的私钥
     * @throws InvalidKeySpecException  数据格式非法。
     * @throws IllegalArgumentException 如果参数不是合法的Base64编码字符串
     */
    public static RSAPrivateKey base64String2PriKey(String keyString)
            throws InvalidKeySpecException, IllegalArgumentException {
        return bytes2PriKey(Base64.getDecoder().decode(keyString));
    }
}
