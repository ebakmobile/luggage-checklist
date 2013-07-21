/*	
	NOTICE for Luggage & Suitcase Checklist, an Android app:
    Copyright (C) 2012 EBAK Mobile

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package com.lugcheck.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.lugcheck.util.AppUtil;

/**
 * Contains static helper methods for database code.
 * 
 * @author ecbrodie
 */
public class DBUtil {

	/**
	 * Cannot be instantiated.
	 */
	private DBUtil() {
	}

	/**
	 * Gets the statements from the script in an sql file. The entire file is read, then it is
	 * tokenized, with the semicolon(;) character serving as a delimiter.
	 * 
	 * @param filename
	 *            the filename
	 * @return the statements from sql file
	 */
	public static List<String> getStatementsFromSqlFile(Context context, String filename) {
		BufferedReader reader = null;
		StringBuilder fileContents = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(AppUtil.getInputStreamFromAssets(
					context, filename)));
			String line;
			while ((line = reader.readLine()) != null) {
				fileContents.append(line).append(" ");
				// NO NEWLINES IN THE fileContents!!!
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		String[] statements = fileContents.toString().trim().split(";");
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < statements.length; i++) {
			String trimmedStatement = statements[i].trim();
			// If the last statement ended with a semicolon, then there is a chance that
			// the last element of statements may have no SQL statement
			if (trimmedStatement.length() > 0) {
				result.add(trimmedStatement);
			}
		}
		return result;
	}

}
