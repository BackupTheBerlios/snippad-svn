/*
 * ShowEditorAction.java
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

import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * Toggles the showing of the wiki text editor.
 * @author darkjedi
 */
public class ShowEditorAction extends SnipPadBaseAction {
    public ShowEditorAction() {
        setText( "Show &Editor@Ctrl+E");
        setToolTipText( "Show the Wiki editor" );
        setChecked( SnipPad.getConfiguration().showEditor() );
    }
    
    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        snippad.getCurrentWikiViewer().showEditor( isChecked() );
    }
}
