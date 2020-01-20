/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313221 $ $Date: 1999-12-01 19:39:14 +0100 (Wed, 01 Dec 1999) $
 */
public class ResourceEntry implements Entry {
    private URL src=null;

    public ResourceEntry(Engine e, URL s, String t) {
        e.debug(this,"Creating Resource Source=\""+s+"\" Target=\""+t+"\"");
        this.src=s;
    }

    public void create(OutputStream o)
    throws IOException, CreationException {
        InputStream in=new BufferedInputStream(this.src.openStream());
        OutputStream out=new BufferedOutputStream(o);
        int data=-1;
        while((data=in.read())!=-1) out.write(data);
        in.close();
        out.flush();
    }
    
    public boolean equals(Entry e) {
        try {
            ResourceEntry r=(ResourceEntry)e;
            if(r.src.equals(this.src)) return(true);
            return(false);
        } catch (Exception x) {
            return(false);
        }
    }
}
