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

import java.io.Serializable;
import java.util.TimerTask;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * {@link GeneralTimer} which will use App Engine {@link Queue} and
 * {@link DeferredTimerTask} to schedule the given task.
 * 
 * This class is <code>enum</code> to implement singleton pattern safely.
 * 
 * @author <a href="mailto:vladimir.orany@appsatori.eu">Vladimir Orany</a>
 * 
 */
public enum AppEngineQueueTimer implements GeneralTimer {

    /**
     * The single instance of this {@link GeneralTimer}.
     */
    INSTANCE;

    @Override
    public void schedule(final TimerTask task, final long timeout) {
        if (!(task instanceof Serializable)) {
            throw new IllegalArgumentException("Only serializable tasks can be used by this timer!");
        }
        try {
            QueueFactory.getQueue("timer").add(TaskOptions.Builder.withCountdownMillis(timeout).payload(new DeferredTimerTask(task)));
        } catch (Exception e) {
            throw new IllegalStateException("Timer queue does not exist! Create one in queues.xml!", e);
        }
    }
}
