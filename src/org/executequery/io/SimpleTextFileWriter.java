/*
 * SimpleTextFileWriter.java
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

package org.executequery.io;

import org.executequery.gui.text.LineSeparator;
import org.underworldlabs.util.SystemProperties;

import java.io.*;

public class SimpleTextFileWriter {

    public void write(String path, String text, LineSeparator lineSeparator) throws IOException {

        PrintWriter writer = null;

        try {
            String _text = text.replaceAll("\n", lineSeparator.value);
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), SystemProperties.getProperty("user", "system.file.encoding")))) {
                bw.write(_text);
                bw.flush();
            }
            /*writer = new PrintWriter(new FileWriter(path, false), true);

            String _text = text.replaceAll("\n", lineSeparator.value);
            writer.println(_text);*/

        } finally {

            if (writer != null) {

                writer.close();
                writer = null;
            }

        }

    }

}







