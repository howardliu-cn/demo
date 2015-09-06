package com.wfj.platform.util.signature;

import com.wfj.platform.util.signature.jdk8.Base64;
import com.wfj.platform.util.signature.keytool.RsaKeyLoader;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

/**
 * RSA 签名器
 * <p>create at 15-7-12</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public final class RsaSigner {
    private RsaSigner() {
    }

    /**
     * 使用私钥签名
     *
     * @param data       要签名的数据内容
     * @param privateKey 私钥
     * @return Base64编码的签名字符串
     * @throws NoSuchAlgorithmException 如果当前运行环境中不支持 SHA256withRSA 算法
     * @throws InvalidKeyException      私钥不合法
     * @throws SignatureException       签名对象没有正确初始化
     */
    public static String sign(byte[] data, PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA"); // 此为签名对象
        signature.initSign(privateKey);
        signature.update(data);
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 使用私钥签名
     *
     * @param data                   要签名的数据内容
     * @param privateKeyBase64String 私钥的Base64编码字符串
     * @return Base64编码的签名字符串
     * @throws InvalidKeySpecException  如果私钥字符串不合法。
     * @throws NoSuchAlgorithmException 如果当前运行环境中不支持RSA算法
     * @throws SignatureException       签名对象没有正确初始化
     * @throws InvalidKeyException      私钥不合法
     * @throws IllegalArgumentException 如果提供的私钥编码字符串不是合法的Base64编码字符串
     */
    public static String sign(byte[] data, String privateKeyBase64String)
            throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        RSAPrivateKey privateKey = RsaKeyLoader.base64String2PriKey(privateKeyBase64String);
        return sign(data, privateKey);
    }
}
