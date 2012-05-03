package groovyx.gpars.appengine;

import java.util.concurrent.ThreadFactory;

import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.backends.BackendService;
import com.google.appengine.api.backends.BackendServiceFactory;

/**
 * Wrapper class to make {@link ThreadFactory} returned by
 * {@link BackendService} behave the conventions and return <code>null</code>
 * instead of throwing {@link IllegalStateException} if the maximum number of
 * threads is reached.
 * 
 * This class is <code>enum</code> to implement singleton pattern safely.
 * 
 * @author <a href="mailto:vladimir.orany@appsatori.eu">Vladimir Orany</a>
 * 
 */
public enum AppEngineThreadFactory implements ThreadFactory {
    /**
     * The single instance of this {@link ThreadFactory}.
     */
    INSTANCE;

    @Override
    public Thread newThread(Runnable r) {
        if (BackendServiceFactory.getBackendService().getCurrentBackend() == null) {
            try {
                return ThreadManager.currentRequestThreadFactory().newThread(r);
            } catch (IllegalStateException ies) {
                return null;
            }
        }
        return ThreadManager.backgroundThreadFactory().newThread(r);
    }

}