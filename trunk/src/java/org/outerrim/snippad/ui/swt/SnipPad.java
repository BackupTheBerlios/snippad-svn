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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
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
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.outerrim.snippad.SnipPadConstants;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.SnipPadRenderEngine;
import org.outerrim.snippad.service.WikiWordUtils;
import org.outerrim.snippad.service.config.Configuration;
import org.outerrim.snippad.service.config.ConfigurationException;
import org.outerrim.snippad.ui.swt.actions.AboutAction;
import org.outerrim.snippad.ui.swt.actions.DeleteWikiWordAction;
import org.outerrim.snippad.ui.swt.actions.ExitAction;
import org.outerrim.snippad.ui.swt.actions.MoveWordBackAction;
import org.outerrim.snippad.ui.swt.actions.MoveWordDownAction;
import org.outerrim.snippad.ui.swt.actions.MoveWordForwardAction;
import org.outerrim.snippad.ui.swt.actions.MoveWordUpAction;
import org.outerrim.snippad.ui.swt.actions.NewWikiAction;
import org.outerrim.snippad.ui.swt.actions.NewWikiWordAction;
import org.outerrim.snippad.ui.swt.actions.OpenAction;
import org.outerrim.snippad.ui.swt.actions.PreferencesAction;
import org.outerrim.snippad.ui.swt.actions.PrintAction;
import org.outerrim.snippad.ui.swt.actions.RecentDocumentAction;
import org.outerrim.snippad.ui.swt.actions.RenameWordAction;
import org.outerrim.snippad.ui.swt.actions.SaveAsWikiAction;
import org.outerrim.snippad.ui.swt.actions.SaveWikiAction;
import org.outerrim.snippad.ui.swt.actions.ShowEditorAction;
import org.radeox.api.engine.context.RenderContext;
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
    private SnipPadRenderEngine wikiEngine = new SnipPadRenderEngine();
    
    private String loadedFilename;
    private WikiWord rootWiki;
    private WikiWord selectedWikiWord;
    private boolean modified;
    
    // Actions
    private NewWikiAction actionNew;
    private OpenAction actionOpen;
    private SaveWikiAction actionSaveWiki;
    private SaveAsWikiAction actionSaveAsWiki;
    private PrintAction actionPrint;
    private ExitAction actionExit;
    private ShowEditorAction actionEdit;
    private NewWikiWordAction actionNewWikiWord;
    private DeleteWikiWordAction actionDeleteWikiWord;
    private RenameWordAction actionRenameWord;
	private MoveWordUpAction actionMoveUp;
	private MoveWordDownAction actionMoveDown;
	private MoveWordBackAction actionMoveBack;
	private MoveWordForwardAction actionMoveForward;
    private PreferencesAction actionPreferences;
    private AboutAction actionAbout;

	private MenuManager fileMenu;
	private List recentList = new ArrayList();
	
    static private Configuration config = new Configuration();
    
    static private String TITLE = "SnipPad v" + SnipPadConstants.VERSION;
    static private Log log = LogFactory.getLog( SnipPad.class );
    
    public SnipPad() {
        super( null );
        
        // Load wiki engine and render a blank String, which will pre-load 
        // the wiki engine
        wikiContext.setRenderEngine( wikiEngine );
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
		updateRecentDocuments( filename );
		
        setLoadedFilename( filename );
        clear();
        rootWiki = wiki;
        
        tree.setInput( rootWiki );
        wikiEngine.setDocument( rootWiki );
        
        actionSaveAsWiki.setEnabled( true );
        actionPrint.setEnabled( true );
        actionNewWikiWord.setEnabled( true );
    }
    
    public boolean isModified() { return modified; }
    public void setModified( boolean mod ) { 
        modified = mod;
		actionSaveWiki.setEnabled( modified );
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
            if( isModified() == true ) {
    	        MessageBox message = new MessageBox( getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO );
    	        message.setMessage( "Document not saved, still exit?" );
    	        int confirm = message.open();
    	        if( confirm == SWT.NO ) { return false; }
            }
            
            Point currentSize = this.getShell().getSize();
            config.setInitialSize( currentSize.x, currentSize.y );
            config.save();
        } catch( ConfigurationException E ) {
            logError( "Cannot save configuration", E );
        }
        
		browser.dispose();
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
        actionPrint = new PrintAction();
        actionExit = new ExitAction( this );
        actionEdit = new ShowEditorAction( this );
        actionNewWikiWord = new NewWikiWordAction( this );
        actionDeleteWikiWord = new DeleteWikiWordAction( this );
        actionRenameWord = new RenameWordAction( this );
		actionMoveUp = new MoveWordUpAction( this );
		actionMoveDown = new MoveWordDownAction( this );
		actionMoveBack = new MoveWordBackAction( this );
		actionMoveForward = new MoveWordForwardAction( this );
        actionPreferences = new PreferencesAction( this );
        actionAbout = new AboutAction( this );
        
        actionSaveWiki.setEnabled( false );
        actionSaveAsWiki.setEnabled( false );
        actionPrint.setEnabled( false );
        actionNewWikiWord.setEnabled( false );
        actionDeleteWikiWord.setEnabled( false );
        actionRenameWord.setEnabled( false );
		actionMoveUp.setEnabled( false );
		actionMoveDown.setEnabled( false );
		actionMoveBack.setEnabled( false );
		actionMoveForward.setEnabled( false );
        
        addStatusLine();
        addMenuBar();
		addToolBar( SWT.FLAT );
        
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
        browser.addLocationListener( new WikiLocationListener() );
        actionPrint.setBrowser( browser );
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
//        int ops = DND.DROP_MOVE;
//        Transfer[] transfers = new Transfer[] { WikiTransfer.getInstance() };
//        tree.addDragSupport( ops, transfers, new WikiWordDragListener( tree ) );
//        tree.addDropSupport( ops, transfers, new WikiWordTreeDropAdapter( tree ) );
        
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
        
        fileMenu = new MenuManager( "&File" );
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
        fileMenu.add( actionPrint );		
        fileMenu.add( new Separator() {
			public String getId() { return "RECENT"; }
        });
		// Recent Documents list
		reloadRecentDocuments();
        fileMenu.add( new Separator() );
        fileMenu.add( actionExit );
        
        editMenu.add( actionNewWikiWord );
        editMenu.add( actionRenameWord );
        editMenu.add( actionDeleteWikiWord );
        editMenu.add( new Separator() );
		editMenu.add( actionMoveUp );
		editMenu.add( actionMoveDown );
		editMenu.add( actionMoveBack );
		editMenu.add( actionMoveForward );
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
        
		toolBar.add( actionNew );
        toolBar.add( actionOpen );
        toolBar.add( actionSaveWiki );
		toolBar.add( new Separator() );
		toolBar.add( actionPrint );
		toolBar.add( new Separator() );
        toolBar.add( actionNewWikiWord );
		toolBar.add( actionDeleteWikiWord );
		toolBar.add( new Separator() );
		toolBar.add( actionMoveUp );
		toolBar.add( actionMoveDown );
		toolBar.add( actionMoveBack );
		toolBar.add( actionMoveForward );
		
        return toolBar;
    }
    
    /**
     * Creates the popup menu for the wiki tree.
     * @return Menu containing the entries
     */
    private Menu createTreePopupMenu() {
        
        MenuManager popupMenu = new MenuManager();
        popupMenu.add( actionNewWikiWord );
        popupMenu.add( actionRenameWord );
        popupMenu.add( actionDeleteWikiWord );
		popupMenu.add( new Separator() );
		popupMenu.add( actionMoveUp );
		popupMenu.add( actionMoveDown );
		popupMenu.add( actionMoveBack );
		popupMenu.add( actionMoveForward );

        return popupMenu.createContextMenu( tree.getTree() );
    }
    
    private String getTitle( String f ) {
        return TITLE + " : " + (f == null ? "(New File)" : f);
    }
    
	private void updateRecentDocuments( String file ) {
		for( Iterator it = recentList.iterator(); it.hasNext(); ) {
			IContributionItem item = fileMenu.remove( ((IAction)it.next()).getId() );
			log.debug( "Removed item : " + item );
			if( item != null ) {
				item.update();
			}
		}
		recentList.clear();
		
		getConfiguration().addRecentDocument( file );
		reloadRecentDocuments();
	}
	
	private void reloadRecentDocuments() {
		for( Iterator it = getConfiguration().getRecentDocumentsIterator(); it.hasNext(); ) {
			IAction action = new RecentDocumentAction( this, (String)it.next() );
			fileMenu.insertAfter( "RECENT", action );
			recentList.add( action );
		}
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
        WikiWord newWord = (WikiWord)selection.getFirstElement();

        // If the new and old are the same, do nothing
        if( selectedWikiWord == newWord ) {
            return;
        }
        
        // Check if the last word's text is still modified, and warn if it is
        if( text.isModified() ) {
	        MessageBox message = new MessageBox( getShell(), 
	                SWT.ICON_QUESTION | SWT.YES | SWT.NO );
	        message.setMessage( "Wiki text not saved, lose edits?" );
	        int confirm = message.open();
	        if( confirm == SWT.NO ) { 
                IStructuredSelection select = new StructuredSelection( selectedWikiWord );
                tree.setSelection( select, true );
                return;
	        }
        }
                
        selectedWikiWord = newWord;
        if( selectedWikiWord == null ) {	// No selection, clear everything
            clear();
            actionDeleteWikiWord.setEnabled( false );
            actionRenameWord.setEnabled( false );
			actionMoveUp.setEnabled( false );
			actionMoveDown.setEnabled( false );
			actionMoveBack.setEnabled( false );
			actionMoveForward.setEnabled( false );
            return; 
        } else {
            actionDeleteWikiWord.setEnabled( true );
            actionRenameWord.setEnabled( true );
			actionMoveUp.setEnabled( true );
			actionMoveDown.setEnabled( true );
			actionMoveBack.setEnabled( true );
			actionMoveForward.setEnabled( true );
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
    
    private class WikiLocationListener 
    implements LocationListener {
        /**
         * @see org.eclipse.swt.browser.LocationListener#changed(org.eclipse.swt.browser.LocationEvent)
         */
        public void changed( LocationEvent event ) {
            // TODO Implement Back/Forward ability
        }
        
        /**
         * @see org.eclipse.swt.browser.LocationListener#changing(org.eclipse.swt.browser.LocationEvent)
         */
        public void changing( LocationEvent event ) {
            // Check to see if we are a wiki:// URL
            if( event.location.startsWith( "wiki" ) ) {
                log.debug( "Going to location : " + event.location );
                String location = event.location.replaceAll( "wiki://", "" );
                WikiWord link = WikiWordUtils.wordExists( rootWiki, location );
                if( link == null ) {
                    log.error( "Linked word does not exist" );
                }
                IStructuredSelection selection = new StructuredSelection( link );
                tree.setSelection( selection, true );
                
                // Set doit to false so the browser itself doesn't try to navigate
                event.doit = false;
            }
        }
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
