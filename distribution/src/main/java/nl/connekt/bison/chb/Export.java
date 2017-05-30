
package nl.connekt.bison.chb;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="quays">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="quay" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="quaycode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="userstopcodes">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="userstopcodedata" maxOccurs="unbounded" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="dataownercode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="userstopcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="validfrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "quays"
})
@XmlRootElement(name = "export")
public class Export {

    @XmlElement(required = true)
    protected Export.Quays quays;

    /**
     * Gets the value of the quays property.
     * 
     * @return
     *     possible object is
     *     {@link Export.Quays }
     *     
     */
    public Export.Quays getQuays() {
        return quays;
    }

    /**
     * Sets the value of the quays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Export.Quays }
     *     
     */
    public void setQuays(Export.Quays value) {
        this.quays = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="quay" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="quaycode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="userstopcodes">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="userstopcodedata" maxOccurs="unbounded" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="dataownercode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="userstopcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="validfrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "quay"
    })
    public static class Quays {

        protected List<Export.Quays.Quay> quay;

        /**
         * Gets the value of the quay property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the quay property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getQuay().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Export.Quays.Quay }
         * 
         * 
         */
        public List<Export.Quays.Quay> getQuay() {
            if (quay == null) {
                quay = new ArrayList<Export.Quays.Quay>();
            }
            return this.quay;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="quaycode" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="userstopcodes">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="userstopcodedata" maxOccurs="unbounded" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="dataownercode" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="userstopcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="validfrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            "quaycode",
            "userstopcodes"
        })
        public static class Quay {

            @XmlElement(required = true)
            protected String quaycode;
            @XmlElement(required = true)
            protected Export.Quays.Quay.Userstopcodes userstopcodes;

            /**
             * Gets the value of the quaycode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getQuaycode() {
                return quaycode;
            }

            /**
             * Sets the value of the quaycode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setQuaycode(String value) {
                this.quaycode = value;
            }

            /**
             * Gets the value of the userstopcodes property.
             * 
             * @return
             *     possible object is
             *     {@link Export.Quays.Quay.Userstopcodes }
             *     
             */
            public Export.Quays.Quay.Userstopcodes getUserstopcodes() {
                return userstopcodes;
            }

            /**
             * Sets the value of the userstopcodes property.
             * 
             * @param value
             *     allowed object is
             *     {@link Export.Quays.Quay.Userstopcodes }
             *     
             */
            public void setUserstopcodes(Export.Quays.Quay.Userstopcodes value) {
                this.userstopcodes = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="userstopcodedata" maxOccurs="unbounded" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="dataownercode" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="userstopcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="validfrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
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
                "userstopcodedata"
            })
            public static class Userstopcodes {

                protected List<Export.Quays.Quay.Userstopcodes.Userstopcodedata> userstopcodedata;

                /**
                 * Gets the value of the userstopcodedata property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the userstopcodedata property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getUserstopcodedata().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Export.Quays.Quay.Userstopcodes.Userstopcodedata }
                 * 
                 * 
                 */
                public List<Export.Quays.Quay.Userstopcodes.Userstopcodedata> getUserstopcodedata() {
                    if (userstopcodedata == null) {
                        userstopcodedata = new ArrayList<Export.Quays.Quay.Userstopcodes.Userstopcodedata>();
                    }
                    return this.userstopcodedata;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="dataownercode" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="userstopcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="validfrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
                    "dataownercode",
                    "userstopcode",
                    "validfrom"
                })
                public static class Userstopcodedata {

                    @XmlElement(required = true)
                    protected String dataownercode;
                    @XmlElement(required = true)
                    protected String userstopcode;
                    @XmlElement(required = true)
                    protected String validfrom;

                    /**
                     * Gets the value of the dataownercode property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDataownercode() {
                        return dataownercode;
                    }

                    /**
                     * Sets the value of the dataownercode property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDataownercode(String value) {
                        this.dataownercode = value;
                    }

                    /**
                     * Gets the value of the userstopcode property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getUserstopcode() {
                        return userstopcode;
                    }

                    /**
                     * Sets the value of the userstopcode property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setUserstopcode(String value) {
                        this.userstopcode = value;
                    }

                    /**
                     * Gets the value of the validfrom property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getValidfrom() {
                        return validfrom;
                    }

                    /**
                     * Sets the value of the validfrom property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setValidfrom(String value) {
                        this.validfrom = value;
                    }

                }

            }

        }

    }

}
