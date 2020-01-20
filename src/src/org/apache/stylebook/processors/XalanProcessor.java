/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
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
import org.apache.xalan.xpath.xml.FormatterToDOM;
import org.apache.xalan.xpath.xml.XMLParserLiaison;
import org.apache.xalan.xpath.xml.XMLParserLiaisonDefault;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTProcessor;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313202 $ $Date: 1999-11-30 13:28:55 +0100 (Tue, 30 Nov 1999) $
 */
public class XalanProcessor extends AbstractComponent implements Processor {

    public Document process(Document doc, CreationContext c, Parameters p)
    throws CreationException, IOException {
        try {

            // Create a new XSLTProcessor instance
            XMLParserLiaison liaison=new Liaison(this.engine);
            XSLTProcessor proc=XSLTProcessorFactory.getProcessor(liaison);
            proc.setDiagnosticsOutput(System.out);
            // Set processor parameters
            Enumeration enum=p.getParameterNames();
            while (enum.hasMoreElements()) {
                String name=(String)enum.nextElement();
                proc.setStylesheetParam(name,proc.createXString(p.getParameter(name)));
            }
            // Retrieve the style
            String styf=p.getParameter("stylesheet");
            if (null==styf) styf=this.getStyleSheet(doc);
            if (null==styf) return(doc);
            // Setup the input sources and the result target
            Document res=this.engine.getParser().create();
            Document sty=this.engine.getParser().parse(new InputSource(styf));
            XSLTInputSource docin=new XSLTInputSource(doc);
            XSLTInputSource styin=new XSLTInputSource(sty);
            XSLTResultTarget out=new XSLTResultTarget(new FormatterToDOM(res));
            // Process the document
            log("Applying XSL sheet \""+styf+"\"");
            proc.process(docin,styin,out);
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

    private class Liaison extends XMLParserLiaisonDefault {
        private Engine engine=null;
        private Document document=null;

        public Liaison(Engine e) {
            this.engine=e;
        }

        public Document createDocument() {
            return(this.engine.getParser().create());
        }

        public void parse(InputSource in)
        throws IOException, SAXException {
            try {
                this.document=this.engine.getParser().parse(in);
            } catch (CreationException e) {
                this.engine.debug(this,"CreationException "+e.getMessage());
                Exception e2=e.getException();
                if((e2==null)||(!(e2 instanceof SAXException)))
                    throw new SAXException("Exception parsing from Xalan");
                throw (SAXException)e2;
            }
        }

        public Document getDocument() {
            return(this.document);
        }

        public boolean supportsSAX() {
            return(false);
        }
    }
}
