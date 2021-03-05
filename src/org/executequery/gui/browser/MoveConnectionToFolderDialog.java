/*
 * MoveConnectionToFolderDialog.java
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
import org.executequery.databasemediators.DatabaseConnection;
import org.executequery.gui.BaseDialog;
import org.executequery.gui.DefaultPanelButton;
import org.executequery.gui.browser.nodes.ConnectionsFolderNode;
import org.executequery.localization.Bundles;
import org.underworldlabs.swing.LinkButton;
import org.underworldlabs.swing.actions.ReflectiveAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MoveConnectionToFolderDialog extends BaseDialog {

    private JList list;
    private DefaultListModel listModel;
    private ConnectionsTreePanel treePanel;
    private DatabaseConnection databaseConnection;

    public MoveConnectionToFolderDialog(DatabaseConnection databaseConnection, ConnectionsTreePanel treePanel) {

        super("Move to folder...", true);
        this.databaseConnection = databaseConnection;
        this.treePanel = treePanel;
        init();

        pack();
        this.setLocation(GUIUtilities.getLocationForDialog(this.getSize()));
        setVisible(true);
    }

    private void init() {

        listModel = new DefaultListModel<ConnectionsFolderNode>();
        for (ConnectionsFolderNode folder : treePanel.getFolderNodes()) {

            listModel.addElement(folder);
        }

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    move(null);
                }

            }
        });

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() >= 2) {

                    move(null);
                }
            }
        });

        Action newFolderAction = new AbstractAction("New Folder") {
            public void actionPerformed(ActionEvent e) {
                newFolder(e);
            }
        };
        LinkButton newFolderButton = new LinkButton(newFolderAction);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridy++;
        gbc.gridx++;
        gbc.insets.top = 10;
        gbc.insets.left = 7;
        gbc.insets.right = 5;
        gbc.insets.bottom = 5;
        panel.add(new JLabel("Choose the destination folder"), gbc);
        gbc.gridx++;
        gbc.weightx = 1.0;
        gbc.insets.right = 10;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        panel.add(newFolderButton, gbc);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets.left = 5;
        gbc.insets.right = 5;
        gbc.insets.top = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(list), gbc);

        ReflectiveAction action = new ReflectiveAction(this);

        JPanel base = new JPanel(new BorderLayout());
        base.setPreferredSize(new Dimension(400, 350));

        base.add(panel, BorderLayout.CENTER);
        base.add(buttonPanel(action), BorderLayout.SOUTH);

        Container c = getContentPane();
        c.setLayout(new GridBagLayout());
        c.add(base, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 5), 0, 0));

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private JPanel buttonPanel(ReflectiveAction action) {

        JButton okButton = new DefaultPanelButton(action, Bundles.get("common.ok.button"), "move");
        JButton cancelButton = new DefaultPanelButton(action, Bundles.get("common.cancel.button"), "cancel");

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx++;
        gbc.weightx = 0.5;
        gbc.insets.top = 5;
        gbc.anchor = GridBagConstraints.EAST;
        buttonPanel.add(okButton, gbc);
        gbc.weightx = 0;
        gbc.gridx++;
        gbc.insets.left = 5;
        buttonPanel.add(cancelButton, gbc);

        return buttonPanel;
    }

    public void newFolder(ActionEvent e) {

        ConnectionsFolder newFolder = treePanel.newFolder();
        if (newFolder != null) {

            ConnectionsFolderNode newFolderNode = treePanel.getFolderNode(newFolder);
            listModel.addElement(newFolderNode);

            list.requestFocus();
            list.setSelectedValue(newFolderNode, true);
        }

    }

    public void move(ActionEvent e) {

        treePanel.moveToFolder(databaseConnection, (ConnectionsFolderNode) list.getSelectedValue());
        dispose();
    }

    public void cancel(ActionEvent e) {

        dispose();
    }

}


