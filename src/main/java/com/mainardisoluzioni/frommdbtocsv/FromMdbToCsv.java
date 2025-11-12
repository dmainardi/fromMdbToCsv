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

import java.time.LocalDateTime;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
public class FromMdbToCsv {

    public static void main(String[] args) {
        final String configFilePath;
        if (args.length > 0)
            configFilePath = args[0];
        else
            configFilePath = "app.config";

        
        Converter converter = new Converter();
        converter.convertFromAccessToCsvInLevaStyle(ConfigFileLoader.loadConfigFile(configFilePath), LocalDateTime.now().minusMonths(3));
    }
}
