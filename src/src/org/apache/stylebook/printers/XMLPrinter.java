/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook.printers;

import org.apache.stylebook.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * The XMLPrinter Printer writes documents in their canonical XML form.
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313195 $ $Date: 1999-11-15 23:08:18 +0100 (Mon, 15 Nov 1999) $
 */
public class XMLPrinter extends AbstractComponent implements Printer {
    /**
     * Print a DOM Document.
     */
    public void print(Document doc, CreationContext c, OutputStream out)
    throws CreationException, IOException {
        printDocument(doc,new PrintStream(out),0);
    }

    /** Process a nodelist calling the appropriate print...() method. */
    private void printNodeList(NodeList list, PrintStream out, int indent) {
        for (int x=0;x<list.getLength();x++) {
            Node node=list.item(x);
            switch (node.getNodeType()) {
                case Node.ATTRIBUTE_NODE:
                    printAttribute((Attr)node,out,indent);
                    break;
                case Node.CDATA_SECTION_NODE:
                    printCDATASection((CDATASection)node,out,indent);
                    break;
                case Node.COMMENT_NODE:
                    printComment((Comment)node,out,indent);
                    break;
                case Node.DOCUMENT_FRAGMENT_NODE:
                    printDocumentFragment((DocumentFragment)node,out,indent);
                    break;
                case Node.DOCUMENT_NODE:
                    printDocument((Document)node,out,indent);
                    break;
                case Node.DOCUMENT_TYPE_NODE:
                    printDocumentType((DocumentType)node,out,indent);
                    break;
                case Node.ELEMENT_NODE:
                    printElement((Element)node,out,indent);
                    break;
                case Node.ENTITY_NODE:
                    printEntity((Entity)node,out,indent);
                    break;
                case Node.ENTITY_REFERENCE_NODE:
                    printEntityReference((EntityReference)node,out,indent);
                    break;
                case Node.NOTATION_NODE:
                    printNotation((Notation)node,out,indent);
                    break;
                case Node.PROCESSING_INSTRUCTION_NODE:
                    printProcessingInstruction((ProcessingInstruction)node,out,indent);
                    break;
                case Node.TEXT_NODE:
                    printText((Text)node,out,indent);
                    break;
            }
        }
    }

    /** Print an ATTRIBUTE (name="value") node. */
    private void printAttribute(Attr node, PrintStream out, int indent) {
        out.print(node.getName()+"=\""+node.getValue()+"\"");
    }

    /** Print a CDATA (<[CDATA[ ... ]]>) node. */
    private void printCDATASection(CDATASection node, PrintStream out, int indent) {
        indent(out,indent);
        out.println("<[CDATA["+node.getData()+"]]>");
    }

    /** Print a COMMENT (<!-- ... -->) node. */
    private void printComment(Comment node, PrintStream out, int indent) {
        indent(out,indent);
        out.println("<!--"+node.getData()+"-->");
    }

    /** Print a DOCUMENT FRAGMENT (????) node. */
    private void printDocumentFragment(DocumentFragment node, PrintStream out, int indent) {
        indent(out,indent);
        out.println("<!-- Document Fragment Node -->");
    }

    /** Print a DOCUMENT node. */
    private void printDocument(Document node, PrintStream out, int indent) {
        indent(out,indent);
        out.println("<?xml version=\"1.0\"?>");
        printNodeList(node.getChildNodes(),out,indent);
    }

    /** Print a DOCUMENT TYPE (<!DOCTYPE ... bla bla bla>) node. */
    private void printDocumentType(DocumentType node, PrintStream out, int indent) {
        indent(out,indent);
        out.println("<!DOCTYPE "+node.getName()+">");
        // Should we insert ENTITIES and NOTATIONS?
    }

    /** Print a ELEMENT (do I need to supply the example ??? :) node. */
    private void printElement(Element node, PrintStream out, int indent) {
        // Print the tag name
        indent(out,indent);
        out.print("<"+node.getTagName());
        // Process attributes
        NamedNodeMap atts=node.getAttributes();
        if ((null!=atts) && (atts.getLength()>0)) {
            for (int x=0; x<atts.getLength(); x++) {
                out.print(' ');
                printAttribute((Attr)atts.item(x),out,indent);
            }
        }
        // Process child nodes
        if ((null!=node.getChildNodes()) &&
                (node.getChildNodes().getLength()>0)) {
            out.println(">");
            printNodeList(node.getChildNodes(),out,indent+1);
            indent(out,indent);
            out.println("</"+node.getTagName()+">");
        } else {
            out.println("/>");
        }
    }

    /** Print a ENTITY node (NOTE: This should be used with DOCTYPE). */
    private void printEntity(Entity node, PrintStream out, int indent) {
        indent(out,indent);
        out.println("<!-- Entity Node -->");
    }

    /** Print a ENTITY REFERENCE node (NOTE: Again, used in within DOCTYPE). */
    private void printEntityReference(EntityReference node, PrintStream out, int indent) {
        indent(out,indent);
        out.println("<!-- Entity Reference Node -->");
    }

    /** Print a NOTATION node (NOTE: Again -3rd-, used in within DOCTYPE). */
    private void printNotation(Notation node, PrintStream out, int indent) {
        indent(out,indent);
        out.println("<!-- Notation Node -->");
    }

    /** Print a PROCESSING INSTRUCTION (<?name ...?>) node. */
    private void printProcessingInstruction(ProcessingInstruction node, PrintStream out, int indent) {
        indent(out,indent);
        out.println("<?"+node.getTarget()+" "+node.getData()+"?>");
    }

    /** Print a TEXT (see example for ELEMENT) node. */
    private void printText(Text node, PrintStream out, int indent) {
        String data=node.getData();
        if (data.length()==0) return;
        data=leftTrim(data);
        if (data.length()==0) return;
        Text curr=node;
        while(true) {
            Node nextnode=curr.getNextSibling();
            if (nextnode==null) break;
            if (nextnode.getNodeType()!=Node.TEXT_NODE) break;
            Text next=(Text)nextnode;
            String app=next.getData();
            if (app.length()>0) {
                if (Character.isWhitespace(app.charAt(0))) {
                    data=trim(data)+' '+leftTrim(app);
                } else {
                    data=trim(data)+leftTrim(app);
                }
            }
            next.setData("");
            curr=next;
        }
        data=trim(data);
        node.setData(data);
        if (data.length()>0) {
            indent(out,indent);
            out.println(data);
        }
    }

    /** Remove white space at string beginning. */
    private String leftTrim(String data) {
        if (data.length()==0) return(data);
        int index=0;
        for (int x=0;x<data.length();x++) {
            if (Character.isWhitespace(data.charAt(x))) index=x+1;
            else break;
        }
        if (index==data.length()) return("");
        return(data.substring(index));
    }

    /** Remove white space at string ending. */
    private String rightTrim(String data) {
        if (data.length()==0) return(data);
        int index=data.length();
        for (int x=index-1;x>=0;x--) {
            if (Character.isWhitespace(data.charAt(x))) index=x;
            else break;
        }
        return(data.substring(0,index));
    }

    /** Remove white space at string beginning and ending. */
    private String trim(String data) {
        return(leftTrim(rightTrim(data)));
    }

    /** Indent data (print whitespaces). */
    private void indent(PrintStream out, int indent) {
        for (int x=0; x<indent; x++) out.print("  ");
    }
}
