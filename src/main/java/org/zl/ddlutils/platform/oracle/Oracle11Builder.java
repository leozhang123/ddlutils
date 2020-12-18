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

import org.zl.ddlutils.Platform;

/**
 * 
 * 
 * @author leo
 * @version 0.1
 */
public class Oracle11Builder extends Oracle10Builder {
	/**
     * Creates a new builder instance.
     * 
     * @param platform The plaftform this builder belongs to
     */
    public Oracle11Builder(Platform platform)
    {
        super(platform);
    }

}
