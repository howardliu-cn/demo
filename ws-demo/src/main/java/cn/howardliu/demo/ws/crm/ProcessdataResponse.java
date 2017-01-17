
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
 *         &lt;element name="processdataResult" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="outputpara" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "processdataResult",
    "outputpara",
    "rtn",
    "errormsg"
})
@XmlRootElement(name = "processdataResponse")
public class ProcessdataResponse {

    protected short processdataResult;
    protected String outputpara;
    protected int rtn;
    protected String errormsg;

    /**
     * 获取processdataResult属性的值。
     * 
     */
    public short getProcessdataResult() {
        return processdataResult;
    }

    /**
     * 设置processdataResult属性的值。
     * 
     */
    public void setProcessdataResult(short value) {
        this.processdataResult = value;
    }

    /**
     * 获取outputpara属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutputpara() {
        return outputpara;
    }

    /**
     * 设置outputpara属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutputpara(String value) {
        this.outputpara = value;
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
