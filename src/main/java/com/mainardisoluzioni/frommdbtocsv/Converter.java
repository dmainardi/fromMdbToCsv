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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Mainardi Davide <davide at mainardisoluzioni.com>
 */
public class Converter {
    private final String MDB_HYSTORIC_DB_PATH_KEY = "mdb.hystoric.db.path";
    private final String MDB_WORK_DB_PATH_KEY = "mdb.work.db.path";
    private final String CSV_FILE_PATH_KEY = "csv.file.path";
    private final String UCANACCESS_PREFIX = "jdbc:ucanaccess://";
    private final String CSV_SEPARATOR = ";";
    
    public void convertFromAccessToCsvInLevaStyle(Properties configFile, LocalDateTime lowerLimitCreation) {
        if (isConfigFileComplete(configFile)) {
            String hystoricDbPath = configFile.getProperty(MDB_HYSTORIC_DB_PATH_KEY);
            String workDbPath = configFile.getProperty(MDB_WORK_DB_PATH_KEY);
            String csvFilePath = configFile.getProperty(CSV_FILE_PATH_KEY);
            if (
                    hystoricDbPath != null && !hystoricDbPath.isBlank()
                    &&
                    workDbPath != null && !workDbPath.isBlank()
                    &&
                    csvFilePath != null && !csvFilePath.isBlank()
                    ) {
                
                StringBuilder hystoricQueryStringBuilder = prepareHystoricQueryStringBuilder(lowerLimitCreation);
                String workQueryText = "SELECT * FROM Programmi;";
                
                Map<String, Recipe> recipes;
                
                Connection hystoricConnection = null;
                Connection workConnection = null;
                FileWriter csvFileWriter = null;
                BufferedWriter csvBufferedWriter = null;
                try {
                    try {
                        workConnection = DriverManager.getConnection(UCANACCESS_PREFIX + workDbPath);
                        PreparedStatement workPreparedStatement = workConnection.prepareStatement(workQueryText);
                        ResultSet workResultSet = workPreparedStatement.executeQuery();
                        recipes = createRecipesFromDatabase(workResultSet);
                        
                        hystoricConnection = DriverManager.getConnection(UCANACCESS_PREFIX + hystoricDbPath);
                        PreparedStatement hystoricPreparedStatement = hystoricConnection.prepareStatement(hystoricQueryStringBuilder.toString());
                        if (lowerLimitCreation != null)
                            hystoricPreparedStatement.setTimestamp(1, Timestamp.valueOf(lowerLimitCreation));
                        ResultSet ingressiResultSet = hystoricPreparedStatement.executeQuery();
                        
                        csvFileWriter = new FileWriter(csvFilePath);
                        csvBufferedWriter = new BufferedWriter(csvFileWriter);
                        
                        // Write header
                        csvBufferedWriter.write("Nome programma");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Timestamp");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Colpi");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Tipo");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Altezza");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Passo");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Avanzamento");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Avanzamento 2");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Delta 1");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Offset Z");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Correzione");
                        csvBufferedWriter.write(CSV_SEPARATOR);
                        csvBufferedWriter.write("Strati");
                        csvBufferedWriter.newLine();
                        
                        // Write content
                        while (ingressiResultSet.next()) {
                            String codiceRicetta = ingressiResultSet.getString("Nome programma");
                            Recipe recipe = recipes.get(codiceRicetta);
                            
                            csvBufferedWriter.write(codiceRicetta);
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(DateTimeFormatter.ISO_DATE_TIME.format(ingressiResultSet.getTimestamp("Ora Fine").toLocalDateTime()));
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(valueIntoCsv(ingressiResultSet.getObject("Colpi")));
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(recipe != null ? valueIntoCsv(recipe.getTipo()) : "");
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(recipe != null ? valueIntoCsv(recipe.getAltezza()) : "");
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(recipe != null ? valueIntoCsv(recipe.getPasso()) : "");
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(recipe != null ? valueIntoCsv(recipe.getAvanzamento()) : "");
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(recipe != null ? valueIntoCsv(recipe.getAvanzamento2()) : "");
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(recipe != null ? valueIntoCsv(recipe.getDelta1()) : "");
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(recipe != null ? valueIntoCsv(recipe.getOffsetZ()) : "");
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(recipe != null ? valueIntoCsv(recipe.getCorrezione()) : "");
                            csvBufferedWriter.write(CSV_SEPARATOR);
                            csvBufferedWriter.write(recipe != null ? valueIntoCsv(recipe.getStrati()) : "");
                            
                            csvBufferedWriter.newLine();
                        }
                    }   
                    catch (SQLTimeoutException e) {
                        System.err.println("Timeout error reading Microsoft Access database - " + e.getMessage());
                    } catch (SQLException e) {
                        System.err.println("Error in Microsoft Access database reading - " + e.getMessage());
                    } catch (IOException e) {
                        System.err.println("Timeout error writing the CSV file - " + e.getMessage());
                    } finally {
                        if (hystoricConnection != null)
                            hystoricConnection.close();
                        if (workConnection != null)
                            workConnection.close();
                        if (csvBufferedWriter != null)
                            csvBufferedWriter.close();
                        if (csvFileWriter != null)
                            csvFileWriter.close();
                    }
                } catch (SQLException e) {
                    System.err.println("Error during Microsoft Access database closing - " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("Error during CSV file closing - " + e.getMessage());
                }
            }
            else
                System.err.println("Config file with correct keys but with empty value - check the config file");
        }
        else
            System.err.println("Config file without correct keys - add 'mdb.hystoric.db.path', 'mdb.work.db.path' and 'csv.file.path' keys.");
    }
    
    private String valueIntoCsv(Object value) {
        String replacedSymbol = ",";
        if (CSV_SEPARATOR.equals(replacedSymbol))
            replacedSymbol = ";";
        return (value != null) ? value.toString().replace(CSV_SEPARATOR, replacedSymbol) : "";
    }
    
    private boolean isConfigFileComplete(Properties configFile) {
        return 
                configFile != null
                &&
                configFile.containsKey(MDB_HYSTORIC_DB_PATH_KEY)
                &&
                configFile.containsKey(MDB_WORK_DB_PATH_KEY)
                &&
                configFile.containsKey(CSV_FILE_PATH_KEY);
    }
    
    private StringBuilder prepareHystoricQueryStringBuilder(LocalDateTime lowerLimitCreation) {
        StringBuilder result = new StringBuilder("SELECT * FROM Ingressi ");
        List<StringBuilder> conditions = new ArrayList<>();
        conditions.add(new StringBuilder(" colpi IS NOT NULL "));
        conditions.add(new StringBuilder(" colpi > 0 "));
        if (lowerLimitCreation != null)
            conditions.add(new StringBuilder(" `Ora Inizio` > ? "));
        result = result.append(" WHERE ");
        for (int i = 0; i < conditions.size(); i++) {
            if (i > 0)
                result = result.append("AND ");
            result = result.append(conditions.get(i));
        }
        result.append(";");
        
        return result;
    }
    
    private Map<String, Recipe> createRecipesFromDatabase(ResultSet rs) throws SQLException {
        Map<String, Recipe> recipes = new HashMap<>();
        
        if (rs != null) {
            while (rs.next()) {
                String codice = rs.getString("Descrizione");
                recipes.put(
                        codice,
                        new Recipe(
                                codice,
                                rs.getInt("tipo"),
                                rs.getDouble("altezza"),
                                rs.getDouble("passo"),
                                rs.getDouble("avanzamento"),
                                rs.getDouble("avanzamento2"),
                                rs.getInt("strati"),
                                rs.getDouble("delta1"),
                                rs.getDouble("correzione"),
                                rs.getDouble("offsetZ")
                        )
                );
            }
        }
        
        return recipes;
    }
}
