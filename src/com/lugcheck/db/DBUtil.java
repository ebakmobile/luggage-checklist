package com.lugcheck.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains static helper methods for database code.
 * @author ecbrodie
 */
public class DBUtil {
	
	/**
	 * Cannot be instantiated.
	 */
	private DBUtil() {}
	
	/**
	 * Gets the statements from the script in an sql file.
	 * The entire file is read, then it is tokenized, with the semicolon(;)
	 * character serving as a delimiter.
	 *
	 * @param filename the filename
	 * @return the statements from sql file
	 */
	public static List<String> getStatementsFromSqlFile(String filename) {
		BufferedReader reader = null;
		StringBuilder fileContents = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				fileContents.append(line).append(" ");
				// NO NEWLINES IN THE fileContents!!!
			}
		} catch (Exception e) {
			throw new RuntimeException();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException();
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
