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
     * 
     */
    public ModifiedEvent( Object source, boolean modified ) {
        this.source = source;
        this.modified = modified;
    }
    
    public boolean isModified() { return modified; }
    public Object getSource() { return source; }
}
