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
public class ComponentFactory {
    /** Deny construction. */
    private ComponentFactory() {
        super();
    }

    /**
     * Create a new Component instance.
     *
     * @param classname The name of the Component class.
     * @return A non-null Component instance.
     * @exception InitializationException If the Component instance cannot be
     *                                    created.
     */
    public static Component getComponent(String classname)
    throws InitializationException {
        try {
            return ((Component)Class.forName(classname).newInstance());
        } catch (ClassCastException e) {
            throw new InitializationException("Class \""+classname+
                                              "\" is not a Component");
        } catch (InstantiationException e) {
            throw new InitializationException("Class \""+classname+
                                              "\" cannot be instantiated");
        } catch (IllegalAccessException e) {
            throw new InitializationException("Class \""+classname+
                                              "\" cannot be accessed");
        } catch (ClassNotFoundException e) {
            throw new InitializationException("Class \""+classname+
                                              "\" not found");
        }
    }

    /**
     * Create a new Parser instance.
     *
     * @param classname The name of the Parser class.
     * @return A non-null Parser instance.
     * @exception InitializationException If the Parser instance cannot be
     *                                    created.
     */
    public static Parser getParser(String classname)
    throws InitializationException {
        try {
            return ((Parser)getComponent(classname));
        } catch (ClassCastException e) {
            throw new InitializationException("Class \""+classname+
                                              "\" is not a Parser");
        }
    }

    /**
     * Create a new Producer instance.
     *
     * @param classname The name of the Producer class.
     * @return A non-null Producer instance.
     * @exception InitializationException If the Producer instance cannot be
     *                                    created.
     */
    public static Producer getProducer(String classname)
    throws InitializationException {
        try {
            return ((Producer)getComponent(classname));
        } catch (ClassCastException e) {
            throw new InitializationException("Class \""+classname+
                                              "\" is not a Producer");
        }
    }

    /**
     * Create a new Processor instance.
     *
     * @param classname The name of the Processor class.
     * @return A non-null Processor instance.
     * @exception InitializationException If the Processor instance cannot be
     *                                    created.
     */
    public static Processor getProcessor(String classname)
    throws InitializationException {
        try {
            return ((Processor)getComponent(classname));
        } catch (ClassCastException e) {
            throw new InitializationException("Class \""+classname+
                                              "\" is not a Processor");
        }
    }

    /**
     * Create a new Printer instance.
     *
     * @param classname The name of the Printer class.
     * @return A non-null Printer instance.
     * @exception InitializationException If the Printer instance cannot be
     *                                    created.
     */
    public static Printer getPrinter(String classname)
    throws InitializationException {
        try {
            return ((Printer)getComponent(classname));
        } catch (ClassCastException e) {
            throw new InitializationException("Class \""+classname+
                                              "\" is not a Printer");
        }
    }
}
