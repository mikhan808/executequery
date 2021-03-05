/*
 * HeapStatusCommand.java
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

package org.executequery.actions.toolscommands;

import org.executequery.GUIUtilities;
import org.underworldlabs.swing.HeapMemoryDialog;
import org.underworldlabs.swing.actions.BaseCommand;

import java.awt.event.ActionEvent;

/**
 * Heap Memory Status execution
 *
 * @author Takis Diakoumis
 */
public class HeapStatusCommand implements BaseCommand {

    public void execute(ActionEvent e) {

        new HeapMemoryDialog(GUIUtilities.getParentFrame());
    }

}



















