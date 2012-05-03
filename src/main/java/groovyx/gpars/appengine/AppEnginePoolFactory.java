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