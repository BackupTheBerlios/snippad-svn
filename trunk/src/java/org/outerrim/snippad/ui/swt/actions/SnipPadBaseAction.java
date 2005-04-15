/**
 * 
 */
package org.outerrim.snippad.ui.swt.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * @author <a href="mailto:sithlord@berlios.de">Michael Osterlie</a>
 *
 */
public class SnipPadBaseAction extends Action {
    protected SnipPad snippad;
    
    /**
     * 
     */
    public SnipPadBaseAction() {
        super();
    }

    /**
     * @param arg0
     */
    public SnipPadBaseAction( String arg0 ) {
        super( arg0 );
    }

    /**
     * @param arg0
     * @param arg1
     */
    public SnipPadBaseAction( String arg0, ImageDescriptor arg1 ) {
        super( arg0, arg1 );
    }

    /**
     * @param arg0
     * @param arg1
     */
    public SnipPadBaseAction( String arg0, int arg1 ) {
        super( arg0, arg1 );
    }

    public void setSnipPad( SnipPad sp ) { snippad = sp; }
}
