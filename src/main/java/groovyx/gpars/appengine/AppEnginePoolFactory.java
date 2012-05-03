package groovyx.gpars.appengine;

import groovyx.gpars.scheduler.Pool;
import groovyx.gpars.util.PoolFactory;

/**
 * {@link PoolFactory} which will delegates its all methods to
 * {@link AppEnginePool} facade class.
 * 
 * This class is <code>enum</code> to implement singleton pattern safely.
 * 
 * @author <a href="mailto:vladimir.orany@appsatori.eu">Vladimir Orany</a>
 * 
 */
public enum AppEnginePoolFactory implements PoolFactory {

    /**
     * The single instance of this {@link PoolFactory}.
     */
    INSTANCE;

    @Override
    public Pool createPool() {
        return AppEnginePool.getPool();
    }

    @Override
    public Pool createPool(boolean demon) {
        return createPool();
    }

    @Override
    public Pool createPool(int size) {
        return AppEnginePool.getPool(size);
    }

    @Override
    public Pool createPool(boolean demon, int size) {
        return createPool(size);
    }
}