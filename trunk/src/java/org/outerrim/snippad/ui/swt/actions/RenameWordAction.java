/*
 * Filename : RenameWordAction.java
 * Created on Dec 8, 2004
 *
 * Copyright (c) 2004 Marx Promotion Intelligence
 */
package org.outerrim.snippad.ui.swt.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * @author mikeo
 */
public class RenameWordAction extends Action {
    private SnipPad window;
    
    /**
     * 
     */
    public RenameWordAction( SnipPad w ) {
        window = w;
        setText( "&Rename Word@Ctrl+R" );
        setToolTipText( "Renames currently selected word" );
    }

    public void run() {
        InputDialog dlg = new InputDialog( window.getShell(), "Rename Word", "New title of word", "", null );
        int choice = dlg.open();
        if( choice == InputDialog.OK ) {
            String title = dlg.getValue();
            window.getSelectedWiki().setName( title );
            window.setModified( true );
            window.refreshTree();
        }
    }
}
