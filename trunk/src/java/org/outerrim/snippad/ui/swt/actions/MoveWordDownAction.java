package org.outerrim.snippad.ui.swt.actions;

import java.util.List;

import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.WikiViewer;

/**
 * @author <a href="mailto:sithlord@berlios.de">Michael Osterlie</a>
 */
public class MoveWordDownAction extends SnipPadBaseAction {
    /**
     */
    public MoveWordDownAction() {
        setText( "&Move Down" );
        setToolTipText( "Move word up one spot" );
        setImageDescriptor(
                ImageUtil.getImageRegistry().getDescriptor( "down" ) );
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        WikiViewer viewer = snippad.getCurrentWikiViewer();
        WikiWord selected = viewer.getSelectedWiki();
        if( selected == null ) {
            return;
        }

        WikiWord parent = selected.getParent();
        List<WikiWord> words = parent.getWikiWords();
        int index = words.indexOf( selected );

        if( index == words.size() - 1 ) {
            return;
        }

        words.remove( selected );
        words.add( ++index, selected );
        viewer.refreshTree();
        viewer.setModified( true );
    }
}
