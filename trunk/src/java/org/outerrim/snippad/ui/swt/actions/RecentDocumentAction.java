package org.outerrim.snippad.ui.swt.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.outerrim.snippad.data.serialize.SerializeException;
import org.outerrim.snippad.data.serialize.XmlSerializer;
import org.outerrim.snippad.ui.swt.SnipPad;

public class RecentDocumentAction extends SnipPadBaseAction {
	private String displayFile;
	private String filename;
	
	static private final Log log = LogFactory.getLog( RecentDocumentAction.class );
	
	public RecentDocumentAction( String f ) {
		filename = f;
		
		int index = filename.lastIndexOf( File.separator );
		displayFile = filename.substring( index + 1, filename.length() );
		
		setText( displayFile );
		setToolTipText( "Open " + filename );
	}
	
	public String getId() { return filename; }
	
	public void run() {
        try {
            snippad.openWiki( (new XmlSerializer()).load( new FileInputStream( filename ) ), filename );
        } catch( IOException E ) {
            SnipPad.logError( E.getMessage(), E );
        } catch( SerializeException E ) {
            SnipPad.logError( E.getMessage(), E );
        }
	}
}
