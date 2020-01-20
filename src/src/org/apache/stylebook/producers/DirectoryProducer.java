/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook.producers;

import org.apache.stylebook.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313274 $ $Date: 2000-07-09 14:44:21 +0200 (Sun, 09 Jul 2000) $
 */
public class DirectoryProducer extends AbstractComponent implements Producer {
    public Document produce(CreationContext c)
    throws CreationException, java.io.IOException {
        // Create the root document and the root element
        Document d=engine.getParser().create();
        Element root=d.createElement("directory");
        // Retrieve the REAL url
        URL source=c.getSourceURL().openConnection().getURL();
        // Proceed if the protocol is "file:"
        if (source.getProtocol().equals("file")) {
            // Trim the file name and open the directory
            String src=source.getFile();
            //
            // These lines seems to break UNIX files (duplicating the name
            // of the directory.
            // Thanks to "Gerard van Enk" <gerard.van.enk@eo.nl>
            //
            //for(int x=0;x<src.length();x++) if (src.charAt(x)!='/') {
            //    src=src.substring(x);
            //    break;
            //}
            File file=new File(src).getCanonicalFile();
            if (!file.isDirectory()) 
                throw new CreationException(c.getSourceURL()+" ("+file+
                                            ") is not a Directory");
            // List the files of a directory
            File list[]=file.listFiles();
            for (int x=0;x<list.length;x++) {
                Element e=d.createElement("entry");
                String name=list[x].getName();
                if (list[x].isDirectory()) {
                    name=list[x].getName()+"/";
                    e.setAttribute("directory","true");
                }
                e.setAttribute("href",name);
                e.appendChild(d.createTextNode(name));
                root.appendChild(e);
            }
        } else if (source.getProtocol().equals("jar")) {
            // Check the JAR url for jar file and jar entry
            int index=source.getFile().indexOf('!');
            String jarname=source.getFile();
            String dirname="";
            if (index>0) {
                jarname=source.getFile().substring(0,index);
                dirname=source.getFile().substring(index+1);
                if (dirname.equals("/")) dirname="";
            }
            // Check if we can open the jar file (only file:)
            URL jarurl=new URL(jarname);
            if (!jarurl.getProtocol().equalsIgnoreCase("file"))
                throw new CreationException("Cannot open zip files over "+
                                            jarurl.getProtocol()+": protocol");
            jarname=jarurl.getFile();
            // Remove leading '/' chars from jar name and jar entry
            if (System.getProperty("path.separator").equals(";")) {
		// I believe that this is a workaround for a Windows JDK1.1 bug
                for(int x=0;x<jarname.length();x++) if (jarname.charAt(x)!='/') {
                    jarname=jarname.substring(x);
                    break;
                }
            }
            for(int x=0;x<dirname.length();x++) if (dirname.charAt(x)!='/') {
                dirname=dirname.substring(x);
                break;
            }
            // Open the JAR file and check if dirname is a valid directory
            ZipFile f=new ZipFile(jarname);
            if (dirname.length()>0) {
                ZipEntry e=f.getEntry(dirname);
                if (e==null)
                    throw new CreationException("Cannot find \""+dirname+
                                                "\" in \""+jarname+"\"");
                else if (!e.isDirectory())
                    throw new CreationException("Entry \""+dirname+"\" in \""+
                                                jarname+"\" is not a Directory");
            }
            // Enumerate the JAR entries
            Enumeration list=new ZipFile(jarname).entries();
            while(list.hasMoreElements()) {
                ZipEntry zipentry=(ZipEntry)list.nextElement();
                // Check if the entry begins w/ dirname and doesn't go further
                String entry=zipentry.getName();
                if(!entry.startsWith(dirname)) continue;
                entry=entry.substring(dirname.length());
                if(entry.length()==0) continue;
                if ((entry.indexOf('/')!=-1) &&
                    (entry.indexOf('/')!=entry.length()-1)) continue;
                // Create the elements
                Element elem=d.createElement("entry");
                elem.setAttribute("href",entry);
                if(zipentry.isDirectory()) elem.setAttribute("directory","true");
                elem.appendChild(d.createTextNode(entry));
                root.appendChild(elem);
            }
        }
        d.appendChild(root);
        return(d);
    }
}
