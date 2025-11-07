/*
 * Copyright (C) 2025 Mainardi Davide <davide at mainardisoluzioni.com>.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mainardisoluzioni.frommdbtocsv;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
public class ConfigFileLoader {
    
    /**
     * Load the configuration file
     * @param configFileName the name of the file, must be in same directory of the .jar
     * @return the loaded properties file or an empty properties is some error occurred
     */
    public static Properties loadConfigFile(String configFileName) {
        Properties prop = new Properties();
        if (configFileName != null && !configFileName.isBlank()) {
            String fileName = configFileName;
            try (FileInputStream fis = new FileInputStream(fileName)) {
                prop.load(fis);
            } catch (FileNotFoundException ex) {
                System.err.println("FileNotFoundException");
            } catch (IOException ex) {
                System.err.println("IOException");
            }
        }
        return prop;
    }
}
