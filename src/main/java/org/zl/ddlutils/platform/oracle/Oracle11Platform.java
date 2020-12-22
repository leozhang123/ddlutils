/*
 * Copyright 2020 the original author or authors.
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
package org.zl.ddlutils.platform.oracle;

/**
 * 
 * 
 * @author leo
 * @version 0.1
 */
public class Oracle11Platform extends Oracle9Platform {

	 /** Database name of this platform. */
    public static final String DATABASENAME = "Oracle";
    
    /** The standard Oracle jdbc driver. */
    public static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
    
    public static final String JDBC_DRIVER_NAME = "Oracle JDBC driver";
    /**
     * Creates a new platform instance.
     */
    public Oracle11Platform()
    {
        super();
        setSqlBuilder(new Oracle11Builder(this));
        setModelReader(new Oracle11ModelReader(this));
    }
    
	@Override
	public String getName() {
		return DATABASENAME;
	}
    
}
