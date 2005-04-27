/*
 * DialogUtils.java
 * Created on Nov 23, 2004
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
package org.outerrim.snippad.ui.swt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author darkjedi
 */
public final class DialogUtils {
    /**
     * @param parent Parent
     * @return TextControl
     */
    public static Text createTextControl( final Composite parent ) {
        Text text = new Text( parent, SWT.SINGLE | SWT.BORDER );
        GridData textData = new GridData();
        textData.horizontalAlignment = SWT.FILL;
        textData.grabExcessHorizontalSpace = true;
        text.setLayoutData( textData );
        return text;
    }

    /**
     * @param parent Parent
     * @param text Label text
     * @return Label
     */
    public static Label createLabel(
            final Composite parent,
            final String text ) {
        Label label = new Label( parent, SWT.NULL );
        label.setText( text );
        return label;
    }

    /**
     * @param parent Parent
     * @param label Label text
     * @return Check button
     */
    public static Button createCheckButton(
            final Composite parent,
            final String label ) {
        Button button = new Button( parent, SWT.CHECK | SWT.LEFT );
        button.setText( label );
        return button;
    }

    private DialogUtils() { }
}
