/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook.printers;

import org.apache.stylebook.*;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Method;
import org.apache.xml.serialize.XHTMLSerializer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.w3c.dom.Document;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313265 $ $Date: 2000-06-06 03:03:21 +0200 (Tue, 06 Jun 2000) $
 */
public class HTMLSerializer extends AbstractComponent implements org.apache.stylebook.Printer {

    /**
     * Print a DOM Document.
     *
     * @param doc The Document to print.
     * @param out The OutputStream used for printing.
     * @param env The Environment of this printing request.
     * @exception IOException If an I/O error occourred accessing resources.
     * @exception CreationException If the Document cannot be printed.
     */
    public void print(Document doc, CreationContext c, OutputStream out)
    throws CreationException, IOException {
        // Thanks to  Ingo Macherius <macherius@darmstadt.gmd.de>
        //OutputFormat f=new OutputFormat(OutputFormat.METHOD_XHTML,
        OutputFormat f=new OutputFormat(Method.XHTML,
                                        "US-ASCII",true);
        //Serializer s=Serializer.makeSerializer(out,f);
        XHTMLSerializer s = new XHTMLSerializer(out, f);
        s.serialize(doc);
    }
}
