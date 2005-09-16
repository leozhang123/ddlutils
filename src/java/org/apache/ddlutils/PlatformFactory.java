package org.apache.ddlutils;

/*
 * Copyright 1999-2002,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ddlutils.platform.AxionPlatform;
import org.apache.ddlutils.platform.CloudscapePlatform;
import org.apache.ddlutils.platform.Db2Platform;
import org.apache.ddlutils.platform.DerbyPlatform;
import org.apache.ddlutils.platform.FirebirdPlatform;
import org.apache.ddlutils.platform.HsqlDbPlatform;
import org.apache.ddlutils.platform.InterbasePlatform;
import org.apache.ddlutils.platform.MSSqlPlatform;
import org.apache.ddlutils.platform.MaxDbPlatform;
import org.apache.ddlutils.platform.MckoiPlatform;
import org.apache.ddlutils.platform.MySqlPlatform;
import org.apache.ddlutils.platform.Oracle8Platform;
import org.apache.ddlutils.platform.Oracle9Platform;
import org.apache.ddlutils.platform.PostgreSqlPlatform;
import org.apache.ddlutils.platform.SapDbPlatform;
import org.apache.ddlutils.platform.SybasePlatform;

/**
 * A factory of {@link org.apache.ddlutils.Platform} instances based on a case
 * insensitive database name. Note that this is a convenience class as the platforms
 * can also simply be created via their constructors.
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @author <a href="mailto:tomdz@apache.org">Thomas Dudziak</a>
 * @version $Revision: 209952 $
 */
public class PlatformFactory
{
    /** The database name -> platform map */
    private static Map _platforms = null;

    /**
     * Returns the platform map.
     * 
     * @return The platform list
     */
    private static synchronized Map getPlatforms()
    {
        if (_platforms == null)
        {
            // lazy initialization
            _platforms = new HashMap();
            registerPlatforms();
        }
        return _platforms;
    }
    
    /**
     * Creates a new platform for the given (case insensitive) database name
     * or returns null if the database is not recognized.
     * 
     * @param databaseName The name of the database (case is not important)
     * @return The platform or <code>null</code> if the database is not supported
     */
    public static synchronized Platform createNewPlatformInstance(String databaseName) throws DdlUtilsException
    {
        Class platformClass = (Class)getPlatforms().get(databaseName.toLowerCase());

        try
        {
            return platformClass != null ? (Platform)platformClass.newInstance() : null;
        }
        catch (Exception ex)
        {
            throw new DdlUtilsException("Could not create platform for database "+databaseName, ex);
        }
    }

    /**
     * Creates a new platform for the specified database. This is a shortcut method that uses
     * {@link PlatformUtils#determineDatabaseType(String, String)} to determine the parameter
     * for {@link #createNewPlatformInstance(String)}.
     * 
     * @param jdbcDriver        The jdbc driver
     * @param jdbcConnectionUrl The connection url
     * @return The platform or <code>null</code> if the database is not supported
     */
    public static synchronized Platform createNewPlatformInstance(String jdbcDriver, String jdbcConnectionUrl) throws DdlUtilsException
    {
        return createNewPlatformInstance(new PlatformUtils().determineDatabaseType(jdbcDriver, jdbcConnectionUrl));
    }

    /**
     * Creates a new platform for the specified database. This is a shortcut method that uses
     * {@link PlatformUtils#determineDatabaseType(DataSource)} to determine the parameter
     * for {@link #createNewPlatformInstance(String)}.
     * 
     * @param dataSource The data source for the database
     * @return The platform or <code>null</code> if the database is not supported
     */
    public static synchronized Platform createNewPlatformInstance(DataSource dataSource) throws DdlUtilsException
    {
        return createNewPlatformInstance(new PlatformUtils().determineDatabaseType(dataSource));
    }

    /**
     * Returns a list of all supported platforms.
     * 
     * @return The names of the currently registered platforms
     */
    public static synchronized String[] getSupportedPlatforms()
    {
        return (String[])getPlatforms().keySet().toArray(new String[0]);
    }

    /**
     * Determines whether the indicated platform is supported.
     * 
     * @param platformName The name of the platform
     * @return <code>true</code> if the platform is supported
     */
    public static boolean isPlatformSupported(String platformName)
    {
        return getPlatforms().containsKey(platformName.toLowerCase());
    }

    /**
     * Registers a new platform.
     * 
     * @param platformName  The platform name
     * @param platformClass The platform class which must implement the {@link Platform} interface
     */
    public static synchronized void registerPlatform(String platformName, Class platformClass)
    {
        addPlatform(getPlatforms(), platformName, platformClass);
    }

    /**
     * Registers the known platforms.
     */
    private static void registerPlatforms()
    {
        addPlatform(_platforms, AxionPlatform.DATABASENAME,      AxionPlatform.class);
        addPlatform(_platforms, CloudscapePlatform.DATABASENAME, CloudscapePlatform.class);
        addPlatform(_platforms, Db2Platform.DATABASENAME,        Db2Platform.class);
        addPlatform(_platforms, DerbyPlatform.DATABASENAME,      DerbyPlatform.class);
        addPlatform(_platforms, FirebirdPlatform.DATABASENAME,   FirebirdPlatform.class);
        addPlatform(_platforms, HsqlDbPlatform.DATABASENAME,     HsqlDbPlatform.class);
        addPlatform(_platforms, InterbasePlatform.DATABASENAME,  InterbasePlatform.class);
        addPlatform(_platforms, MaxDbPlatform.DATABASENAME,      MaxDbPlatform.class);
        addPlatform(_platforms, MckoiPlatform.DATABASENAME,      MckoiPlatform.class);
        addPlatform(_platforms, MSSqlPlatform.DATABASENAME,      MSSqlPlatform.class);
        addPlatform(_platforms, MySqlPlatform.DATABASENAME,      MySqlPlatform.class);
        addPlatform(_platforms, Oracle8Platform.DATABASENAME,    Oracle8Platform.class);
        addPlatform(_platforms, Oracle9Platform.DATABASENAME,    Oracle9Platform.class);
        addPlatform(_platforms, PostgreSqlPlatform.DATABASENAME, PostgreSqlPlatform.class);
        addPlatform(_platforms, SapDbPlatform.DATABASENAME,      SapDbPlatform.class);
        addPlatform(_platforms, SybasePlatform.DATABASENAME,     SybasePlatform.class);
    }

    /**
     * Registers a new platform.
     * 
     * @param platformMap   The map to add the platform info to 
     * @param platformName  The platform name
     * @param platformClass The platform class which must implement the {@link Platform} interface
     */
    private static synchronized void addPlatform(Map platformMap, String platformName, Class platformClass)
    {
        if (!Platform.class.isAssignableFrom(platformClass))
        {
            throw new IllegalArgumentException("Cannot register class "+platformClass.getName()+" because it does not implement the "+Platform.class.getName()+" interface");
        }
        platformMap.put(platformName.toLowerCase(), platformClass);        
    }
}