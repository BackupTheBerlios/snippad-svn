/*
 * WikiWordUtils.java
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

import java.util.List;

import org.outerrim.snippad.data.WikiWord;

/**
 * @author darkjedi
 */
public final class WikiWordUtils {
    /**
     * Recursive method to see if a particular wikiword is available.
     *
     * @param parent
     *            Parent wiki word
     * @param name
     *            WikiWord name to look for
     * @return true if it exists, false otherwise
     */
    public static WikiWord wordExists(
            final WikiWord parent,
            final String name ) {
        WikiWord word = null;

        List<WikiWord> children = parent.getWikiWords();
        for( WikiWord child : children ) {
            // First check to see if the child is what we want
            if( child.getName().equals( name ) ) {
                word = child;
                break;
            }

            // If not, check the child's children
            word = wordExists( child, name );
            if( word != null ) {
                break;
            }
        }

        return word;
    }

    private WikiWordUtils() { }
}
