/*
 * Filename : WikiWordTreeDropAdapter.java
 * Created on Dec 6, 2004
 *
 * Copyright (c)2004 Michael Osterlie
 *
 * This file is part of snippad.
 *
 * snippad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * snippad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
    private static final Log LOG = LogFactory.getLog(
            WikiWordTreeDropAdapter.class );

    /**
     * @param view Viewer
     */
    public WikiWordTreeDropAdapter( final Viewer view ) {
        super( view );
    }

    /**
     * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(
     * java.lang.Object)
     */
    public boolean performDrop( final Object obj ) {
        WikiWord target = (WikiWord) getCurrentTarget();
        if( target != null ) {
            int loc = getCurrentLocation();
            if( loc == LOCATION_BEFORE || loc == LOCATION_AFTER ) {
                LOG.debug( "Inserting object, getting parent word" );
                target = target.getParent();
            }
        }

        if( target == null ) {
            target = (WikiWord) getViewer().getInput();
        }

        LOG.debug( "Target is : " + target.getName() );

        WikiWord[] toDrop = (WikiWord[]) obj;
        TreeViewer viewer = (TreeViewer) getViewer();

        for( int i = 0; i < toDrop.length; ++i ) {
            if( toDrop[i].equals( target ) || target.hasParent( toDrop[i] ) ) {
                return false;
            }
        }

        for( int i = 0; i < toDrop.length; ++i ) {
            LOG.debug( "Processing word : " + toDrop[i].getName() );
            LOG.debug( "Adding new word (" + toDrop[i].getName()
                    + ") to word : " + target.getName() );
            int index = determineIndex();
            if( index == -1 ) {
                LOG.debug( "Adding word" );
                target.addWikiWord( toDrop[i] );
            } else {
                LOG.debug( "Adding word at index : " + index );
                target.addWikiWord( toDrop[i], index );
            }
            viewer.reveal( toDrop[i] );
        }

        return true;
    }

    /**
     * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(
     *      java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
     */
    public boolean validateDrop(
            final Object obj,
            final int op,
            final TransferData data ) {
        return WikiTransfer.getInstance().isSupportedType( data );
    }

    private int determineIndex() {
        TreeViewer tree = (TreeViewer) getViewer();
        WikiWordContentProvider content =
            (WikiWordContentProvider) tree.getContentProvider();
        WikiWord target = (WikiWord) getCurrentTarget();
        List children = Arrays.asList(
                content.getChildren( target.getParent() ) );
        int targetLocation = children.indexOf( target );
        LOG.debug( "Starting target location is : " + targetLocation );
        // int location = getCurrentLocation();
        // if( location == LOCATION_AFTER ) {
        // ++targetLocation;
        // }
        //
        // int thisLocation = children.indexOf( content );
        // if( thisLocation < targetLocation ) {
        // --targetLocation;
        // }

        return targetLocation;
    }
}
