package com.wfj.platform.util.signature.handler;

import com.wfj.platform.util.signature.RsaSigner;
import com.wfj.platform.util.signature.json.SignedJsonVerifier;
import com.wfj.platform.util.signature.json.SignedJsonVerifyException;
import com.wfj.platform.util.signature.keytool.RsaKeyLoader;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PrivateSignatureHandlerTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String privateKeyString = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCnGWgYymQ0DBLA5dOLmIW8e5CemuwJ+ccTw1a9yYgA38B+ksUmw6PWG89BtjnkuewBAii/mV2zZNh24mMOMQT4kxVqeq1VJCPI4ZFvQQ/mljJtJPBxcTZ0zngx+8lokMXlxjj4KdbkIH7aywa4x6ObbJ8eK/lfXP0bvOPWw/dpxe2L33qF35rzuHPfrVISuA8Nou5M6d8Ln6mW24hP1dA5Ob4t7Agq2lYUb+fFQBPddd+t5v0dMbvtgbjt0BbUxCpFDwWyZfvEFxLIXJkFqnqx5aqh3bTFluHD2eThuq3CZ8Z0mbkWzq0rjFow7oCgQ4pJ8hru2plpo1fRArZm7iBtAgMBAAECggEBAJkBKZExmWkS+QUKLJcxFJwOpNCl2KYVwVT2U3G6nD74osEDUWT6VQWTN0bbPc2S9GfD+13dZ7ABEjhheQQgnIWj4EM8i4RfcCjbapjtgUrJkujfnw6w9IUmHWbfP3/wuFR6GeYaIXuHycA7kS8XFlcVseklqNTKR7TjU9huXhjJuY/RoKBbSiZAGaguO4iPoZZk/QGoxccoCQxJF60KRNu1dCqTOIcmhqcVu+i+PQruymulSKX9ZRwlYO3Scz+stKnptSWNwBz968TS7+Rzir0T3+dMPams1AH3uHe+T8JLUs5NvLuMTIZzDo6O+aFGo4ne3vvmBGfEB67w8i2x8IECgYEA1ZRXZr9Zr5TK2aoGFCDYtlVWHa4CrjkvHTaP8UuR9RSrKAck3qUwk1tU+JA3shhVC0vCNk1/oDn48mGrZeH0i3TWc6nuLWrLE8VmoX2jIa+3dyi7h97E+GYHeTKhTpSLGGY92RsOBjVh0b/Ll5bsw2DGjF2th8xM2VI4zyM/vY0CgYEAyEm7mbUcDBoLJR9942l9uw9tS3Q0kxcbPV9KytXkwhjYbGdtjuGfEiDXLVgU8BAIJ0ZRUAtA6KSoFFD1iMQobu7gBBjUIgCfgkc+fH9buGlKv9iW6T9AquMbSWrSgbXF5QaC55fAooNgr5axM+vWl5tgnTWGVHFdtu9XQfYuBmECgYBlPPik+oTvpm70+BQDjIJNA2xbizU4EmETzt4yOWkJK+/pfGFsrA63eq5vWCUeZUxCm3mGtfuOHoyzj7poA9AgHpTcpKsCmkGCsKpyWBRwjlM/x24E/IKPYAWg3G/7yIuaWDRu6dUe+kTQ4MIHrAG0pvXWaT0tRpkS1leZUBMRrQKBgQDEGq18ij+z+av/5R21lIxuo2Q4BMeVXYJmTO9GOreI9BqzyXET/QVrEoyc8SlPA+N30Pm8jcg4AUAw5DQEfUu5kln0qPrLcCC9xlQAQhLkNPPjc4YPSsdeio8lC1qhdgEVhZKWf5c1h70bL0jBtaCfQJsQUl/8PiOsAhxFkWzvAQKBgAjEkMsj/U99zzo9FQ/BikWL+x7nrJkLCCAzFVyQPPFVSGdRHPdeBu2ycpifo1xrVVdhO9mRXuBL8RwdX0STfM0R3tgcn+5a6PFWz9VT+9uQCxUUSJ9/sO2CRnvStKz3uCRNWNUawBPz57QkapjljM/FRunALygnKyGDvIScUxhb";
    private String pubKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApxloGMpkNAwSwOXTi5iFvHuQnprsCfnHE8NWvcmIAN/AfpLFJsOj1hvPQbY55LnsAQIov5lds2TYduJjDjEE+JMVanqtVSQjyOGRb0EP5pYybSTwcXE2dM54MfvJaJDF5cY4+CnW5CB+2ssGuMejm2yfHiv5X1z9G7zj1sP3acXti996hd+a87hz361SErgPDaLuTOnfC5+pltuIT9XQOTm+LewIKtpWFG/nxUAT3XXfreb9HTG77YG47dAW1MQqRQ8FsmX7xBcSyFyZBap6seWqod20xZbhw9nk4bqtwmfGdJm5Fs6tK4xaMO6AoEOKSfIa7tqZaaNX0QK2Zu4gbQIDAQAB";

    @Test
    public void testSign() throws Exception {
        PrivateSignatureHandler handler = new PrivateSignatureHandler();
        handler.setCaller("admin");
        handler.setUsername("admin");
        handler.setPrivateKeyString(privateKeyString);
        JSONObject json = new JSONObject();
        json.put("start", "0");
        json.put("limit", "20");
        String signatureResult = handler.sign(json);
        logger.debug("包装的json数据为：{}", signatureResult);
        assertEquals(SignedJsonVerifier
                .verify(signatureResult, new SignedJsonVerifier.PublicKeyProvider() {
                    @Override
                    public PublicKey lookUpPublicKey(String caller) throws Exception {
                        return RsaKeyLoader.base64String2PubKey(pubKeyString);
                    }
                }), "admin");
    }

    @Test
    public void testVerify()
            throws SignedJsonVerifyException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException,
            SignatureException, UnsupportedEncodingException {
        JSONObject json = new JSONObject();
        JSONObject messageBody = new JSONObject();
        messageBody.put("contentSid", "1");
        messageBody.put("lowerLimit", "2");
        messageBody.put("upperLimit", "3");
        messageBody.put("orderBy", "4");
        messageBody.put("showText", URLEncoder.encode("中文", "UTF-8"));
        json.put("messageBody", messageBody.toString());
        String stamp = "2b5f98dc-c198-454c-8b3d-83be2c600ce6";
        json.put("stamp", stamp);
        json.put("caller", "admin");
        String signature = RsaSigner
                .sign((messageBody.toString() + stamp).getBytes(), RsaKeyLoader.base64String2PriKey(privateKeyString));
        json.put("signature", signature);
        logger.debug("签名结果为：{}", json.toString());
        assertEquals(SignedJsonVerifier
                .verify(json.toString(), new SignedJsonVerifier.PublicKeyProvider() {
                    @Override
                    public PublicKey lookUpPublicKey(String caller) throws Exception {
                        return RsaKeyLoader.base64String2PubKey(pubKeyString);
                    }
                }), "admin");
    }
}