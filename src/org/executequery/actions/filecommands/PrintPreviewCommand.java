/*
 * PrintPreviewCommand.java
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

package org.executequery.actions.filecommands;

import org.executequery.GUIUtilities;
import org.executequery.actions.othercommands.AbstractBaseCommand;
import org.executequery.gui.BaseDialog;
import org.executequery.gui.editor.PrintSelectDialog;
import org.executequery.gui.editor.QueryEditor;
import org.executequery.print.PrintFunction;
import org.executequery.print.PrintPreviewer;
import org.underworldlabs.swing.GUIUtils;
import org.underworldlabs.swing.util.SwingWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Takis Diakoumis
 */
public class PrintPreviewCommand extends AbstractBaseCommand {

    public void execute(ActionEvent e) {

        PrintFunction printFunction = null;

        try {

            printFunction = GUIUtilities.getPrintableInFocus();

            if (printFunction == null) {

                return;
            }

            // if the frame in focus is a Query Editor
            // display the print selection dialog (text or table)
            if (printFunction instanceof QueryEditor) {

                BaseDialog dialog =
                        new BaseDialog(PrintSelectDialog.PRINT_PREVIEW_TITLE, true, false);

                dialog.addDisplayComponent(createPanel(dialog, printFunction));
                dialog.display();

                return;
            }

            SwingWorker worker = new SwingWorker() {
                public Object construct() {

                    return showPreview();
                }

                public void finished() {

                    GUIUtils.scheduleGC();
                }
            };

            worker.start();
        } finally {

            printFunction = null;
        }

    }

    private JPanel createPanel(BaseDialog dialog, PrintFunction printFunction) {
        return new PrintSelectDialog(
                dialog, (QueryEditor) printFunction, PrintSelectDialog.PRINT_PREVIEW);
    }

    private String showPreview() {

        PrintFunction printFunction = null;

        try {
            printFunction = GUIUtilities.getPrintableInFocus();

            if (printFunction.getPrintable() != null) {

                new PrintPreviewer(
                        printFunction.getPrintable(), printFunction.getPrintJobName());
            }

            return bundledString("done");

        } finally {

            printFunction = null;
        }

    }

}


