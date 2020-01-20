/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook.parsers;

import org.apache.stylebook.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313202 $ $Date: 1999-11-30 13:28:55 +0100 (Tue, 30 Nov 1999) $
 */
public class CachingParser extends AbstractParser implements Parser {
    private Parser parser=null;
    private int cachesize=10;
    private Hashtable cache=new Hashtable();

    /**
     * Initialize this component instance.
     *
     * @param e The Engine owning this Component instance
     * @param p This component initialization Parameters
     */
    public void init(Engine e, Parameters p)
    throws InitializationException {
        super.init(e,p);
        // Instantiate the sub-parser
        String cls=p.getParameter("parserclass");
        if (cls==null)
            throw new InitializationException("Parameter \"parserclass\" not "+
                                              "specified");
        this.parser=ComponentFactory.getParser(cls);
        // Create the parameters for the sub-parser
        Parameters p2=new BasicParameters();
        Enumeration n=p.getParameterNames();
        while(n.hasMoreElements()) {
            String name=(String)n.nextElement();
            String value=p.getParameter(name);
            if(!name.startsWith("parser.")) continue;
            name=name.substring(7);
            p2.setParameter(name,value);
        }
        // Initialize the sub-parser
        this.parser.init(e,p2);
        // Set up the cache size
        try {
            this.cachesize=Integer.parseInt(p.getParameter("cachesize"));
        } catch (Exception x) {
            debug("Cache size not specified (default=10)");
            this.cachesize=10;
        }
    }

    /**
     * Create a new empty DOM Document object.
     *
     * @return A non-null DOM Document object.
     * @exception DocumentException If the new Document cannot be created.
     */
    public Document create() {
        return(this.parser.create());
    }

    /**
     * Parse the specified InputSource.
     *
     * @param in The InputSource to parse.
     * @return A non-null DOM Document object.
     * @exception IOException If an I/O error occourred accessing the specified
     *                        System-ID.
     * @exception CreationException If an error occourred parsing the document.
     */
    public Document parse(InputSource in)
    throws IOException, CreationException {
        String name=in.getSystemId();
        if (name==null) return(this.parser.parse(in));
        CachedEntry ent=(CachedEntry)this.cache.get(name);
        Document doc=null;
        if (ent==null) {
            doc=this.parser.parse(in);
            ent=new CachedEntry(doc);
            this.cache.put(name,ent);
        } else {
            ent.hits++;
            doc=ent.document;
            if (doc==null) doc=this.parser.parse(in);
            else log("Serving cached document \""+name+"\" ("+ent.hits+")");
            ent.document=doc;
        }
        purge();
        return(doc);
    }

    private void purge() {
        // Cache purging algorithm...
    }

    private class CachedEntry {
        public Document document=null;
        public int hits=0;

        public CachedEntry(Document doc) {
            this.document=doc;
        }
    }
}