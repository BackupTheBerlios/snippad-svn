package org.outerrim.snippad.ui.swt.actions;

import java.util.List;

import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.WikiViewer;

/**
 * @author <a href="mailto:sithlord@berlios.de">Michael Osterlie</a>
 */
public class MoveWordForwardAction extends SnipPadBaseAction {
    /**
     */
    public MoveWordForwardAction() {
        setText( "&Move Forward" );
        setToolTipText( "Move word into the word above it" );
        setImageDescriptor(
                ImageUtil.getImageRegistry().getDescriptor( "forward" ) );
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
        List siblings = parent.getWikiWords();
        int index = siblings.indexOf( selected );
        if( index == 0 ) {
            return;
        }

        WikiWord newParent = (WikiWord) siblings.get( --index );

        siblings.remove( selected );
        newParent.addWikiWord( selected );

        viewer.setModified( true );
        viewer.refreshTree();
    }
}
