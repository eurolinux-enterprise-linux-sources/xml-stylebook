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

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313195 $ $Date: 1999-11-15 23:08:18 +0100 (Mon, 15 Nov 1999) $
 */
public class BasicParameters implements Parameters {
    private Hashtable table=null;

    public BasicParameters() {
        super();
        this.table=new Hashtable();
    }

    /**
     * Retrieve a value for a specified parameter.
     *
     * @param name The parameter name.
     * @return The parameter value or null if the parameter has no specified
     *         value.
     */
    public String getParameter(String name) {
        return((String)this.table.get(name));
    }

    /**
     * Retrieve a value for a specified parameter.
     *
     * @param name The parameter name.
     * @param def The parameter default value.
     * @return The parameter value or def if the parameter has no specified
     *         value.
     */
    public String getParameter(String name, String def) {
        String val=getParameter(name);
        if (val==null) return(def);
        else return(val);
    }

    /**
     * Get all valid parameter names.
     *
     * @return A non-null (maybe empty) Enumeration.
     */
    public Enumeration getParameterNames() {
        return(table.keys());
    }

    /**
     * Set or update parameter value.
     *
     * @parameter name The parameter name.
     * @parameter value The parameter value.
     * @return The old value of the parameter or null.
     */
    public String setParameter(String name, String value) {
        if (value==null) return((String)this.table.remove(name));
        else return((String)this.table.put(name,value));
    }

    /**
     * Merge the values from another Parameters object into this instance.
     *
     * @param p The Parameters object from wich values must be retrieved.
     */
    public Parameters merge(Parameters p) {
        if (p==null) return(this);
        Enumeration e=p.getParameterNames();
        while (e.hasMoreElements()) {
            String name=(String)e.nextElement();
            String value=p.getParameter(name);
            this.setParameter(name,value);
        }
        return(this);
    }
}