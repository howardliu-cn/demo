package com.wfj.platform.util.signature;

import com.wfj.platform.util.signature.jdk8.Base64;
import com.wfj.platform.util.signature.keytool.RsaKeyLoader;

import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * RSA 签名校验器
 * <p>create at 15-7-12</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public final class RsaSignatureVerifier {
    private RsaSignatureVerifier() {
    }

    /**
     * 使用公钥校验签名
     *
     * @param data      要校验的签名数据
     * @param publicKey 公钥
     * @param sign      提供的签名
     * @return 签名是否合法
     * @throws NoSuchAlgorithmException 如果当前运行环境中不支持 SHA256withRSA 算法
     * @throws InvalidKeyException      公钥不合法
     * @throws SignatureException       签名对象没有正确初始化
     * @throws IllegalArgumentException 如果提供的签名不是合法的Base64编码字符串
     */
    public static boolean verify(byte[] data, PublicKey publicKey, String sign)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IllegalArgumentException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * 使用公钥校验签名
     *
     * @param data 要校验的签名数据
     * @param publicKeyBase64String 公钥的Base64编码字符串
     * @param sign 提供的签名
     * @return 签名是否合法
     * @throws InvalidKeySpecException 公钥数据格式非法
     * @throws NoSuchAlgorithmException 如果当前运行环境中不支持RSA算法
     * @throws SignatureException 签名对象没有正确初始化
     * @throws InvalidKeyException 公钥不合法
     * @throws IllegalArgumentException 如果提供的签名或公钥编码字符串不是合法的Base64编码字符串
     */
    public static boolean verify(byte[] data, String publicKeyBase64String, String sign)
            throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException,
            IllegalArgumentException {
        RSAPublicKey publicKey = RsaKeyLoader.base64String2PubKey(publicKeyBase64String);
        return verify(data, publicKey, sign);
    }
}
