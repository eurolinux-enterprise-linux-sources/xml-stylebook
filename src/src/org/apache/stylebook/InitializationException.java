/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

/**
 * A InitializationException is thrown whenever a Component cannot be
 * initialized for any reason.
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313195 $ $Date: 1999-11-15 23:08:18 +0100 (Mon, 15 Nov 1999) $
 */
public class InitializationException extends Exception {
    /**
     * Create a new InitializationException instance.
     */
    public InitializationException() {
        super();
    }

    /**
     * Create a new InitializationException instance with a specified detail
     * message.
     *
     * @param m The detail message.
     */
    public InitializationException(String m) {
        super(m);
    }

}
