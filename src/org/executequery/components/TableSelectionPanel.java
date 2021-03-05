/*
 * TableSelectionPanel.java
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

package org.executequery.components;

import org.executequery.ApplicationException;
import org.executequery.databasemediators.DatabaseConnection;
import org.executequery.databaseobjects.*;
import org.executequery.databaseobjects.impl.DatabaseObjectFactoryImpl;
import org.executequery.datasource.ConnectionManager;
import org.executequery.gui.WidgetFactory;
import org.executequery.localization.Bundles;
import org.executequery.log.Log;
import org.underworldlabs.jdbc.DataSourceException;
import org.underworldlabs.swing.ActionPanel;
import org.underworldlabs.swing.DynamicComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Vector;

/**
 * Panel containing connection -> catalog/schema -> table
 * selection combo boxes.
 *
 * @author Takis Diakoumis
 */
public class TableSelectionPanel extends ActionPanel
        implements ItemListener {

    private JComboBox connectionsCombo;

    private JComboBox schemasCombo;

    private JComboBox tablesCombo;

    public TableSelectionPanel() {

        super(new GridBagLayout());
        init();
        connectionSelected();
    }

    private void init() {

        connectionsCombo = createConnectionsCombo();
        schemasCombo = createSchemasCombo();
        tablesCombo = createTablesCombo();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets.right = 5;
        gbc.gridy = 0;
        gbc.gridx = 0;
        add(createLabel("connection"), gbc);
        gbc.gridx = 1;
        gbc.insets.right = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(connectionsCombo, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets.top = 5;
        gbc.insets.right = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(createLabel("schema"), gbc);
        gbc.gridx = 1;
        gbc.insets.right = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(schemasCombo, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weighty = 1.0;
        gbc.insets.right = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(createLabel("table"), gbc);
        gbc.gridx = 1;
        gbc.insets.right = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(tablesCombo, gbc);

    }

    public DatabaseHost getSelectedHost() {

        return (DatabaseHost) connectionsCombo.getSelectedItem();
    }

    public DatabaseSource getSelectedSource() {

        return (DatabaseSource) schemasCombo.getSelectedItem();
    }

    public DatabaseTable getSelectedTable() {

        return (DatabaseTable) tablesCombo.getSelectedItem();
    }

    public void itemStateChanged(ItemEvent e) {

        Object source = e.getSource();

        if (source == connectionsCombo) {

            connectionSelected();

        } else if (source == schemasCombo) {

            schemaSelected();

        } else if (source == tablesCombo) {

            tableSelected();
        }

    }

    private void connectionSelected() {

        try {

            DatabaseHost host = getSelectedHost();

            if (host != null) {

                List<DatabaseSchema> schemas = host.getSchemas();

                if (schemas != null && schemas.size() > 0) {

                    populateModelForCombo(schemasCombo, schemas);

                } else {

                    List<DatabaseCatalog> catalogs = host.getCatalogs();

                    if (catalogs != null && catalogs.size() > 0) {

                        populateModelForCombo(schemasCombo, catalogs);

                    } else {

                        clearCombos();
                    }

                }

                schemaSelected();

            } else {

                clearCombos();
            }

        } catch (DataSourceException e) {

            handleDataSourceException(e);
        }
    }

    private void schemaSelected() {

        try {

            DatabaseSource schema = getSelectedSource();

            if (schema != null) {

                List<NamedObject> tables = schema.getTables();

                populateModelForCombo(tablesCombo, tables);

            } else {

                populateModelForCombo(tablesCombo, null);
            }

        } catch (DataSourceException e) {

            handleDataSourceException(e);
        }

    }

    private void tableSelected() {


    }

    private void populateModelForCombo(JComboBox comboBox, List<?> list) {

        DynamicComboBoxModel model = (DynamicComboBoxModel) comboBox.getModel();

        if (list != null && list.size() > 0) {

            model.setElements(list);
            comboBox.setEnabled(true);

        } else {

            model.removeAllElements();
            comboBox.setEnabled(false);
        }

    }

    private void clearCombos() {

        populateModelForCombo(schemasCombo, null);

        populateModelForCombo(tablesCombo, null);
    }

    private JLabel createLabel(String key) {

        return new JLabel(Bundles.get(getClass(), key));
    }

    private JComboBox createSchemasCombo() {

        return comboBoxForModel(new DynamicComboBoxModel());
    }

    private JComboBox createTablesCombo() {

        return comboBoxForModel(new DynamicComboBoxModel());
    }

    private JComboBox createConnectionsCombo() {

        DatabaseObjectFactory factory = databaseObjectFactory();

        Vector<DatabaseHost> hosts = new Vector<DatabaseHost>();

        for (DatabaseConnection connection : activeConnections()) {

            hosts.add(factory.createDatabaseHost(connection));
        }

        ComboBoxModel model = new DynamicComboBoxModel(hosts);

        JComboBox comboBox = comboBoxForModel(model);
        comboBox.setEnabled(true);

        return comboBox;
    }

    private JComboBox comboBoxForModel(ComboBoxModel model) {

        JComboBox comboBox = WidgetFactory.createComboBox(model);
        comboBox.addItemListener(this);
        comboBox.setEnabled(false);

        return comboBox;
    }

    private DatabaseObjectFactory databaseObjectFactory() {

        return new DatabaseObjectFactoryImpl();
    }

    private Vector<DatabaseConnection> activeConnections() {

        return ConnectionManager.getActiveConnections();
    }

    private void handleDataSourceException(DataSourceException e) {

        Log.error("Error during database object selection", e);
        throw new ApplicationException(e);
    }


}

