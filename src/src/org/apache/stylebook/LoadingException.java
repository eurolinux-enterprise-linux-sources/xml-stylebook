/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

/**
 * A LoadingException is thrown whenever the Loaded cannot load and create a
 * project.
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313195 $ $Date: 1999-11-15 23:08:18 +0100 (Mon, 15 Nov 1999) $
 */
public class LoadingException extends Exception {
    /**
     * Create a new LoadingException instance.
     */
    public LoadingException() {
        super();
    }

    /**
     * Create a new LoadingException instance with a specified detail
     * message.
     *
     * @param m The detail message.
     */
    public LoadingException(String m) {
        super(m);
    }

}
