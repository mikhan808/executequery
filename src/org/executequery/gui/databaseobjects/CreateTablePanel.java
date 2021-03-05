/*
 * CreateTablePanel.java
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

package org.executequery.gui.databaseobjects;

import org.executequery.ActiveComponent;
import org.executequery.EventMediator;
import org.executequery.GUIUtilities;
import org.executequery.components.BottomButtonPanel;
import org.executequery.databasemediators.DatabaseConnection;
import org.executequery.databaseobjects.impl.DefaultDatabaseMetaTag;
import org.executequery.event.ApplicationEvent;
import org.executequery.event.DefaultKeywordEvent;
import org.executequery.event.KeywordEvent;
import org.executequery.event.KeywordListener;
import org.executequery.gui.ActionContainer;
import org.executequery.gui.ExecuteQueryDialog;
import org.executequery.gui.browser.ConnectionsTreePanel;
import org.executequery.gui.browser.nodes.DatabaseObjectNode;
import org.executequery.gui.table.CreateTableFunctionPanel;
import org.executequery.localization.Bundles;
import org.underworldlabs.jdbc.DataSourceException;

import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * <p>The Create Panel function
 *
 * @author Takis Diakoumis
 */
public class CreateTablePanel extends CreateTableFunctionPanel
        implements ActionListener,
        KeywordListener,
        ActiveComponent {

    /**
     * This objects title as an internal frame
     */
    public static final String TITLE = Bundles.get(CreateTablePanel.class, "title");

    /**
     * This objects icon as an internal frame
     */
    public static final String FRAME_ICON = "NewTable16.png";

    /**
     * the parent container
     */
    private ActionContainer parent;

    private ConnectionsTreePanel treePanel;
    private TreePath currentPath;

    /**
     * <p> Constructs a new instance.
     */
    public CreateTablePanel(ActionContainer parent, boolean temporary) {
        super(temporary);
        this.parent = parent;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setFocusComponent();
    }

    public CreateTablePanel(DatabaseConnection dc, ActionContainer parent, boolean temporary) {
        this(parent, temporary);
        connectionsCombo.setSelectedItem(dc);
    }

    /**
     * <p>Initializes the state of this instance.
     */
    private void jbInit() throws Exception {

        addButtonsPanel(new BottomButtonPanel(
                this, bundleString("Create"), "create-table", parent.isDialog()));
        setPreferredSize(new Dimension(750, 480));
        EventMediator.registerListener(this);
    }

    /**
     * Indicates that a [long-running] process has begun or ended
     * as specified. This may trigger the glass pane on or off
     * or set the cursor appropriately.
     *
     * @param inProcess - true | false
     */
    public void setInProcess(boolean inProcess) {
        if (parent != null) {

            if (inProcess) {

                parent.block();

            } else {

                parent.unblock();
            }
        }
    }

    /**
     * Notification of a new keyword added to the list.
     */
    public void keywordsAdded(KeywordEvent e) {
        sqlText.setSQLKeywords(true);
    }

    public boolean canHandleEvent(ApplicationEvent event) {
        return (event instanceof DefaultKeywordEvent);
    }

    /**
     * Notification of a keyword removed from the list.
     */
    public void keywordsRemoved(KeywordEvent e) {
        sqlText.setSQLKeywords(true);
    }

    public Vector<String> getHostedSchemasVector() {
        try {
            return metaData.getHostedSchemasVector();
        } catch (DataSourceException e) {
            GUIUtilities.displayExceptionErrorDialog(
                    "Error retrieving the catalog/schema list for the " +
                            "selected connection.\n\nThe system returned:\n" +
                            e.getExtendedMessage(), e);
            return new Vector<String>(0);
        }
    }

    public Vector<String> getSchemaTables(String schemaName) {
        try {
            return metaData.getSchemaTables(schemaName);
        } catch (DataSourceException e) {
            GUIUtilities.displayExceptionErrorDialog(
                    bundleString("error.retrieving", bundleString("table"), bundleString("selected-connection"), e.getExtendedMessage())
                    , e);
            return new Vector<String>(0);
        }
    }

    public Vector<String> getColumnNamesVector(String tableName, String schemaName) {
        try {
            return metaData.getColumnNamesVector(tableName, schemaName);
        } catch (DataSourceException e) {
            GUIUtilities.displayExceptionErrorDialog(
                    bundleString("error.retrieving", bundleString("column-names"), bundleString("selected-table"), e.getExtendedMessage())
                    , e);
            return new Vector<String>(0);
        }
    }

    /**
     * Releases database resources before closing.
     */
    public void cleanup() {
        EventMediator.deregisterListener(this);
        metaData.closeConnection();
    }

    /**
     * Action listener implementation.<br>
     * Executes the create table script.
     *
     * @param e event
     */
    public void actionPerformed(ActionEvent e) {

        DatabaseConnection dc = getSelectedConnection();
        if (dc == null) {
            GUIUtilities.displayErrorMessage(
                    bundleString("message.notConnected"));
            return;
        }
        tablePanel.stopEditing();
        consPanel.stopEditing();
        createTable();
    }

    public static String getTITLE() {
        return TITLE;
    }

    public String toString() {
        return TITLE;
    }

    private void createTable() {
        try {
            if (!checkFullType())
                return;
            String querys = getSQLText();
            ExecuteQueryDialog eqd = new ExecuteQueryDialog(bundleString("title"), querys, getSelectedConnection(), true, "^");
            eqd.display();
            boolean commit = eqd.getCommit();
            if (commit) {
                if (treePanel != null && currentPath != null) {
                    DatabaseObjectNode node = (DatabaseObjectNode) currentPath.getLastPathComponent();
                    if (node.getDatabaseObject() instanceof DefaultDatabaseMetaTag)
                        treePanel.reloadPath(currentPath);
                    else
                        treePanel.reloadPath(currentPath.getParentPath());
                }
                parent.finished();

            }

        } catch (Exception exc) {
            GUIUtilities.displayExceptionErrorDialog("Error:\n" + exc.getMessage(), exc);
        }

    }

    public ConnectionsTreePanel getTreePanel() {
        return treePanel;
    }

    public void setTreePanel(ConnectionsTreePanel treePanel) {
        this.treePanel = treePanel;
    }

    public TreePath getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(TreePath currentPath) {
        this.currentPath = currentPath;
    }
}

