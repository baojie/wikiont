package edu.iastate.cs.indus.owl.jena.test;

import java.io.File;

import com.hp.hpl.jena.ontology.OntClass;

import edu.iastate.cs.indus.owl.jena.JenaModel;

/**
 * @author Jie Bao
 * @since 2004-05-01
 */
public class TestJena
{
    static JenaModel testModel;

    /**
     * Create an Example ontology
     */
    void makeSampleModel()
    {
        testModel = new JenaModel("http://somewhere/", "Somewhere");
        //t.sampleModel();
        OntClass baojie = testModel.addClass("BaoJie");
        OntClass human = testModel.addClass("Human");
        baojie.addSuperClass(human);

    }

    void readSampleModel(String path)
    {
        testModel = new JenaModel(new File(path));
    }

    // for test purpose
    public static void main(String args[])
    {
        try
        {
            TestJena test = new TestJena();
            test.readSampleModel("C:\\111.owl");
            //System.out.println(t.listTriple(true));
            testModel.writeOntology(System.out);
            System.out.println("uri = " + testModel.getOntURI());
            System.out.println("prefix = " + testModel.getNsPrefix());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
