/*
 * WikiWord.java
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
package org.outerrim.snippad.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Object that stores a WikiWord. Heart of the snippad document structure, as 
 * everything is a wikiword, even the document itself.
 * 
 * @author darkjedi
 */
public class WikiWord {
    private String name;
    private String wikiText;
    private WikiWord parent;
    private List wikiWords = new ArrayList();
    
    static private final Log log = LogFactory.getLog( WikiWord.class );
    
    /** 
     * Used to store a cached version of the rendered wikiText.
     * Will only be used if the wikiText is asked for, to save loading time. 
     */
    private String htmlText;
    
    public WikiWord() {}
    
    public WikiWord( String name, String text ) {
        this.name = name;
        this.wikiText = text;
    }
    
    public String getName() { return name; }
    public String getWikiText() { return wikiText; }
    public String getHtmlText() { return htmlText; }
    public WikiWord getParent() { return parent; }
    
    /**
     * Retrieves the children words of this word.
     * 
     * @return List containing the children WikiWords
     */
    public List getWikiWords() {
        return wikiWords;
    }
    
    public void setName( String name ) { this.name = name; }
    public void setWikiText( String text ) { this.wikiText = text; }
    
    public void setParent( WikiWord word ) {
        if( word != null && word.equals( parent ) ) { return; }
        
        if( parent != null ) {
            log.debug( "Deleting " + this.getName() + " from word : " + parent.getName() );
            parent.deleteWikiWord( this );
        }

        parent = word;
        if( parent != null ) {
            log.debug( "Adding " + this.getName() + " to word : " + parent.getName() );
            parent.addWikiWord( this );
        }

    }

    /**
     * Sets the HTML text for this wikiword. Adds the base html tags, plus the
     * CSS location.
     * 
     * @param text HTML text
     * @param cssLocation Location of the CSS file for display
     */
    public void setHtmlText( String text, String cssLocation ) {
        String html = "<html>\n" +
                      "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssLocation + "\" />\n" +
                      "<body>\n" + 
                      text + 
                      "\n</body></html>\n";
        this.htmlText = html; 
    }
    
    /**
     * Adds a child word to this word.
     * @param word The child to add
     */
    public void addWikiWord( WikiWord word ) {
        if( !wikiWords.contains( word ) ) {
            wikiWords.add( word );
            word.setParent( this );
        }
    }
    
    public void addWikiWord( WikiWord word, int location ) {
        if( wikiWords.contains( word ) ) {
            wikiWords.add( location, word );
        } else {
            wikiWords.add( location, word );
            word.setParent( this );
        }
    }
    
    /**
     * Deletes a child word from this word.
     * @param word The child to remove
     */
    public void deleteWikiWord( WikiWord word ) {
        wikiWords.remove( word );
    }
    
    public boolean hasParent( WikiWord word ) {
        if( parent == null ) { return false; }
        
        return parent.equals( word ) || parent.hasParent( word );
    }
    
    public String toString() { return name; }
    
    public boolean equals( Object obj ) {
        if( !(obj instanceof WikiWord ) ) { return false; }
        if( this == obj ) { return true; }
        WikiWord word = (WikiWord)obj;
        return (name.equals( word.name ) &&
                wikiText.equals( word.wikiText ) &&
                wikiWords.equals( word.wikiWords ));
    }
}
