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

import org.eclipse.swt.SWT;
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
    }
    
    public String getText() {
        return editor.getText();
    }
    
    public void addSaveListener( SelectionListener listener ) {
        btnSave.addSelectionListener( listener );
    }
    
    private void handleRevert() {
        editor.setText( text );
    }
    
    private void buildLayout() {
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        setLayout( layout );

        editor = new Text( this, SWT.BORDER | 
                           		 SWT.MULTI | 
                           		 SWT.WRAP | 
                           		 SWT.H_SCROLL | 
                           		 SWT.V_SCROLL );
        GridData editorData = new GridData( GridData.FILL,
                                            GridData.FILL,
                                            true,
                                            true );
        editor.setLayoutData( editorData );
        
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
        text = editor.getText();
        window.setModified( true );
    }
}
