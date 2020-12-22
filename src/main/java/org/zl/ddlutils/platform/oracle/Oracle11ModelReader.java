package org.zl.ddlutils.platform.oracle;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import org.zl.ddlutils.Platform;
import org.zl.ddlutils.model.Column;
import org.zl.ddlutils.platform.DatabaseMetaDataWrapper;

public class Oracle11ModelReader extends Oracle10ModelReader {

    /**
     * Creates a new model reader for Oracle 12 databases.
     * 
     * @param platform The platform that this model reader belongs to
     */
    public Oracle11ModelReader(Platform platform)
    {
        super(platform);
    }

	/**
     * {@inheritDoc}
     */
    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map<String, Object> values) throws SQLException
    {
    	// For more Information see https://docs.oracle.com/cd/B19306_01/java.102/b14188/datamap.htm
		Column column = super.readColumn(metaData, values);

		if (column.getTypeCode() == Types.NUMERIC)
		{
			// We're back-mapping the NUMBER columns returned by Oracle
			// Note that the JDBC driver returns NUMERIC for these NUMBER columns
			switch (column.getSizeAsInt())
			{
			case 1:
				if (column.getScale() == 0)
				{
					column.setTypeCode(Types.BIT);
				}
				break;
			case 3:
				if (column.getScale() == 0)
				{
					column.setTypeCode(Types.TINYINT);
				}
				break;
			case 5:
				if (column.getScale() == 0)
				{
					column.setTypeCode(Types.SMALLINT);
				}
				break;				
			// no description found for BIGINT <-> NUMBER but a mapping of SQLServer BIGINT to NUMBER (19)
			case 19:
				if (column.getScale() == 0)
				{
					column.setTypeCode(Types.BIGINT);
				}
				break;
			case 38:
				if (column.getScale() == 0)
				{
					column.setTypeCode(Types.INTEGER);
				}
				break;
			case 0:
				// if 0 then cancel size define
				column.setSize(null);
			}
			
		}else if (column.getTypeCode() == Types.DECIMAL) {
			if(column.getSizeAsInt()==0) {
				column.setSize(null);
			}
		}
		return column;
	}

}
