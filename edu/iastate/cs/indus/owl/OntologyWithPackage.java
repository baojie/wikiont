package edu.iastate.cs.indus.owl;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import edu.iastate.cs.indus.owl.jena.JenaModel;
import java.io.File;

/**
 * add package and domain uitlitiies to the jena model
 *
 * @author Jie Bao
 * @version 2004-04-23
 */
public class OntologyWithPackage
    extends JenaModel
{
    // system classes
    public static OntClass SYSTEM_CLASS;
    public static OntClass PACKAGE;
    public static Individual SYSTEM_GLOBAL_PKG;
    public static ObjectProperty IN_PACKAGE;

    public OntologyWithPackage(String uri, String nsPrefix)
    {
        super(uri, nsPrefix);
        initialize();
    }

    public OntologyWithPackage(File file)
    {
        super(file);
        initialize();
    }

    /**
     * setup system classes and properties
     *
     * @author Jie Bao
     * @version 2004-04-27
     */
    public void initialize()
    {
        SYSTEM_CLASS = addClass("SYSTEM_");
        PACKAGE = addClass("SYSTEM_PACKAGE");
        PACKAGE.addSuperClass(SYSTEM_CLASS);

        SYSTEM_GLOBAL_PKG = this.addInstance("SYSTEM_GLOBAL_PKG", PACKAGE);

        // clss_in_package
        IN_PACKAGE = model.createObjectProperty(ontURI +
                                                "SYSTEM_IN_PACKAGE");
        IN_PACKAGE.setRange(PACKAGE);
        IN_PACKAGE.setDomain(com.hp.hpl.jena.vocabulary.RDFS.Resource);
    }

}
