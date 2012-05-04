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

import java.util.TimerTask;

import com.google.appengine.api.taskqueue.DeferredTask;

/**
 * This class represents wrapper that converts {@link TimerTask} to
 * {@link DeferredTask} which can be added to App Engine
 * {@link com.google.appengine.api.taskqueue.Queue}.
 * 
 * The {@link TimerTask} supplied must be {@link java.io.Serializable}.
 * 
 * @author <a href="mailto:vladimir.orany@appsatori.eu">Vladimir Orany</a>
 * 
 */
@SuppressWarnings("serial")
final class DeferredTimerTask implements DeferredTask {
    private final TimerTask task;

    /**
     * Creates new wrapper of given task.
     * @param task task to be wrapped. Must be {@link java.io.Serializable}.
     */
    public DeferredTimerTask(TimerTask task) {
        this.task = task;
    }

    public void run() {
        task.run();
    }
}