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

import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;

/**
 * Action to create a new file.
 * @author darkjedi
 */
public class NewWikiAction extends SnipPadBaseAction {
    public NewWikiAction() {
        setText( "&New@Ctrl+N" );
        setToolTipText( "Create a new Wiki" );
		setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "new" ) );
    }
    
    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        WikiWord newWiki = new WikiWord();
        newWiki.setName( "root" );
        snippad.openWiki( newWiki, null );
    }
}
