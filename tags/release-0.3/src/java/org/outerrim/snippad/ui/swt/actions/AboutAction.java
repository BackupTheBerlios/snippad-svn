/*
 * AboutAction.java
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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * Action to display an about dialog.
 * @author darkjedi
 */
public class AboutAction extends Action {
    private SnipPad window;
    
    public AboutAction( SnipPad w ) {
        window = w;
        setText( "&About" );
        setToolTipText( "About SnipPad" );
    }
    
    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        String message = "SnipPad v" + window.getVersion();
        MessageDialog.openInformation( window.getShell(), "About SnipPad", message );
    }
}
