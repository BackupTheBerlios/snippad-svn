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

import org.eclipse.swt.graphics.Point;

/**
 * Utility class to hold all configurable variables.
 * @author darkjedi
 */
public class Configuration {
    /** Location of the CSS file to use when rendering the HTML */
    private String cssLocation = "http://snippad.berlios.de/snippad.css";
    
    /** Show the Wiki Editor by default on startup */
    private boolean showEditor = true;
    
    /** Initial size of the window */
    private Point initialSize = new Point( 1024, 600 );
    
    /** Default location for the Save As dialog */
    private String saveAsLocation = System.getProperty( "user.home" );
    
    public String getCssLocation() { return cssLocation; }
    public String getDefaultSaveAsLocation() { return saveAsLocation; }
    public boolean showEditor() { return showEditor; }
    public Point getInitialSize() { return initialSize; }
    
    public void setCssLocation( String css ) { cssLocation = css; }
    public void setDefaultSaveAsLocation( String loc ) { saveAsLocation = loc; }
    public void setShowEditor( boolean show ) { showEditor = show; }
    public void setInitialSize( int x, int y ) { initialSize = new Point( x, y ); }
}
