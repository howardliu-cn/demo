package com.wfj.platform.util.signature.handler;

import com.wfj.platform.util.signature.exception.CallerNotFoundException;
import com.wfj.platform.util.signature.json.JsonSigner;
import com.wfj.platform.util.signature.keytool.RsaKeyLoader;
import net.sf.json.JSON;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.security.spec.InvalidKeySpecException;

/**
 * <br/>create at 15-7-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class PrivateSignatureHandler {
    private String caller;// 调用方声明
    private String username;// 调用方登录用户名
    private String privateKeyFilePath;// 私钥的文件路径
    private File privateKeyFile;// 私钥的文件对象
    private String privateKeyString;// 私钥的Base64文本

    /**
     * 将要发送的JSON内容签名包裹
     *
     * @param json 要签名的JSON内容，可以是 {@link net.sf.json.JSONObject} 、 {@link net.sf.json.JSONArray} 、 {@link net.sf.json.JSONNull}
     * @return 包裹后的Json字符串，请勿重复进行JSON转换，否则会导致签名失效、无法验证
     * @throws InvalidKeySpecException 数据格式非法。
     * @throws java.io.IOException     如果传入的是私钥文件对象或文件路径有异常，可能会出现这种异常。
     */
    public String sign(JSON json) throws IOException, InvalidKeySpecException {
        return JsonSigner
                .wrapSignature(json, RsaKeyLoader.base64String2PriKey(getPrivateKeyString()),
                        getCaller(), getUsername());
    }

    /**
     * 获取私钥文件内容
     *
     * @return 私钥文件内容
     * @throws IOException
     */
    public String getPrivateKeyString() throws IOException {
        if (StringUtils.isBlank(privateKeyString)) {
            privateKeyString = readString(privateKeyFile);
        }
        return privateKeyString;
    }

    public void setPrivateKeyString(String privateKeyString) {
        this.privateKeyString = privateKeyString;
    }

    /**
     * 从提供的文件中读取内容，并以字符串格式返回。
     *
     * @param file 待读取文件
     * @return 文件内容字符串。当传入的file对象为{@code null}，或file对象指向文件不存在，或file对象指向文件夹，直接返回{@code null}。
     * @throws java.io.IOException 文件读取异常。
     */
    private String readString(File file) throws IOException {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    /**
     * 获取私钥文件路径
     *
     * @return 私钥文件路径
     */
    public String getPrivateKeyFilePath() {
        return privateKeyFilePath;
    }

    /**
     * 设置私钥文件路径
     *
     * @param privateKeyFilePath 私钥文件路径
     * @throws FileNotFoundException 如果输入路径为空或对应路径文件不存在，抛出该异常。
     */
    public void setPrivateKeyFilePath(String privateKeyFilePath) throws FileNotFoundException {
        if (StringUtils.isBlank(privateKeyFilePath)) {
            throw new FileNotFoundException("Caused by the path of file is not exist!");
        }
        privateKeyFile = new File(privateKeyFilePath);
        if (!privateKeyFile.exists()) {
            throw new FileNotFoundException("Caused by the file '" + privateKeyFilePath + "' is not found");
        }
        this.privateKeyFilePath = privateKeyFilePath;
    }

    /**
     * 获取调用者信息
     *
     * @return 调用者信息
     */
    public String getCaller() {
        return caller;
    }

    /**
     * 设置调用者信息
     *
     * @param caller 调用者信息
     * @throws CallerNotFoundException 如果输入的调用者信息为空，抛出该异常。
     */
    public void setCaller(String caller) throws CallerNotFoundException {
        if (StringUtils.isBlank(caller)) {
            throw new CallerNotFoundException("Caused by caller is null");
        }
        this.caller = caller;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
