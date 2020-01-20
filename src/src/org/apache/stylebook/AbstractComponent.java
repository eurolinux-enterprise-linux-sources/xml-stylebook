/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313195 $ $Date: 1999-11-15 23:08:18 +0100 (Mon, 15 Nov 1999) $
 */
public abstract class AbstractComponent implements Component {
    /** The Engine owning this Component instance. */
    protected Engine engine=null;
    /** This component initialization Parameters. */
    protected Parameters params=null;

    /**
     * Initialize this component instance.
     *
     * @param e The Engine owning this Component instance
     * @param p This component initialization Parameters
     */
    public void init(Engine e, Parameters p)
    throws InitializationException {
        if (e==null) throw new NullPointerException("Null Engine");
        if (p==null) throw new NullPointerException("Null Parameters");
        this.engine=e;
        this.params=p;
        this.debug("Initializing");
    }

    /**
     * Report a debug message to the owning Engine.
     *
     * @param msg The debug message to report.
     */
    protected void debug(String message) {
        this.engine.debug(this, message);
    }

    /**
     * Report a log message to the owning Engine.
     *
     * @param msg The log message to report.
     */
    protected void log(String message) {
        this.engine.log(this, message);
    }
}
