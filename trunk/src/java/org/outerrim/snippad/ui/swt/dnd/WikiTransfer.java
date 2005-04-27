/*
 * Filename : WikiTransfer.java
 * Created on Dec 6, 2004
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
package org.outerrim.snippad.ui.swt.dnd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.outerrim.snippad.data.WikiWord;

/**
 * @author mikeo
 */
public final class WikiTransfer extends ByteArrayTransfer {
    private static WikiTransfer instance = new WikiTransfer();
    private static final String TYPE_NAME = "wiki-transfer-format";
    private static final int TYPE_ID = registerType( TYPE_NAME );

    private static final Log LOG = LogFactory.getLog( WikiTransfer.class );

    /**
     * Returns singleton WikiTransfer instance.
     *
     * @return WikiTransfer instance
     */
    public static WikiTransfer getInstance() {
        return instance;
    }

    /** Private constructor. */
    private WikiTransfer() { }

    /**
     * @see org.eclipse.swt.dnd.Transfer#javaToNative(
     * java.lang.Object, org.eclipse.swt.dnd.TransferData)
     */
    protected void javaToNative( final Object obj, final TransferData data ) {
        byte[] bytes = toByteArray( (WikiWord[]) obj );
        if( bytes != null ) {
            super.javaToNative( bytes, data );
        } else {
            LOG.debug( "javaToNative() : bytes[] is null" );
        }
    }

    private String bytesToString( final byte[] bytes ) {
        StringBuffer buffer = new StringBuffer();
        for( int i = 0; i < bytes.length; ++i ) {
            buffer.append( bytes[i] );
        }
        return buffer.toString();
    }

    /**
     * @see org.eclipse.swt.dnd.Transfer#nativeToJava(
     * org.eclipse.swt.dnd.TransferData)
     */
    protected Object nativeToJava( final TransferData data ) {
        byte[] bytes = (byte[]) super.nativeToJava( data );
        LOG.debug( "nativeToJava() : bytes[] is : " + bytes );
        return fromByteArray( bytes );
    }

    /**
     * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
     */
    protected String[] getTypeNames() {
        return new String[] {
                TYPE_NAME
        };
    }

    /**
     * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
     */
    protected int[] getTypeIds() {
        return new int[] {
                TYPE_ID
        };
    }

    /**
     * Converts a byte array into a WikiWord array.
     *
     * @param bytes
     *            byte array to transform
     * @return WikiWord array
     */
    private WikiWord[] fromByteArray( final byte[] bytes ) {
        DataInputStream in = new DataInputStream(
                new ByteArrayInputStream( bytes ) );

        try {
            // read number of words
            int n = in.readInt();

            // read words
            WikiWord[] words = new WikiWord[n];
            for( int i = 0; i < n; ++i ) {
                WikiWord word = readWikiWord( null, in );
                if( word == null ) {
                    return null;
                }
                words[i] = word;
            }
            return words;
        } catch( IOException e ) {
            LOG.debug( "fromByteArray()", e );
            return null;
        }
    }

    /**
     * Converts an array of WikiWords into a byte array.
     *
     * @param words
     *            Array of WikiWords
     * @return byte array representation of the WikiWord array
     */
    private byte[] toByteArray( final WikiWord[] words ) {
        /*
         * Transfer data is an array of words. Serialized version is: (int)
         * number of words (WikiWord) word 1 (WikiWord) word 2 ... repeat as
         * needed see writeWikiWord() for the (WikiWord) format
         */
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( byteOut );

        byte[] bytes = null;

        try {
            // write out number of markers
            LOG.debug( "Writing " + words.length + " words to byte array" );
            out.writeInt( words.length );

            for( int i = 0; i < words.length; ++i ) {
                writeWikiWord( (WikiWord) words[i], out );
            }
            out.close();
            bytes = byteOut.toByteArray();
        } catch( IOException e ) {
            LOG.debug( "toByteArray()", e );
            // when in doubt send nothing
        }

        return bytes;
    }

    /**
     * Reads and returns a single WikiWord from the given stream.
     *
     * @param parent
     *            Parent word
     * @param dataIn
     *            Stream to read the data from
     * @return The WikiWord contained in the data stream
     * @throws IOException
     */
    private WikiWord readWikiWord(
            final WikiWord parent,
            final DataInputStream dataIn )
            throws IOException {
        /*
         * Serialization format is as follows: (String) name of the word
         * (String) wiki text (int) number of child words (WikiWord) child 1 ...
         * repeat for each child
         */
        String title = dataIn.readUTF();
        String text = dataIn.readUTF();
        int n = dataIn.readInt();
        LOG.debug( title + " has " + n + " children" );
        WikiWord newParent = new WikiWord( title, text );
        newParent.setParent( parent );

        for( int i = 0; i < n; ++i ) {
            readWikiWord( newParent, dataIn );
        }

        return newParent;
    }

    /**
     * Writes the given WikiWord to the output stream.
     *
     * @param word
     *            The word to write
     * @param dataOut
     *            The stream to write to
     * @throws IOException
     */
    private void writeWikiWord(
            final WikiWord word,
            final DataOutputStream dataOut )
            throws IOException {
        /*
         * Serialization format is as follows: (String) name of the word
         * (String) wiki text (int) number of child words (WikiWord) child 1 ...
         * repeat for each child
         */
        dataOut.writeUTF( word.getName() );
        dataOut.writeUTF( word.getWikiText() );
        List children = word.getWikiWords();
        LOG.debug( "Word has " + children.size() + " children" );
        dataOut.writeInt( children.size() );
        for( int i = 0, size = children.size(); i < size; ++i ) {
            writeWikiWord( (WikiWord) children.get( i ), dataOut );
        }
    }
}
