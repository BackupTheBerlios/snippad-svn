/*
 * ImageUtil.java
 * Created on Sep 19, 2004
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
package org.outerrim.snippad.service;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

/**
 * Utility classes for processing of images. Not currently used, as images aren't
 * loading correctly.
 * @author darkjedi
 */
public class ImageUtil {
    static private ImageRegistry registry;
    static private Log log = LogFactory.getLog( ImageUtil.class );
    
    static public ImageRegistry getImageRegistry() {
        if( registry == null ) {
            registry = new ImageRegistry();
            URL saveas = registry.getClass().getResource( "/icons/gtk-save-as.gif" );
            URL open = registry.getClass().getResource( "/icons/gtk-open.gif" );
            URL newWiki = registry.getClass().getResource( "/icons/gtk-new.gif" );
            registry.put( "saveas",
                          ImageDescriptor.createFromURL( saveas ) );
            registry.put( "open",
                          ImageDescriptor.createFromURL( open ) );
            registry.put( "newWiki",
                          ImageDescriptor.createFromURL( newWiki ) );
        }
        
        return registry;
    }
}
