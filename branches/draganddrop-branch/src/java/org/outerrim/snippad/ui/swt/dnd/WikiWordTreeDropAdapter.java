/*
 * Filename : WikiWordTreeDropAdapter.java
 * Created on Dec 6, 2004
 *
 * Copyright (c) 2004 Marx Promotion Intelligence
 */
package org.outerrim.snippad.ui.swt.dnd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.outerrim.snippad.data.WikiWord;

/**
 * @author mikeo
 */
public class WikiWordTreeDropAdapter extends ViewerDropAdapter {
    static private final Log log = LogFactory.getLog( WikiWordTreeDropAdapter.class );
    
    /**
     * @param arg0
     */
    public WikiWordTreeDropAdapter( Viewer view ) {
        super( view );
    }

    /**
     * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
     */
    public boolean performDrop( Object obj ) {
        WikiWord target = (WikiWord)getCurrentTarget();
        if( target != null ) {
            int loc = getCurrentLocation();
            if( loc == LOCATION_BEFORE || loc == LOCATION_AFTER ) {
                target = target.getParent();
            }
        }
        if( target == null ) {
            target = (WikiWord)getViewer().getInput();
        }
        WikiWord[] toDrop = (WikiWord[])obj;
        TreeViewer viewer = (TreeViewer)getViewer();
        
        for( int i = 0; i < toDrop.length; ++i ) {
            if( toDrop[i].equals( target ) || target.hasParent( toDrop[i] ) ) {
                return false;
            }
        }
        
        for( int i = 0; i < toDrop.length; ++i ) {
            toDrop[i].setParent( target );
            log.debug( "Adding word to tree : " + target.getName() );
//            target.addWikiWord( toDrop[i] );
            viewer.add( target, toDrop[i] );
            viewer.reveal( toDrop[i] );
        }
        
        return true;
    }

    /**
     * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
     */
    public boolean validateDrop( Object obj, int op, TransferData data ) {
        return WikiTransfer.getInstance().isSupportedType( data );
    }

}
