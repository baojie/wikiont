package edu.iastate.anthill.indus.gui ;

import java.awt.Dimension ;
import java.awt.Toolkit ;
import javax.swing.JFrame ;
import javax.swing.JMenuBar ;
import javax.swing.JSplitPane ;
import javax.swing.JTabbedPane ;

import edu.iastate.anthill.indus.IndusConstants ;
import edu.iastate.anthill.indus.gui.panel.browser.BrowserPanel ;
import edu.iastate.anthill.indus.gui.panel.owl.OwlPanel ;
import edu.iastate.anthill.indus.gui.panel.tree.PackageTreePanel ;
import edu.iastate.anthill.indus.owl.node.DefaultOntologyNode ;
import edu.iastate.anthill.indus.gui.panel.tree.SubclassTreePanel ;
import javax.swing.UIManager;
import javax.swing.*;
import edu.iastate.anthill.wikiont.*;




/**
 * <p>Title: </p>
 * <p>Description: GUI of the project</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Jie Bao
 * @version 1.0 2004-04-20
 */

abstract public class EditorGUI
        extends EditorBasis
{
    public JMenuBar mainMenu = new JMenuBar () ;

    public JFrame mainFrame ;

    JTabbedPane tabPanel = new JTabbedPane () ;

    JSplitPane jPanelVertical ;

    JSplitPane jPanelHorizontal ;

    protected PackageTreePanel treePanel = null ;

    protected SubclassTreePanel subclassPanel = null ;

    protected OwlPanel owlPanel = null ;

    protected BrowserPanel browserPanel = null ;

    private static final String BASE_TITLE = "INDUS Ontology Editor" ;

    public EditorGUI ()
    {
        super () ;
        try
        {
            UIManager.setLookAndFeel (
                    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" ) ;
        }
        catch ( Exception ex )
        {
        }

        mainFrame = new JFrame () ;
        mainFrame.setTitle ( BASE_TITLE ) ;

        Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize () ;
        Dimension frameSize = screenSize ;
        frameSize.height *= 0.9 ;
        frameSize.width *= 0.9 ;

        mainFrame.setSize ( frameSize ) ;
        mainFrame.setVisible ( true ) ;

        mainFrame.show () ;
    }




    /**
     * createPanels
     */
    public void createPanels ()
    {
        treePanel = new PackageTreePanel ( this ) ;
        subclassPanel = new SubclassTreePanel ( this ) ;
        owlPanel = new OwlPanel ( this ) ;
        browserPanel = new BrowserPanel ( this ) ;
        jbInit () ;
    }




    /**
     * clear the Panels
     */
    public void clearPanels ()
    {
        if ( tabPanel != null )
        {
            tabPanel.removeAll () ;
        }
        if ( jPanelHorizontal != null )
        {
            jPanelHorizontal.removeAll () ;
        }
    }

    protected void jbInit ()
    {
        tabPanel.removeAll () ;

        // trees
        tabPanel.addTab ( "Package View" , IndusConstants.iconTree , treePanel ) ;
        tabPanel.addTab ( "Subclass View" , IndusConstants.iconTree ,
                          subclassPanel ) ;

// whole panel

        jPanelVertical = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ,
                                          owlPanel ,
                                          browserPanel ) ;
        jPanelVertical.setContinuousLayout ( false ) ;
        jPanelVertical.setOneTouchExpandable ( true ) ;

        jPanelHorizontal = new JSplitPane ( JSplitPane.HORIZONTAL_SPLIT ,
                                            tabPanel ,
                                            jPanelVertical ) ;
        jPanelHorizontal.setContinuousLayout ( false ) ;
        jPanelHorizontal.setOneTouchExpandable ( true ) ;

        mainFrame.setContentPane ( jPanelHorizontal ) ;

        Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize () ;
        Dimension frameSize = screenSize ;
        frameSize.height *= 0.9 ;
        frameSize.width *= 0.9 ;

        mainFrame.setSize ( frameSize ) ;
        mainFrame.setVisible ( true ) ;

        jPanelVertical.setDividerLocation ( 0.5 ) ;
        jPanelHorizontal.setDividerLocation ( 0.3 ) ;

//        mainFrame.getContentPane().add(jPanelHorizontal, null);
    }




    /**
     * @author Jie Bao
     * @version 2004-04-22
     */
    public void UpdateOWLPane ()
    {
        // input: ontology model
        // output: the OWL text in the right panel
        owlPanel.updatePreview ( ontologyModel.getOWLOntology () ) ;
    }


    // 2004-04-22
    public void UpdateBrowserPane ( DefaultOntologyNode node )
    {
        this.browserPanel.updatePreview ( node.getNodeInformation ( true ) ) ;
    }




    /**
     * Update mainframe title to currently editing file, or title given bu user
     *
     * @param newTitle String - Could be null, default is the currently editing file
     *
     * @author Jie Bao
     * @since  2004-05-01
     */
    public void UpdateTitle ( String newTitle )
    {
        if ( newTitle == null )
        {
            mainFrame.setTitle ( BASE_TITLE + " - " + currentdoc ) ;
        }
        else if ( newTitle.length () == 0 )
        {
            mainFrame.setTitle ( BASE_TITLE ) ;
        }
        else
        {
            mainFrame.setTitle ( BASE_TITLE + " - " + newTitle ) ;
        }

    }
}
