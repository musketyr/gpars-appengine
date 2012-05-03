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
public final class DeferredTimerTask implements DeferredTask {
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