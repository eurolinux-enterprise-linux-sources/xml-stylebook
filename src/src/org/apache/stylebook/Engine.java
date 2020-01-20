/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook;

import java.util.Enumeration;
import org.w3c.dom.Document;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313195 $ $Date: 1999-11-15 23:08:18 +0100 (Mon, 15 Nov 1999) $
 */
public interface Engine {
    /**
     * Return the Parser instance owned by this Engine.
     *
     * @return A non-null Parser instance.
     */
    public Parser getParser();

    /**
     * Return a Producer instance associated with the specified name.
     *
     * @param name A non-null String representing the Producer name.
     * @return A non-null Producer instance or null if no Producer is associated
     *         with the specified name.
     */
    public Producer getProducer(String name);

    /**
     * Return a Processor instance associated with the specified name.
     *
     * @param name A non-null String representing the Processor name.
     * @return A non-null Processor instance or null if no Processor is
     *         associated with the specified name.
     */
    public Processor getProcessor(String name);

    /**
     * Return a Printer instance associated with the specified name.
     *
     * @param name A non-null String representing the Printer name.
     * @return A non-null Printer instance or null if no Printer is
     *         associated with the specified name.
     */
    public Printer getPrinter(String name);

    /**
     * Report a debug message to the user.
     *
     * @param msg The debug message to report.
     */
    public void debug(Object source, String message);

    /**
     * Report a log message to the user.
     *
     * @param msg The log message to report.
     */
    public void log(Object source, String message);
}
