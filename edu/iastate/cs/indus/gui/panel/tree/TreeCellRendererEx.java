package edu.iastate.cs.indus.gui.panel.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import edu.iastate.cs.indus.IndusConstants;
import edu.iastate.cs.indus.owl.node.DefaultOntologyNode;

public class TreeCellRendererEx
    extends JLabel
    implements TreeCellRenderer
{
    static protected Font defaultFont;
    static protected ImageIcon Icons[];

    /** Color to use for the background when selected. */
    static protected final Color SelectedBackgroundColor = Color.LIGHT_GRAY;

    static
    {
        try
        {
            defaultFont = new Font("SansSerif", 0, 12);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            Icons = new ImageIcon[8];

            Icons[DefaultOntologyNode.ROOT] = IndusConstants.iconRoot;
            Icons[DefaultOntologyNode.PACKAGE] = IndusConstants.iconPackage;

            Icons[DefaultOntologyNode.CLASS] = IndusConstants.iconClass;
            Icons[DefaultOntologyNode.PROPERTY] = IndusConstants.iconProperty;
            Icons[DefaultOntologyNode.INSTANCE] = IndusConstants.iconInstance;

            Icons[DefaultOntologyNode.ALL_CLASSES] = IndusConstants.
                iconAllClasses;
            Icons[DefaultOntologyNode.ALL_PROPERTIES] = IndusConstants.
                iconAllProperties;
            Icons[DefaultOntologyNode.ALL_INSTANCES] = IndusConstants.
                iconAllInstances;

        }
        catch (Exception e)
        {
            System.out.println("Couldn't load images: " + e);
        }
    }

    /** Whether or not the item that was last configured is selected. */
    protected boolean selected;

    /**
     * This is messaged from JTree whenever it needs to get the size
     * of the component or it wants to draw it.
     * This attempts to set the font based on value, which will be
     * a TreeNode.
     */

    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf, int row,
                                                  boolean hasFocus)
    {
        String stringValue = tree.convertValueToText(value, selected,
            expanded, leaf, row, hasFocus);
        // Set the text.
        setText(stringValue);

// Tooltips used by the tree.
        setToolTipText(stringValue);

// Set the image.
        setIcon(Icons[ ( (DefaultOntologyNode) value).getType()]);

        if (hasFocus)
        {
            setForeground(Color.blue);
        }
        else
        {
            setForeground(Color.black);
        }
        // Update the selected flag for the next paint.
//  }
        this.selected = selected;

        return this;
    }

    /**
      /**
      * paint is subclassed to draw the background correctly.  JLabel
      * currently does not allow backgrounds other than white, and it
      * will also fill behind the icon.  Something that isn't desirable.
      */
     public void paint(Graphics g)
     {
         Color bColor;
         Icon currentI = getIcon();

         if (selected)
         {
             bColor = SelectedBackgroundColor;
         }
         else if (getParent() != null)
         {

             /* Pick background color up from parent (which will come from
                the JTree we're contained in). */
             bColor = getParent().getBackground();
         }
         else
         {
             bColor = getBackground();
         }
         g.setColor(bColor);
         if (currentI != null && getText() != null)
         {
             int offset = (currentI.getIconWidth() + getIconTextGap());

             if (getComponentOrientation().isLeftToRight())
             {
                 g.fillRect(offset, 0, getWidth() - 1 - offset,
                            getHeight() - 1);
             }
             else
             {
                 g.fillRect(0, 0, getWidth() - 1 - offset,
                            getHeight() - 1);
             }
         }
         else
         {
             g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
         }
         super.paint(g);
     }
}
