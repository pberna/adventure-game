/*
 *   Adventure Game, a digital gamebook written in java with Libgdx.
 *   Copyright (C) 2018 Pedro Berná
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   Email contact: lomodastudios@gmail.com
 */

package com.pberna.engine.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.table.DatabaseTable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class PersistenceHelper {

    private PersistenceHelper() { }

    public static String getPreparedStatementString(StatementBuilder statementBuilder) {
        try {
            return statementBuilder.prepareStatementString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T fillEntity(Result result, Class<T> type) {
        T entity = instantiateEntity(type);

        if(entity == null) {
            return null;
        }

        fillFields(result, entity, type);

        return entity;
    }

    private static <T> T instantiateEntity(Class<T> type) {
        T entity = null;

        try {
            entity = type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return entity;
    }

    private static <T> void fillFields(Result result, T entity, Class<T> type) {
        for (Field field: type.getDeclaredFields()) {
            String classFieldName = field.getName();

            for(Annotation annotation: field.getAnnotations())  {
                if(annotation instanceof DatabaseField) {
                    DatabaseField databaseFieldAnnotation = (DatabaseField)annotation;
                    String fieldNameInResult = databaseFieldAnnotation.columnName().equals("") ? classFieldName : databaseFieldAnnotation.columnName();
                    int fieldIndexInResult = result.getColumnIndex(fieldNameInResult);
                    if(fieldIndexInResult >= 0) {
                        assignValueToField(result, fieldIndexInResult, entity, field);
                    }
                    break;
                }
            }
        }
    }

    private static <T> void assignValueToField(Result result, int fieldIndexInResult, T entity, Field field) {
        Class<?> fieldType = field.getType();
        try {
            field.setAccessible(true);
            if (fieldType.getName().equals("int")) {
                field.set(entity, result.getInt(fieldIndexInResult));
            } else if (fieldType.getName().equals("float")) {
                field.set(entity, result.getFloat(fieldIndexInResult));
            } else if (fieldType.getName().equals("java.lang.String")) {
                field.set(entity, result.getString(fieldIndexInResult));
            } else if (fieldType.getName().equals("java.util.Date")) {
                field.set(entity, new Date(result.getLong(fieldIndexInResult)));
            } else if (fieldType.getName().equals("boolean")) {
                field.set(entity, result.getInt(fieldIndexInResult) > 0);
            } else {
                throw new UnsupportedOperationException("Cannot read type " + fieldType.toString() + " from a Result of Database");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

    public static <T> ArrayList<T> fillEntitiesList(Result result, Class<T> type) {
        ArrayList<T> entitiesList = new ArrayList<T>();
        while(!result.isEmpty() && result.moveToNext()) {
            entitiesList.add(PersistenceHelper.fillEntity(result, type));
        }
        return entitiesList;
    }

    public static <T> String getInsertStatement(T entity, Class<T> type) {
        ArrayList<String> columnNames = new ArrayList<String>();
        ArrayList<String> columnValues = new ArrayList<String>();
        String tableName = "";

        for(Annotation annotation : type.getAnnotations()) {
            if(annotation.annotationType() == DatabaseTable.class) {
                tableName = "'" + ((DatabaseTable) annotation).tableName() + "'";
                break;
            }
        }

        if(tableName.equals("")) {
            return "";
        }

        for (Field field: type.getDeclaredFields()) {
            String classFieldName = field.getName();

            for(Annotation annotation: field.getAnnotations())  {
                if(annotation instanceof DatabaseField) {
                    DatabaseField databaseFieldAnnotation = (DatabaseField)annotation;

                    if(!(databaseFieldAnnotation.generatedId())) {
                        String fieldNameInDb = databaseFieldAnnotation.columnName().equals("") ? classFieldName : databaseFieldAnnotation.columnName();
                        columnNames.add("'" + fieldNameInDb + "'");
                        try {
                            field.setAccessible(true);
                            if(field.get(entity) == null) {
                                continue;
                            }
                            if (field.getType() == String.class) {
                                columnValues.add("'" + field.get(entity).toString().replace("'", "''") + "'");
                            } else if (field.getType() == Date.class) {
                                columnValues.add(String.valueOf(((Date) field.get(entity)).getTime()));
                            } else if(field.getType() == boolean.class) {
                                columnValues.add(String.valueOf (((Boolean) field.get(entity)) ? 1 : 0 ));
                            } else {
                                columnValues.add(field.get(entity).toString());
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }

        StringBuilder sbFirstPart = new StringBuilder();
        StringBuilder sbSecondPart = new StringBuilder();
        for(int i = 0; i < columnNames.size() && i < columnValues.size(); i++) {
            if(i == 0) {
                sbFirstPart.append("INSERT INTO ").append(tableName).append(" (").append(columnNames.get(i));
                sbSecondPart.append(" VALUES (").append(columnValues.get(i));
            } else {
                sbFirstPart.append(", ").append(columnNames.get(i));
                sbSecondPart.append(", ").append(columnValues.get(i));
            }
        }
        sbFirstPart.append(")");
        sbSecondPart.append(")");

        if(columnNames.size() == 0 || columnValues.size() == 0) {
            return "";
        }

        return sbFirstPart.toString()  + "\n" + sbSecondPart.toString();
    }

}
