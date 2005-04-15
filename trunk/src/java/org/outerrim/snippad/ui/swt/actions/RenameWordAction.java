/*
 * Filename : RenameWordAction.java
 * Created on Dec 8, 2004
 *
 * Copyright (c) 2004 Marx Promotion Intelligence
 */
package org.outerrim.snippad.ui.swt.actions;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.WikiViewer;

/**
 * @author mikeo
 */
public class RenameWordAction extends SnipPadBaseAction {
    /**
     * 
     */
    public RenameWordAction() {
        setText( "&Rename Word@Ctrl+R" );
        setToolTipText( "Renames currently selected word" );
		setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "renameword" ) );
    }

    public void run() {
        InputDialog dlg = new InputDialog( 
                Display.getCurrent().getActiveShell(), 
                "Rename Word", 
                "New title of word", 
                "", 
                null );
        int choice = dlg.open();
        if( choice == Window.OK ) {
            String title = dlg.getValue();
            WikiViewer viewer = snippad.getCurrentWikiViewer();
            viewer.getSelectedWiki().setName( title );
            viewer.setModified( true );
            viewer.refreshTree();
        }
    }
}
