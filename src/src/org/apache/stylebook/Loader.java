/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.xml.sax.InputSource;
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
 * @version CVS $Revision: 313202 $ $Date: 1999-11-30 13:28:55 +0100 (Tue, 30 Nov 1999) $
 */
public class Loader {
    private Engine engine=null;

    public Loader(Engine e) {
        this.engine=e;
    }

    public Project load(URL loader, URL project)
    throws LoadingException {
        try {
            // Parse the project file
            this.engine.log(this,"Parsing Project file");
            Document p=this.engine.getParser().parse(project);
            // Prepare a temporary creationcontext
            CreationContext c=new BasicContext(project,"");
            // Parse the loader configuration file
            this.engine.debug(this,"Parsing Loader configuration");
            Document d=this.engine.getParser().parse(loader);
            // Check the parsed loader configuration root element
            if (!d.getDocumentElement().getTagName().equals("loader"))
                throw new LoadingException("Loader config is not <loader>");
            // Apply the different processors
            NodeList l=d.getDocumentElement().getChildNodes();
            for(int x=0;x<l.getLength();x++) {
                if (l.item(x).getNodeType()!=Node.ELEMENT_NODE) continue;
                p=process(p,(Element)l.item(x),c);
            }
            // Create the project
            Parameters param=new BasicParameters();
            param.setParameter("stylebook.project",project.toExternalForm());
            Project pr=new Project(this.engine,p,param,project);
            return(pr);
        } catch (IOException e) {
            this.engine.log(this,"Caught "+e.getClass().getName());
            throw new LoadingException(e.getMessage());
        } catch (CreationException e) {
            this.engine.log(this,"Caught "+e.getClass().getName());
            throw new LoadingException(e.getMessage());
        }
    }

    private Document process(Document d, Element e, CreationContext c)
    throws IOException, CreationException, LoadingException {
        if(!e.getTagName().equals("processor"))
            throw new LoadingException("Unknown element <"+e.getTagName()+">");
        String name=e.getAttribute("name");
        Parameters p=new NodeListParameters(e.getChildNodes());
        d=this.engine.getProcessor(name).process(d,c,p);
        return(d);
    }
}