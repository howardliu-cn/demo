
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
 *         &lt;element name="testconnectuserResult" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="rtn" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "testconnectuserResult",
    "rtn",
    "errormsg"
})
@XmlRootElement(name = "testconnectuserResponse")
public class TestconnectuserResponse {

    protected short testconnectuserResult;
    protected int rtn;
    protected String errormsg;

    /**
     * 获取testconnectuserResult属性的值。
     * 
     */
    public short getTestconnectuserResult() {
        return testconnectuserResult;
    }

    /**
     * 设置testconnectuserResult属性的值。
     * 
     */
    public void setTestconnectuserResult(short value) {
        this.testconnectuserResult = value;
    }

    /**
     * 获取rtn属性的值。
     * 
     */
    public int getRtn() {
        return rtn;
    }

    /**
     * 设置rtn属性的值。
     * 
     */
    public void setRtn(int value) {
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
