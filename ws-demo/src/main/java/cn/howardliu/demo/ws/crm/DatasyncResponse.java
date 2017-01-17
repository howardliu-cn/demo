
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
 *         &lt;element name="datasyncResult" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="access" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="msg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "datasyncResult",
    "access",
    "msg"
})
@XmlRootElement(name = "datasyncResponse")
public class DatasyncResponse {

    protected short datasyncResult;
    protected boolean access;
    protected String msg;

    /**
     * 获取datasyncResult属性的值。
     * 
     */
    public short getDatasyncResult() {
        return datasyncResult;
    }

    /**
     * 设置datasyncResult属性的值。
     * 
     */
    public void setDatasyncResult(short value) {
        this.datasyncResult = value;
    }

    /**
     * 获取access属性的值。
     * 
     */
    public boolean isAccess() {
        return access;
    }

    /**
     * 设置access属性的值。
     * 
     */
    public void setAccess(boolean value) {
        this.access = value;
    }

    /**
     * 获取msg属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置msg属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsg(String value) {
        this.msg = value;
    }

}
