package edu.iastate.anthill.wikiont;

import java.io.File;

import edu.iastate.anthill.indus.owl.jena.JenaModel;

/**
 * Create wiki pages from jena model
 *
 * @author Jie Bao
 * @since 1.0 2004-07-02
 */
public class WikiPageFactory
{

    WikiPage page;

    public WikiPageFactory(JenaModel model, String localPrefix)
    {
        page = new WikiPage(model, localPrefix);
    }

    public static void main(String[] args)
    {
        String dir = "C:\\baojie-outer\\semanticwww\\wiki\\";
        try
        {
            JenaModel model = new JenaModel(new File("camera.owl"));

            WikiPageFactory app = new WikiPageFactory(model, "camera");
            app.page.savePages(dir);

        }
        catch (Exception ex)
        {
            System.out.println(
                "Exception when loading ontology, may caused by unaccesible importing");
            ex.printStackTrace();
        }

    }
}
