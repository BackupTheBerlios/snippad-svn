/*
 * DeleteWikiWordAction.java
 * Created on Oct 1, 2004
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
 * Action to delete a wikiword.
 * @author darkjedi
 */
public class DeleteWikiWordAction extends Action {
    private SnipPad window;
    
    public DeleteWikiWordAction( SnipPad w ) {
        window = w;
        setText( "&Delete Word@Ctrl+D" );
        setToolTipText( "Delete the selected wiki word" );
		setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "deleteword" ) );
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */    
    public void run() {
        WikiWord selected = window.getSelectedWiki();
        if( selected == null ) {
            return;
        }
        
        MessageBox message = new MessageBox( window.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO );
        message.setMessage( "Are you sure you want to delete '" + selected.getName() + "' and all it's children?" );
        int confirm = message.open();
        if( confirm == SWT.NO ) { return; }
        
        WikiWord parent = selected.getParent();
        parent.deleteWikiWord( selected );
        window.refreshTree();
        window.setModified( true );
    }

}
