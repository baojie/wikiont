package edu.iastate.anthill.indus.owl;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import edu.iastate.anthill.indus.owl.node.DefaultOntologyNode;

/**
 * @author Jie Bao
 * @since 2004-04-29
 */

public class ResourceWithPackage
{
    static String PACKAGE_RES_CONNECTOR = "_";
    static String PACKAGE_PACKAGE_CONNECTOR = "-";

    private OntResource res;
    short type;
    OntologyWithPackage model;

    /**
     *
     * @param packageName String
     * @param inPackageName String
     * @param isPackage boolean
     * @return String   : if package name is given, return a  concatenated name
     * with package name and in-package name; if not, return just the in-package
     * name
     */
    String makeJenaLocalName(String packageName, String inPackageName,
                             boolean isPackage)
    {

        if ( (packageName == null) ||
            (packageName.compareToIgnoreCase(
            OntologyWithPackage.SYSTEM_GLOBAL_PKG.getLocalName()) == 0))
        {
            return inPackageName;
        }
        else
        {
            return packageName +
                (isPackage ? PACKAGE_PACKAGE_CONNECTOR : PACKAGE_RES_CONNECTOR)
                + inPackageName;
        }
    }

    /**
     * Create form name and add it in the model
     *
     * @param inPackageName String
     * @param type short
     * @param pkg Individual
     * @param model OntologyWithPackage
     * @param instanceOf OntClass - be used when type is INSTANCE, otherwise discarded
     */
    public ResourceWithPackage(String inPackageName,
                               short type,
                               Individual fatherPackage,
                               OntologyWithPackage model,
                               OntClass instanceOf)
    {
        this.type = type;
        this.model = model;

        String pkgName = null;
        if (fatherPackage != null)
        {
            pkgName = fatherPackage.getLocalName();
        }
        String jenaLocalName = makeJenaLocalName(pkgName, inPackageName,
                                                 (type ==
                                                  DefaultOntologyNode.PACKAGE));

        switch (type)
        {
            case DefaultOntologyNode.CLASS:
                res = model.addClass(jenaLocalName);
                break;
            case DefaultOntologyNode.PACKAGE:
                res = model.addInstance(jenaLocalName,
                                        OntologyWithPackage.PACKAGE);
                break;
            case DefaultOntologyNode.PROPERTY:
                res = model.addProperty(jenaLocalName);
                break;
            case DefaultOntologyNode.INSTANCE:
                res = model.addInstance(jenaLocalName, instanceOf);
                break;
            case DefaultOntologyNode.ROOT:
                res = model.createOntology(inPackageName);
                return; // ontology node is not in any package
        }

        if (fatherPackage == null)
        {
            res.addProperty(model.IN_PACKAGE,
                            OntologyWithPackage.SYSTEM_GLOBAL_PKG);
        }
        else
        {
            res.addProperty(model.IN_PACKAGE, fatherPackage);
        }

        //       Debug.trace(res.getURI());
    }

    public ResourceWithPackage(OntResource res, short type,
                               OntologyWithPackage model)
    {
        this.type = type;
        this.model = model;
        this.res = res;
    }

    public OntResource getRes()
    {
        return res;
    }

    public String getPackageName()
    {
        // query about the package name
        Statement s = res.getProperty(OntologyWithPackage.IN_PACKAGE);

        // not in any package
        if (s == null)
        {
            return null;
        }

        RDFNode object = s.getObject();

        if (object instanceof Resource)
        {
            return ( (Resource) object).getLocalName();
        }
        else
        {
            // object is a literal
            return object.toString();
        }

    }

    /**
     * inPackageName = JenamLocalName - PackageName
     *
     * @return String
     * @since 2004-04-30
     */
    public String getInPackageName()
    {
        String jenaName = getJenaLocalName();
        if (type < DefaultOntologyNode.CLASS)
        {
            return jenaName;
        }

        try
        {
            String splitter = (type == DefaultOntologyNode.PACKAGE) ? // package?
                PACKAGE_PACKAGE_CONNECTOR : PACKAGE_RES_CONNECTOR;

            //String packgName = getPackageName();
            String name[] = jenaName.split(splitter);
            if (name.length > 1)
            {
                return name[1];
            }
            else
            {
                return jenaName;
            }
        }
        catch (Exception ex)
        {
            System.out.println("Exception with " + jenaName);
            ex.printStackTrace();
            return jenaName;
        }

    }

    public String getJenaLocalName()
    {
        if (res != null)
        {
            return res.getLocalName();
        }
        else
        {
            // has no corresponding resrource in jena model
            switch (type)
            {
                case DefaultOntologyNode.ALL_CLASSES:
                    return "Classes";
                case DefaultOntologyNode.ALL_PROPERTIES:
                    return "Properties";
                case DefaultOntologyNode.ALL_INSTANCES:
                    return "Instances";
                default:
                    return "NULL";
            }
        }
    }

    /**
     * Rename the resource
     * @param newInPackageName String
     * @param isPackage boolean
     *
     * @author Jie Bao
     * @since 2004-04-30
     */
    public void rename(String newInPackageName, boolean isPackage)
    {
        String newName = makeJenaLocalName(getPackageName(), newInPackageName,
                                           isPackage);
        res = model.renameResource(res, newName);
    }

    /**
     * return in-package name
     *
     * @author Jie Bao
     * @since 2004-04-30
     */
    public String toString()
    {
        if (type == DefaultOntologyNode.ROOT)
        {
            // the res is an Ontology object
            // return the url
            return res.getURI();
        }
        else
        {
            return getInPackageName();
        }
    }

    public short getType()
    {
        return type;
    }

}
