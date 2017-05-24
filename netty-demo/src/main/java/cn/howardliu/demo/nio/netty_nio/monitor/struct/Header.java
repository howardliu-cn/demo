package cn.howardliu.demo.nio.netty_nio.monitor.struct;

import java.io.Serializable;
import java.util.UUID;

/**
 * <br>created at 17-5-11
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class Header implements Serializable {
    private int crcCode = 0x001;
    private String tag = UUID.randomUUID().toString();
    private int length;
    private byte type;

    public int getCrcCode() {
        return crcCode;
    }

    public Header setCrcCode(int crcCode) {
        this.crcCode = crcCode;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public Header setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public int getLength() {
        return length;
    }

    public Header setLength(int length) {
        this.length = length;
        return this;
    }

    public byte getType() {
        return type;
    }

    public Header setType(byte type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", type=" + type +
                '}';
    }
}
