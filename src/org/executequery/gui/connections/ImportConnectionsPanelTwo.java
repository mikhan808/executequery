/*
 * ImportConnectionsPanelTwo.java
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

package org.executequery.gui.connections;

import org.underworldlabs.swing.ActionPanel;
import org.underworldlabs.swing.ProgressBar;
import org.underworldlabs.swing.ProgressBarFactory;

import javax.swing.*;
import java.awt.*;

public class ImportConnectionsPanelTwo extends ActionPanel {

    private ProgressBar progressBar;

    private JTextArea textArea;

    public ImportConnectionsPanelTwo() {

        super(new GridBagLayout());
        try {

            init();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void init() throws Exception {

        textArea = new JTextArea();
        textArea.setMargin(new Insets(5, 5, 5, 5));
        textArea.setEditable(false);

        progressBar = ProgressBarFactory.create(true);
        ((JComponent) progressBar).setPreferredSize(new Dimension(1, 22));
        progressBar.fillWhenStopped();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridheight = 1;
        gbc.insets.top = 7;
        gbc.insets.bottom = 15;
        gbc.insets.right = 5;
        gbc.insets.left = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(new JLabel("Exporting selections..."), gbc);
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.insets.bottom = 20;
        gbc.fill = GridBagConstraints.BOTH;
        add((JComponent) progressBar, gbc);
        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets.top = 10;
        gbc.insets.right = 0;
        gbc.insets.left = 0;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(textArea), gbc);

        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }

    public void append(final String text) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textArea.append(text);
                textArea.append("\n");
            }
        });

    }

    public void stop() {

        progressBar.stop();
    }

    public void start() {

        progressBar.start();
        append("Importing ... ");
    }

}

