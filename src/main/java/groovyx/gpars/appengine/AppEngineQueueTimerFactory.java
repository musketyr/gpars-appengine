// GPars - Groovy Parallel Systems
//
// Copyright © 2008-12  The original author or authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package groovyx.gpars.appengine;

import groovyx.gpars.util.GeneralTimer;
import groovyx.gpars.util.TimerFactory;

/**
 * Implementation of {@link TimerFactory} which will always return
 * {@link AppEngineQueueTimerFactory} singleton instance.
 * 
 * This class is <code>enum</code> to implement singleton pattern safely.
 * 
 * @author <a href="mailto:vladimir.orany@appsatori.eu">Vladimir Orany</a>
 * 
 */
public enum AppEngineQueueTimerFactory implements TimerFactory {

    /**
     * The single instance of this {@link TimerFactory}.
     */
    INSTANCE;

    @Override
    public GeneralTimer createTimer(String name, boolean daemon) {
        return AppEngineQueueTimer.INSTANCE;
    }

}