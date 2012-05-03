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
