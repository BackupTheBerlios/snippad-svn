/*
 * SaveAsWikiAction.java
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.data.serialize.SerializeException;
import org.outerrim.snippad.data.serialize.XmlSerializer;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * Saves the snippad document to a new file.
 * @author darkjedi
 */
public class SaveAsWikiAction extends Action {
    private SnipPad window;
    
    static private Log log = LogFactory.getLog( SaveAsWikiAction.class );
    
    public SaveAsWikiAction( SnipPad w ) {
        window = w;
        setText( "Save &As" );
        setToolTipText( "Save wikis to file" );
//        setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "saveas" ) );
    }
    
    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        FileDialog dialog = new FileDialog( window.getShell(), SWT.SAVE );
        dialog.setFilterNames( new String[] { "SnipPad Files", "All Files (*,*)" } );
        dialog.setFilterExtensions( new String[] { "*.snip", "*,*" } );
        dialog.setFilterPath( SnipPad.getConfiguration().getDefaultSaveAsLocation() );
        String filename = dialog.open();
        if( filename == null ) { return; }
        
        log.debug( "Saving to filename : " + filename );
        File saveFile = new File( filename );
        String path = saveFile.getParentFile().getPath();
        SnipPad.getConfiguration().setDefaultSaveAsLocation( path );
        try {
	        WikiWord root = window.getRootWiki();
	        (new XmlSerializer()).save( root, new FileOutputStream( saveFile ) );
	        window.setLoadedFilename( filename );
        } catch( SerializeException E ) {
            SnipPad.logError( E.getMessage(), E );
        } catch( FileNotFoundException E ) {
            SnipPad.logError( E.getMessage(), E );
        }
    }
}
