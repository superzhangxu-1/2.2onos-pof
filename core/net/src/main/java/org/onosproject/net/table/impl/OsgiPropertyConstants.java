/*
 *
 *  * Copyright 2018-present Open Networking Foundation
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 *
 */

package org.onosproject.net.table.impl;

public class OsgiPropertyConstants {

    private OsgiPropertyConstants() {
    }

    public static final String ALLOW_EXTRANEOUS_TABLES = "allowExtraneousTables";
    public static final boolean ALLOW_EXTRANEOUS_TABLES_DEFAULT = false;

    public static final String PURGE_ON_DISCONNECTION_TABLE = "purgeOnDisconnection";
    public static final boolean PURGE_ON_DISCONNECTION_TABLE_DEFAULT = false;

    public static final String POLL_FREQUENCY_TABLE = "fallbackFlowPollFrequency";
    public static final int POLL_FREQUENCY_TABLE_DEFAULT = 30;

}
