/*
 * SerializeException.java
 * Created on Sep 2, 2004
 *
 * Copyright (c)2004 Michael Osterlie
 *
 * This file is part of ack.
 *
 * ack is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * ack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.outerrim.snippad.data.serialize;

/**
 * Exception occurring when saving the document to a store.
 * @author darkjedi
 */
public class SerializeException extends Exception {
    private static final long serialVersionUID = -3440630951756453850L;

    /**
     *
     */
    public SerializeException() {
        super();
    }

    /**
     * @param message Message
     */
    public SerializeException( final String message ) {
        super( message );
    }

    /**
     * @param message Message
     * @param e Exception that caused this exception
     */
    public SerializeException( final String message, final Throwable e ) {
        super( message, e );
    }

    /**
     * @param e Exception that caused this exception
     */
    public SerializeException( final Throwable e ) {
        super( e );
    }
}
