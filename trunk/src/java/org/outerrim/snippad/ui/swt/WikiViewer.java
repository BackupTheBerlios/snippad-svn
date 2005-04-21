/**
 * 
 */
package org.outerrim.snippad.ui.swt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.SnipPadRenderEngine;
import org.outerrim.snippad.service.WikiWordUtils;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.engine.context.BaseRenderContext;

/**
 * @author <a href="mailto:sithlord@berlios.de">Michael Osterlie</a>
 *
 */
public class WikiViewer extends Composite {
    private RenderContext wikiContext = new BaseRenderContext();
    private SnipPadRenderEngine wikiEngine = new SnipPadRenderEngine();
    
    private TreeViewer tree;
    private Browser browser;
    private WikiEditor text;
    private SashForm editorSash;

    private WikiWord rootWiki;
    private WikiWord selectedWikiWord;
    private String loadedFilename;
    private boolean modified;

    private List<ModificationListener> modificationListeners = new LinkedList<ModificationListener>();
    
    static private final Log log = LogFactory.getLog( WikiViewer.class );
    
    /**
     * @param parent
     * @param style
     */
    public WikiViewer( Composite parent ) {
        super( parent, SWT.BORDER );
        
        buildLayout();
        
        // Load wiki engine and render a blank String, which will pre-load 
        // the wiki engine
        wikiContext.setRenderEngine( wikiEngine );
        wikiEngine.render( "", wikiContext );
    }

    public WikiWord getRootWiki() { return rootWiki; }
    public WikiWord getSelectedWiki() { return selectedWikiWord; }
    public String getLoadedFilename() { return loadedFilename; }

    public void print() {
        browser.execute( "print()" );
    }
    
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
//        updateRecentDocuments( filename );
        
        setLoadedFilename( filename );
        clear();
        rootWiki = wiki;
        
        tree.setInput( rootWiki );
        wikiEngine.setDocument( rootWiki );
    }
    
    public boolean isModified() { return modified; }
    public void setModified( boolean mod ) { 
        modified = mod;
        notifyModificationListeners();
    }
    
    public String getTitle() {
        int index = loadedFilename.lastIndexOf( File.separator );
        String file = loadedFilename.substring( index + 1, loadedFilename.length() );

        return file + (modified ? "*" : "");
    }
    
    public void refreshTree() {
        tree.refresh();
    }
    
    public void dispose() {
        log.debug( "Disposing" );
        browser.dispose();
        super.dispose();
    }
    
    public void addModificationListener( ModificationListener listener ) {
        modificationListeners.add( listener );
    }
    
    public void removeModificationListener( ModificationListener listener ) {
        modificationListeners.remove( listener );
    }
    
    private void notifyModificationListeners() {
        ModifiedEvent me = new ModifiedEvent( this, modified );
        for( ModificationListener ml : modificationListeners ) {
            ml.modified( me );
        }
    }
    
    private void buildLayout() {
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        setLayout( layout );
        
        SashForm sashForm = new SashForm( this, SWT.HORIZONTAL );
        GridData sashData = new GridData( 
                GridData.FILL,
                GridData.FILL,
                true,
                true );
        sashForm.setLayoutData( sashData );
        
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
        text = new WikiEditor( editorSash );
        text.setEnabled( false );
        text.addSaveListener( new SelectionListener() {
            public void widgetSelected( SelectionEvent event ) {
                handleSaveWikiWord();
            }
            
            public void widgetDefaultSelected( SelectionEvent event ) {
                handleSaveWikiWord();
            }
        });
        
        showEditor( SnipPad.getConfiguration().showEditor() );
        
        sashForm.setWeights( new int[] { 20, 80 } );
        editorSash.setWeights( new int[] { 60, 40 } );
        
        // Setup drag and drop
//        int ops = DND.DROP_MOVE;
//        Transfer[] transfers = new Transfer[] { WikiTransfer.getInstance() };
//        tree.addDragSupport( ops, transfers, new WikiWordDragListener( tree ) );
//        tree.addDropSupport( ops, transfers, new WikiWordTreeDropAdapter( tree ) );
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
        if( selectedWikiWord == null ) {    // No selection, clear everything
            clear();
            ActionManager.getDeleteWikiWordAction().setEnabled( false );
            ActionManager.getRenameWordAction().setEnabled( false );
            ActionManager.getMoveUpAction().setEnabled( false );
            ActionManager.getMoveDownAction().setEnabled( false );
            ActionManager.getMoveBackAction().setEnabled( false );
            ActionManager.getMoveForwardAction().setEnabled( false );
            return; 
        } else {
            ActionManager.getDeleteWikiWordAction().setEnabled( true );
            ActionManager.getRenameWordAction().setEnabled( true );
            ActionManager.getMoveUpAction().setEnabled( true );
            ActionManager.getMoveDownAction().setEnabled( true );
            ActionManager.getMoveBackAction().setEnabled( true );
            ActionManager.getMoveForwardAction().setEnabled( true );
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
        setModified( true );
    }
    
    /**
     * Creates the popup menu for the wiki tree.
     * @return Menu containing the entries
     */
    private Menu createTreePopupMenu() {
        MenuManager popupMenu = new MenuManager();
        popupMenu.add( ActionManager.getNewWikiWordAction() );
        popupMenu.add( ActionManager.getRenameWordAction() );
        popupMenu.add( ActionManager.getDeleteWikiWordAction() );
        popupMenu.add( new Separator() );
        popupMenu.add( ActionManager.getMoveUpAction() );
        popupMenu.add( ActionManager.getMoveDownAction() );
        popupMenu.add( ActionManager.getMoveBackAction() );
        popupMenu.add( ActionManager.getMoveForwardAction() );

        return popupMenu.createContextMenu( tree.getTree() );
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
     * Updates the HTML in the wikiword of the text has been changed
     * @param wiki The wiki to update
     */
    private void updateWikiHtml( WikiWord wiki ) {
        String html = wikiEngine.render( wiki.getWikiText(), wikiContext );
        wiki.setHtmlText( html, SnipPad.getConfiguration().getCssLocation() );
        
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
}
