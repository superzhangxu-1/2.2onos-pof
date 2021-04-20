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

package org.onosproject.store.table.flow.impl;

public class OsgiPropertyConstants {

    private OsgiPropertyConstants() {
    }

    public static final String MESSAGE_HANDLER_THREAD_POOL_SIZE = "msgHandlerPoolSize";
    public static final int MESSAGE_HANDLER_THREAD_POOL_SIZE_DEFAULT = 8;

    public static final String BACKUP_ENABLED = "backupEnabled";
    public static final boolean DEFAULT_BACKUP_ENABLED = false;

    public static final String BACKUP_PERIOD_MILLIS = "backupPeriod";
    public static final int DEFAULT_BACKUP_PERIOD_MILLIS = 2000;

    public static final String PERSISTENCE_ENABLED = "persistenceEnabled";
    public static final boolean DEFAULT_PERSISTENCE_ENABLED = false;

}
