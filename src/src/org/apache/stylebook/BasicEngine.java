/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313202 $ $Date: 1999-11-30 13:28:55 +0100 (Tue, 30 Nov 1999) $
 */
public class BasicEngine implements Engine {
    /** Our Parser instance */
    private Parser parser=null;
    /** The Producer table */
    private Hashtable producers=new Hashtable();
    /** The Processor table */
    private Hashtable processors=new Hashtable();
    /** The Printers table */
    private Hashtable printers=new Hashtable();
    /** The Logger instance */
    private Logger logger=null;
    /** The debug() activation flag */
    private boolean debug=false;

    /**
     * Create a new instance of this Engine.
     *
     * @param parser The name of the Parser class used to read configuration.
     * @param cont The SystemID of the configuration file.
     * @param logger The Logger used by this Engine.
     */
    public BasicEngine(String parser, URL conf, Logger logger)
    throws InitializationException {
        super();
        // Check the Logger
        if (logger==null)
            throw new InitializationException("No Logger instance specified");
        else this.logger=logger;
        // Parse the configuration file with the specified Parser
        Parser pars=ComponentFactory.getParser(parser);
        pars.init(this,new BasicParameters());
        Document d=null;
        try {
            if ((d=pars.parse(conf))==null)
                throw new InitializationException("Parser returned null doc");
        } catch (Exception e) {
            throw new InitializationException("Cannot parse configurations \""+
                                              conf+"\"");
        }
        // Step thru the different elements to configure the engine
        this.logger.log(this,"Initializing");
        Element e=d.getDocumentElement();
        if (!e.getTagName().equals("engine"))
            throw new InitializationException("Conf don't begin with <engine>");
        NodeList l=e.getChildNodes();
        for (int x=0; x<l.getLength(); x++) {
            if (l.item(x).getNodeType()!=Node.ELEMENT_NODE) continue;
            Element n=(Element)l.item(x);
            String t=n.getTagName();
            if (t.equals("debug"))
                if(n.getAttribute("enabled").equalsIgnoreCase("true"))
                    this.debug=true;
                else this.debug=false;
            else if (t.equals("parser")) configureParser(n);
            else if (t.equals("producer")) configureProducer(n);
            else if (t.equals("processor")) configureProcessor(n);
            else if (t.equals("printer")) configurePrinter(n);
            else throw new InitializationException("Unknown Element <"+t+">");
        }
        if(this.parser==null) this.parser=pars;
    }

    /** Configure the Parser from a givent Element */
    private void configureParser(Element e)
    throws InitializationException {
        if (this.parser!=null)
            throw new InitializationException("Parser already configured");
        String classname=e.getAttribute("class");
        this.parser=ComponentFactory.getParser(classname);
        this.parser.init(this,new NodeListParameters(e.getChildNodes()));
    }

    /** Configure a Producer from a givent Element */
    private void configureProducer(Element e)
    throws InitializationException {
        String name=e.getAttribute("name");
        String cls=e.getAttribute("class");
        if(name.length()==0)
            throw new InitializationException("No name specified for Producer");
        Producer p=ComponentFactory.getProducer(cls);
        p.init(this,new NodeListParameters(e.getChildNodes()));
        if(this.producers.put(name,p)!=null)
            throw new InitializationException("Duplucate Producer \""+
                                              name+"\"");
    }

    /** Configure a Processor from a givent Element */
    private void configureProcessor(Element e)
    throws InitializationException {
        String name=e.getAttribute("name");
        String cls=e.getAttribute("class");
        if(name.length()==0)
            throw new InitializationException("No name specified for Processor");
        Processor p=ComponentFactory.getProcessor(cls);
        p.init(this,new NodeListParameters(e.getChildNodes()));
        if(this.processors.put(name,p)!=null)
            throw new InitializationException("Duplucate Processor \""+
                                              name+"\"");
    }

    /** Configure a Printer from a givent Element */
    private void configurePrinter(Element e)
    throws InitializationException {
        String name=e.getAttribute("name");
        String cls=e.getAttribute("class");
        if(name.length()==0)
            throw new InitializationException("No name specified for Printer");
        Printer p=ComponentFactory.getPrinter(cls);
        p.init(this,new NodeListParameters(e.getChildNodes()));
        if(this.printers.put(name,p)!=null)
            throw new InitializationException("Duplucate Printer \""+
                                              name+"\"");
    }

    /**
     * Return the Parser instance owned by this Engine.
     *
     * @return A non-null Parser instance.
     */
    public Parser getParser() {
        return(this.parser);
    }

    /**
     * Return a Producer instance associated with the specified name.
     *
     * @param name A non-null String representing the Producer name.
     * @return A non-null Producer instance or null if no Producer is associated
     *         with the specified name.
     */
    public Producer getProducer(String name) {
        return((Producer)this.producers.get(name));
    }

    /**
     * Return a Processor instance associated with the specified name.
     *
     * @param name A non-null String representing the Processor name.
     * @return A non-null Processor instance or null if no Processor is
     *         associated with the specified name.
     */
    public Processor getProcessor(String name) {
        return((Processor)this.processors.get(name));
    }

    /**
     * Return a Printer instance associated with the specified name.
     *
     * @param name A non-null String representing the Printer name.
     * @return A non-null Printer instance or null if no Printer is
     *         associated with the specified name.
     */
    public Printer getPrinter(String name) {
        return((Printer)this.printers.get(name));
    }

    /**
     * Report a debug message to the user.
     *
     * @param msg The debug message to report.
     */
    public void debug(Object source, String msg) {
        if(this.debug) this.logger.log(source,msg);
    }

    /**
     * Report a log message to the user.
     *
     * @param msg The log message to report.
     */
    public void log(Object source, String msg) {
        this.logger.log(source,msg);
    }
}
