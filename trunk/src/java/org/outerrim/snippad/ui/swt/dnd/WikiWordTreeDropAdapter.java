/*
 * Filename : WikiWordTreeDropAdapter.java
 * Created on Dec 6, 2004
 *
 * Copyright (c) 2004 Marx Promotion Intelligence
 */
package org.outerrim.snippad.ui.swt.dnd;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.ui.swt.WikiWordContentProvider;

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
                log.debug( "Inserting object, getting parent word" );
                target = target.getParent();
            }
        }
        
        if( target == null ) {
            target = (WikiWord)getViewer().getInput();
        }

        log.debug( "Target is : " + target.getName() );
        
        WikiWord[] toDrop = (WikiWord[])obj;
        TreeViewer viewer = (TreeViewer)getViewer();
        
        for( int i = 0; i < toDrop.length; ++i ) {
            if( toDrop[i].equals( target ) || target.hasParent( toDrop[i] ) ) {
                return false;
            }
        }
        
        for( int i = 0; i < toDrop.length; ++i ) {
			log.debug( "Processing word : " + toDrop[i].getName() );
            log.debug( "Adding new word (" + toDrop[i].getName() + ") to word : " + target.getName() );
            int index = determineIndex();
            if( index == -1 ) {
				log.debug( "Adding word" );
                target.addWikiWord( toDrop[i] );                
            } else {
                log.debug( "Adding word at index : " + index );
                target.addWikiWord( toDrop[i], index );
            }
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

    private int determineIndex() {
        TreeViewer tree = (TreeViewer)getViewer();
        WikiWordContentProvider content = (WikiWordContentProvider)tree.getContentProvider();
        WikiWord target = (WikiWord)getCurrentTarget();
        List children = Arrays.asList( content.getChildren( target.getParent() ) );
        int targetLocation = children.indexOf( target );
		log.debug( "Starting target location is : " + targetLocation );
//        int location = getCurrentLocation();
//        if( location == LOCATION_AFTER ) {
//            ++targetLocation;
//        }
//
//        int thisLocation = children.indexOf( content );
//        if( thisLocation < targetLocation ) {
//        	--targetLocation;
//        }

        return targetLocation;
    }
}
