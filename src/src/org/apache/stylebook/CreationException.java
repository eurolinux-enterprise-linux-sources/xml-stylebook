/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

import org.w3c.dom.Document;

/**
 * A CreationException is thrown whenever an Entry cannot be produced for
 * any reason.
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313195 $ $Date: 1999-11-15 23:08:18 +0100 (Mon, 15 Nov 1999) $
 */
public class CreationException extends Exception {
    private transient Exception exception=null;
    private transient Document document=null;

    /**
     * Create a new CreationException instance.
     */
    public CreationException() {
        this(null,null,null);
    }

    /**
     * Create a new CreationException instance with a specified detail message.
     *
     * @param m The detail message.
     */
    public CreationException(String m) {
        this(m,null,null);
    }

    /**
     * Create a new CreationException instance with a specified nested
     * exception.
     *
     * @param e The nested exception.
     */
    public CreationException(Exception e) {
        this(null,e,null);
    }

    /**
     * Create a new CreationException instance with a specified invalid
     * document.
     *
     * @param d The invalid document.
     */
    public CreationException(Document d) {
        this(null,null,d);
    }

    /**
     * Create a new CreationException instance with a specified detail message
     * and a specified nested exception.
     *
     * @param m The detail message.
     * @param e The nested exception.
     */
    public CreationException(String m, Exception e) {
        this(m,e,null);
    }

    /**
     * Create a new CreationException instance with a specified detail message
     * and a specified invalid document.
     *
     * @param m The detail message.
     * @param d The invalid document.
     */
    public CreationException(String m, Document d) {
        this(m,null,d);
    }

    /**
     * Create a new CreationException instance with a specified nested
     * exception and a specified invalid document.
     *
     * @param e The nested exception.
     * @param d The invalid document.
     */
    public CreationException(Exception e, Document d) {
        this(null,e,d);
    }

    /**
     * Create a new CreationException instance with a specified detail message,
     * a specified nested exception and a specified invalid document.
     *
     * @param m The detail message.
     * @param e The nested exception.
     * @param d The invalid document.
     */
    public CreationException(String m, Exception e, Document d) {
        super(m);
        this.exception=e;
        this.document=d;
    }

    /**
     * Retrieve the nested exception of this CreationException.
     *
     * @return The invalid document or null if this wasn't specified.
     */
    public Exception getException() {
        return(this.exception);
    }

    /**
     * Retrieve the invalid document of this CreationException.
     *
     * @return The invalid document or null if this wasn't specified.
     */
    public Document getDocument() {
        return(this.document);
    }
}
