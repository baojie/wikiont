package edu.isu.indus.xml;

/**
 * @author Jie Bao
 * @
 */

// ����W3C��DOM ��
import java.io.*;
// JAXP������DOM I/O����
import javax.xml.parsers.*;

// ��׼Java��
import javax.swing.*;
import javax.swing.tree.*;

import org.w3c.dom.*;
import org.xml.sax.*;

public class XTree
    extends JTree {
    /**
     * ����Ա������TreeNode�������ڴ洢JTree��ģ�͡�
     *DefaultMutableTreeNode������javax.swing.tree�б������\uFFFD
     *Ĭ���ṩ��MutableTreeNode�ӿڵ�һ��ʵ�֡�
     */
    private DefaultMutableTreeNode treeNode;
    /**
     * ������Ա����JAXP��һ���֣���4����XML�ı���ת����DOM��Document Object Model) ����
     */
    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private Document doc;

    /**
     * ����캯��ͨ��ʹ�ô��͵��������е�XML�ı�����һ��XTree����
     * @param text ��һ��XML��ʽ��XML�ı�
     * @exception ParserConfigurationException ����캯���������÷�����ͻ��׳��쳣
     */
    public XTree(String text) throws ParserConfigurationException {
        this();
        try {
            refresh(text);
        }
        catch (ParserConfigurationException ex) {
        }
        catch (SAXParseException ex) {
        }
    }

    public XTree() throws ParserConfigurationException {
        super();
// ����Tree��Ⱦ�Ļ�����
        getSelectionModel().setSelectionMode(TreeSelectionModel.
                                             SINGLE_TREE_SELECTION);
        setShowsRootHandles(true);
        setEditable(false);
// ͨ���ʼ�������DOM4�������\uFFFD
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        db = dbf.newDocumentBuilder();
// ����DOM��ڵ㲢�Ұ���ת����JTree����ģ��
        setModel(createDefaultTree());
    }

    /**
     * ��������һ��DOM �ڵ㣬Ȼ�����ӽڵ��еݹ�ֱ�����еĽӵ㶼����ӵ�DefaultMutableTreeNode�С�
     * ����һ��ݹ鷽����Ϊ���ҵ���ڵ��µ�ÿһ���ӽڵ㣬��ÿ�ζ�Ҫ�����Լ���
         * JTreeȻ��Ϳ���ʹ��DefaultMutableTreeNode�����ˣ���Ϊ���Ѿ��������ˡ�
     *
     * @param root org.w3c.Node.Node
     *
     * @return ����һ����ڸ�ڵ�DefaultMutableTreeNode����
     */

    private DefaultMutableTreeNode createTreeNode(Node root) {
        DefaultMutableTreeNode treeNode = null;
        String type, name, value;
        NamedNodeMap attribs;
        Node attribNode;

// �Ӹ�ڵ���ȡ�����
        type = getNodeType(root);
        name = root.getNodeName();
        value = root.getNodeValue();

        treeNode = new DefaultMutableTreeNode(root.getNodeType() ==
                                              Node.TEXT_NODE ? value : name);

// ��ʾ����
        attribs = root.getAttributes();
        if (attribs != null) {
            for (int i = 0; i < attribs.getLength(); i++) {
                attribNode = attribs.item(i);
                name = attribNode.getNodeName().trim();
                value = attribNode.getNodeValue().trim();

                if (value != null) {
                    if (value.length() > 0) {
                        treeNode.add(new DefaultMutableTreeNode(
                            "[Attribute] --> " + name + "=\"" + value + "\""));
                    } //end if ( value.length() �� 0 )
                } //end if ( value != null )
            } //end for( int i = 0; i < attribs.getLength(); i++ )
        } //end if( attribs != null )

// �������ӽڵ㣬�ݹ�
        if (root.hasChildNodes()) {
            NodeList children;
            int numChildren;
            Node node;
            String data;

            children = root.getChildNodes();
// ����ӽڵ�ǿյĻ���ֻ�ݹ�
            if (children != null) {
                numChildren = children.getLength();

                for (int i = 0; i < numChildren; i++) {
                    node = children.item(i);
                    if (node != null) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            treeNode.add(createTreeNode(node));
                        } //end if( node.getNodeType() == Node.ELEMENT_NODE )

                        data = node.getNodeValue();

                        if (data != null) {
                            data = data.trim();
                            if (!data.equals("\n") && !data.equals("\r\n") &&
                                data.length() > 0) {
                                treeNode.add(createTreeNode(node));
                            } //end if ( !data.equals("\n") && !data.equals("\r\n") && data.length() �� 0 )
                        } //end if( data != null )
                    } //end if( node != null )
                } //end for (int i=0; i < numChildren; i++)
            } //end if( children != null )
        } //end if( root.hasChildNodes() )
        return treeNode;
    } //end createTreeNode( Node root )

    /**
     * ��������createTreeNode()��4jϵһ���ַ��ĳһ�����͵Ľڵ�\uFFFD
     *
     * @param node org.w3c.Node.Node
     * @return ������ʾ�ڵ�����ַ�\uFFFD
     */
    private String getNodeType(Node node) {
        String type;

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE: {
                type = "Element";
                break;
            }
            case Node.ATTRIBUTE_NODE: {
                type = "Attribute";
                break;
            }
            case Node.TEXT_NODE: {
                type = "Text";
                break;
            }
            case Node.CDATA_SECTION_NODE: {
                type = "CData section";
                break;
            }
            case Node.ENTITY_REFERENCE_NODE: {
                type = "Entity reference";
                break;
            }
            case Node.ENTITY_NODE: {
                type = "Entity";
                break;
            }
            case Node.PROCESSING_INSTRUCTION_NODE: {
                type = "Processing instruction";
                break;
            }
            case Node.COMMENT_NODE: {
                type = "Comment";
                break;
            }
            case Node.DOCUMENT_NODE: {
                type = "Document";
                break;
            }
            case Node.DOCUMENT_TYPE_NODE: {
                type = "Document type";
                break;
            }
            case Node.DOCUMENT_FRAGMENT_NODE: {
                type = "Document fragment";
                break;
            }
            case Node.NOTATION_NODE: {
                type = "Notation";
                break;
            }
            default: {
                type = "???";
                break;
            }
        } // ���� switch( node.getNodeType() )
        return type;
    } //���� getNodeType()

    /**
     * Read the specified XML file and generatethe JTree
     *
     * @param inFile : the name of the XMLfile
     * @throws Exception
     * @version: 2003-09-23
     * @return : org.w3c.Node.Node
     */
    private Node readXMLFile(String text) {
        ByteArrayInputStream byteStream;

        byteStream = new ByteArrayInputStream(text.getBytes());

        try {
            doc = db.parse(byteStream);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (Node) doc.getDocumentElement();
    }

    public Document getDoc() {
        return doc;
    } ///���� parseXml()

    /**
     * Create the Tree Model from XML Document
     *
     * @param text : the XML file name
     * @version : 2003-09-23
     */
    private DefaultTreeModel createTree(String text) {
        DefaultMutableTreeNode treeNode;
        Node newNode;

        // ����DOM��ڵ㲢����ת����Ϊһ��Treeģ��
        newNode = readXMLFile(text);
        if (newNode != null) {
            treeNode = createTreeNode(newNode);
            return new DefaultTreeModel(treeNode);
        }
        else {
            return null;
        }
    } //����buildTree()

    private DefaultTreeModel createDefaultTree() {
        DefaultMutableTreeNode root;
        DefaultMutableTreeNode instructions, openingDoc, editingDoc, savingDoc;
        DefaultMutableTreeNode openingDocText, editingDocText, savingDocText;
        DefaultMutableTreeNode development, addingFeatures, contactingKyle;

        root = new DefaultMutableTreeNode("Welcome to XEditor ");
        instructions = new DefaultMutableTreeNode("Instructions");
        openingDoc = new DefaultMutableTreeNode("Opening XML Documents");
        openingDocText = new DefaultMutableTreeNode("When invoking the XmlEditor from the command - line, you must specify the filename.");
        editingDoc = new DefaultMutableTreeNode("Editing an XML Document");
        editingDocText = new DefaultMutableTreeNode("XML text in the right hand frame can be edited directly. The\"refresh\" button will rebuild the JTree in the left frame.");
        savingDoc = new DefaultMutableTreeNode("Saving an XML Document");
        savingDocText = new DefaultMutableTreeNode("This iteration of the XmlEditor does not provide the ability to save your document.That will come with the next article.");
        root.add(instructions);
        instructions.add(openingDoc);
        instructions.add(editingDoc);
        openingDoc.add(openingDocText);
        editingDoc.add(editingDocText);
        return new DefaultTreeModel(root);
    }

    public void refresh(String text) throws ParserConfigurationException,
        SAXParseException {

        getSelectionModel().setSelectionMode(TreeSelectionModel.
                                             SINGLE_TREE_SELECTION);
        setShowsRootHandles(true);
        setEditable(false);

        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            db = dbf.newDocumentBuilder();
            treeNode = createTreeNode(readXMLFile(text));
            setModel(new DefaultTreeModel(treeNode));

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                                          "XML Document is invalidate!\n\n" +
                                          ex.getMessage());
            ex.getStackTrace();
        }
    } //end of XTree()

} //���� class XTree
