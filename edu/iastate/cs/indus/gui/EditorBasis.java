package edu.iastate.cs.indus.gui;

import java.io.File;

import com.hp.hpl.jena.shared.JenaException;

import edu.iastate.cs.indus.owl.OntologyWithPackage;

/**
 * <p>Title: </p>
 * <p>Description: Class to handle GUI insensitive information</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Jie Bao
 * @version 2004-04-20
 */
abstract public class EditorBasis
{
    protected OntologyWithPackage ontologyModel = null; // ontology model;
    protected File currentdoc = null; // file to save the model

    /**
     * Save current ontology model
     *
     * @param newFile File
     * @throws JenaException
     *
     * @since 2004-05-01
     */
    public void saveOntology(File newFile) throws JenaException
    {
        if (ontologyModel == null)
        {
            throw new JenaException("model is null");
        }
        if (newFile == null)
        {
            throw new JenaException("new file name is null");
        }

        if (currentdoc == null)
        {
            currentdoc = newFile;
        }
        if (ontologyModel.isModified() ||
            currentdoc.getPath().compareTo(newFile.getPath()) != 0)
        {
            ontologyModel.writeOntology(newFile);
        }
        currentdoc = newFile;
    }

    public void saveOntology()
    {
        if (ifEverSaved())
        {
            saveOntology(currentdoc);
        }
    }

    /**
     * query if the ontology is save
     *
     * @return boolean, true if saved, false if not ever saved
     * @since 2004-05-01
     */
    public boolean ifEverSaved()
    {
        return currentdoc != null;
    }

    /**
     * if the document need to be saved
     * @return boolean
     * 
     * @since 2004-05-01
     */
    public boolean ifNeedSave()
    {
        if (ontologyModel == null)
        {
            return false;
        }
        else
        {
            return ontologyModel.isModified();
        }
    }

    /**
     * close the ontology
     * @since 2004-05-01
     */
    public void closeOntology()
    {
        ontologyModel = null;
        currentdoc = null;
    }

    /**
     *
     * @param theFile File
     * @since 2004-05-01
     */
    public void openOntology(File theFile)
    {
        ontologyModel = new OntologyWithPackage(theFile);
        currentdoc = theFile;
    }

    public void newOntology(String baseuri, String prefix)
    {
        ontologyModel = new OntologyWithPackage(
            baseuri, prefix);
        currentdoc = null;
    }

    public OntologyWithPackage getOntologyModel()
    {
        return ontologyModel;
    }
}
