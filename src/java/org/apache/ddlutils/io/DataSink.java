package org.apache.ddlutils.io;

/*
 * Copyright 1999-2004 The Apache Software Foundation.
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

import org.apache.commons.beanutils.DynaBean;

/**
 * Marks classes that can receive dyna beans read by the {@link org.apache.ddlutils.io.DataReader}.
 * 
 * @author <a href="mailto:tomdz@apache.org">Thomas Dudziak</a>
 * @version $Revision:$
 */
public interface DataSink
{
    /**
     * Adds a dyna bean.
     * 
     * @param bean The dyna bean to add
     */
    public void addBean(DynaBean bean);
}
