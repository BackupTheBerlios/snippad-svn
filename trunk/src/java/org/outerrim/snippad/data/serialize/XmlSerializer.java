/*
 * XmlSerializer.java
 * Created on Sep 17, 2004
 *
 * Copyright (c)2004 Michael Osterlie
 *
 * This file is part of snippad.
 *
 * snippad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * snippad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.outerrim.snippad.data.serialize;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.outerrim.snippad.data.WikiWord;

/**
 * SnipPad serializer for saving a snippad document to an XML file.
 *
 * @author darkjedi
 */
public class XmlSerializer implements WikiSerializer {
    private static final String VERSION = "0.2";

    private static final String ROOT = "snippad";

    private static final String SYSTEMID =
        "http://snippad.berlios.de/snippad.dtd";

    private static final Log LOG = LogFactory.getLog( XmlSerializer.class );

    /**
     * @see org.outerrim.snippad.data.serialize.WikiSerializer#load(
     *      java.io.InputStream)
     */
    public WikiWord load( final InputStream input ) throws SerializeException {
        try {
            // Use a ZipInputStrem for reading the file
            GZIPInputStream zipInput = new GZIPInputStream(
                    new BufferedInputStream( input ) );
            SAXBuilder builder = new SAXBuilder( true );
            Document doc = builder.build( zipInput );
            Element root = doc.getRootElement();
            if( !root.getName().equals( "snippad" ) ) {
                String error = "Root is not 'snippad' : " + root.getName();
                LOG.fatal( error );
                throw new SerializeException( error );
            }

            String version = root.getAttribute( "version" ).getValue();
            if( !version.equals( VERSION ) ) {
                String error = "Trying to load XML file with version : "
                        + version + ", expected : " + VERSION;
                throw new SerializeException( error );
            }

            return loadWikiWords( root );
        } catch( JDOMException e ) {
            throw new SerializeException( e.getMessage(), e );
        } catch( IOException e ) {
            throw new SerializeException( e.getMessage(), e );
        }
    }

    /**
     * @see org.outerrim.snippad.data.serialize.WikiSerializer#save(
     *      org.outerrim.snippad.data.WikiWord,OutputStream)
     */
    public void save( final WikiWord wiki, final OutputStream output )
            throws SerializeException {
        Element root = saveWikiWords( wiki );

        // Somewhat hackish, but the root element is not a <wiki> element,
        // so change it and add the XML version attribute
        root.setName( "snippad" );
        root.setAttribute( "version", VERSION );

        DocType doctype = new DocType( ROOT, SYSTEMID );
        Document doc = new Document( root, doctype );
        XMLOutputter outputter = new XMLOutputter( "  ", true );

        try {
            // Use a ZipOutputStream for writing the file
            GZIPOutputStream zipOutput = new GZIPOutputStream(
                    new BufferedOutputStream( output ) );
            outputter.output( doc, zipOutput );
            zipOutput.close();
        } catch( IOException e ) {
            throw new SerializeException( e.getMessage(), e );
        }
    }

    /**
     * Recursively load WikiWords from the given root jdom Element.
     *
     * @param root
     *            Root element
     * @return WikiWord contain the wiki information and all child WikiWords
     */
    private WikiWord loadWikiWords( final Element root ) {
        Element wikiText = root.getChild( "text" );
        Attribute wikiName = root.getAttribute( "name" );
        List wikis = root.getChildren( "wiki" );

        WikiWord rootWiki = new WikiWord();
        rootWiki.setName( wikiName.getValue() );
        rootWiki.setWikiText( wikiText.getValue() );

        for( Iterator itWikis = wikis.iterator(); itWikis.hasNext(); ) {
            WikiWord child = loadWikiWords( (Element) itWikis.next() );
            rootWiki.addWikiWord( child );
            child.setParent( rootWiki );
        }

        return rootWiki;
    }

    /**
     * Recursively save WikiWords into jdom Elements.
     *
     * @param word
     *            The root wiki word
     * @return An Element which is the root of a jdom Element tree
     */
    private Element saveWikiWords( final WikiWord word ) {
        Element wiki = new Element( "wiki" );
        wiki.setAttribute( "name", word.getName() );
        Element wikiText = new Element( "text" );
        wikiText.setText( word.getWikiText() );
        wiki.addContent( wikiText );

        List<WikiWord> wikis = word.getWikiWords();
        for( WikiWord wikiword : wikis ) {
            wiki.addContent( saveWikiWords( wikiword ) );
        }

        return wiki;
    }

    /**
     * @param args Arguments
     */
    public static void main( final String[] args ) {
        XmlSerializer xml = new XmlSerializer();
        File xmlFile = new File(
                "/home/darkjedi/projects/snippad/doc/test.xml" );
        try {
            WikiWord word = xml.load( new FileInputStream( xmlFile ) );
            LOG.debug( "Name of root WikiWord : " + word.getName() );
            LOG.debug( "Text of root WikiWord : " + word.getWikiText() );
            LOG.debug( "Number of child WikiWords : "
                    + word.getWikiWords().size() );
        } catch( FileNotFoundException e ) {
            LOG.error( e );
        } catch( SerializeException e ) {
            LOG.error( e );
        }
    }
}
