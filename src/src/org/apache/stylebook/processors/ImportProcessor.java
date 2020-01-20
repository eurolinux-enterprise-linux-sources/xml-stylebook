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
import java.net.URL;
import java.util.StringTokenizer;
import org.w3c.dom.*;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313202 $ $Date: 1999-11-30 13:28:55 +0100 (Tue, 30 Nov 1999) $
 */
public class ImportProcessor extends AbstractComponent implements Processor {

    public Document process(Document doc, CreationContext c, Parameters p)
    throws CreationException, IOException {
        processNode(doc.getDocumentElement(),doc,c);
        return(doc);
    }

    private void processNode(Node node, Document doc, CreationContext ctx)
    throws IOException, CreationException {
        if(node==null) return;
        if(node.getNodeType()!=Node.PROCESSING_INSTRUCTION_NODE) {
            NodeList l=node.getChildNodes();
            for (int x=0; x<l.getLength(); x++) processNode(l.item(x),doc,ctx);
            return;
        }
        ProcessingInstruction pi=(ProcessingInstruction)node;
        if (pi.getTarget().equals("import")) {
            String producer=null;
            String source="";
            StringTokenizer tok=new StringTokenizer(pi.getData());
            while(tok.hasMoreTokens()) {
                String p=tok.nextToken();
                StringTokenizer tok2=new StringTokenizer(p,"\"=",false);
                if(tok2.countTokens()!=2)
                    throw new CreationException("Invalid parameter \""+p+"\"");
                String name=tok2.nextToken();
                String value=tok2.nextToken();
                if(name.equals("producer")) producer=value;
                else if(name.equals("source")) source=value;
                else throw new CreationException("Invalid parameter \""+p+"\"");
            }
            if(producer==null)
                throw new CreationException("Producer not specified");
            log("Importing \""+source+"\" using Producer \""+producer+"\"");
            URL src=new URL(ctx.getSourceURL(),source);
            BasicContext ctx2=new BasicContext(src,ctx.getTargetName());
            ctx2.merge(ctx);
            Document newdoc=this.engine.getProducer(producer).produce(ctx2);
            Node elem=copyNode(doc,newdoc.getDocumentElement());
            if (elem==null) return;
            pi.getParentNode().replaceChild(elem,pi);
        }
    }

    private Node copyNode(Document d, Node n) {
        if (n==null) return(null);
        Node copy=null;
        switch(n.getNodeType()) {
            case Node.ELEMENT_NODE:
                copy=d.createElement(((Element)n).getTagName());
                NamedNodeMap m=n.getAttributes();
                for (int k=0; k<m.getLength(); k++) {
                    Attr a=(Attr)m.item(k);
                    ((Element)copy).setAttribute(a.getName(),a.getValue());
                }
                break;
            case Node.CDATA_SECTION_NODE:
                copy=d.createCDATASection(((CharacterData)n).getData());
                break;
            case Node.COMMENT_NODE:
                copy=d.createComment(((CharacterData)n).getData());
                break;
            case Node.TEXT_NODE:
                copy=d.createTextNode(((CharacterData)n).getData());
                break;
            case Node.ENTITY_REFERENCE_NODE:
                copy=d.createEntityReference(n.getNodeName());
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                String target=((ProcessingInstruction)n).getTarget();
                String data=((ProcessingInstruction)n).getData();
                copy=d.createProcessingInstruction(target,data);
                break;
        }
        if (copy==null) return(null);
        NodeList l=n.getChildNodes();
        for (int x=0;x<l.getLength();x++) {
            Node child=copyNode(d,l.item(x));
            if (child!=null) copy.appendChild(child);
        }
        return(copy);
    }
}
