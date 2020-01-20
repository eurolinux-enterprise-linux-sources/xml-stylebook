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
import java.util.Vector;
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
public class BasicEntry extends BasicContext implements Entry {
    private Producer producer=null;
    private Printer printer=null;
    private Vector tasks=new Vector();

    public BasicEntry(Engine e, URL s, String t, Element d, Parameters p)
    throws LoadingException {
        super(s,t);
        String prod=d.getAttribute("producer");
        String prnt=d.getAttribute("printer");
        e.debug(this,"Creating Entry Source=\""+s+"\" Target=\""+t+"\""+
                     " ["+prod+"/"+prnt+"]");
        if ((this.producer=e.getProducer(prod))==null)
            throw new LoadingException("Invalid Producer \""+prod+"\"");
        if ((this.printer=e.getPrinter(prnt))==null)
            throw new LoadingException("Invalid Printer \""+prnt+"\"");
        NodeList l=d.getChildNodes();
        Parameters p2=new NodeListParameters(l).merge(p);
        for(int x=0;x<l.getLength();x++) {
            if(l.item(x).getNodeType()!=Node.ELEMENT_NODE) continue;
            Element el=(Element)l.item(x);
            if(el.getTagName().equals("parameter")) continue;
            if(!el.getTagName().equals("processor"))
                throw new LoadingException("Invalid Tag <"+el.getTagName()+">");
            Processor proc=e.getProcessor(el.getAttribute("name"));
            if(proc==null)
                    throw new LoadingException("Invalid Processr \""+proc+"\"");
            Parameters par=new NodeListParameters(el.getChildNodes()).merge(p2);
            this.tasks.add(new Task(proc,par));
        }
        this.merge(p2);
    }

    public void create(OutputStream out)
    throws IOException, CreationException {
        Document d=this.producer.produce(this);
        Enumeration e=this.tasks.elements();
        while(e.hasMoreElements()) {
            Task t=(Task)e.nextElement();
            d=t.processor.process(d,this,t.parameters);
        }
        this.printer.print(d,this,out);
    }

    private class Task {
        public Processor processor=null;
        public Parameters parameters=null;

        public Task(Processor proc, Parameters param) {
            this.processor=proc;
            this.parameters=param;
        }
    }
    
    public boolean equals(Entry e) {
        return(false);
    }
}
