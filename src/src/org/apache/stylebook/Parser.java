/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

import java.io.IOException;
import java.net.URL;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313202 $ $Date: 1999-11-30 13:28:55 +0100 (Tue, 30 Nov 1999) $
 */
public interface Parser extends Component{
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
    throws IOException, CreationException;

    /**
     * Parse the specified InputSource.
     *
     * @param in The InputSource to parse.
     * @return A non-null DOM Document object.
     * @exception IOException If an I/O error occourred accessing the specified
     *                        System-ID.
     * @exception CreationException If an error occourred parsing the document.
     */
    public Document parse(URL url)
    throws IOException, CreationException;

    /**
     * Create a new empty DOM Document object.
     *
     * @return A non-null DOM Document object.
     */
    public Document create();
}
