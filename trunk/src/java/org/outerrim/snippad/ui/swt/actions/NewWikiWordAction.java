/*
 * NewWikiWordAction.java
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
package org.outerrim.snippad.ui.swt.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * Action to create a new wikiword
 * @author darkjedi
 */
public class NewWikiWordAction extends Action {
    private SnipPad window;
    
    static private Log log = LogFactory.getLog( NewWikiWordAction.class );
    
    public NewWikiWordAction( SnipPad w ) {
        window = w;
        setText( "New &Word@Ctrl+W" );
        setToolTipText( "Create a new node attached to the selected node" );
//        setImageDescriptor( ImageUtil.getImageRegistry().getDescriptor( "newWiki" ) );
    }
    
    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        NewWordDialog input = new NewWordDialog();
        int selected = input.open();
        if( selected == NewWordDialog.OK ) {
            window.setModified( true );
        }
    }
    
    /**
     * Dialog which asks for the name and location of the new word.
     * @author darkjedi
     */
    private class NewWordDialog extends TitleAreaDialog {
        private Text text; 
        private Button selected;
        private WikiWord selectedWiki = null;
        
        public NewWordDialog() {
            super( window.getShell() );
        }
        
        /**
         * @see org.eclipse.jface.dialogs.Dialog#okPressed()
         */
        protected void okPressed() {
            String name = text.getText();
            WikiWord word = new WikiWord();
            word.setName( name );
            word.setWikiText( "" );
            
            if( !selected.getSelection() ) {
                log.info( "Adding new word to root" );
                selectedWiki = window.getRootWiki();
            }
            
            selectedWiki.addWikiWord( word );
            window.refreshTree();
            close();
        }
        
        /**
         * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
         */
        protected Control createDialogArea( Composite parent ) {
            setTitle( "New Wiki Word" );
            setMessage( "Enter information for new word" );
            Composite panel = new Composite( parent, SWT.NONE ); //(Composite)super.createDialogArea( parent );
            GridLayout layout = new GridLayout( 3, false );
            panel.setLayout( layout );
            Label textLabel = new Label( panel, SWT.RIGHT );
            textLabel.setText( "Word Name:" );
            
            text = new Text( panel, SWT.SINGLE | SWT.BORDER );
            GridData data = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
            data.horizontalSpan = 2;
            text.setLayoutData( data );
            
            Label wordLabel = new Label( panel, SWT.RIGHT );
            wordLabel.setText( "Attach to:" );
            
            selectedWiki = window.getSelectedWiki();                        
            selected = new Button( panel, SWT.RADIO );
            selected.setText( "Selected Word" );
            Button root = new Button( panel, SWT.RADIO );
            root.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING ) );
            root.setText( "Root" );
            
            if( selectedWiki == null ) {
                root.setSelection( true );
                selected.setEnabled( false );
            } else {
                selected.setSelection( true );
            }
            
            text.setFocus();

            return panel;
        }
        
        /**
         * @see org.eclipse.jface.window.Window#getInitialSize()
         */
        protected Point getInitialSize() {
            return new Point( 300, 225 );
        }
    }
}
