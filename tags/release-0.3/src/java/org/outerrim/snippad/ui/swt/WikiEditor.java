/*
 * WikiEditor.java
 * Created on Sep 18, 2004
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
package org.outerrim.snippad.ui.swt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Editor object for wiki text.
 * @author darkjedi
 */
public class WikiEditor extends Composite {
    private SnipPad window;
    private Text editor;
    private Button btnRevert;
    private Button btnSave;
    private boolean modified;
    
    static private final Log log = LogFactory.getLog( WikiEditor.class );
    
    /** 
     * Pre-modified text, used for the Revert button
     */
    private String text;
    
    public WikiEditor( Composite parent, SnipPad pad ) {
        super( parent, SWT.BORDER );
        window = pad;
        
        buildLayout();
    }
    
    /**
     * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
     */
    public void setEnabled( boolean enabled ) {
        editor.setEnabled( enabled );
        btnSave.setEnabled( enabled );
        btnRevert.setEnabled( enabled );
        super.setEnabled( enabled );
    }

    public void setText( String text ) {
        editor.setText( text );
        this.text = text;
        modified = false;
    }
    
    public String getText() {
        return editor.getText();
    }
    
    public void addSaveListener( SelectionListener listener ) {
        btnSave.addSelectionListener( listener );
    }
    
    public boolean isModified() { return modified; }
    
    private void handleRevert() {
        editor.setText( text );
    }
    
    private void buildLayout() {
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        setLayout( layout );

        editor = new Text( this, SWT.BORDER 
                           		 	| SWT.MULTI 
                           		 	| SWT.WRAP 
                           		 	| SWT.H_SCROLL 
                           		 	| SWT.V_SCROLL );
        GridData editorData = new GridData( GridData.FILL,
                                            GridData.FILL,
                                            true,
                                            true );
        editor.setLayoutData( editorData );
		
		// Both listeners are needed to make sure that the document isn't set as modified when we
		// setText(), but only when an actual key is pressed. The ModifyListener is need to make
		// sure that we only set the text as modified (which setText() then immediately marks as
		// not modified), not when a meta key is pressed (Like Ctrl when copying text, etc).
        editor.addKeyListener( new KeyListener() {
            public void keyReleased( KeyEvent event ) {
                if( modified ) {
                    window.setModified( true );
                }
            }
            
            public void keyPressed( KeyEvent event ) {}
        });
		editor.addModifyListener( new ModifyListener() {
			public void modifyText( ModifyEvent e ) {
				modified = true;
			}
		});
        
        Composite buttonBar = new Composite( this, SWT.NONE );
        buttonBar.setLayout( new RowLayout() );
        buttonBar.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

        btnRevert = new Button( buttonBar, SWT.PUSH );
        btnRevert.setText( "Revert" );
        btnRevert.addSelectionListener( new SelectionListener() {
            public void widgetSelected( SelectionEvent event ) {
                handleRevert();
            }
            
            public void widgetDefaultSelected( SelectionEvent event ) {
                handleRevert();
            }
        });
        
        btnSave = new Button( buttonBar, SWT.PUSH );
        btnSave.setText( "Save" );
        btnSave.addSelectionListener( new SelectionListener() {
            public void widgetSelected( SelectionEvent event ) {
                handleSave();
            }
            
            public void widgetDefaultSelected( SelectionEvent event ) {
                handleSave();
            }
        });
    }
    
    private void handleSave() {
        modified = false;
        text = editor.getText();
        window.setModified( true );
    }
}
