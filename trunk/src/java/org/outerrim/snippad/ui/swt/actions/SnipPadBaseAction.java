/**
 *
 */
package org.outerrim.snippad.ui.swt.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * @author <a href="mailto:sithlord@berlios.de">Michael Osterlie</a>
 */
public class SnipPadBaseAction extends Action {
    /**
     */
    protected SnipPad snippad;

    /**
     */
    public SnipPadBaseAction() {
        super();
    }

    /**
     * @param name Display name
     */
    public SnipPadBaseAction( final String name ) {
        super( name );
    }

    /**
     * @param name Display name
     * @param image Image
     */
    public SnipPadBaseAction( final String name, final ImageDescriptor image ) {
        super( name, image );
    }

    /**
     * @param name Display name
     * @param arg1 int
     */
    public SnipPadBaseAction( final String name, final int arg1 ) {
        super( name, arg1 );
    }

    /**
     * @param sp SnipPad instance
     */
    public void setSnipPad( final SnipPad sp ) {
        snippad = sp;
    }
}
