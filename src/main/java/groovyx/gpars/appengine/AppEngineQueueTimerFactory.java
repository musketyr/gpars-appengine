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