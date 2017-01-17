
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
 *         &lt;element name="sendagentlogResult" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="rtn" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="errormsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "sendagentlogResult",
    "rtn",
    "errormsg"
})
@XmlRootElement(name = "sendagentlogResponse")
public class SendagentlogResponse {

    protected short sendagentlogResult;
    protected short rtn;
    protected String errormsg;

    /**
     * 获取sendagentlogResult属性的值。
     * 
     */
    public short getSendagentlogResult() {
        return sendagentlogResult;
    }

    /**
     * 设置sendagentlogResult属性的值。
     * 
     */
    public void setSendagentlogResult(short value) {
        this.sendagentlogResult = value;
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

}
