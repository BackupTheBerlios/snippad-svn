/*
 * Filename : WikiTransfer.java
 * Created on Dec 6, 2004
 *
 * Copyright (c) 2004 Marx Promotion Intelligence
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
public class WikiTransfer extends ByteArrayTransfer {
    static private WikiTransfer instance = new WikiTransfer();
    static private final String TYPE_NAME = "wiki-transfer-format";
    static private final int TYPE_ID = registerType( TYPE_NAME );

    static private final Log log = LogFactory.getLog( WikiTransfer.class );
    
    /**
     * Returns singleton WikiTransfer instance
     * @return WikiTransfer instance
     */
    static public WikiTransfer getInstance() { return instance; }
    
    /** Private constructor */
    private WikiTransfer() {}

    protected void javaToNative( Object obj, TransferData data ) {
        byte[] bytes = toByteArray( (WikiWord[])obj );
        if( bytes != null ) {
            super.javaToNative( bytes, data );
        } else {
            log.debug( "javaToNative() : bytes[] is null" );
        }
    }
    
    private String bytesToString( byte[] bytes ) {
        StringBuffer buffer = new StringBuffer();
        for( int i = 0; i < bytes.length; ++i ) {
            buffer.append( bytes[i] );
        }
        return buffer.toString();
    }
    
    protected Object nativeToJava( TransferData data ) {
        byte[] bytes = (byte[])super.nativeToJava( data );
        log.debug( "nativeToJava() : bytes[] is : " + bytes );
        return fromByteArray( bytes );
    }
    
    /**
     * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
     */
    protected String[] getTypeNames() {
        return new String[] { TYPE_NAME };
    }

    /**
     * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
     */
    protected int[] getTypeIds() {
        return new int[] { TYPE_ID };
    }

    /**
     * Converts a byte array into a WikiWord array
     * @param bytes byte array to transform
     * @return WikiWord array
     */
    private WikiWord[] fromByteArray( byte[] bytes ) {
        DataInputStream in = new DataInputStream( new ByteArrayInputStream( bytes ) );
        
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
        } catch( IOException E ) {
            log.debug( "fromByteArray()", E );
            return null;
        }
    }
    
    /**
     * Converts an array of WikiWords into a byte array
     * @param words Array of WikiWords
     * @return byte array representation of the WikiWord array
     */
    private byte[] toByteArray( WikiWord[] words ) {
        /*
         * Transfer data is an array of words. Serialized version is:
         * (int) number of words
         * (WikiWord) word 1
         * (WikiWord) word 2
         * ... repeat as needed
         * see writeWikiWord() for the (WikiWord) format
         */
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( byteOut );
        
        byte[] bytes = null;
        
        try {
            // write out number of markers
            log.debug( "Writing " + words.length + " words to byte array" );
            out.writeInt( words.length );
            
            for( int i = 0; i < words.length; ++i ) {
                writeWikiWord( (WikiWord)words[i], out );
            }
            out.close();
            bytes = byteOut.toByteArray();
        } catch( IOException E ) {
            log.debug( "toByteArray()", E );
            // when in doubt send nothing
        }
        
        return bytes;
    }
    
    /**
     * Reads and returns a single WikiWord from the given stream
     * @param parent Parent word
     * @param dataIn Stream to read the data from
     * @return The WikiWord contained in the data stream
     * @throws IOException
     */
    private WikiWord readWikiWord( WikiWord parent, DataInputStream dataIn )
    throws IOException {
        /*
         * Serialization format is as follows:
         * (String) name of the word
         * (String) wiki text
         * (int) number of child words
         * (WikiWord) child 1
         * ... repeat for each child
         */
        String title = dataIn.readUTF();
        String text = dataIn.readUTF();
        int n = dataIn.readInt();
        log.debug( title + " has " + n + " children" );
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
     * @param word The word to write
     * @param dataOut The stream to write to
     * @throws IOException
     */
    private void writeWikiWord( WikiWord word, DataOutputStream dataOut )
    throws IOException {
        /*
         * Serialization format is as follows:
         * (String) name of the word
         * (String) wiki text
         * (int) number of child words
         * (WikiWord) child 1
         * ... repeat for each child
         */
        dataOut.writeUTF( word.getName() );
        dataOut.writeUTF( word.getWikiText() );
        List children = word.getWikiWords();
        log.debug( "Word has " + children.size() + " children" );
        dataOut.writeInt( children.size() );
        for( int i = 0, size = children.size(); i < size; ++i ) {
            writeWikiWord( (WikiWord)children.get(i), dataOut );
        }
    }
}
