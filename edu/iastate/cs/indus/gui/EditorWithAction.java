package edu.iastate.cs.indus.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import edu.iastate.cs.indus.gui.action.HelpAboutAction;
import edu.iastate.cs.indus.gui.action.OntologyCloseAction;
import edu.iastate.cs.indus.gui.action.OntologyExitAction;
import edu.iastate.cs.indus.gui.action.OntologyNewAction;
import edu.iastate.cs.indus.gui.action.OntologyOpenAction;
import edu.iastate.cs.indus.gui.action.OntologySaveAction;
import edu.iastate.cs.indus.gui.action.OntologySaveAsAction;
import edu.iastate.cs.indus.gui.action.SetupOWLFormatAction;
import edu.iastate.cs.utils.Debug;

/**
 * Description: Actions handlers

 * @author Jie Bao
 * @version 1.0
 */

abstract public class EditorWithAction
    extends EditorGUI
{
    JMenu jMenuOntology = new JMenu("Ontology");
    JMenu jMenuHelp = new JMenu("Help");
    JMenu jMenuSetup = new JMenu("Setup");

    public EditorWithAction()
    {
        super();

        // menu
        mainMenu = createBasicMenu();
        mainFrame.setJMenuBar(mainMenu);
        mainFrame.validate();

        // exit
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        final EditorWithAction parent = this;
        mainFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                OntologyExitAction exitAction = new OntologyExitAction(parent);
                exitAction.doExit();
            }
        });

    }

    /**
     * Add a menu item
     *
     * @param text String
     * @param icon Icon
     * @param listener ActionListener
     * @param menu JMenu
     * @param shortKey String example "ctrl N" "typed a" "INSERT" "alt shift released X"
     *
     * @author Jie Bao
     * @version 2004-04-26
     */
    private void addMenuItem(String text, Icon icon, ActionListener listener,
                             JMenu menu, String shortKey)
    {
        JMenuItem jMenuItem = (icon == null) ?
            new JMenuItem(text) : new JMenuItem(text, icon);
        jMenuItem.addActionListener(listener);
        if (shortKey != null)
        {
            jMenuItem.setAccelerator(KeyStroke.getKeyStroke(shortKey));
        }
        menu.add(jMenuItem);
    }

    public JMenuBar createBasicMenu()
    {
        clearAll();

        // ============ Ontology Menu ==============

        // ontology - new
        addMenuItem("New Ontology...", null, new OntologyNewAction(this),
                    jMenuOntology, "ctrl N");
        // ontology - open
        addMenuItem("Open Ontology...", null, new OntologyOpenAction(this),
                    jMenuOntology, "ctrl O");
        // ontology - exit
        addMenuItem("Exit", null, new OntologyExitAction(this),
                    jMenuOntology, "ctrl X");

        mainMenu.add(jMenuOntology);

        // ============ Help Menu ==============

        // help - about
        addMenuItem("About", null, new HelpAboutAction(this),
                    jMenuHelp, null);
        mainMenu.add(jMenuHelp);

        return mainMenu;
    }

    private void clearAll()
    {
        mainMenu.removeAll();
        jMenuOntology.removeAll();
        jMenuHelp.removeAll();
        jMenuSetup.removeAll();
    }

    public JMenuBar createMenuBar()
    {
        clearAll();

        // ============ Ontology Menu ==============

        // ontology - new
        addMenuItem("New Ontology ...", null, new OntologyNewAction(this),
                    jMenuOntology, "ctrl N");

        // ontology - open
        addMenuItem("Open Ontology ...", null, new OntologyOpenAction(this),
                    jMenuOntology, "ctrl O");

        // ontology - save
        addMenuItem("Save Ontology ...", null, new OntologySaveAction(this),
                    jMenuOntology, "ctrl S");

        // ontology - saveAs
        addMenuItem("Save Ontology As ...", null, new OntologySaveAsAction(this),
                    jMenuOntology, "alt ctrl S");

        // ontology - close
        addMenuItem("Close", null, new OntologyCloseAction(this),
                    jMenuOntology, "ctrl C");

        jMenuOntology.addSeparator();

        // ontology - exit
        addMenuItem("Exit", null, new OntologyExitAction(this),
                    jMenuOntology, "ctrl X");

        mainMenu.add(jMenuOntology);

        // ============ Setup Menu ==============

        // setup - owl
        addMenuItem("OWL format", null, new SetupOWLFormatAction(this),
                    jMenuSetup, null);

        mainMenu.add(jMenuSetup);

        // ============ Example Menu ==============

        mainMenu.add(createExampleMenu());

        // ============ Help Menu ==============

        // help - about
        addMenuItem("About", null, new HelpAboutAction(this),
                    jMenuHelp, null);
        mainMenu.add(jMenuHelp);

        return mainMenu;
    }

    public JMenu createExampleMenu()
    {
        // example

        JMenu jMenuExample = new JMenu();
        jMenuExample.setText("Example");

        ActionListener a = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Debug.trace("To show a example");
            }
        };
        addMenuItem("Nothing yet ", null, a,
                    jMenuExample, null);

        return jMenuExample;

    }

}
