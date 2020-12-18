package org.zl.ddlutils.platform.maxdb;

import org.zl.ddlutils.platform.sapdb.SapDbPlatform;

/**
 * The platform implementation for MaxDB. It is currently identical to the SapDB
 * implementation as there is no difference in the functionality we're using.
 * Note that DdlUtils is currently not able to distinguish them based on the
 * jdbc driver or subprotocol as they are identical.   
 * 
 * @version $Revision: 231306 $
 */
public class MaxDbPlatform extends SapDbPlatform
{
    /** Database name of this platform. */
    public static final String DATABASENAME = "MaxDB";

    /**
     * Creates a new platform instance.
     */
    public MaxDbPlatform()
    {
        super();
        setSqlBuilder(new MaxDbBuilder(this));
        setModelReader(new MaxDbModelReader(this));
    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return DATABASENAME;
    }
}
