package org.zl.ddlutils.alteration;

import org.zl.ddlutils.model.Database;

/**
 * Marker interface for changes to a database model element.
 * 
 * @version $Revision: $
 */
public interface ModelChange
{
    /**
     * Applies this change to the given database.
     * 
     * @param database      The database
     * @param caseSensitive Whether the case of names matters
     */
    public void apply(Database database, boolean caseSensitive);
}
