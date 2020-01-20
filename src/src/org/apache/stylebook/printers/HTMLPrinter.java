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
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
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
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313270 $ $Date: 2000-06-23 23:21:08 +0200 (Fri, 23 Jun 2000) $
 */
public class HTMLPrinter extends AbstractComponent implements Printer
{

    final static String ENCODING = "UTF8";

    /**
     * Print a DOM Document.
     *
     * @param doc The Document to print.
     * @param out The OutputStream used for printing.
     * @param env The Environment of this printing request.
     * @exception IOException If an I/O error occourred accessing resources.
     * @exception CreationException If the Document cannot be printed.
     */
    public void print(Document doc, CreationContext c, OutputStream out)
    throws CreationException, IOException {       
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(out,ENCODING));
        printDocument(doc, writer);
        writer.flush();        
    }     


    /** Process a nodelist calling the appropriate print...() method. */
    private void printNodeList(NodeList list, BufferedWriter out) throws IOException {
        for ( int x=0;x<list.getLength();x++ )
        {
            Node node=list.item(x);
            switch ( node.getNodeType() )
            {
            case Node.ATTRIBUTE_NODE:
                printAttribute((Attr)node,out);
                break;
            case Node.CDATA_SECTION_NODE:
                printCDATASection((CDATASection)node,out);
                break;
            case Node.COMMENT_NODE:
                printComment((Comment)node,out);
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                printDocumentFragment((DocumentFragment)node,out);
                break;
            case Node.DOCUMENT_NODE:
                printDocument((Document)node,out);
                break;
            case Node.DOCUMENT_TYPE_NODE:
                printDocumentType((DocumentType)node,out);
                break;
            case Node.ELEMENT_NODE:
                printElement((Element)node,out);
                break;
            case Node.ENTITY_NODE:
                printEntity((Entity)node,out);
                break;
            case Node.ENTITY_REFERENCE_NODE:
                printEntityReference((EntityReference)node,out);
                break;
            case Node.NOTATION_NODE:
                printNotation((Notation)node,out);
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                printProcessingInstruction((ProcessingInstruction)node,out);
                break;
            case Node.TEXT_NODE:
                printText((Text)node,out);                   
                break;
            }
        }
    }

    /** Print an ATTRIBUTE (name="value") node. */
    private void printAttribute(Attr node, BufferedWriter out) throws IOException {
        out.write(node.getName()+"=\""+node.getValue()+"\"");
    }

    /** Print a CDATA (<[CDATA[ ... ]]>) node. */
    private void printCDATASection(CDATASection node, BufferedWriter out) throws IOException {
        printString(node.getData(),out);
    }

    /** Print a COMMENT (<!-- ... -->) node. */
    private void printComment(Comment node, BufferedWriter out) throws IOException {
        out.write("<!--"+node.getData()+"-->");
    }

    /** Print a DOCUMENT FRAGMENT (????) node. */
    private void printDocumentFragment(DocumentFragment node, BufferedWriter out) throws IOException {
        out.write("<!-- Document Fragment Node -->");
    }

    /** Print a DOCUMENT node. */
    private void printDocument(Document node, BufferedWriter out) throws IOException {
        printNodeList(node.getChildNodes(),out);
    }

    /** Print a DOCUMENT TYPE (<!DOCTYPE ... bla bla bla>) node. */
    private void printDocumentType(DocumentType node, BufferedWriter out) throws IOException {
        out.write("<!-- DOCTYPE "+node.getName()+" -->");
    }

    /** Print a ELEMENT (do I need to supply the example ??? :) node. */
    private void printElement(Element node, BufferedWriter out) throws IOException {
        String tag=node.getTagName().toUpperCase();
        out.write("<"+tag);
        // Process attributes
        NamedNodeMap atts=node.getAttributes();
        if ( (null!=atts) && (atts.getLength()>0) )
        {
            for ( int x=0; x<atts.getLength(); x++ )
            {
                out.write(' ');
                printAttribute((Attr)atts.item(x),out);
            }
        }
        out.write(">");
        // Process child nodes
        if ( (null!=node.getChildNodes()) && 
        (node.getChildNodes().getLength()>0) )
        {
            printNodeList(node.getChildNodes(),out);
        }

        // Print the END tag...
        if ( tag.equals("AREA") ) return;
        else if ( tag.equals("BASE") ) return;
        else if ( tag.equals("BASEFONT") ) return;
        else if ( tag.equals("BR") ) return;
        else if ( tag.equals("COL") ) return;
        else if ( tag.equals("FRAME") ) return;
        else if ( tag.equals("HR") ) return;
        else if ( tag.equals("IMG") ) return;
        else if ( tag.equals("INPUT") ) return;
        else if ( tag.equals("ISINDEX") ) return;
        else if ( tag.equals("LINK") ) return;
        else if ( tag.equals("META") ) return;
        else if ( tag.equals("PARAM") ) return;
        out.write("</"+tag+">");
    }

    /** Print a ENTITY node (NOTE: This should be used with DOCTYPE). */
    private void printEntity(Entity node, BufferedWriter out) throws IOException {
        out.write("<!-- Entity Node -->");
    }

    /** Print a ENTITY REFERENCE node (NOTE: Again, used in within DOCTYPE). */
    private void printEntityReference(EntityReference node, BufferedWriter out) throws IOException {
        printString(node.getFirstChild().getNodeValue(),out);
    }

    /** Print a NOTATION node (NOTE: Again -3rd-, used in within DOCTYPE). */
    private void printNotation(Notation node, BufferedWriter out) throws IOException {
        out.write("<!-- Notation Node -->");
    }

    /** Print a PROCESSING INSTRUCTION (<?name ...?>) node. */
    private void printProcessingInstruction(ProcessingInstruction node, BufferedWriter out) throws IOException {
        out.write("<!-- PI:"+node.getTarget()+" "+node.getData()+"-->");
    }

    /** Print a TEXT (see example for ELEMENT) node. */
    private void printText(Text node, BufferedWriter out) throws IOException {
        printString(node.getData(),out);
    }

    private void printString(String data, BufferedWriter out) throws IOException {
        for ( int x=0; x<data.length(); x++ )
        {
            char c=data.charAt(x);           
            if ( c=='\n' ) out.write("\n");
            else if ( c==34 ) out.write("&quot;");
            else if ( c==38 ) out.write("&amp;");
            else if ( c==60 ) out.write("&lt;");
            else if ( c==62 ) out.write("&gt;");
            else if ( c==8364 ) out.write("&euro;");
            else if ( (c>=160) && (c<=255) ) switch ( c )
                {
                case 160: out.write("&nbsp;");   break;
                case 161: out.write("&iexcl;");  break;
                case 162: out.write("&cent;");   break;
                case 163: out.write("&pound;");  break;
                case 164: out.write("&curren;"); break;
                case 165: out.write("&yen;");    break;
                case 166: out.write("&brvbar;"); break;
                case 167: out.write("&sect;");   break;
                case 168: out.write("&uml;");    break;
                case 169: out.write("&copy;");   break;
                case 170: out.write("&ordf;");   break;
                case 171: out.write("&laquo;");  break;
                case 172: out.write("&not;");    break;
                case 173: out.write("&shy;");    break;
                case 174: out.write("&reg;");    break;
                case 175: out.write("&macr;");   break;
                case 176: out.write("&deg;");    break;
                case 177: out.write("&plusmn;"); break;
                case 178: out.write("&sup2;");   break;
                case 179: out.write("&sup3;");   break;
                case 180: out.write("&acute;");  break;
                case 181: out.write("&micro;");  break;
                case 182: out.write("&para;");   break;
                case 183: out.write("&middot;"); break;
                case 184: out.write("&cedil;");  break;
                case 185: out.write("&sup1;");   break;
                case 186: out.write("&ordm;");   break;
                case 187: out.write("&raquo;");  break;
                case 188: out.write("&frac14;"); break;
                case 189: out.write("&frac12;"); break;
                case 190: out.write("&frac34;"); break;
                case 191: out.write("&iquest;"); break;
                case 192: out.write("&Agrave;"); break;
                case 193: out.write("&Aacute;"); break;
                case 194: out.write("&Acirc;");  break;
                case 195: out.write("&Atilde;"); break;
                case 196: out.write("&Auml;");   break;
                case 197: out.write("&Aring;");  break;
                case 198: out.write("&AElig;");  break;
                case 199: out.write("&Ccedil;"); break;
                case 200: out.write("&Egrave;"); break;
                case 201: out.write("&Eacute;"); break;
                case 202: out.write("&Ecirc;");  break;
                case 203: out.write("&Euml;");   break;
                case 204: out.write("&Igrave;"); break;
                case 205: out.write("&Iacute;"); break;
                case 206: out.write("&Icirc;");  break;
                case 207: out.write("&Iuml;");   break;
                case 208: out.write("&ETH;");    break;
                case 209: out.write("&Ntilde;"); break;
                case 210: out.write("&Ograve;"); break;
                case 211: out.write("&Oacute;"); break;
                case 212: out.write("&Ocirc;");  break;
                case 213: out.write("&Otilde;"); break;
                case 214: out.write("&Ouml;");   break;
                case 215: out.write("&times;");  break;
                case 216: out.write("&Oslash;"); break;
                case 217: out.write("&Ugrave;"); break;
                case 218: out.write("&Uacute;"); break;
                case 219: out.write("&Ucirc;");  break;
                case 220: out.write("&Uuml;");   break;
                case 221: out.write("&Yacute;"); break;
                case 222: out.write("&THORN;");  break;
                case 223: out.write("&szlig;");  break;
                case 224: out.write("&agrave;"); break;
                case 225: out.write("&aacute;"); break;
                case 226: out.write("&acirc;");  break;
                case 227: out.write("&atilde;"); break;
                case 228: out.write("&auml;");   break;
                case 229: out.write("&aring;");  break;
                case 230: out.write("&aelig;");  break;
                case 231: out.write("&ccedil;"); break;
                case 232: out.write("&egrave;"); break;
                case 233: out.write("&eacute;"); break;
                case 234: out.write("&ecirc;");  break;
                case 235: out.write("&euml;");   break;
                case 236: out.write("&igrave;"); break;
                case 237: out.write("&iacute;"); break;
                case 238: out.write("&icirc;");  break;
                case 239: out.write("&iuml;");   break;
                case 240: out.write("&eth;");    break;
                case 241: out.write("&ntilde;"); break;
                case 242: out.write("&ograve;"); break;
                case 243: out.write("&oacute;"); break;
                case 244: out.write("&ocirc;");  break;
                case 245: out.write("&otilde;"); break;
                case 246: out.write("&ouml;");   break;
                case 247: out.write("&divide;"); break;
                case 248: out.write("&oslash;"); break;
                case 249: out.write("&ugrave;"); break;
                case 250: out.write("&uacute;"); break;
                case 251: out.write("&ucirc;");  break;
                case 252: out.write("&uuml;");   break;
                case 253: out.write("&yacute;"); break;
                case 254: out.write("&thorn;");  break;
                case 255: out.write("&yuml;");   break;
                }
            else out.write(c);
        }
    }        
}
