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
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.ImageUtil;
import org.outerrim.snippad.service.WikiWordUtils;
import org.outerrim.snippad.ui.swt.WikiViewer;

/**
 * Action to create a new wikiword.
 * @author darkjedi
 */
public class NewWikiWordAction extends SnipPadBaseAction {
    private static final Log LOG = LogFactory.getLog( NewWikiWordAction.class );

    /**
     */
    public NewWikiWordAction() {
        setText( "New &Word@Ctrl+W" );
        setToolTipText( "Create a new word" );
        setImageDescriptor(
                ImageUtil.getImageRegistry().getDescriptor( "newword" ) );
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        NewWordDialog input = new NewWordDialog();
        int selected = input.open();
        if( selected == Window.OK ) {
            snippad.getCurrentWikiViewer().setModified( true );
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
            super( Display.getCurrent().getActiveShell() );
        }

        /**
         * @see org.eclipse.jface.dialogs.Dialog#okPressed()
         */
        protected void okPressed() {
            String name = text.getText();
            WikiViewer viewer = snippad.getCurrentWikiViewer();

            // Check if a wikiword with this name already exists
            if( WikiWordUtils.wordExists( viewer.getRootWiki(), name )
                    != null ) {
                MessageBox message = new MessageBox( getShell(), SWT.ICON_ERROR
                        | SWT.OK );
                message.setMessage( "Word with this name already exists!" );
                message.open();
                return;
            }

            WikiWord word = new WikiWord();
            word.setName( name );
            word.setWikiText( "" );

            if( !selected.getSelection() ) {
                LOG.info( "Adding new word to root" );
                selectedWiki = viewer.getRootWiki();
            }

            selectedWiki.addWikiWord( word );
            viewer.refreshTree();
            close();
        }

        /**
         * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(
         * org.eclipse.swt.widgets.Composite)
         */
        protected Control createDialogArea( final Composite parent ) {
            WikiViewer viewer = snippad.getCurrentWikiViewer();

            setTitle( "New Wiki Word" );
            setMessage( "Enter information for new word" );
            Composite panel = new Composite( parent, SWT.NONE );
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

            selectedWiki = viewer.getSelectedWiki();
            selected = new Button( panel, SWT.RADIO );
            selected.setText( "Selected Word" );
            Button root = new Button( panel, SWT.RADIO );
            root.setLayoutData(
                    new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING ) );
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
