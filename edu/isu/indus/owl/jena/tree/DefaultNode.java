package edu.isu.indus.owl.jena.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.*;

public class DefaultNode
    extends DefaultMutableTreeNode
{
    public DefaultNode(Resource obj)
    {
        super(obj);
    }

    public DefaultNode(String url)
    {
        Model model = ModelFactory.createDefaultModel();
        Resource res = model.createResource(
            url);
        this.setUserObject(res);
    }


    /**
     *
     * @param depth int  - 0 for root
     * @return String
     */
    public String print(int depth)
    {
        String Leading = "    ";
        String toPrint = new String();
        for (int i = 0; i < depth; i++)
        {
            toPrint += Leading;
        }
        toPrint += ( (Resource)this.getUserObject()).getLocalName();
        return toPrint;
    }
}
