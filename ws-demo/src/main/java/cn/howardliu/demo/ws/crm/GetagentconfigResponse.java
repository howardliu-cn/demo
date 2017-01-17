
package cn.howardliu.demo.ws.crm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getagentconfigResult" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="rtn" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="errormsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="configinfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getagentconfigResult",
    "rtn",
    "errormsg",
    "configinfo"
})
@XmlRootElement(name = "getagentconfigResponse")
public class GetagentconfigResponse {

    protected short getagentconfigResult;
    protected short rtn;
    protected String errormsg;
    protected String configinfo;

    /**
     * 获取getagentconfigResult属性的值。
     * 
     */
    public short getGetagentconfigResult() {
        return getagentconfigResult;
    }

    /**
     * 设置getagentconfigResult属性的值。
     * 
     */
    public void setGetagentconfigResult(short value) {
        this.getagentconfigResult = value;
    }

    /**
     * 获取rtn属性的值。
     * 
     */
    public short getRtn() {
        return rtn;
    }

    /**
     * 设置rtn属性的值。
     * 
     */
    public void setRtn(short value) {
        this.rtn = value;
    }

    /**
     * 获取errormsg属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrormsg() {
        return errormsg;
    }

    /**
     * 设置errormsg属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrormsg(String value) {
        this.errormsg = value;
    }

    /**
     * 获取configinfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfiginfo() {
        return configinfo;
    }

    /**
     * 设置configinfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfiginfo(String value) {
        this.configinfo = value;
    }

}
