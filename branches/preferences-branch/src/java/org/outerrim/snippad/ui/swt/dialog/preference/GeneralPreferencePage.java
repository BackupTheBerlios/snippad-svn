/*
 * GeneralPreferencePage.java
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
package org.outerrim.snippad.ui.swt.dialog.preference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.outerrim.snippad.ui.swt.SnipPad;
import org.outerrim.snippad.ui.swt.dialog.DialogUtils;

/**
 * @author darkjedi
 */
public class GeneralPreferencePage extends PreferencePage {

    static private final Log log = LogFactory.getLog( GeneralPreferencePage.class );
    
    public GeneralPreferencePage() {
        super( "General" );
    }

    public IPreferenceNode getNode() {
        IPreferenceNode node = new PreferenceNode( "General", this );
        return node;
    }
    
    /**
     * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
     */
    protected IPreferenceStore doGetPreferenceStore() {
        return SnipPad.getConfiguration().getStore();
    }
    
    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents( Composite parent ) {
        Composite composite = new Composite( parent, SWT.NULL );
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout( layout );
        GridData data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = SWT.FILL;
        composite.setLayoutData( data );
        
        Composite css = new Composite( composite, SWT.NULL );
        GridLayout cssLayout = new GridLayout();
        cssLayout.numColumns = 2;
        css.setLayout( cssLayout );
        css.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        
        Label cssLabel = DialogUtils.createLabel( css, "CSS File" );
        Text cssText = DialogUtils.createTextControl( css );

        Button showButton = DialogUtils.createCheckButton( 
                composite, 
                "Show Editor on Startup" );

        return composite;
    }
    
    /**
     * @see org.eclipse.jface.preference.PreferencePage#doComputeSize()
     */
    protected Point doComputeSize() {
        return new Point( 300, 300 );
    }
}
