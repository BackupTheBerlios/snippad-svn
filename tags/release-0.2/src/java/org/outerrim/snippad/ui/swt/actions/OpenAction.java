/*
 * OpenAction.java
 * Created on Sep 18, 2004
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
package org.outerrim.snippad.ui.swt.actions;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.outerrim.snippad.data.serialize.SerializeException;
import org.outerrim.snippad.data.serialize.XmlSerializer;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * Action to Open a file.
 * @author darkjedi
 */
public class OpenAction extends Action {
    private SnipPad window;
    
    static private Log log = LogFactory.getLog( OpenAction.class );
    
    public OpenAction( SnipPad w ) {
        window = w;
        setText( "&Open@Ctrl+O" );
        setToolTipText( "Open a SnipPad file" );
//        setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "open" ) );
    }
    
    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
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

        FileDialog dialog = new FileDialog( window.getShell(), SWT.OPEN );
        dialog.setFilterNames( new String[] { "XML Files", "All Files (*,*)" } );
        dialog.setFilterExtensions( new String[] { "*.xml", "*,*" } );
        dialog.setFilterPath( SnipPad.getConfiguration().getDefaultSaveAsLocation() );
        String filename = dialog.open();
        if( filename == null ) { return; }
        
        log.debug( "Opening : " + filename );
        try {
            window.setWiki( (new XmlSerializer()).load( new FileInputStream( filename ) ), filename );
        } catch( IOException E ) {
            SnipPad.logError( E.getMessage(), E );
        } catch( SerializeException E ) {
            SnipPad.logError( E.getMessage(), E );
        }
        super.run();
    }
}
