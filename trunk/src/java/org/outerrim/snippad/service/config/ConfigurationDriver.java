/*
 * ConfigurationDriver.java
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

/**
 * Interface for processing of the configuration.
 * @author darkjedi
 */
public interface ConfigurationDriver {
    /**
     * Save the configuration.
     * @param config Configuration object holding the preferences
     * @throws ConfigurationException On an error saving the preferences
     */
    public abstract void save( Configuration config )
    throws ConfigurationException;

    /**
     * Load the configuration.
     * @return Configuration object holding the preferences.
     * @throws ConfigurationException On an error loading the preferences.
     */
    public abstract Configuration load() 
    throws ConfigurationException;
}