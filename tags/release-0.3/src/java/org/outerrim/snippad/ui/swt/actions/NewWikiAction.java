/*
 * NewWikiAction.java
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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * Action to create a new file.
 * @author darkjedi
 */
public class NewWikiAction extends Action {
    private SnipPad window;
    
    public NewWikiAction( SnipPad w ) {
        window = w;
        setText( "&New@Ctrl+N" );
        setToolTipText( "Create a new Wiki" );
		setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "new" ) );
    }
    
    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        if( window.isModified() == true ) {
	        MessageBox message = new MessageBox( window.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO );
	        message.setMessage( "Document not saved, still open a new file?" );
	        int confirm = message.open();
	        if( confirm == SWT.NO ) { return; }
        }

        WikiWord newWiki = new WikiWord();
        newWiki.setName( "root" );
        window.setWiki( newWiki, null );
    }
}
