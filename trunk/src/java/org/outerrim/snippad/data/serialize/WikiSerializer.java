/*
 * WikiSerializer.java
 * Created on Sep 17, 2004
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
package org.outerrim.snippad.data.serialize;

import java.io.InputStream;
import java.io.OutputStream;

import org.outerrim.snippad.data.WikiWord;

/**
 * Interface for classes that process a wiki document.
 * @author darkjedi
 */
public interface WikiSerializer {
    /**
     * Saves the document.
     *
     * @param wiki Root wikiword
     * @param output Where to save the document to
     * @throws SerializeException If an error occurred while saving
     */
    void save( WikiWord wiki, OutputStream output ) throws SerializeException;

    /**
     * Loads a document.
     *
     * @param input Where the document is stored.
     * @return The root wikiword containing the document.
     * @throws SerializeException If an error occurred while loading
     */
    WikiWord load( InputStream input ) throws SerializeException;
}
