/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook.producers;

import org.apache.stylebook.*;
import java.io.IOException;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313195 $ $Date: 1999-11-15 23:08:18 +0100 (Mon, 15 Nov 1999) $
 */
public class ProjectProducer extends AbstractComponent implements Producer {
    public Document produce(CreationContext c)
    throws CreationException, java.io.IOException {
        String project=c.getParameter("stylebook.project");
        if(project==null) throw new CreationException("Cannot produce project");
        return(engine.getParser().parse(new InputSource(project)));
    }
}
