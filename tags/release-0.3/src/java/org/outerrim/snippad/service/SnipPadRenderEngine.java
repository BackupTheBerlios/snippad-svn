/*
 * SnipPadRenderEngine.java
 * Created on Dec 22, 2004
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

import org.outerrim.snippad.data.WikiWord;
import org.radeox.api.engine.WikiRenderEngine;
import org.radeox.engine.BaseRenderEngine;

/**
 * @author darkjedi
 */
public class SnipPadRenderEngine extends BaseRenderEngine
implements WikiRenderEngine {
    private WikiWord document;
    
    public void setDocument( WikiWord word ) {
        document = word;
    }
    
    /**
     * @see org.radeox.api.engine.WikiRenderEngine#exists(java.lang.String)
     */
    public boolean exists( String name ) {
        if( document != null ) {
            return WikiWordUtils.wordExists( document, name ) != null;
        }
        
        return false;
    }

    /**
     * @see org.radeox.api.engine.WikiRenderEngine#showCreate()
     */
    public boolean showCreate() {
        return true;
    }

    /**
     * @see org.radeox.api.engine.WikiRenderEngine#appendLink(java.lang.StringBuffer, java.lang.String, java.lang.String, java.lang.String)
     */
    public void appendLink( StringBuffer buffer,
                            String name,
                            String view,
                            String anchor) {
        buffer.append( "<a href=\"wiki://" + name + "#" + anchor + "\">" + view + "</a>" );
    }

    /**
     * @see org.radeox.api.engine.WikiRenderEngine#appendLink(java.lang.StringBuffer, java.lang.String, java.lang.String)
     */
    public void appendLink( StringBuffer buffer, String name, String view ) {
        buffer.append( "<a href=\"wiki://" + name + "\">" + view + "</a>" );
    }

    /**
     * @see org.radeox.api.engine.WikiRenderEngine#appendCreateLink(java.lang.StringBuffer, java.lang.String, java.lang.String)
     */
    public void appendCreateLink( StringBuffer buffer, 
                                  String name, 
                                  String view ) {
        buffer.append( "[create " + view + "]" );
    }

    /**
     * @see org.radeox.api.engine.RenderEngine#getName()
     */
    public String getName() {
        return "snippad-wiki";
    }
}
