package org.outerrim.snippad.ui.swt.actions;

import org.eclipse.jface.action.Action;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.SnipPad;

public class MoveWordBackAction extends Action {
	private SnipPad window;
	
	public MoveWordBackAction( SnipPad w ) {
		window = w;
        setText( "&Move Back" );
        setToolTipText( "Move word back to parent word" );
        setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "back" ) );		
	}
	
	public void run() {
		WikiWord selected = window.getSelectedWiki();
		if( selected == null ) {
			return;
		}
		
		WikiWord parent = selected.getParent();
		WikiWord pParent = parent.getParent();
		if( pParent == null ) {
			return;
		}
		
		parent.deleteWikiWord( selected );
		pParent.addWikiWord( selected );
		
		window.setModified( true );
		window.refreshTree();
	}
}
