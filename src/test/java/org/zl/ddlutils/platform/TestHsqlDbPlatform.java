package org.zl.ddlutils.platform;

import org.zl.ddlutils.TestPlatformBase;
import org.zl.ddlutils.io.DatabaseIO;
import org.zl.ddlutils.platform.hsqldb.HsqlDbPlatform;

/**
 * Tests the Hsqldb platform.
 * 
 * @version $Revision: 231110 $
 */
public class TestHsqlDbPlatform extends TestPlatformBase
{
    /**
     * {@inheritDoc}
     */
    protected String getDatabaseName()
    {
        return HsqlDbPlatform.DATABASENAME;
    }

    /**
     * Tests the column types.
     */
    public void testColumnTypes() throws Exception
    {
        assertEqualsIgnoringWhitespaces(
            "DROP TABLE \"coltype\" IF EXISTS;\n" + //
            "CREATE TABLE \"coltype\"\n" + //
            "(\n" + //
            "    \"COL_ARRAY\"           LONGVARBINARY,\n"+
            "    \"COL_BIGINT\"          BIGINT,\n"+
            "    \"COL_BINARY\"          BINARY("+Integer.MAX_VALUE+"),\n"+
            "    \"COL_BIT\"             BOOLEAN,\n"+
            "    \"COL_BLOB\"            LONGVARBINARY,\n"+
            "    \"COL_BOOLEAN\"         BOOLEAN,\n"+
            "    \"COL_CHAR\"            CHAR(15),\n"+
            "    \"COL_CLOB\"            LONGVARCHAR,\n"+
            "    \"COL_DATALINK\"        LONGVARBINARY,\n"+
            "    \"COL_DATE\"            DATE,\n"+
            "    \"COL_DECIMAL\"         DECIMAL(15,3),\n"+
            "    \"COL_DECIMAL_NOSCALE\" DECIMAL(15,0),\n"+
            "    \"COL_DISTINCT\"        LONGVARBINARY,\n"+
            "    \"COL_DOUBLE\"          DOUBLE,\n"+
            "    \"COL_FLOAT\"           DOUBLE,\n"+
            "    \"COL_INTEGER\"         INTEGER,\n"+
            "    \"COL_JAVA_OBJECT\"     OBJECT,\n"+
            "    \"COL_LONGVARBINARY\"   LONGVARBINARY,\n"+
            "    \"COL_LONGVARCHAR\"     LONGVARCHAR,\n"+
            "    \"COL_NULL\"            LONGVARBINARY,\n"+
            "    \"COL_NUMERIC\"         NUMERIC(15,0),\n"+
            "    \"COL_OTHER\"           OTHER,\n"+
            "    \"COL_REAL\"            REAL,\n"+
            "    \"COL_REF\"             LONGVARBINARY,\n"+
            "    \"COL_SMALLINT\"        SMALLINT,\n"+
            "    \"COL_STRUCT\"          LONGVARBINARY,\n"+
            "    \"COL_TIME\"            TIME,\n"+
            "    \"COL_TIMESTAMP\"       TIMESTAMP,\n"+
            "    \"COL_TINYINT\"         SMALLINT,\n"+
            "    \"COL_VARBINARY\"       VARBINARY(15),\n"+
            "    \"COL_VARCHAR\"         VARCHAR(15)\n"+
            ");\n",
            getColumnTestDatabaseCreationSql());
    }

    /**
     * Tests the column constraints.
     */
    public void testColumnConstraints() throws Exception
    {
        // adapted version for HsqlDb of the schema used for getColumnTestDatabaseCreationSql
        final String schema =
            "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
            "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='columnconstraintstest'>\n" +
            "  <table name='constraints'>\n" +
            "    <column name='COL_PK' type='VARCHAR' size='32' primaryKey='true'/>\n" +
            "    <column name='COL_PK_AUTO_INCR' type='INTEGER' primaryKey='true' autoIncrement='true'/>\n" +
            "    <column name='COL_NOT_NULL' type='BINARY' size='100' required='true'/>\n" +
            "    <column name='COL_NOT_NULL_DEFAULT' type='DOUBLE' required='true' default='-2.0'/>\n" +
            "    <column name='COL_DEFAULT' type='CHAR' size='4' default='test'/>\n" +
            "  </table>\n" +
            "</database>";

        assertEqualsIgnoringWhitespaces(
            "DROP TABLE \"constraints\" IF EXISTS;\n" +
            "CREATE TABLE \"constraints\"\n"+
            "(\n"+
            "    \"COL_PK\"               VARCHAR(32),\n"+
            "    \"COL_PK_AUTO_INCR\"     INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1),\n"+
            "    \"COL_NOT_NULL\"         BINARY(100) NOT NULL,\n"+
            "    \"COL_NOT_NULL_DEFAULT\" DOUBLE DEFAULT -2.0 NOT NULL,\n"+
            "    \"COL_DEFAULT\"          CHAR(4) DEFAULT 'test',\n"+
            "    PRIMARY KEY (\"COL_PK\", \"COL_PK_AUTO_INCR\")\n"+
            ");\n",
            getDatabaseCreationSql(schema));
    }

    /**
     * Tests the table constraints.
     */
    public void testTableConstraints() throws Exception
    {
        assertEqualsIgnoringWhitespaces(
            "ALTER TABLE \"table3\" DROP CONSTRAINT \"testfk\";\n"+
            "ALTER TABLE \"table2\" DROP CONSTRAINT \"table2_FK_COL_FK_1_COL_FK_2_table1\";\n"+
            "DROP TABLE \"table3\" IF EXISTS;\n"+
            "DROP TABLE \"table2\" IF EXISTS;\n"+
            "DROP TABLE \"table1\" IF EXISTS;\n"+
            "CREATE TABLE \"table1\"\n"+
            "(\n"+
            "    \"COL_PK_1\"    VARCHAR(32) NOT NULL,\n"+
            "    \"COL_PK_2\"    INTEGER,\n"+
            "    \"COL_INDEX_1\" BINARY(100) NOT NULL,\n"+
            "    \"COL_INDEX_2\" DOUBLE NOT NULL,\n"+
            "    \"COL_INDEX_3\" CHAR(4),\n"+
            "    PRIMARY KEY (\"COL_PK_1\", \"COL_PK_2\")\n"+
            ");\n"+
            "CREATE INDEX \"testindex1\" ON \"table1\" (\"COL_INDEX_2\");\n"+
            "CREATE UNIQUE INDEX \"testindex2\" ON \"table1\" (\"COL_INDEX_3\", \"COL_INDEX_1\");\n"+
            "CREATE TABLE \"table2\"\n"+
            "(\n"+
            "    \"COL_PK\"   INTEGER,\n"+
            "    \"COL_FK_1\" INTEGER,\n"+
            "    \"COL_FK_2\" VARCHAR(32) NOT NULL,\n"+
            "    PRIMARY KEY (\"COL_PK\")\n"+
            ");\n"+
            "CREATE TABLE \"table3\"\n"+
            "(\n"+
            "    \"COL_PK\" VARCHAR(16),\n"+
            "    \"COL_FK\" INTEGER NOT NULL,\n"+
            "    PRIMARY KEY (\"COL_PK\")\n"+
            ");\n"+
            "ALTER TABLE \"table2\" ADD CONSTRAINT \"table2_FK_COL_FK_1_COL_FK_2_table1\" FOREIGN KEY (\"COL_FK_1\", \"COL_FK_2\") REFERENCES \"table1\" (\"COL_PK_2\", \"COL_PK_1\");\n"+
            "ALTER TABLE \"table3\" ADD CONSTRAINT \"testfk\" FOREIGN KEY (\"COL_FK\") REFERENCES \"table2\" (\"COL_PK\");\n",
            getTableConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the proper escaping of character sequences where HsqlDb requires it.
     */
    public void testCharacterEscaping() throws Exception
    {
        assertEqualsIgnoringWhitespaces(
            "DROP TABLE \"escapedcharacters\" IF EXISTS;\n"+
            "CREATE TABLE \"escapedcharacters\"\n"+
            "(\n"+
            "    \"COL_PK\"   INTEGER,\n"+
            "    \"COL_TEXT\" VARCHAR(128) DEFAULT '\'\'',\n"+
            "    PRIMARY KEY (\"COL_PK\")\n"+
            ");\n",
            getCharEscapingTestDatabaseCreationSql());
    }
}