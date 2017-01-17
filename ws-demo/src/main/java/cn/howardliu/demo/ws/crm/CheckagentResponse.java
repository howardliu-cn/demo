
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
 *         &lt;element name="checkagentResult" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="rtn" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="errormsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastdate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lasttoken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "checkagentResult",
    "rtn",
    "errormsg",
    "lastdate",
    "lasttoken"
})
@XmlRootElement(name = "checkagentResponse")
public class CheckagentResponse {

    protected short checkagentResult;
    protected short rtn;
    protected String errormsg;
    protected String lastdate;
    protected String lasttoken;

    /**
     * 获取checkagentResult属性的值。
     * 
     */
    public short getCheckagentResult() {
        return checkagentResult;
    }

    /**
     * 设置checkagentResult属性的值。
     * 
     */
    public void setCheckagentResult(short value) {
        this.checkagentResult = value;
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
     * 获取lastdate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastdate() {
        return lastdate;
    }

    /**
     * 设置lastdate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastdate(String value) {
        this.lastdate = value;
    }

    /**
     * 获取lasttoken属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLasttoken() {
        return lasttoken;
    }

    /**
     * 设置lasttoken属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLasttoken(String value) {
        this.lasttoken = value;
    }

}
