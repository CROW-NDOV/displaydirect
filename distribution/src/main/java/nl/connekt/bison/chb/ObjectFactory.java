
package nl.connekt.bison.chb;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the nl.connekt.bison.chb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: nl.connekt.bison.chb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Export }
     * 
     */
    public Export createExport() {
        return new Export();
    }

    /**
     * Create an instance of {@link Export.Quays }
     * 
     */
    public Export.Quays createExportQuays() {
        return new Export.Quays();
    }

    /**
     * Create an instance of {@link Export.Quays.Quay }
     * 
     */
    public Export.Quays.Quay createExportQuaysQuay() {
        return new Export.Quays.Quay();
    }

    /**
     * Create an instance of {@link Export.Quays.Quay.Userstopcodes }
     * 
     */
    public Export.Quays.Quay.Userstopcodes createExportQuaysQuayUserstopcodes() {
        return new Export.Quays.Quay.Userstopcodes();
    }

    /**
     * Create an instance of {@link Export.Quays.Quay.Userstopcodes.Userstopcodedata }
     * 
     */
    public Export.Quays.Quay.Userstopcodes.Userstopcodedata createExportQuaysQuayUserstopcodesUserstopcodedata() {
        return new Export.Quays.Quay.Userstopcodes.Userstopcodedata();
    }

}
