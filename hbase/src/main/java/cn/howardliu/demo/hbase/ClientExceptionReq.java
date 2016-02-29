package cn.howardliu.demo.hbase;

import java.io.Serializable;

public class ClientExceptionReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String errId;    //生成规则,errLevel+flag+sysCode+busiCode+errCode+sysErrCode+timeStamp			(00-000-000-000-0-0-yyyyMMddHHmmssSSS)
    private String sysCode;
    private String busiCode;
    private String busiDesc;
    private String errCode;
    private String errDesc;
    private String sysErrCode;
    private String sysErrDesc;
    private String throwableDesc;
    private String createDate;
    private String errLevel;
    private String flag;
    private String processStatus;
    private Throwable e;

    public String getErrId() {
        return this.errId;
    }

    public void setErrId(String errId) {
        this.errId = errId;
    }

    public String getSysCode() {
        return this.sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getBusiCode() {
        return this.busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    public String getBusiDesc() {
        return this.busiDesc;
    }

    public void setBusiDesc(String busiDesc) {
        this.busiDesc = busiDesc;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrDesc() {
        return this.errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    public String getThrowableDesc() {
        return this.throwableDesc;
    }

    public void setThrowableDesc(String throwableDesc) {
        this.throwableDesc = throwableDesc;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSysErrCode() {
        return this.sysErrCode;
    }

    public void setSysErrCode(String sysErrCode) {
        this.sysErrCode = sysErrCode;
    }

    public String getSysErrDesc() {
        return this.sysErrDesc;
    }

    public void setSysErrDesc(String sysErrDesc) {
        this.sysErrDesc = sysErrDesc;
    }

    public String getErrLevel() {
        return this.errLevel;
    }

    public void setErrLevel(String errLevel) {
        this.errLevel = errLevel;
    }

    public String getFlag() {
        if (this.flag == null || this.flag.isEmpty()) {
            return "0";
        }
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getProcessStatus() {
        if (this.processStatus == null || this.processStatus.isEmpty()) {
            return "0";
        }
        return this.processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String toString() {
        return "ClientExceptionReq [errId=" + this.errId + ", sysCode=" + this.sysCode + ", busiCode=" + this.busiCode + ", busiDesc=" + this.busiDesc + ", errCode=" + this.errCode + ", errDesc="
                + this.errDesc + ", sysErrCode=" + this.sysErrCode + ", sysErrDesc=" + this.sysErrDesc + ", throwableDesc=" + this.throwableDesc + ", createDate=" + this.createDate + ", errLevel="
                + this.errLevel + ", flag=" + this.flag + ", processStatus=" + this.processStatus + "]";
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }
}