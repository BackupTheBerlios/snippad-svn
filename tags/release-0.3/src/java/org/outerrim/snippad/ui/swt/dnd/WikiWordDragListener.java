/*
 * Filename : WikiWordDragListener.java
 * Created on Dec 6, 2004
 *
 * Copyright (c) 2004 Marx Promotion Intelligence
 */
package org.outerrim.snippad.ui.swt.dnd;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.outerrim.snippad.data.WikiWord;

/**
 * @author mikeo
 */
public class WikiWordDragListener extends DragSourceAdapter {
    private StructuredViewer viewer;
    
    static private final Log log = LogFactory.getLog( WikiWordDragListener.class );
    
    /**
     * 
     */
    public WikiWordDragListener( StructuredViewer view ) {
        viewer = view;
    }

    
    public void dragFinished( DragSourceEvent event ) {
        if( !event.doit ) {
            return;
        }
        
        if( event.detail == DND.DROP_MOVE ) {
            IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
//			for( Iterator it = selection.iterator(); it.hasNext(); ) {
//				WikiWord word = (WikiWord)it.next();
//                word.getParent().deleteWikiWord( word );
//            }

			viewer.refresh();
        }
    }
    
    public void dragSetData( DragSourceEvent event ) {
        IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
        WikiWord[] words = (WikiWord[])selection.toList().toArray( new WikiWord[selection.size()] );
        if( WikiTransfer.getInstance().isSupportedType( event.dataType ) ) {
            event.data = words;
        }
		
		for( Iterator it = selection.iterator(); it.hasNext(); ) {
			WikiWord word = (WikiWord)it.next();
            word.getParent().deleteWikiWord( word );
        }		
    }
    
    public void dragStart( DragSourceEvent event ) {
        event.doit = !viewer.getSelection().isEmpty();
    }
}
