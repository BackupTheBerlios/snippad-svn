/*
 * SnipPad.java
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.outerrim.snippad.SnipPadConstants;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.config.Configuration;
import org.outerrim.snippad.service.config.ConfigurationException;
import org.outerrim.snippad.ui.swt.actions.AboutAction;
import org.outerrim.snippad.ui.swt.actions.DeleteWikiWordAction;
import org.outerrim.snippad.ui.swt.actions.ExitAction;
import org.outerrim.snippad.ui.swt.actions.NewWikiAction;
import org.outerrim.snippad.ui.swt.actions.NewWikiWordAction;
import org.outerrim.snippad.ui.swt.actions.OpenAction;
import org.outerrim.snippad.ui.swt.actions.PreferencesAction;
import org.outerrim.snippad.ui.swt.actions.SaveAsWikiAction;
import org.outerrim.snippad.ui.swt.actions.SaveWikiAction;
import org.outerrim.snippad.ui.swt.actions.ShowEditorAction;
import org.outerrim.snippad.ui.swt.dnd.WikiTransfer;
import org.outerrim.snippad.ui.swt.dnd.WikiWordDragListener;
import org.outerrim.snippad.ui.swt.dnd.WikiWordTreeDropAdapter;
import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.engine.BaseRenderEngine;
import org.radeox.engine.context.BaseRenderContext;

/**
 * Main window for the SWT UI of SnipPad
 * @author darkjedi
 */
public class SnipPad extends ApplicationWindow {
    private TreeViewer tree;
    private Browser browser;
    private WikiEditor text;
    private SashForm editorSash;
    private RenderContext wikiContext = new BaseRenderContext();
    private RenderEngine wikiEngine = new BaseRenderEngine();
    
    private String loadedFilename;
    private WikiWord rootWiki;
    private WikiWord selectedWikiWord;
    private boolean modified;
    
    // Actions
    private NewWikiAction actionNew;
    private OpenAction actionOpen;
    private SaveWikiAction actionSaveWiki;
    private SaveAsWikiAction actionSaveAsWiki;
    private ExitAction actionExit;
    private ShowEditorAction actionEdit;
    private NewWikiWordAction actionNewWikiWord;
    private DeleteWikiWordAction actionDeleteWikiWord;
    private PreferencesAction actionPreferences;
    private AboutAction actionAbout;
    
    static private Configuration config = new Configuration();
    
    static private String TITLE = "SnipPad v" + SnipPadConstants.VERSION;
    static private Log log = LogFactory.getLog( SnipPad.class );
    
    public SnipPad() {
        super( null );
        
        // Render a blank String, which will pre-load the wiki engine
        wikiEngine.render( "", wikiContext );
        
        // Load config
        try {
            config.load();
        } catch( ConfigurationException E ) {
            if( E.getCause() instanceof FileNotFoundException ) {
                log.info( "Config file not found, not saved?" );
            } else {
                log.error( "Error loading configuration", E );
            }
        }
    }
    
    public String getVersion() { return SnipPadConstants.VERSION; }
    public WikiWord getRootWiki() { return rootWiki; }
    public WikiWord getSelectedWiki() { return selectedWikiWord; }
    public String getLoadedFilename() { return loadedFilename; }
    
    /**
     * Sets the filename of the loaded wiki.
     * 
     * @param f The filename
     */
    public void setLoadedFilename( String f ) { 
        loadedFilename = f;
        setModified( false );
    }
    
    /**
     * Programmatically selects a wikiword.
     * 
     * @param word The word to select
     */
    public void setSelectedWiki( WikiWord word ) {
        tree.setSelection( new StructuredSelection( word ) );
    }
    
    /**
     * Sets the currently loaded wiki.
     * 
     * @param wiki The root wikiword
     * @param filename Filename of this document
     */
    public void setWiki( WikiWord wiki, String filename ) {
        setLoadedFilename( filename );
        clear();
        rootWiki = wiki;
        
        tree.setInput( rootWiki );
        
        actionSaveWiki.setEnabled( true );
        actionSaveAsWiki.setEnabled( true );
        actionNewWikiWord.setEnabled( true );
    }
    
    public boolean isModified() { return modified; }
    public void setModified( boolean mod ) { 
        modified = mod;
        getShell().setText( getTitle( loadedFilename ) + (mod ? "*" : "") );
    }
    
    public void refreshTree() {
        tree.refresh();
    }
    
    /**
     * Shows/Hides the editor component.
     * 
     * @param show Shows if true, hides if false
     */
    public void showEditor( boolean show ) {
        if( show ) {
            editorSash.setMaximizedControl( null );
        } else {
            editorSash.setMaximizedControl( browser );
        }
    }
    
    /**
     * Perform any cleanup necessary to exit the program (save configuration, etc).
     * @see org.eclipse.jface.window.Window#close()
     */
    public boolean close() {
        try {
            Point currentSize = this.getShell().getSize();
            config.setInitialSize( currentSize.x, currentSize.y );
            config.save();
        } catch( ConfigurationException E ) {
            logError( "Cannot save configuration", E );
        }
        
        return super.close();
    }
    
    /**
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell( Shell shell ) {
        actionNew = new NewWikiAction( this );
        actionOpen = new OpenAction( this );
        actionSaveAsWiki = new SaveAsWikiAction( this );
        actionSaveWiki = new SaveWikiAction( this, actionSaveAsWiki );
        actionExit = new ExitAction( this );
        actionEdit = new ShowEditorAction( this );
        actionNewWikiWord = new NewWikiWordAction( this );
        actionDeleteWikiWord = new DeleteWikiWordAction( this );
        actionPreferences = new PreferencesAction( this );
        actionAbout = new AboutAction( this );
        
        actionSaveWiki.setEnabled( false );
        actionSaveAsWiki.setEnabled( false );
        actionNewWikiWord.setEnabled( false );
        actionDeleteWikiWord.setEnabled( false );
        
        addStatusLine();
        addMenuBar();
        
        super.configureShell( shell );
    }
    
    /**
     * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents( Composite parent ) {
        getShell().setText( TITLE );
        
        SashForm sashForm = new SashForm( parent, SWT.HORIZONTAL );
        
        tree = new TreeViewer( sashForm );
        tree.setContentProvider( new WikiWordContentProvider() );
        
        tree.addSelectionChangedListener( new ISelectionChangedListener() {
            public void selectionChanged( SelectionChangedEvent event ) {
                handleTreeSelectionChanged( event );
            }
        });
        
        tree.getTree().setMenu( createTreePopupMenu() );
        
        editorSash = new SashForm( sashForm, SWT.VERTICAL );

        browser = new Browser( editorSash, SWT.BORDER );
        text = new WikiEditor( editorSash, this );
        text.setEnabled( false );
        text.addSaveListener( new SelectionListener() {
            public void widgetSelected( SelectionEvent event ) {
                handleSaveWikiWord();
            }
            
            public void widgetDefaultSelected( SelectionEvent event ) {
                handleSaveWikiWord();
            }
        });
        
        showEditor( config.showEditor() );
        
        sashForm.setWeights( new int[] { 20, 80 } );
        editorSash.setWeights( new int[] { 60, 40 } );
        
        // Setup drag and drop
        int ops = DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { WikiTransfer.getInstance() };
        tree.addDragSupport( ops, transfers, new WikiWordDragListener( tree ) );
        tree.addDropSupport( ops, transfers, new WikiWordTreeDropAdapter( tree ) );
        
        return sashForm;
    }
    
    /**
     * @see org.eclipse.jface.window.Window#initializeBounds()
     */
    protected void initializeBounds() {
        getShell().setSize( config.getInitialSize() );
    }
    
    /**
     * @see org.eclipse.jface.window.ApplicationWindow#createMenuManager()
     */
    protected MenuManager createMenuManager() {
        MenuManager mainMenu = new MenuManager( "" );
        
        MenuManager fileMenu = new MenuManager( "&File" );
        MenuManager editMenu = new MenuManager( "&Edit" );
        MenuManager helpMenu = new MenuManager( "&Help" );
        
        mainMenu.add( fileMenu );
        mainMenu.add( editMenu );
        mainMenu.add( helpMenu );
        
        fileMenu.add( actionNew );
        fileMenu.add( actionOpen );
        fileMenu.add( actionSaveWiki );
        fileMenu.add( actionSaveAsWiki );
        fileMenu.add( new Separator() );
        fileMenu.add( actionExit );
        
        editMenu.add( actionNewWikiWord );
        editMenu.add( actionDeleteWikiWord );
        editMenu.add( new Separator() );
        editMenu.add( actionEdit );
        editMenu.add( actionPreferences );
        
        helpMenu.add( actionAbout );
        
        return mainMenu;
    }
    
    /**
     * @see org.eclipse.jface.window.ApplicationWindow#createToolBarManager(int)
     */
    protected ToolBarManager createToolBarManager( int style ) {
        ToolBarManager toolBar = new ToolBarManager( style );
        
        toolBar.add( actionOpen );
        toolBar.add( actionSaveWiki );
        toolBar.add( actionEdit );
        toolBar.add( actionNewWikiWord );

        return toolBar;
    }
    
    /**
     * Creates the popup menu for the wiki tree.
     * @return Menu containing the entries
     */
    private Menu createTreePopupMenu() {
        
        MenuManager popupMenu = new MenuManager();
        popupMenu.add( actionNewWikiWord );
        popupMenu.add( actionDeleteWikiWord );

        return popupMenu.createContextMenu( tree.getTree() );
    }
    
    private String getTitle( String f ) {
        return TITLE + " : " + (f == null ? "(New File)" : f);
    }
    
    /**
     * Resets the application to a clean state.
     */
    private void clear() {
        log.debug( "Clearing" );
        text.setText( "" );
        text.setEnabled( false );
        browser.setText( "<html><body></body></html>" );
        browser.update();
    }
    
    /**
     * Handles events for the wikiword tree.
     * 
     * @param event The event to handle
     */
    private void handleTreeSelectionChanged( SelectionChangedEvent event ) {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        
        selectedWikiWord = (WikiWord)selection.getFirstElement();
        if( selectedWikiWord == null ) {	// No selection, clear everything
            clear();
            actionDeleteWikiWord.setEnabled( false );
            return; 
        } else {
            actionDeleteWikiWord.setEnabled( true );
        }
        
        if( selectedWikiWord.getHtmlText() == null ) {
            updateWikiHtml( selectedWikiWord );
        }
        
        text.setEnabled( true );
        text.setText( selectedWikiWord.getWikiText() );
        updateBrowser( selectedWikiWord.getHtmlText() );        
    }
    
    /**
     * Handler for saving the text of a wiki. Updates the HTML in the browser.
     */
    private void handleSaveWikiWord() {
        selectedWikiWord.setWikiText( text.getText() );
        updateWikiHtml( selectedWikiWord );
        updateBrowser( selectedWikiWord.getHtmlText() );
    }
    
    /**
     * Updates the HTML in the wikiword of the text has been changed
     * @param wiki The wiki to update
     */
    private void updateWikiHtml( WikiWord wiki ) {
        String html = wikiEngine.render( wiki.getWikiText(), wikiContext );
        wiki.setHtmlText( html, config.getCssLocation() );
        
        // Write this to a test file so it may be browsed
        try {
            File testFile = new File( "test.html" );
            log.debug( "Writing test html file : " + testFile.getAbsoluteFile() );
            FileWriter writer = new FileWriter( testFile );
            writer.write( wiki.getHtmlText() );
            writer.close();
        } catch( IOException E ) {
            log.error( E );
        }
    }
    
    /**
     * Sets the HTML in the browser
     * @param html The HTML
     */
    private void updateBrowser( String html ) {
        browser.setText( selectedWikiWord.getHtmlText() );
        browser.update();        
    }
    
    /**
     * Retrieves the Configuration object containing the preferences.
     * @return Configuration of the application
     */
    static public Configuration getConfiguration() { return config; }
    
    /**
     * Logs and displays an error.
     * 
     * @param message The message to display
     * @param E Exception that caused the error
     */
    static public void logError( String message, Exception E ) {
        log.error( E.getMessage(), E );
        Status status = new Status( Status.ERROR, "org.outerrim.snippad.SnipPad", Status.ERROR, E.getMessage(), E );
        String title = "Error : " + E.getClass().toString();
        ErrorDialog.openError( (Shell)null, title, null, status );                    
    }
    
    static public void main( String[] args ) {
        SnipPad sp = new SnipPad();
        sp.setBlockOnOpen( true );
        sp.open();
        Display.getCurrent().dispose();
    }
}
