package edu.iastate.cs.indus.owl.hierarchy;

import javax.swing.JTree;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JFrame;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

/**
 * Generic tree data structure
 *
 * @author Jie Bao
 * @since 2004-05-01
 */

public class Tree
    extends JTree
{
    DefaultMutableTreeNode top;

    public Tree()
    {
        super();
    }

    public Tree(DefaultMutableTreeNode top)
    {
        super(top);
        this.top = top;
    }

    public String toString()
    {
        return toString(false);
    }

    public String toString(boolean toHTML)
    {
        String blank = toHTML ? "&nbsp;&nbsp;&nbsp;&nbsp;" : "    ";
        String endline = "|---";
        String line = toHTML ? "|&nbsp;&nbsp;&nbsp;" : "|   ";
        String crlf = toHTML ? "<BR></BR>" : "\n";

        if (top == null)
        {
            return "";
        }
        String toPrint = "";

        Enumeration e = top.preorderEnumeration();
        while (e.hasMoreElements())
        {
            DefaultMutableTreeNode nn = (DefaultMutableTreeNode) e.nextElement();
            String leading = line;

            if (nn.getLevel() == 0)
            {
                leading = "";
            }
            else if (nn.getLevel() == 1)
            {
                leading = endline;
            }
            else
            {
                leading = endline;
                for (int i = 0; i < nn.getLevel() - 1; i++)
                {
                    leading = line + leading;
                }
            }
            toPrint += leading + nn + crlf;
        }

        if (toHTML)
        {
            return toPrint;
        }
        else
        {
            return toPrint;
        }
    }

    public void setTop(DefaultMutableTreeNode top)
    {
        this.top = top;
    }

    public DefaultMutableTreeNode getTop()
    {
        return top;
    }

    private void sampleTree()
    {
        top = new DefaultMutableTreeNode("http://semanticWWW.com/indus.owl#USA");

        DefaultMutableTreeNode iowa = new DefaultMutableTreeNode(
            "http://semanticWWW.com/indus.owl#Iowa");
        top.add(iowa);
        iowa.add(new DefaultMutableTreeNode("http://semanticWWW.com/indus.owl#Ames"));
        iowa.add(new DefaultMutableTreeNode("http://semanticWWW.com/indus.owl#DesMoines"));

        DefaultMutableTreeNode va = new DefaultMutableTreeNode(
            "http://semanticWWW.com/indus.owl#Virginia");
        top.add(va);
        va.add(new DefaultMutableTreeNode("http://semanticWWW.com/indus.owl#Richmond"));
        va.add(new DefaultMutableTreeNode("http://semanticWWW.com/indus.owl#Petersberg"));

    }

// for test purpose
    public static void main(String[] args)
    {
        Tree t = new Tree();
        t.sampleTree();

        JFrame frame = new JFrame();
        frame.setSize(800, 600);

        JEditorPane pane = new JEditorPane();
        frame.getContentPane().add(new JScrollPane(pane));

//b.listTriple();
        pane.setContentType("text/html");
        pane.setText(t.toString(true));

        frame.setVisible(true);

        // System.out.print(t);
    }
}
