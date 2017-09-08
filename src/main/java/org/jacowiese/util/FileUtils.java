/**
 * File name : FileUtils.java
 * Created by: Jaco Wiese (CP325745)
 * Created on: 08 Sep 2017 at 1:14:36 PM
 */

package org.jacowiese.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Jaco Wiese (CP325745)
 */
public class FileUtils {

	public static String loadResource(String filename) throws FileNotFoundException, IOException {
		StringBuilder tempStr = new StringBuilder();
		
		FileReader reader = new FileReader(filename);
		
		char[] buf = new char[1024];
		while (reader.read(buf, 0, 1024) != -1) {
			tempStr.append(buf);
		}
		
		return tempStr.toString();
	}
	
}
