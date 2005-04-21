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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.outerrim.snippad.SnipPadConstants;
import org.outerrim.snippad.data.WikiWord;
import org.outerrim.snippad.service.config.Configuration;
import org.outerrim.snippad.service.config.ConfigurationException;
import org.outerrim.snippad.ui.swt.actions.RecentDocumentAction;

/**
 * Main window for the SWT UI of SnipPad
 * @author darkjedi
 */
public class SnipPad extends ApplicationWindow {
    private CTabFolder tabFolder;
	private MenuManager fileMenu;
	private List<RecentDocumentAction> recentList = new ArrayList<RecentDocumentAction>();
    private Map<WikiViewer,CTabItem> tabItemMap = new HashMap<WikiViewer,CTabItem>();
	
    static private Configuration config = new Configuration();
    
    static private String TITLE = "SnipPad v" + SnipPadConstants.VERSION;
    static private Log log = LogFactory.getLog( SnipPad.class );
    
    public SnipPad() {
        super( null );
        
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
    
    public void openWiki( WikiWord word, String filename ) {
        CTabItem item = new CTabItem( tabFolder, SWT.CLOSE );
        
        WikiViewer viewer = new WikiViewer( tabFolder );
        viewer.setWiki( word, filename );
        viewer.addModificationListener( new WikiViewerModificationListener() );
        item.setText( viewer.getTitle() );
        item.setControl( viewer );
        tabItemMap.put( viewer, item );
        tabFolder.setSelection( item );
        
        ActionManager.getPrintAction().setEnabled( true );
        ActionManager.getNewWikiWordAction().setEnabled( true );
    }
    
    public List<WikiViewer> getWikiViewers() {
        List<WikiViewer> list = new LinkedList<WikiViewer>();
        CTabItem[] items = tabFolder.getItems();
        for( int i = items.length - 1; i >= 0; --i ) {
            list.add( (WikiViewer)items[i].getControl() );
        }
        return list;
    }
    
    public WikiViewer getCurrentWikiViewer() {
        WikiViewer viewer = (WikiViewer)tabFolder.getSelection().getControl();
        return viewer;
    }
    
    public String getVersion() { return SnipPadConstants.VERSION; }
    
    /**
     * Perform any cleanup necessary to exit the program (save configuration, etc).
     * @see org.eclipse.jface.window.Window#close()
     */
    public boolean close() {
        try {
            int modified = modifiedCount();
            if( modified > 0 ) {
    	        MessageBox message = new MessageBox( getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO );
    	        message.setMessage( modified + " document(s) not saved, still exit?" );
    	        int confirm = message.open();
    	        if( confirm == SWT.NO ) { return false; }
            }
            
            Point currentSize = this.getShell().getSize();
            config.setInitialSize( currentSize.x, currentSize.y );
            config.save();

            CTabItem[] items = tabFolder.getItems();
            for( int i = items.length - 1; i >= 0; --i ) {
                Control control = items[i].getControl();
                if( control instanceof WikiViewer ) {
                    ((WikiViewer)control).dispose();
                } else {
                    log.debug( "Control is not of type WikiViewer : " + control.getClass().getName() );
                }
            }
        } catch( ConfigurationException E ) {
            logError( "Cannot save configuration", E );
        }
        
        return super.close();
    }
    
    private int modifiedCount() {
        int count = 0;
        for( WikiViewer view : getWikiViewers() ) {
            if( view.isModified() ) {
                ++count;
            }
        }
        return count;
    }
    
    /**
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell( Shell shell ) {
        
        addStatusLine();
        addMenuBar();
		addToolBar( SWT.FLAT );
        
        ActionManager.registerSnipPad( this );
        
        super.configureShell( shell );
    }
    
    /**
     * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents( Composite parent ) {
        getShell().setText( TITLE );
        
        tabFolder = new CTabFolder( parent, SWT.BORDER | SWT.FLAT );
        tabFolder.setSimple( false );
        
        return tabFolder;
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
        
        fileMenu.add( ActionManager.getNewWikiAction() );
        fileMenu.add( ActionManager.getOpenAction() );
        fileMenu.add( ActionManager.getSaveWikiAction() );
        fileMenu.add( ActionManager.getSaveAsWikiAction() );
        fileMenu.add( new Separator() );
        fileMenu.add( ActionManager.getPrintAction() );		
        fileMenu.add( new Separator() {
			public String getId() { return "RECENT"; }
        });
		// Recent Documents list
		reloadRecentDocuments();
        fileMenu.add( new Separator() );
        fileMenu.add( ActionManager.getExitAction() );
        
        editMenu.add( ActionManager.getNewWikiWordAction() );
        editMenu.add( ActionManager.getRenameWordAction() );
        editMenu.add( ActionManager.getDeleteWikiWordAction() );
        editMenu.add( new Separator() );
		editMenu.add( ActionManager.getMoveUpAction() );
		editMenu.add( ActionManager.getMoveDownAction() );
		editMenu.add( ActionManager.getMoveBackAction() );
		editMenu.add( ActionManager.getMoveForwardAction() );
        editMenu.add( new Separator() );
        editMenu.add( ActionManager.getEditAction() );
        editMenu.add( ActionManager.getPreferencesAction() );
        
        helpMenu.add( ActionManager.getAboutAction() );
        
        return mainMenu;
    }
    
    /**
     * @see org.eclipse.jface.window.ApplicationWindow#createToolBarManager(int)
     */
    protected ToolBarManager createToolBarManager( int style ) {
        ToolBarManager toolBar = new ToolBarManager( style );
        
		toolBar.add( ActionManager.getNewWikiAction() );
        toolBar.add( ActionManager.getOpenAction() );
        toolBar.add( ActionManager.getSaveWikiAction() );
		toolBar.add( new Separator() );
		toolBar.add( ActionManager.getPrintAction() );
		toolBar.add( new Separator() );
        toolBar.add( ActionManager.getNewWikiWordAction() );
		toolBar.add( ActionManager.getDeleteWikiWordAction() );
		toolBar.add( new Separator() );
		toolBar.add( ActionManager.getMoveUpAction() );
		toolBar.add( ActionManager.getMoveDownAction() );
		toolBar.add( ActionManager.getMoveBackAction() );
		toolBar.add( ActionManager.getMoveForwardAction() );
		
        return toolBar;
    }
    
    private String getTitle( String f ) {
        return TITLE + " : " + (f == null ? "(New File)" : f);
    }
    
	private void updateRecentDocuments( String file ) {
        for( IAction action : recentList ) {
			IContributionItem item = fileMenu.remove( action.getId() );
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
            RecentDocumentAction action = new RecentDocumentAction( (String)it.next() );
            action.setSnipPad( this );
			fileMenu.insertAfter( "RECENT", action );
			recentList.add( action );
		}
	}
    
    private class WikiViewerModificationListener
    implements ModificationListener {

        /**
         * @see org.outerrim.snippad.ui.swt.ModificationListener#modified(org.outerrim.snippad.ui.swt.ModifiedEvent)
         */
        public void modified( ModifiedEvent me ) {
            log.debug( "modifed()" );
            WikiViewer source = (WikiViewer)me.getSource();
            CTabItem tabItem = (CTabItem)tabItemMap.get( source );
            tabItem.setText( source.getTitle() );
            
            if( me.isModified() ) {
                ActionManager.getSaveWikiAction().setEnabled( true );
                ActionManager.getSaveAsWikiAction().setEnabled( true );
            } else {
                ActionManager.getSaveWikiAction().setEnabled( false );
                ActionManager.getSaveAsWikiAction().setEnabled( false );
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
        Status status = new Status( IStatus.ERROR, "org.outerrim.snippad.SnipPad", IStatus.ERROR, E.getMessage(), E );
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
