/*
 DefaultXMLDocument.java
 :tabSize=4:indentSize=4:noTabs=true:
 :folding=explicit:collapseFolds=1:
 jsXe is the Java Simple XML Editor
     jsXe is a gui application that can edit an XML document and create a tree view.
 The user can then edit this tree and the content in the tree and save the
 document.
     This file contains the default implementation of the XMLDocument abstract class.
 It represents a generic XML document.
 This file written by Ian Lewis (IanLewis@member.fsf.org)
 Copyright (C) 2002 Ian Lewis
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 Optionally, you may find a copy of the GNU General Public License
 from http://www.fsf.org/copyleft/gpl.txt
 */

package edu.isu.indus.xml;

//{{{ imports
/*
 All classes are listed explicitly so
 it is easy to see which package it
 belongs to.
 */

//{{{ jsXe classes
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import edu.iastate.cs.utils.Debug;
import edu.isu.indus.XmlEditor;

//}}}

//}}}

public class DefaultXMLDocument
    extends XMLDocument {

    protected DefaultXMLDocument(File file) throws FileNotFoundException,
        IOException { //{{{
        setDefaultProperties();
        setModel(file);
        validated = false;
    } //}}}

    protected DefaultXMLDocument(Reader reader) throws IOException { //{{{
        setDefaultProperties();
        setModel(reader);
        name = getUntitledLabel();
        validated = false;
    } //}}}

    protected DefaultXMLDocument(String string) throws IOException { //{{{
        setDefaultProperties();
        setModel(string);
        name = getUntitledLabel();
        validated = false;
    } //}}}

    public void validate() throws SAXParseException, SAXException,
        ParserConfigurationException, IOException { //{{{
        if (!isValidated()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(
                source)));
            doc.getDocumentElement().normalize();
            document = doc;
            validated = true;
        }
    } //}}}

    public Document getDocument() { //{{{
        return document;
    } //}}}

    public String getName() { //{{{
        return name;
    } //}}}

    public String getSource() throws IOException { //{{{
        //if the document is validated we go by the DOM
        //if it's not we go by the source.

        if (isValidated()) {
            DOMSerializer.DOMSerializerConfiguration config = new DOMSerializer.
                DOMSerializerConfiguration();
            config.setParameter("format-output", getProperty("format-output"));
            config.setParameter("whitespace-in-element-content",
                                getProperty("whitespace-in-element-content"));
            config.setParameter("indent", new Integer(getProperty("indent")));
            DOMSerializer serializer = new DOMSerializer(config);
            serializer.setEncoding(getProperty("encoding"));
            return serializer.writeToString(getDocument());
        }
        else {
            return source;
        }

    } //}}}

    public File getFile() { //{{{
        return XMLFile;
    } //}}}

    public void setModel(File file) throws FileNotFoundException, IOException { //{{{
        if (file != null) {
            int nextchar = 0;
            name = file.getName();
            validated = false;
            FileReader reader = new FileReader(file);

            StringBuffer text = new StringBuffer();
            char[] buffer = new char[READ_SIZE];

            int bytesRead;
            do {
                bytesRead = reader.read(buffer, 0, READ_SIZE);
                if (bytesRead != -1) {
                    text.append(buffer, 0, bytesRead);
                }
            }
            while (bytesRead != -1);
            source = text.toString();
            XMLFile = file;
        }
        else {
            throw new FileNotFoundException("File Not Found: null");
        }
    } //}}}

    public void setModel(Reader reader) throws IOException { //{{{
        validated = false;

        StringBuffer text = new StringBuffer();
        char[] buffer = new char[READ_SIZE];

        int bytesRead;
        do {
            bytesRead = reader.read(buffer, 0, READ_SIZE);
            if (bytesRead != -1) {
                text.append(buffer, 0, bytesRead);
            }
        }
        while (bytesRead != -1);
        source = text.toString();
    } //}}}

    public void setModel(String string) { //{{{
        validated = false;
        source = string;
    } //}}}

    public boolean isValidated() { //{{{
        return validated;
    } //}}}

    public void save() throws IOException, SAXParseException, SAXException,
        ParserConfigurationException { //{{{
        if (XMLFile != null) {
            saveAs(XMLFile);
        }
        else {
            //You shouldn't call this when the document is untitled but
            //if you do default to saving to the home directory.
            File newFile = new File(System.getProperty("user.home") + getName());
            setModel(newFile);
        }
    } //}}}

    public void saveAs(File file) throws IOException, SAXParseException,
        SAXException, ParserConfigurationException { //{{{
        validate();

        DOMSerializer.DOMSerializerConfiguration config = new DOMSerializer.
            DOMSerializerConfiguration();
        config.setParameter("format-output", getProperty("format-output"));
        config.setParameter("whitespace-in-element-content",
                            getProperty("whitespace-in-element-content"));
        config.setParameter("indent", new Integer(getProperty("indent")));

        DOMSerializer serializer = new DOMSerializer(config);
        serializer.setEncoding(getProperty("encoding"));

        FileOutputStream out = new FileOutputStream(file);

        serializer.writeNode(out, getDocument());
        setModel(file);
    } //}}}

    public boolean equals(Object o) throws ClassCastException { //{{{
        if (XMLFile != null) {
            boolean caseInsensitiveFilesystem = (File.separatorChar == '\\'
                                                 ||
                                                 File.separatorChar == ':' /* Windows or MacOS */);

            File file;

            try {
                XMLDocument doc = (XMLDocument) o;
                file = doc.getFile();
            }
            catch (ClassCastException cce) {
                try {
                    file = (File) o;
                }
                catch (ClassCastException cce2) {
                    throw new ClassCastException(
                        "Could not cast to XMLDocument or File.");
                }
            }

            try {
                if (caseInsensitiveFilesystem) {

                    if (file.getCanonicalPath().equalsIgnoreCase(getFile().
                        getCanonicalPath())) {
                        return true;
                    }

                }
                else {

                    if (file.getCanonicalPath().equals(getFile().
                        getCanonicalPath())) {
                        return true;
                    }
                }
            }
            catch (IOException ioe) {
                Debug.exitError(null, ioe.getMessage(), 1);
            }
        }

        return false;
    } //}}}

    //{{{ Private members

    private String getUntitledLabel() { //{{{
        XMLDocument[] docs = XmlEditor.getXMLDocuments();
        int untitledNo = 0;
        for (int i = 0; i < docs.length; i++) {
            if (docs[i].getName().startsWith("Untitled-")) {
                // Kinda stolen from jEdit
                try {
                    untitledNo = Math.max(untitledNo,
                                          Integer.parseInt(docs[i].getName().
                        substring(9)));
                }
                catch (NumberFormatException nf) {}
            }
        }
        return "Untitled-" + Integer.toString(untitledNo + 1);
    } //}}}

    private void setDefaultProperties() { ///{{{
        setProperty("format-output", "false");
        setProperty("whitespace-in-element-content", "true");
        setProperty("encoding", "UTF-8");
        setProperty("indent", "4");
    } //}}}

    // 2003-10-03 Jie Bao
    public String toString()
    {
        return source;
    }


    private Document document;
    private File XMLFile;
    private String name;
    private String source = new String();
    private boolean validated;
    private static final int READ_SIZE = 5120;
    //}}}
}
