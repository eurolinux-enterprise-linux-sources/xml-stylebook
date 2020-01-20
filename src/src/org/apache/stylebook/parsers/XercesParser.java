/*****************************************************************************
 * Copyright (C) 1999 The Apache Software Foundation.   All rights reserved. *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1,  a copy of wich has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package org.apache.stylebook.parsers;

import java.io.IOException;
import org.apache.stylebook.AbstractComponent;
import org.apache.stylebook.Parser;
import org.apache.stylebook.CreationException;
import org.apache.xerces.dom.DocumentImpl;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 *
 * @author <a href="mailto:pier@apache.org">Pierpaolo Fumagalli</a>
 * @author Copyright 1999 &copy; <a href="http://www.apache.org">The Apache
 *         Software Foundation</a>. All rights reserved.
 * @version CVS $Revision: 313289 $ $Date: 2002-01-22 19:24:53 +0100 (Tue, 22 Jan 2002) $
 */
public class XercesParser extends AbstractParser implements Parser,ErrorHandler {
  DocumentBuilderFactory docFactory;
  
  /**
   * Instantiate a DocumentBuilderFactory.
   */
  public XercesParser()
  {
    docFactory = DocumentBuilderFactory.newInstance();
    docFactory.setNamespaceAware(true);
    docFactory.setExpandEntityReferences(true);
  }  


    /**
     * Parse the specified InputSource.
     *
     * @param in The InputSource to parse.
     * @return A non-null DOM Document object.
     * @exception IOException If an I/O error occourred accessing the specified
     *                        System-ID.
     * @exception CreationException If an error occourred parsing the document.
     */
    public Document parse(InputSource in)
    throws IOException, CreationException {
        this.debug("Parsing \""+in.getSystemId()+"\"");
        try {
          DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
          Document document = docBuilder.parse(in);

          if (document==null) {
              throw new CreationException("Cannot retrieve parsed document");
          } else return(document);
        } catch (IOException e) {
          this.log(e.getMessage());
          throw new CreationException("IOException caught while using DocumentBuilder to parse an XML document.",e);
        } catch (ParserConfigurationException e) {
          this.log(e.getMessage());
          throw new CreationException("ParserConfigurationException caught while using DocumentBuilder to parse an XML document.",e);
        } catch (SAXNotRecognizedException e) {
            this.log(e.getMessage());
            throw new CreationException("SAXNotRecognizedException caught while using DocumentBuilder to parse an XML document.",e);
        } catch (SAXException e) {
            this.log(e.getMessage());
            throw new CreationException("SAXException caught using DocumentBuilder to parse an XML document.",e);
        }
    }

    /**
     * Create a new empty DOM Document object.
     *
     * @return A non-null DOM Document object.
     * @exception DocumentException If the new Document cannot be created.
     */
    public Document create() {
        this.debug("Creating new Document");
        return new DocumentImpl();
    }

    /**
     * Receive notification of a recoverable error.
     *
     * @param e The Exception thrown during parsing.
     * @exception SAXException The Exception notifying the client.
     */
    public void error(SAXParseException e)
    throws SAXException {
        throw new SAXException(e.getMessage()+" [File: \""+e.getSystemId()+
                               "\" Line: "+e.getLineNumber()+" Column: "+
                               e.getColumnNumber()+"]",e);
    }

    /**
     * Receive notification of a non-recoverable error.
     *
     * @param e The Exception thrown during parsing.
     * @exception SAXException The Exception notifying the client.
     */
    public void fatalError(SAXParseException e)
    throws SAXException {
        throw new SAXException(e.getMessage()+" [File: \""+e.getSystemId()+
                               "\" Line: "+e.getLineNumber()+" Column: "+
                               e.getColumnNumber()+"]",e);
    }

    /**
     * Receive notification of a warning.
     *
     * @param e The Exception thrown during parsing.
     * @exception SAXException The Exception notifying the client.
     */
    public void warning(SAXParseException e)
    throws SAXException {
        throw new SAXException(e.getMessage()+" [File: \""+e.getSystemId()+
                               "\" Line: "+e.getLineNumber()+" Column: "+
                               e.getColumnNumber()+"]",e);
    }
}