package org.outerrim.snippad.ui.swt.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.SnipPad;

public class MoveWordUpAction extends Action {
	private SnipPad window;
	
	public MoveWordUpAction( SnipPad w ) {
		window = w;
        setText( "&Move Up" );
        setToolTipText( "Move word down one spot" );
        setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "up" ) );
	}
	
	public void run() {
		WikiWord selected = window.getSelectedWiki();
		if( selected == null ) {
			return;
		}
		
		WikiWord parent = selected.getParent();
		List words = parent.getWikiWords();
		int index = words.indexOf( selected );
		
		if( index == 0 ) {
			return;
		}
		
		words.remove( selected );
		words.add( --index, selected );
		window.refreshTree();
		window.setModified( true );
	}
}
