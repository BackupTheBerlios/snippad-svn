/*
 * Filename : RenameWordAction.java
 * Created on Dec 8, 2004
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
     */
    public RenameWordAction() {
        setText( "&Rename Word@Ctrl+R" );
        setToolTipText( "Renames currently selected word" );
        setImageDescriptor(
                ImageUtil.getImageRegistry().getDescriptor( "renameword" ) );
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
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
