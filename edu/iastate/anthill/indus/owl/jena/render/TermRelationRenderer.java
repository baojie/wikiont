package edu.iastate.anthill.indus.owl.jena.render;

import com.hp.hpl.jena.ontology.OntClass ;
import com.hp.hpl.jena.ontology.OntProperty ;
import com.hp.hpl.jena.util.iterator.ExtendedIterator ;

public class TermRelationRenderer
    extends OntologyConstants
{
    public TermRelationRenderer(String onturi)
    {
        super(onturi);
    }

    public String getSubClassHierarchy(OntClass subCla, int treeLevel)
    {

        // get subclass tree (ignore restrictions in the hierarchy)

        String subClaTree = "";
        String prefix = "";
        for (int i = 0; i < treeLevel; i++)
        {
            prefix += ".";

        }
        ExtendedIterator iter = subCla.listSubClasses(true);
        while (iter.hasNext())
        {

            subCla = (OntClass) iter.next();

            if (subCla.isAnon())
            {
                continue; // if subclass has restriction, don't display
            }

            subClaTree += NEWLINE + prefix + term2Link(subCla);

            if (subCla.listSubClasses() != null)
            {
                subClaTree += getSubClassHierarchy(subCla, treeLevel + 1);
            }
        }

        return subClaTree;
    }

    public String getSuperClassHierarchy(OntClass supCla, int treeLevel)
    {

        // get superclass tree (ignore restrictions in the hierarchy)

        String supClaTree = "";
        String prefix = "";
        for (int i = 0; i < treeLevel; i++)
        {
            prefix += ".";

        }
        ExtendedIterator iter = supCla.listSuperClasses(true);
        while (iter.hasNext())
        {

            supCla = (OntClass) iter.next();

            if (supCla.isAnon())
            {
                continue; // if superclass has restriction, don't display
            }

            supClaTree += NEWLINE + prefix + term2Link(supCla);

            if (supCla.listSuperClasses() != null)
            {
                supClaTree += getSuperClassHierarchy(supCla, treeLevel + 1);
            }
        }

        return supClaTree;
    }

    public String getSuperPropertyHierarchy(OntProperty prop, int treeLevel)
    {

        // get supproperty tree (ignore restrictions in the hierarchy)

        String supPropTree = "";
        String prefix = "";
        for (int i = 0; i < treeLevel; i++)
        {
            prefix += ".";

        }
        ExtendedIterator iter = prop.listSuperProperties(true);
        while (iter.hasNext())
        {

            OntProperty supProp = (OntProperty) iter.next();

            // skip if same property appears
            if (supProp.getURI() == prop.getURI())
            {
                continue;
            }

            supPropTree += NEWLINE + prefix + term2Link(supProp);

            if (supProp.listSuperProperties() != null)
            {
                supPropTree += getSuperPropertyHierarchy(supProp, treeLevel + 1);
            }
        }

        return supPropTree;
    }

    public String getSubPropertyHierarchy(OntProperty prop, int treeLevel)
    {

        // get subproperty tree (ignore restrictions in the hierarchy)

        String subPropTree = "";
        String prefix = "";
        for (int i = 0; i < treeLevel; i++)
        {
            prefix += ".";

        }
        ExtendedIterator iter = prop.listSubProperties(true);
        while (iter.hasNext())
        {

            OntProperty subProp = (OntProperty) iter.next();

            // skip if same property appears
            if (subProp.getURI() == prop.getURI())
            {
                continue;
            }

            subPropTree += NEWLINE + prefix + term2Link(subProp);

            if (subProp.listSubProperties() != null)
            {
                subPropTree += getSubPropertyHierarchy(subProp, treeLevel + 1);
            }
        }

        return subPropTree;
    }

}
