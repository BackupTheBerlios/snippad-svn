package org.outerrim.snippad.ui.swt.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.outerrim.snippad.data.serialize.SerializeException;
import org.outerrim.snippad.data.serialize.XmlSerializer;
import org.outerrim.snippad.ui.swt.SnipPad;

public class RecentDocumentAction extends Action {
	private SnipPad window;
	private String displayFile;
	private String filename;
	
	static private final Log log = LogFactory.getLog( RecentDocumentAction.class );
	
	public RecentDocumentAction( SnipPad w, String f ) {
		window = w;
		filename = f;
		
		int index = filename.lastIndexOf( File.separator );
		displayFile = filename.substring( index + 1, filename.length() );
		
		setText( displayFile );
		setToolTipText( "Open " + filename );
	}
	
	public String getId() { return filename; }
	
	public void run() {
        if( window.isModified() == true ) {
	        MessageBox message = new MessageBox( 
	                window.getShell(), 
	                SWT.ICON_QUESTION | 
	                	SWT.YES | 
	                	SWT.NO );
	        message.setMessage( "Document not saved, still open a file?" );
	        int confirm = message.open();
	        if( confirm == SWT.NO ) { return; }
        }
		
        try {
            window.setWiki( (new XmlSerializer()).load( new FileInputStream( filename ) ), filename );
        } catch( IOException E ) {
            SnipPad.logError( E.getMessage(), E );
        } catch( SerializeException E ) {
            SnipPad.logError( E.getMessage(), E );
        }
	}
}
