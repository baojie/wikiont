package edu.isu.indus;

//Java core
//XML
//GUI
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import edu.iastate.cs.utils.Debug;
import edu.iastate.cs.utils.FileFilterEx;
import edu.isu.indus.gui.MainFrame;
import edu.isu.indus.gui.dlg.AppOptionsPanel;
import edu.isu.indus.gui.dlg.OptionsPanel;
import edu.isu.indus.gui.view.DocumentView;
import edu.isu.indus.xml.UnrecognizedDocTypeException;
import edu.isu.indus.xml.XMLDocument;
import edu.isu.indus.xml.XMLDocumentFactory;

public class XmlEditor
    {

// ���ÿ�ܵĿ�͸�
    private static final String DefaultDocument =
        "<?xml version='1.0' encoding='UTF-8'?>\n<default_element>default_node</default_element>";

    private static Vector XMLDocuments = new Vector();

    private static final ImageIcon jsXeIcon = new ImageIcon(XmlEditor.class.
        getResource("/edu/isu/indus/icons/icon.jpg"), "XEditor");

    private static OptionsPanel optionDlg;

    public static void main(String[] args) {
        //{{{ Check the java version
        String javaVersion = System.getProperty("java.version");
        if (javaVersion.compareTo("1.3") < 0) {
            System.err.println("ERROR: You are running Java version " +
                               javaVersion + ".");
            System.err.println("Java 1.3 or later is required.");
            System.exit(1);
        } //}}}

        MainFrame tabbedview = new MainFrame();

        //{{{ Parse command line arguments
        if (args.length >= 1) {

            if (!openXMLDocuments(tabbedview, args)) {
                try {
                    if (!openXMLDocument(tabbedview,
                                         XmlEditor.getDefaultDocument())) {
                        Debug.exitError(tabbedview,
                                        "Could not open default document.", 1);
                    }
                }
                catch (IOException ioe) {
                    Debug.exitError(tabbedview,
                                    "Could not open default document: " +
                                    ioe.toString(), 1);
                }
            }
        }
        else {
            try {
                if (!openXMLDocument(tabbedview, XmlEditor.getDefaultDocument())) {
                    Debug.exitError(tabbedview,
                                    "Could not open default document.", 1);
                }
            }
            catch (IOException ioe) {
                Debug.exitError(tabbedview,
                                "Could not open default document: " +
                                ioe.toString(), 1);
            }
        }
        //}}}

        tabbedview.show();
    } //}}}

    /**
     * Gets the default XML document in jsXe. This is necessary
     * as XML documents cannot be blank files.
     * @return jsXe's default XML document.
     */
    public static String getDefaultDocument() { //{{{
        return DefaultDocument;
    } //}}}

    // NEW FROM JSXE
    /**
     * Gets an array of the open XMLDocuments.
     * @return An array of XMLDocuments that jsXe currently has open.
     */
    public static XMLDocument[] getXMLDocuments() { //{{{
        XMLDocument[] documents = new XMLDocument[XMLDocuments.size()];
        for (int i = 0; i < XMLDocuments.size(); i++) {
            documents[i] = (XMLDocument) XMLDocuments.get(i);
        }
        return documents;
    } //}}}

    /**
         * Shows an open file dialog for jsXe. When a file is selected jsXe attempts
     * to open it.
     * @param view The view that is to be the parent of the file dialog
     * @return true if the file is selected and opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
    public static boolean showOpenFileDialog(MainFrame view) throws
        IOException { //{{{
        // if current file is null, defaults to home directory
        DocumentView docView = view.getDocumentView();
        XMLDocument doc = docView.getXMLDocument();
        File docFile = doc.getFile();
        JFileChooser loadDialog = new JFileChooser(docFile);
        //Add a filter to display only XML files
        FileFilterEx firstFilter = new FileFilterEx("xml", "XML Documents");
        loadDialog.addChoosableFileFilter(firstFilter);

        //The "All Files" file filter is added to the dialog
        //by default. Put it at the end of the list.
        javax.swing.filechooser.FileFilter all = loadDialog.
            getAcceptAllFileFilter();
        loadDialog.removeChoosableFileFilter(all);
        loadDialog.addChoosableFileFilter(all);
        loadDialog.setFileFilter(firstFilter);

        int returnVal = loadDialog.showOpenDialog(view);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return openXMLDocument(view, loadDialog.getSelectedFile());
        }
        return true;
    } //}}}

    /**
     * Attempts to open an XML document in jsXe from a file on disk.
     * @param view The view to open the document in.
     * @param file The file to open.
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
    public static boolean openXMLDocument(MainFrame view, File file) throws
        IOException { //{{{

        if (file == null) {
            return false;
        }

        XMLDocument doc = getOpenXMLDocument(file);
        if (doc != null) {
            view.setDocument(doc);
            return true;
        }

        //At this point we know the file is not open
        //we need to open it.
        //right now unrecognized doc exceptions should not be thrown.
        XMLDocumentFactory factory = XMLDocumentFactory.newInstance();
        try {
            XMLDocument document = factory.newXMLDocument(file);

            if (document != null) {

                try {
                    XMLDocuments.add(document);
                    view.addDocument(document);
                }
                catch (IOException ioe) {
                    //recover by removing the document
                    XMLDocuments.remove(document);
                    throw ioe;
                }
                return true;
            }
        }
        catch (UnrecognizedDocTypeException udte) {}

        return false;

    } //}}}

    /**
     * Attempts to open an XML document in the form of a String object in jsXe.
     * @param view The view to open the document in.
     * @param doc The String document to open.
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
    public static boolean openXMLDocument(MainFrame view, String doc) throws
        IOException { //{{{
        return openXMLDocument(view, new StringReader(doc));
    } //}}}

    /**
     * Attempts to open an XML document in the form of a Reader object in jsXe.
     * @param view The view to open the document in.
     * @param reader The Reader document to open.
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
    public static boolean openXMLDocument(MainFrame view, Reader reader) throws
        IOException { //{{{
        //We are assuming the contents of the reader do not
        //exist on disk and therefore could not be opened already.
        //right now unrecognized doc exceptions should not be thrown.
        XMLDocumentFactory factory = XMLDocumentFactory.newInstance();
        try {

            XMLDocument document = factory.newXMLDocument(reader);
            if (document != null) {
                //for now do not open the file unless it validates.

                try {
                    XMLDocuments.add(document);
                    view.addDocument(document);
                }
                catch (IOException ioe) {
                    //recover by removing the document
                    XMLDocuments.remove(document);
                    throw ioe;
                }

                return true;
            }

        }
        catch (UnrecognizedDocTypeException udte) {}

        return false;

    } //}}}

    /**
     * Gets the XMLDocument for this file if the file is open already. Returns
     * null if the file is not open.
     * @param file The file that is open in jsXe
     * @return the XMLDocument for the given file or null if the file not open.
     */
    public static XMLDocument getOpenXMLDocument(File file) { //{{{

        boolean caseInsensitiveFilesystem = (File.separatorChar == '\\'
                                             ||
                                             File.separatorChar ==
                                             ':'
                                             /* Windows or MacOS */);

        //Check if the file is already open, if so
        //change focus to that file
        for (int i = 0; i < XMLDocuments.size(); i++) {

            XMLDocument doc = (XMLDocument) XMLDocuments.get(i);
            File docfile = doc.getFile();
            if (docfile != null) {
                try {
                    if (caseInsensitiveFilesystem) {

                        if (file.getCanonicalPath().equalsIgnoreCase(docfile.
                            getCanonicalPath())) {
                            return doc;
                        }

                    }
                    else {

                        if (file.getCanonicalPath().equals(docfile.
                            getCanonicalPath())) {
                            return doc;
                        }
                    }
                }
                catch (IOException ioe) {
                    Debug.exitError(null, ioe.getMessage(), 1);
                }
            }
        }

        return null;
    } //}}}

    /**
     * Closes an open XML document.
     * @param view The view that contains the document.
     * @param document The document to close.
     * @return true if the document was closed successfully.
     */
    public static boolean closeXMLDocument(MainFrame view,
                                           XMLDocument document) { //{{{
        view.removeDocument(document);
        XMLDocuments.remove(document);
        if (view.getDocumentCount() == 0) {
            try {
                openXMLDocument(view, XmlEditor.getDefaultDocument());
            }
            catch (IOException ioe) {
                Debug.exitError(view, "Could not open default document.", 1);
            }
        }
        return true;
    } //}}}

    /**
     * Gets jsXe's icon that is displayed in the about menu,
     * taskbar and upper left hand corner (where appropriate)
     * @return jsXe's icon
     */
    public static ImageIcon getIcon() { //{{{
        return jsXeIcon;
    } //}}}

    /**
     * Called when exiting jsXe.
     * @param view The view from which the exit was called.
     */
    public static void exit(MainFrame view) { //{{{
        //nothing much here yet. Open documents should
        //be checked for dirty documents.
        Debug.trace("Do something in exit() !");

        //saves properties
        view.close();

        System.exit(0);
    } //}}}

    /**
     * Gets the options panel for the jsXe application.
     * @return The OptionsPanel with the options for jsXe.
     */
    public static final OptionsPanel getOptionsPanel() { //{{{
        optionDlg = new AppOptionsPanel();
        return optionDlg;
    } //}}}

    /**
     * Open the XML documents in the command line arguments.
     * @param view
     * @param args
     * @return
     */
    private static boolean openXMLDocuments(MainFrame view, String args[]) { //{{{

        boolean success = false;
        for (int i = 0; i < args.length; i++) {
            //success becomes true if at least one document is opened
            //successfully.
            if (args[i] != null) {
                try {
                    success = success || openXMLDocument(view, new File(args[i]));
                }
                catch (IOException ioe) {
                    //I/O error doesn't change value of success
                    JOptionPane.showMessageDialog(view, ioe, "I/O Error",
                                                  JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        return success;
    } //}}}
}