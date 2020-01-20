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
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.zip.ZipFile;

/**
 * The URLHandler class implements URLStreamHandlerFactory, wich is the 
 * factory used by StyleBook to produce URLConnections.<br>
 * Handled by this factory are the &quot;res&quot; protocol (wich handles
 * resources derived from the actual or the system class loaders) and the
 * &quot;sbk&quot; protocol wich handles files in the style name space and the
 * sources name space.
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313217 $ $Date: 1999-12-01 15:21:27 +0100 (Wed, 01 Dec 1999) $
 */
public class URLHandler implements URLStreamHandlerFactory {
    private URL sty=null;
    private URL src=null;
    
    public URLHandler() {
        super();
    }

    public URLHandler(File sources, File style)
    throws IOException {
        super();
        if (sources==null) throw new IOException("Null Sources");
        if (style==null) throw new IOException("Null Style");

        if (style.isDirectory()) {
            this.sty=style.getCanonicalFile().toURL();
        } else if (style.exists()) {
            // Check if it's a proper ZipFile
            new ZipFile(style).close();
            this.sty=new URL("jar:"+style.getCanonicalFile().toURL()+"!/");
        } else throw new IOException("Style File/Directory not found");

        if (sources.isFile()) {
            this.src=sources.getCanonicalFile().toURL();
        } else throw new IOException("Sources File/Directory not found");
    }
    
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (protocol==null) return(null);
        try {
            if (protocol.equalsIgnoreCase("sbk"))
                return(new StylebookStreamHandler(this.sty,this.src));
            if (protocol.equalsIgnoreCase("res"))
                return(new ResourceStreamHandler());
            else return(null);
        } catch (IOException e) {
            return(null);
        }
    }

    private class StylebookStreamHandler extends URLStreamHandler {
        private URL style=null;
        private URL sources=null;

        private StylebookStreamHandler() {
            super();
        }

        private StylebookStreamHandler(URL style, URL sources)
        throws IOException {
            this();
            this.style=style;
            this.sources=sources;
        }

        public URLConnection openConnection(URL u) throws IOException {
            if (u==null) return(null);
            String file=u.getFile();
            for(int x=0;x<file.length();x++) if (file.charAt(x)!='/') {
                file=file.substring(x);
                break;
            }
            if (file.startsWith("style/")) {
                if (file.length()==6) return(this.style.openConnection());
                return new URL(this.style,file.substring(6)).openConnection();
            } else if (file.startsWith("sources/")) {
                if (file.length()==8) return(this.sources.openConnection());
                return new URL(this.sources,file.substring(8)).openConnection();
            } else throw new IOException("Invalid StyleBook URL \""+u+"\"");
        }
    }

    private class ResourceStreamHandler extends URLStreamHandler {

        private ResourceStreamHandler() {
            super();
        }

        public URLConnection openConnection(URL u) throws IOException {
            ClassLoader l=this.getClass().getClassLoader();
            if (l==null) return(null);
            String file=u.getFile();
            for(int x=0;x<file.length();x++) if (file.charAt(x)!='/') {
                file=file.substring(x);
                break;
            }
            URL x=l.getResource(file);
            if (x==null) x=ClassLoader.getSystemResource(u.getFile());
            if (x==null) return(null);
            return(x.openConnection());
        }
    }
}
    