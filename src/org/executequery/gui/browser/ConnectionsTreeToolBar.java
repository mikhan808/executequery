/*
 * ConnectionsTreeToolBar.java
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

package org.executequery.gui.browser;

import org.executequery.GUIUtilities;
import org.executequery.localization.Bundles;
import org.executequery.util.ThreadUtils;
import org.underworldlabs.swing.toolbar.PanelToolBar;

import javax.swing.*;

/**
 * @author Takis Diakoumis
 */
class ConnectionsTreeToolBar extends PanelToolBar {

    /**
     * move connection up button
     */
    private JButton upButton;

    /**
     * move connection down button
     */
    private JButton downButton;

    /**
     * the reload node button
     */
    private JButton reloadButton;

    /**
     * new connection button
     */
    @SuppressWarnings("unused")
    private JButton newConnectionButton;

    /**
     * delete connection button
     */
    private JButton deleteConnectionButton;

    private JButton connectButton;

    private ConnectionsTreePanel treePanel;

    private ImageIcon connectedIcon;

    private ImageIcon disconnectedIcon;

    public ConnectionsTreeToolBar(ConnectionsTreePanel treePanel) {

        this.treePanel = treePanel;

        init();
    }

    private void init() {


        connectButton = addButton(treePanel, "connectDisconnect",
                GUIUtilities.getAbsoluteIconPath("Connected.png"),
                Bundles.get("action.connect-to-database"));

        newConnectionButton = addButton(
                treePanel, "newConnection",
                GUIUtilities.getAbsoluteIconPath("NewConnection16.png"),
                Bundles.getCommon("newConnection.button"));

        addButton(
                treePanel, "newFolder",
                GUIUtilities.getAbsoluteIconPath("NewFolder16.png"),
                bundleString("newFolder"));

        deleteConnectionButton = addButton(
                treePanel, "deleteConnection",
                GUIUtilities.getAbsoluteIconPath("Delete16.png"),
                Bundles.getCommon("delete.button"));

        upButton = addButton(
                treePanel, "moveConnectionUp",
                GUIUtilities.getAbsoluteIconPath("Up16.png"),
                bundleString("moveConnectionUp"));

        downButton = addButton(
                treePanel, "moveConnectionDown",
                GUIUtilities.getAbsoluteIconPath("Down16.png"),
                bundleString("moveConnectionDown"));

        reloadButton = addButton(
                treePanel, "reloadSelection",
//                GUIUtilities.getAbsoluteIconPath("Reload16.png"), 
                GUIUtilities.getAbsoluteIconPath("Refresh16.png"),
                bundleString("reloadSelection"));

        addButton(
                treePanel, "sortConnections",
                GUIUtilities.getAbsoluteIconPath("SortAtoZ16.png"),
                bundleString("sortConnections"));

        addButton(treePanel.getTreeFindAction());

        addButton(
                treePanel, "collapseAll",
                GUIUtilities.getAbsoluteIconPath("Collapse16.png"),
                bundleString("collapseAll"));
        connectedIcon = GUIUtilities.loadIcon("Connected.png");
        disconnectedIcon = GUIUtilities.loadIcon("Disconnected.png");

    }

    protected void enableButtons(final boolean enableUpButton,
                                 final boolean enableDownButton,
                                 final boolean enableReloadButton,
                                 final boolean enableDeleteButton,
                                 final boolean enableConnected,
                                 final boolean databaseConnected) {

        ThreadUtils.invokeLater(new Runnable() {

            public void run() {

                upButton.setEnabled(enableUpButton);
                downButton.setEnabled(enableDownButton);
                reloadButton.setEnabled(enableReloadButton);
                deleteConnectionButton.setEnabled(enableDeleteButton);
                connectButton.setEnabled(enableConnected);
                if (enableConnected) {
                    if (databaseConnected) {
                        connectButton.setIcon(connectedIcon);
                    } else {
                        connectButton.setIcon(disconnectedIcon);
                    }
                }
            }

        });
    }

    private String bundleString(String key) {
        return Bundles.get(getClass(), key);
    }

}





