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
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.StringFieldEditor;
import org.outerrim.snippad.service.config.Configuration;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * @author darkjedi
 */
public class GeneralPreferencePage extends FieldEditorPreferencePage {

    private static final Log LOG = LogFactory.getLog(
            GeneralPreferencePage.class );

    /**
     */
    public GeneralPreferencePage() {
        super( "General", FLAT );
    }

    /**
     * @return Preference node
     */
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
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#
     *      createFieldEditors()
     */
    protected void createFieldEditors() {
        addField( new StringFieldEditor(
                Configuration.CSS_LOCATION,
                "CSS Location",
                getFieldEditorParent() ) );
        addField( new BooleanFieldEditor(
                Configuration.SHOW_EDITOR,
                "Show Editor By Default",
                getFieldEditorParent() ) );
        addField( new IntegerFieldEditor(
                Configuration.NUMBER_RECENT,
                "# of Recent Documents to Track",
                getFieldEditorParent() ) );
    }
}
