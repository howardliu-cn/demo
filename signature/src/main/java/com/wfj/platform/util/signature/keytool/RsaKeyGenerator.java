package com.wfj.platform.util.signature.keytool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * RSA密钥生成器
 * <p>create at 15-7-12</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public final class RsaKeyGenerator {
    private static Logger log = LoggerFactory.getLogger(RsaKeyGenerator.class);

    private RsaKeyGenerator() {
    }

    /**
     * 基础的Key生成方法。
     *
     * @param size 要生成的密钥长度。bit位数。通常是8的倍数。建议使用2048位。<br/>
     *                当位数小于512位时会抛出{@link java.security.InvalidParameterException}异常；位数小于1024位时会输出警告。
     * @return 生成的密钥对
     * @throws java.security.NoSuchAlgorithmException 如果当前Java环境不支持RSA算法。
     */
    public static KeyPair keyGen(int size) throws NoSuchAlgorithmException {
        if (size < 1) {
            throw new IllegalArgumentException("key size can not be " + size);
        }
        if (size < 1024) {
            log.warn("RSA key with key size " + size + " is too weak.");
        }
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(size);
        return gen.generateKeyPair();
    }
}
