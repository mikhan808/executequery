/*
 * AbstractPropertiesBasePanel.java
 *
 * Copyright (C) 2002-2017 Takis Diakoumis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.executequery.gui.prefs;

import org.executequery.Constants;
import org.executequery.GUIUtilities;
import org.executequery.actions.othercommands.RestoreDefaultsCommand;
import org.executequery.localization.Bundles;
import org.underworldlabs.swing.DefaultButton;
import org.underworldlabs.util.SystemProperties;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User preferences base panel.
 *
 * @author Takis Diakoumis
 */
abstract class AbstractPropertiesBasePanel extends JPanel
        implements UserPreferenceFunction,
        PreferenceChangeListener,
        PreferenceTableModelListener {

    public static final int TABLE_ROW_HEIGHT = 26;
    
    /** common font used across props panels */
    protected static Font panelFont;

    /**
     * common layout constraints acroos props panels
     */
    protected static GridBagConstraints contentPanelConstraints;

    private List<PreferenceChangeListener> listeners;

    static {
        panelFont = new Font("dialog", Font.PLAIN, 12);
        contentPanelConstraints = new GridBagConstraints(
                1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                new Insets(5, 5, 0, 5), 0, 0);
    }

    public AbstractPropertiesBasePanel() {

        super(new GridBagLayout());
        listeners = new ArrayList<>();
        setBorder(BorderFactory.createLineBorder(GUIUtilities.getDefaultBorderColour()));
        init();
    }

    public void addPreferenceChangeListener(PreferenceChangeListener listener) {

        listeners.add(listener);
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent e) {
    }

    @Override
    public void preferenceTableModelChange(PreferenceTableModelChangeEvent e) {

        for (PreferenceChangeListener listener : listeners) {

            listener.preferenceChange(new PreferenceChangeEvent(this, e.getKey(), e.getValue()));
        }

    }

    protected void addContent(JPanel panel) {

        add(panel, contentPanelConstraints);
        if (panel instanceof SimplePreferencesPanel) {

            ((SimplePreferencesPanel) panel).addPreferenceTableModelListener(this);
        }

    }

    private void init() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(new DefaultButton(new RestoreDefaultsCommand(this)));
        add(panel, new GridBagConstraints(
                1, 2, 1, 1, 0, 0,
                GridBagConstraints.SOUTHEAST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 5, 0), 0, 0));

    }

    protected String stringUserProperty(String key) {

        return SystemProperties.getProperty(Constants.USER_PROPERTIES_KEY, key);
    }

    protected String bundledString(String key) {
        return Bundles.get("preferences." + key);
    }

}


