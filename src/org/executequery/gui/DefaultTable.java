/*
 * DefaultTable.java
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

package org.executequery.gui;

import org.underworldlabs.swing.plaf.UIUtils;
import org.underworldlabs.swing.table.DefaultTableHeaderRenderer;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.Enumeration;

/**
 * Default table display using a custom header renderer and fixed min sizes.
 *
 * @author Takis Diakoumis
 */
public class DefaultTable extends JTable implements StandardTable {

    private static final int DEFAULT_ROW_HEIGHT = 24;

    public DefaultTable() {

        this(null);
    }

    public DefaultTable(TableModel model) {

        super(model);
        init();
    }

    public DefaultTable(Object[][] rowData, Object[] columnNames) {

        super(rowData, columnNames);
        init();
    }

    private void init() {

        setRowHeight(Math.max(getRowHeight(), DEFAULT_ROW_HEIGHT));

        if (UIUtils.isDefaultLookAndFeel() || UIUtils.usingOcean()) {

            getTableHeader().setDefaultRenderer(new DefaultTableHeaderRenderer());

        }

    }

    public void setTableColumnWidth(int columnWidth) {

        TableColumn col = null;
        TableColumnModel tcm = getColumnModel();
        for (Enumeration<TableColumn> i = tcm.getColumns(); i.hasMoreElements(); ) {

            col = i.nextElement();
            col.setPreferredWidth(columnWidth);
        }

    }

}


