package com.wfj.platform.util.signature.keytool;

import com.wfj.platform.util.signature.RsaSignatureVerifier;
import com.wfj.platform.util.signature.RsaSigner;
import org.junit.*;

import java.security.*;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.assertTrue;

/**
 * Rsa签名校验测试
 * <p>create at 15-7-12</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class RsaSignVerifyTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException,
            InvalidKeyException {
        String dataString = "Hei! Are you ready?";
        KeyPair keyPair = RsaKeyGenerator.keyGen(2048);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String priKeyString = KeyUtils.toBase64String(privateKey);
        String pubKeyString = KeyUtils.toBase64String(publicKey);
        String sign = RsaSigner.sign(dataString.getBytes(), priKeyString);
        System.out.println(sign);
        System.out.println(sign.length());
        assertTrue(RsaSignatureVerifier.verify(dataString.getBytes(), pubKeyString, sign));
    }
}
