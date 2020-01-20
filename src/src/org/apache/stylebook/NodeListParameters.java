/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

import java.util.Enumeration;
import java.util.Hashtable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313195 $ $Date: 1999-11-15 23:08:18 +0100 (Mon, 15 Nov 1999) $
 */
public class NodeListParameters extends BasicParameters implements Parameters {

    public NodeListParameters(NodeList l) {
        super();
        if (l!=null) for (int x=0; x<l.getLength(); x++) {
            if (l.item(x).getNodeType()!=Node.ELEMENT_NODE) continue;
            Element e=(Element)l.item(x);
            if (!e.getTagName().equals("parameter")) continue;
            this.setParameter(e.getAttribute("name"),e.getAttribute("value"));
        }
    }
}