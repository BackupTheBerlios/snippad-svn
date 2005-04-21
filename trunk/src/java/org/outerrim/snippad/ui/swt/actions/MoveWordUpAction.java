package org.outerrim.snippad.ui.swt.actions;

import java.util.List;

import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.WikiViewer;

public class MoveWordUpAction extends SnipPadBaseAction {
	public MoveWordUpAction() {
        setText( "&Move Up" );
        setToolTipText( "Move word down one spot" );
        setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "up" ) );
	}
	
	public void run() {
        WikiViewer viewer = snippad.getCurrentWikiViewer();
		WikiWord selected = viewer.getSelectedWiki();
		if( selected == null ) {
			return;
		}
		
		WikiWord parent = selected.getParent();
		List<WikiWord> words = parent.getWikiWords();
		int index = words.indexOf( selected );
		
		if( index == 0 ) {
			return;
		}
		
		words.remove( selected );
		words.add( --index, selected );
		viewer.refreshTree();
		viewer.setModified( true );
	}
}
