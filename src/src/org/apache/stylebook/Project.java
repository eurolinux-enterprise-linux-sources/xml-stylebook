/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313221 $ $Date: 1999-12-01 19:39:14 +0100 (Wed, 01 Dec 1999) $
 */
public class Project {
    private Hashtable entries=new Hashtable();
    private Engine engine=null;
    private URL urlbase=null;

    public Project(Engine engine, Document project, Parameters parambase, URL u)
    throws LoadingException, IOException {
        this.engine=engine;
        this.urlbase=u;
        Element root=project.getDocumentElement();
        if(!root.getTagName().equals("project"))
            throw new LoadingException("Project does not start with <project>");
        NodeList l=root.getChildNodes();
        Parameters parameters=new NodeListParameters(l).merge(parambase);
        processNodeList(l,this.urlbase,parameters);
    }

    private void processNodeList(NodeList l, URL urlbase, Parameters parambase)
    throws LoadingException, IOException {
        for(int x=0;x<l.getLength();x++) {
            if(l.item(x).getNodeType()!=Node.ELEMENT_NODE) continue;
            Element e=(Element)l.item(x);
            String name=e.getTagName();
            if(name.equals("parameter")) continue;
            else if(name.equals("create")) setCreateEntry(e,urlbase,parambase);
            else if(name.equals("resource")) setResourceEntry(e,urlbase);
            else if(name.equals("process")) processEntry(e,urlbase,parambase);
            else throw new LoadingException("Invalid element <"+name+">");
        }
    }

    public Enumeration getEntryNames() {
        return(this.entries.keys());
    }

    public void create(String name, OutputStream out)
    throws IOException, CreationException {
        this.engine.log(this,"Creating \""+name+"\"");
        Entry e=(Entry)this.entries.get(name);
        if(e==null) throw new CreationException("No Entry \""+name+"\"");
        e.create(out);
    }

    private void setCreateEntry(Element e, URL urlbase, Parameters parambase)
    throws LoadingException, IOException {
        URL source=new URL(urlbase,e.getAttribute("source"));
        String target=e.getAttribute("target");
        Entry entry=new BasicEntry(this.engine,source,target,e,parambase);
        Entry old=(Entry)this.entries.put(target,entry);
        if(old!=null) if(!old.equals(entry))
            throw new LoadingException("Duplicate Entry \""+target+"\"");
    }

    private void setResourceEntry(Element e, URL urlbase)
    throws LoadingException, IOException {
        URL source=new URL(urlbase,e.getAttribute("source"));
        String target=e.getAttribute("target");
        Entry entry=new ResourceEntry(this.engine,source,target);
        Entry old=(Entry)this.entries.put(target,entry);
        if(old!=null) if(!old.equals(entry))
            throw new LoadingException("Duplicate Entry \""+target+"\"");
    }
    
    private void processEntry(Element e, URL urlbase, Parameters parambase)
    throws LoadingException, IOException {
        // Get the producer and the source, create a temporary context
        String prod=e.getAttribute("producer");
        URL source=new URL(urlbase,e.getAttribute("source"));
        CreationContext ctx=new BasicContext(source,"");
        ctx.merge(parambase);
        // Try to load the producer
        this.engine.debug(this,"Processing Source=\""+source+"\" ["+prod+"]");
        Producer producer=this.engine.getProducer(prod);
        if (producer==null)
            throw new LoadingException("Invalid Producer \""+prod+"\"");
        // Get local parameters
        NodeList l=e.getChildNodes();
        ctx.merge(new NodeListParameters(l));
        // Try to create the subproject
        Document doc=null;
        try {        
            // Produce the document
            doc=producer.produce(ctx);
            // For every processor, generate the project
            for(int x=0;x<l.getLength();x++) {
                if(l.item(x).getNodeType()!=Node.ELEMENT_NODE) continue;
                Element el=(Element)l.item(x);
                if(el.getTagName().equals("parameter")) continue;
                if(!el.getTagName().equals("processor"))
                    throw new LoadingException("Invalid <"+el.getTagName()+">");
                Processor proc=this.engine.getProcessor(el.getAttribute("name"));
                if(proc==null)
                        throw new LoadingException("No Processr \""+proc+"\"");
                Parameters p=new NodeListParameters(el.getChildNodes()).merge(ctx);
                doc=proc.process(doc,ctx,p);
            }
        } catch (CreationException x) {
            x.printStackTrace(System.out);
            throw new LoadingException("Processing Entry ("+x.getMessage()+")");
        }
        // Check for proper document
        if (doc==null) return;
        // Do as a top-level project recursively
        Element root=doc.getDocumentElement();
        if(!root.getTagName().equals("project"))
            throw new LoadingException("Result of process is not <project>");
        l=root.getChildNodes();
        Parameters p=new NodeListParameters(l).merge(ctx);
        processNodeList(l,source,p);

    }
}
