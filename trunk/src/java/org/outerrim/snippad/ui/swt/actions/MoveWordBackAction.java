package org.outerrim.snippad.ui.swt.actions;

import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.ui.swt.WikiViewer;

/**
 * @author <a href="mailto:sithlord@berlios.de">Michael Osterlie</a>
 */
public class MoveWordBackAction extends SnipPadBaseAction {

    /**
     */
    public MoveWordBackAction() {
        setText( "&Move Back" );
        setToolTipText( "Move word back to parent word" );
        setImageDescriptor(
                ImageUtil.getImageRegistry().getDescriptor( "back" ) );
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
        WikiWord pParent = parent.getParent();
        if( pParent == null ) {
            return;
        }

        parent.deleteWikiWord( selected );
        pParent.addWikiWord( selected );

        viewer.setModified( true );
        viewer.refreshTree();
    }
}
