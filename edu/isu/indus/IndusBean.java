package edu.isu.indus;

import edu.isu.indus.owl.Hierarchy;

/**
 * The INDUS ontology editor main interface
 *
 * @author Jie Bao
 * @version 1.0
 */
public class IndusBean
{
    private String className = "";
    private Hierarchy ontology = new Hierarchy();

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String newValue)
    {
        if (newValue != null)
        {
            className = toChinese(newValue);
            ontology.add(className);
        }
    }

    // for internalization
    public static String toChinese(String strvalue)
    {
        try
        {
            if (strvalue == null)
            {
                return null;
            }
            else
            {
                strvalue = new String(strvalue.getBytes("ISO8859_1"), "GBK");
                return strvalue;
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Hierarchy getOntology()
    {
        return ontology;
    }
}
