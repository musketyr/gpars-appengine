package groovyx.gpars.appengine.gaelyk

import groovyx.gaelyk.plugins.PluginBaseScript
import groovyx.gpars.appengine.AppEnginePool

class GParsPlugin extends PluginBaseScript {

    @Override
    public Object run() {

        AppEnginePool.install()

        binding {
            withPool = AppEnginePool.&withPool
            withPGroup = { int size = AppEnginePool.DEFAULT_POOL_SIZE, Closure cl -> cl(AppEnginePool.getPGroup(size))}
        }
    }
}
