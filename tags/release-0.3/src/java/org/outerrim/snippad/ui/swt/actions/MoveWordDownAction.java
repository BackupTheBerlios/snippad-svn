package org.outerrim.snippad.ui.swt.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.SnipPad;

public class MoveWordDownAction extends Action {
	private SnipPad window;
	
	public MoveWordDownAction( SnipPad w ) {
		window = w;
        setText( "&Move Down" );
        setToolTipText( "Move word up one spot" );
        setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "down" ) );		
	}
	
	public void run() {
		WikiWord selected = window.getSelectedWiki();
		if( selected == null ) {
			return;
		}
		
		WikiWord parent = selected.getParent();
		List words = parent.getWikiWords();
		int index = words.indexOf( selected );
		
		if( index == words.size() - 1 ) {
			return;
		}
		
		words.remove( selected );
		words.add( ++index, selected );
		window.refreshTree();
		window.setModified( true );
	}
}
