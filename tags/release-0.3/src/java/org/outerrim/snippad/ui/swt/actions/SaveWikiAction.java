/*
 * SaveWikiAction.java
 * Created on Sep 19, 2004
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.eclipse.jface.action.Action;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.data.serialize.SerializeException;
import org.outerrim.snippad.data.serialize.XmlSerializer;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * Saves the snippad document, defering to Save As if no filename is known.
 * @author darkjedi
 */
public class SaveWikiAction extends Action {
    private SnipPad window;
    private Action actionSaveAs;
    
    /**
     * @param w The parent window
     * @param saveAs The Action to use as the SaveAs action if this is a new file
     */
    public SaveWikiAction( SnipPad w, Action saveAs ) {
        window = w;
        actionSaveAs = saveAs;
        setText( "&Save@Ctrl+S" );
        setToolTipText( "Save the Wiki" );
		setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "save" ) );
    }
    
    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        String filename = window.getLoadedFilename();
        
        if( filename == null ) {	// A new Wiki, not previously loaded, do SaveAs
            actionSaveAs.run();
            return;
        }
        
        try {
	        WikiWord root = window.getRootWiki();
	        (new XmlSerializer()).save( root, new FileOutputStream( filename ) );
	        window.setModified( false );
        } catch( SerializeException E ) {
            SnipPad.logError( E.getMessage(), E );
        } catch( FileNotFoundException E ) {
            SnipPad.logError( E.getMessage(), E );
        }
    }
}
