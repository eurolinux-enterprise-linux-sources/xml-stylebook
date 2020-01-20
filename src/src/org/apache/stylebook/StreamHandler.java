/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313195 $ $Date: 1999-11-15 23:08:18 +0100 (Mon, 15 Nov 1999) $
 */
public class StreamHandler {
    private File base=null;

    public StreamHandler(String base) throws IOException {
        if(base==null) base=System.getProperty("user.dir");
        this.base=new File(base).getCanonicalFile();
        if(!this.base.isDirectory()) {
            mkdir(this.base);
        }
    }

    public OutputStream getOutputStream(String name)
    throws IOException {
        File f=new File(name);
        if (f.isAbsolute()) return new FileOutputStream(f);
        if (f.getParent()!=null) mkdir(new File(base,f.getParent()));
        return(new FileOutputStream(new File(base,name)));
    }

    private void mkdir(File file)
    throws IOException {
        if (file==null) return;
        mkdir(file.getParentFile());
        if (file.isDirectory()) return;
        else file.mkdir();
    }
}
