/*
 * ConfigurationDriverPropertiesImpl.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.graphics.Point;

/**
 * Manages the saving and loading of a Configuration to a properties file.
 * Right now it saves to ~/.snippad.
 * @author darkjedi
 */
public class ConfigurationDriverPropertiesImpl 
implements ConfigurationDriver {
    static private String BASE = "org.outerrim.snippad";
    static private String CSS_LOCATION = BASE + ".cssLocation";
    static private String SAVE_AS_LOCATION = BASE + ".saveAsLocation";
    static private String SHOW_EDITOR = BASE + ".showEditor";
    static private String INITIAL_SIZE_W = BASE + ".initialSize.width";
    static private String INITIAL_SIZE_H = BASE + ".initialSize.height";
    
    static private final String HOME = System.getProperty( "user.home" );
    static private final String CONFIG_FILENAME = HOME + File.separator + ".snippad";
    
    static private Log log = LogFactory.getLog( ConfigurationDriverPropertiesImpl.class );
    
    public void save( Configuration config )
    throws ConfigurationException {
        Properties props = new Properties();
        
        props.setProperty( CSS_LOCATION, config.getCssLocation() );
        props.setProperty( SAVE_AS_LOCATION, config.getDefaultSaveAsLocation() );
        props.setProperty( SHOW_EDITOR, Boolean.toString( config.showEditor() ) );
        
        Point loc = config.getInitialSize();
        props.setProperty( INITIAL_SIZE_W, Integer.toString( loc.x ) );
        props.setProperty( INITIAL_SIZE_H, Integer.toString( loc.y ) );
        
        try {
            props.store( new FileOutputStream( CONFIG_FILENAME ), "Snippad Configuration" );
        } catch( IOException E ) {
            throw new ConfigurationException( E );
        }
    }
    
    public Configuration load() 
    throws ConfigurationException {
        Configuration config = new Configuration();
        Properties props = new Properties();
        
        try {
            log.debug( "Loading properties from : " + CONFIG_FILENAME );
            props.load( new FileInputStream( CONFIG_FILENAME ) );
            
            String property = props.getProperty( CSS_LOCATION );
            if( property != null ) { config.setCssLocation( property ); }
            
            property = props.getProperty( SAVE_AS_LOCATION );
            if( property != null ) { config.setDefaultSaveAsLocation( property ); }
            
            property = props.getProperty( SHOW_EDITOR );
            if( property != null ) { config.setShowEditor( Boolean.valueOf( property ).booleanValue() ); }
            
            property = props.getProperty( INITIAL_SIZE_W );
            if( property != null ) {
                int w = Integer.parseInt( property );
                int h = Integer.parseInt( props.getProperty( INITIAL_SIZE_H ) );
                config.setInitialSize( w, h );
            }
        } catch( FileNotFoundException E ) {
            // Catch and rethrow, as IOE is a superclass of FNFE 
            throw new ConfigurationException( E );
        } catch( IOException E ) {
            throw new ConfigurationException( E );
        }
        
        return config;
    }
}
