/*
 * Filename : Configuration.java
 * Created on Oct 8, 2004
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
package org.outerrim.snippad.service.config;

import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.graphics.Point;
import org.outerrim.snippad.SnipPadConstants;

/**
 * Utility class to hold all configurable variables.
 * @author darkjedi
 */
public class Configuration {
    static public final String CSS_LOCATION = "css_location";
    static public final String SHOW_EDITOR = "show_editor";
    static public final String INITIAL_SIZE_X = "initial_size_x";
    static public final String INITIAL_SIZE_Y = "initial_size_y";
    static public final String SAVE_AS_LOCATION = "save_as_location";
    
    static private PreferenceStore store = new PreferenceStore( 
            SnipPadConstants.PREFERENCE_FILENAME );

    public Configuration() {
        init();
    }
    
    public void save() 
    throws ConfigurationException {
        try {
            store.save();
        } catch( IOException E ) {
            throw new ConfigurationException( E );
        }
    }

    public void load()
    throws ConfigurationException {
        try {
            store.load();
        } catch( IOException E ) {
            throw new ConfigurationException( E );
        }
    }
    
    public IPreferenceStore getStore() { return store; }
    
    public String getCssLocation() { return store.getString( CSS_LOCATION ); }
    public String getDefaultSaveAsLocation() { return store.getString( SAVE_AS_LOCATION ); }
    public boolean showEditor() { return store.getBoolean( SHOW_EDITOR ); }
    public Point getInitialSize() { return new Point( store.getInt( 
            INITIAL_SIZE_X ), store.getInt( INITIAL_SIZE_Y ) ); }
    
    public void setCssLocation( String css ) { store.setValue( CSS_LOCATION, css ); }
    public void setDefaultSaveAsLocation( String loc ) { store.setValue( SAVE_AS_LOCATION, loc ); }
    public void setShowEditor( boolean show ) { store.setValue( SHOW_EDITOR, show ); }
    public void setInitialSize( int x, int y ) { 
        store.setValue( INITIAL_SIZE_X, x );
        store.setValue( INITIAL_SIZE_Y, y );
    }

    private void init() {
        store.setDefault( CSS_LOCATION, "http://snippad.berlios.de/snippad.css" );
        store.setDefault( SHOW_EDITOR, true );
        store.setDefault( INITIAL_SIZE_X, 1024 );
        store.setDefault( INITIAL_SIZE_Y, 600 );
        store.setDefault( SAVE_AS_LOCATION, System.getProperty( "user.home" ) );
    }
}
