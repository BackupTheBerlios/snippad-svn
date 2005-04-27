/**
 *
 */
package org.outerrim.snippad.ui.swt;

/**
 * @author <a href="mailto:sithlord@berlios.de">Michael Osterlie</a>
 *
 */
public class ModifiedEvent {

    private Object source;
    private boolean modified = false;

    /**
     * @param s Source object
     * @param mod Modifed
     */
    public ModifiedEvent( final Object s, final boolean mod ) {
        this.source = s;
        this.modified = mod;
    }

    public boolean isModified() { return modified; }
    public Object getSource() { return source; }
}
