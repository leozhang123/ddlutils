package org.zl.ddlutils.alteration;

import org.zl.ddlutils.model.Column;
import org.zl.ddlutils.model.Database;
import org.zl.ddlutils.model.Table;

/**
 * Represents the removal of the primary key from a table.
 * 
 * @version $Revision: $
 */
public class RemovePrimaryKeyChange extends TableChangeImplBase
{
    /**
     * Creates a new change object.
     * 
     * @param tableName The name of he table to remove the primary key from
     */
    public RemovePrimaryKeyChange(String tableName)
    {
        super(tableName);
    }

    /**
     * {@inheritDoc}
     */
    public void apply(Database model, boolean caseSensitive)
    {
        Table    table  = findChangedTable(model, caseSensitive);
        Column[] pkCols = table.getPrimaryKeyColumns();

        for (int idx = 0; idx < pkCols.length; idx++)
        {
            pkCols[idx].setPrimaryKey(false);
        }
    }
}
