package org.outerrim.snippad.ui.swt.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.SnipPad;

public class MoveWordForwardAction extends Action {
	private SnipPad window;
	
	public MoveWordForwardAction( SnipPad w ) {
		window = w;
        setText( "&Move Forward" );
        setToolTipText( "Move word into the word above it" );
        setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "forward" ) );		
	}
	
	public void run() {
		WikiWord selected = window.getSelectedWiki();
		if( selected == null ) {
			return;
		}
		
		WikiWord parent = selected.getParent();
		List siblings = parent.getWikiWords();
		int index = siblings.indexOf( selected );
		if( index == 0 ) {
			return;
		}
		
		WikiWord newParent = (WikiWord)siblings.get( --index );
		
		siblings.remove( selected );
		newParent.addWikiWord( selected );
		
		window.setModified( true );
		window.refreshTree();
	}
}
