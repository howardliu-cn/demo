package com.wfj.platform.util.signature.keytool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

/**
 * RSA密钥文件生成器
 * <br/>create at 15-7-22
 *
 * @author liufl
 * @since 1.0.0
 */
public class RsaKeyFileGenerator {
    public static void genAndOutToFile(int size, String fileName) throws IOException {
        KeyPair keyPair = null;
        try {
            keyPair = RsaKeyGenerator.keyGen(size);
        } catch (NoSuchAlgorithmException ignored) {
        }
        assert keyPair != null;
        File priFile = new File(fileName);
        File pubFile = new File(fileName + ".pub");
        KeyUtils.outKey(new FileOutputStream(priFile), keyPair.getPrivate());
        KeyUtils.outKey(new FileOutputStream(pubFile), keyPair.getPublic());
    }

    public static void main(String[] args) throws IOException {
        String fileName = "rsaKey";
        if (args.length > 0) {
            for (String arg : args) {
                if (arg != null) {
                    fileName = arg;
                    break;
                }
            }
        }
        genAndOutToFile(2048, fileName);
    }
}
