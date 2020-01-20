/*****************************************************************************
 * Copyright (C) 2000 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook.processors;

import org.apache.stylebook.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.util.Enumeration;
import java.util.StringTokenizer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// Imported TraX classes
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.TransformerException; 
import javax.xml.transform.TransformerConfigurationException; 

// Imported JAVA API for XML Parsing 1.0 classes
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/*
 * A revision of XalanProcessor for use with Xalan-Java 2.
 * If you build Stylebook with Xalan2Processor rather than XalanProcessor, you can
 * use Stylebook to process documents with Xalan-Java 2.
 *
 * @author Donald Leslie (donald_leslie@lotus.com)
 */

public class Xalan2Processor extends AbstractComponent implements Processor {

    public Document process(Document doc, CreationContext c, Parameters p)
    throws CreationException, IOException {
        try {
            // Retrieve the style
            String styf=p.getParameter("stylesheet");
            if (null==styf) styf=this.getStyleSheet(doc);
            if (null==styf) return(doc);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(styf));
            // Set stylesheet parameters
            Enumeration en=p.getParameterNames();
            while (en.hasMoreElements()) {
                String name=(String)en.nextElement();
                transformer.setParameter(name, p.getParameter(name));
            }
			      // Set up DOM container for result.
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
			      dfactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
            Document res = docBuilder.newDocument();
			      // Perform the transformation.
            log("Applying XSL sheet \""+styf+"\"");
 	          transformer.transform(new DOMSource(doc), new DOMResult(res));
            return(res);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new CreationException(e.getMessage(),e,doc);
        }
    }

    /** Search for the &lt;?xml:stylesheet ... ?&gt; processing instruction. */
    private String getStyleSheet(Document sourceTree) {
        String uri=null;
        NodeList children=sourceTree.getChildNodes();
        int nNodes=children.getLength();
        for(int i=0; i<nNodes; i++) {
            Node child=children.item(i);
            if (Node.PROCESSING_INSTRUCTION_NODE==child.getNodeType()) {
                ProcessingInstruction pi=(ProcessingInstruction)child;
                if(pi.getNodeName().equals("xml-stylesheet")) {
                    boolean isOK=true;
                    StringTokenizer tok=new StringTokenizer(pi.getNodeValue(),
                                                                  " \t=");
                    while(tok.hasMoreTokens()) {
                        if(tok.nextToken().equals("type")) {
                            String typeVal=tok.nextToken();
                            typeVal=typeVal.substring(1, typeVal.length()-1);
                            if(!typeVal.equals("text/xsl")) isOK=false;
                        }
                    }
                    if(isOK) {
                        tok=new StringTokenizer(pi.getNodeValue()," \t=");
                        while(tok.hasMoreTokens()) {
                            if(tok.nextToken().equals("href")) {
                                uri=tok.nextToken();
                                uri=uri.substring(1,uri.length()-1);
                            }
                        }
                        break;
                    }
                }
            }
        }
        return(uri);
    }
}
