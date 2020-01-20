/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313278 $ $Date: 2000-07-14 20:31:36 +0200 (Fri, 14 Jul 2000) $
 */
public class StyleBook implements Logger {
    /** The project on wich we must operate */
    private URL project=null;
    /** Our properties */
    private Properties properties=null;
    /** The properties file */
    private String propertiesfile="org/apache/stylebook/data/stylebook.properties";
    /** Deny construction */
    private StyleBook() {}

    /** Create the StyleBook object */
    private StyleBook(String argv[])
    throws IOException {
        super();
        this.properties=new Properties();
        ClassLoader loader=this.getClass().getClassLoader();
        InputStream in=loader.getResourceAsStream(propertiesfile);
        this.properties.load(in);

        String project=null;
        String style=null;

        for(int x=0;x<argv.length;x++) {
            String line=argv[x];
            int pos=line.indexOf('=');
            if (pos<0) {
                if(project!=null) {
                    if(style!=null) {
                        exit("Duplicate Style (\""+style+ "\",\""+line+
                             "\")",1);
                    } else style=line;
                } else project=line;
            } else {
                String name=line.substring(0,pos);
                String value=line.substring(pos+1);
                String old=(String)this.properties.setProperty(name,value);
                log("Overriding "+name+"=\""+value+"\" (Old=\""+old+"\")");
            }
        }
        if(project==null) exit("Project file non specified",1);
        if(style==null) exit("Style file non specified",1);
        File projectFile=new File(project);
        File styleFile=new File(style);
        URLHandler u=new URLHandler(projectFile,styleFile);
        URL.setURLStreamHandlerFactory(u);
        this.project=new URL("sbk:/sources/"+projectFile.getName());
        log("Project URL: \""+this.project+"\"");
    }

    /**
     * Run StyleBook.
     *
     * @param argv Command Line Arguments
     */
    public static void main(String argv[]) throws IOException{
        try {
            StyleBook sb=new StyleBook(argv);
            Engine e=sb.getEngine();
            if (e==null) exit("Error creating engine",3);
            Project p=sb.getProject(e);
            if (p==null) exit("Error creating project",3);
            String targets=sb.properties.getProperty("targetDirectory");
            StreamHandler s=new StreamHandler(targets);
            Enumeration n=p.getEntryNames();
            while (n.hasMoreElements()) {
                String t=(String)n.nextElement();
                p.create(t,s.getOutputStream(t));
            }
        } catch (CreationException x) {
            Exception x2=x.getException();
            if(x2!=null) log(x2);
            exit(x,255);
        } catch (Exception x) {
            exit(x,255);
        }
        exit("All done successfully",0);
    }

    /** Create and Initialize Engine */
    private Engine getEngine() throws IOException {
        try {
            String parser=properties.getProperty("parserClass");
            URL config=new URL(properties.getProperty("engineConfig"));
            return(new BasicEngine(parser,config,this));
        } catch (InitializationException e) {
            log(e);
            return(null);
        }
    }

    /** Create and Initialize Project */
    private Project getProject(Engine e) throws IOException {
        try {
            Loader l=new Loader(e);
            URL u=new URL(properties.getProperty("loaderConfig"));
            return(l.load(u,project));
        } catch (LoadingException x) {
            log(x);
            return(null);
        }
    }

/* ************************************************************************** */

    /**
     * Report a message to the user.
     *
     * @param msg The log message to report.
     */
    public void log(Object source, String msg) {
        String cls=source.getClass().getName();
        cls=cls.substring(cls.lastIndexOf('.')+1);
        System.out.println("["+cls+"] "+msg);
    }

    /** Report a message thru System.out */
    private static void log(String msg) {
        System.out.println("[StyleBook] "+msg);
    }

    /** Report an exception thru System.out */
    private static void log(Exception e) {
        log("Caught "+e.getClass().getName());
        e.printStackTrace(System.out);
    }

    /** Report a message and exit with the specified error number */
    private static void exit(String msg, int exit) {
        log(msg);
        System.exit(exit);
    }

    /** Report an exception and exit with the specified error number */
    private static void exit(Exception e, int exit) {
        log(e);
        System.exit(exit);
    }
}
