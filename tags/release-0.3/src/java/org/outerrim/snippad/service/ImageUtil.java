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
            URL save = registry.getClass().getResource( "/icons/save_edit.gif" );
            registry.put( "save",
                          ImageDescriptor.createFromURL( save ) );
            URL saveas = registry.getClass().getResource( "/icons/saveas_edit.gif" );
            registry.put( "saveas",
                          ImageDescriptor.createFromURL( saveas ) );
            URL newdoc = registry.getClass().getResource( "/icons/newfile_wiz.gif" );
            registry.put( "new",
                          ImageDescriptor.createFromURL( newdoc ) );
            URL open = registry.getClass().getResource( "/icons/open.gif" );
            registry.put( "open",
                          ImageDescriptor.createFromURL( open ) );
            URL print = registry.getClass().getResource( "/icons/print_edit.gif" );
            registry.put( "print",
                          ImageDescriptor.createFromURL( print ) );
            URL newword = registry.getClass().getResource( "/icons/import_log.gif" );
            registry.put( "newword",
                          ImageDescriptor.createFromURL( newword ) );
            URL deleteword = registry.getClass().getResource( "/icons/remove.gif" );
            registry.put( "deleteword",
                          ImageDescriptor.createFromURL( deleteword ) );
            URL renameword = registry.getClass().getResource( "/icons/refresh.gif" );
            registry.put( "renameword",
                          ImageDescriptor.createFromURL( renameword ) );
            URL up = registry.getClass().getResource( "/icons/up.gif" );
            registry.put( "up",
                          ImageDescriptor.createFromURL( up ) );
            URL down = registry.getClass().getResource( "/icons/down.gif" );
            registry.put( "down",
                          ImageDescriptor.createFromURL( down ) );
            URL back = registry.getClass().getResource( "/icons/back.gif" );
            registry.put( "back",
                          ImageDescriptor.createFromURL( back ) );
            URL forward = registry.getClass().getResource( "/icons/forward.gif" );
            registry.put( "forward",
                          ImageDescriptor.createFromURL( forward ) );
        }
        
        return registry;
    }
}
