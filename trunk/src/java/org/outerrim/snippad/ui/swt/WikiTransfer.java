/*
 * Filename : WikiTransfer.java
 * Created on Dec 3, 2004
 *
 * Copyright (c) 2004 Marx Promotion Intelligence
 */
package org.outerrim.snippad.ui.swt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;
import org.outerrim.snippad.data.WikiWord;

/**
 * @author mikeo
 */
public class WikiTransfer extends ByteArrayTransfer {
    static private final String TYPE_NAME = "Wiki";
    static private final int TYPE_ID = registerType( TYPE_NAME );
    static private WikiTransfer instance = new WikiTransfer();
    
    static public WikiTransfer getInstance() { return instance; }
    
    protected void javaToNative( Object obj, TransferData transferData ) {
        if( !checkType( obj ) || !(isSupportedType( transferData ) ) ) {
            DND.error( DND.ERROR_CANNOT_INIT_DRAG );
        }
        
        WikiWord[] words = (WikiWord[])obj;
        try {
            // write data to the byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream writeOut = new DataOutputStream( out );
            for( int i = 0, length = words.length; i < length; ++i ) {
                byte[] nameBuffer = words[i].getName().getBytes();
                byte[] wikiBuffer = words[i].getWikiText().getBytes();
                writeOut.writeInt( nameBuffer.length );
                writeOut.write( nameBuffer );
                writeOut.writeInt( wikiBuffer.length );
                writeOut.write( wikiBuffer );
            }
            byte[] output = out.toByteArray();
            writeOut.close();
            super.javaToNative( output, transferData );
        } catch( IOException E ) {}
    }
    
    protected Object nativeToJava( TransferData transferData ) {
        if( isSupportedType( transferData ) ) {
            byte[] buffer = (byte[])super.nativeToJava( transferData );
            if( buffer == null ) { return null; }
            
            WikiWord[] words = new WikiWord[0];
            try {
                ByteArrayInputStream in = new ByteArrayInputStream( buffer );
                DataInputStream readIn = new DataInputStream( in );
                while( readIn.available() > 20 ) {
                    WikiWord word = new WikiWord();
                    int size = readIn.readInt();
                    byte[] name = new byte[ size ];
                    readIn.read( name );
                    word.setName( new String( name ) );
                    size = readIn.readInt();
                    name = new byte[ size ];
                    readIn.read( name );
                    word.setWikiText( new String( name ) );
                    
                    WikiWord[] newWords = new WikiWord[ words.length +1 ];
                    System.arraycopy( words, 0, newWords, 0, words.length );
                    newWords[ words.length ] = word;
                    words = newWords;
                }
            } catch( IOException E ) {
                return null;
            }
            
            return words;
        }
        
        return null;
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
        return new int[] {TYPE_ID };
    }
    
    private boolean checkType( Object obj ) {
        if( obj == null ||
                !(obj instanceof WikiWord[]) ||
                ((WikiWord[])obj).length == 0) {
            return false;
        }
        
        WikiWord[] types = (WikiWord[])obj;
        for( int i = 0; i < types.length; ++i ) {
            if( types[i] == null ) {
                return false;
            }
        }
        
        return true;
    }

}
