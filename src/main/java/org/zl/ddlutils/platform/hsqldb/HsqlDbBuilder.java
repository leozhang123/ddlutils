package org.zl.ddlutils.platform.hsqldb;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.IOException;

import org.zl.ddlutils.Platform;
import org.zl.ddlutils.alteration.ColumnDefinitionChange;
import org.zl.ddlutils.model.Column;
import org.zl.ddlutils.model.ModelException;
import org.zl.ddlutils.model.Table;
import org.zl.ddlutils.model.TypeMap;
import org.zl.ddlutils.platform.SqlBuilder;

/**
 * The SQL Builder for the HsqlDb database.
 * 
 * @version $Revision$
 */
public class HsqlDbBuilder extends SqlBuilder
{
    /**
     * Creates a new builder instance.
     * 
     * @param platform The plaftform this builder belongs to
     */
    public HsqlDbBuilder(Platform platform)
    {
        super(platform);
        addEscapedCharSequence("'", "''");
    }

    /**
     * {@inheritDoc}
     */
    public void dropTable(Table table) throws IOException
    { 
        print("DROP TABLE ");
        printIdentifier(getTableName(table));
        print(" IF EXISTS");
        printEndOfStatement();
    }

    /**
     * {@inheritDoc}
     */
    public String getSelectLastIdentityValues(Table table) 
    {
        return "CALL IDENTITY()";
    }

    /**
     * Writes the SQL to add/insert a column.
     * 
     * @param table      The table
     * @param newColumn  The new column
     * @param nextColumn The column before which the new column shall be added; <code>null</code>
     *                   if the new column is to be added instead of inserted
     * @throws IOException
     */
    public void insertColumn(Table table, Column newColumn, Column nextColumn) throws IOException
    {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("ADD COLUMN ");
        writeColumn(table, newColumn);
        if (nextColumn != null)
        {
            print(" BEFORE ");
            printIdentifier(getColumnName(nextColumn));
        }
        printEndOfStatement();
    }

    /**
     * Writes the SQL to drop a column.
     * 
     * @param table  The table
     * @param column The column to drop
     */
    public void dropColumn(Table table, Column column) throws IOException
    {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(table));
        printIndent();
        print("DROP COLUMN ");
        printIdentifier(getColumnName(column));
        printEndOfStatement();
    }

    /**
     * {@inheritDoc}
     */
    protected void writeColumn(Table table, Column column) throws IOException
    {
        //see comments in columnsDiffer about null/"" defaults
        printIdentifier(getColumnName(column));
        print(" ");
        print(getSqlType(column));
        if (column.isAutoIncrement())
        {
            if (!column.isPrimaryKey())
            {
                throw new ModelException("Column "+column.getName()+" in table "+table.getName()+" is auto-incrementing but not a primary key column, which is not supported by the platform");
            }
            print(" ");
            writeColumnAutoIncrementStmt(table, column);
        }
        else
        {
            writeColumnDefaultValueStmt(table, column);
        }
        if (column.isRequired())
        {
            print(" ");
            writeColumnNotNullableStmt();
        }
        else if (getPlatformInfo().isNullAsDefaultValueRequired() &&
                 getPlatformInfo().hasNullDefault(column.getTypeCode()))
        {
            print(" ");
            writeColumnNullableStmt();
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException
    {
        print("GENERATED BY DEFAULT AS IDENTITY(START WITH 1)");
    }

    /**
     * {@inheritDoc}
     */
    protected void writeCastExpression(Column sourceColumn, Column targetColumn) throws IOException
    {
        boolean sizeChanged = ColumnDefinitionChange.isSizeChanged(getPlatformInfo(), sourceColumn, targetColumn);
        boolean typeChanged = ColumnDefinitionChange.isTypeChanged(getPlatformInfo(), sourceColumn, targetColumn);

        if (sizeChanged || typeChanged)
        {
            boolean needSubstr = TypeMap.isTextType(targetColumn.getTypeCode()) && sizeChanged && (sourceColumn.getSizeAsInt() > targetColumn.getSizeAsInt());

            if (needSubstr)
            {
                print("SUBSTR(");
            }
            print("CAST(");
            printIdentifier(getColumnName(sourceColumn));
            print(" AS ");
            if (needSubstr)
            {
                print(getNativeType(targetColumn));
            }
            else
            {
                print(getSqlType(targetColumn));
            }
            print(")");
            if (needSubstr)
            {
                print(",1,");
                print(targetColumn.getSize());
                print(")");
            }
        }
        else
        {
            super.writeCastExpression(sourceColumn, targetColumn);
        }
    }
}
