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

import com.google.appengine.api.ThreadManager;
import groovy.lang.Closure;
import groovyx.gpars.GParsExecutorsPool;
import groovyx.gpars.group.DefaultPGroup;
import groovyx.gpars.group.PGroup;
import groovyx.gpars.scheduler.DefaultPool;
import groovyx.gpars.scheduler.Pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * {@link AppEnginePool} helps you using GPars on App Engine.
 * 
 * <p>
 * If you want to use parallel collection use {@link #withPool(Closure)} method.
 * </p>
 * <p>
 * If you want to use actors use {@link #getPGroup()} method.
 * </p>
 * <p>
 * Note that there are differences between running threads on frontend and
 * backend instance. All threads on frontend instance must finish within the
 * deadline limit (currently 30 seconds). There is also limit of having max. 10
 * thread active per request. Contrary there is no limit on backend threads
 * count. Backend threads can also run after the request has finished.
 * </p>
 * 
 * 
 * @author <a href="mailto:vladimir.orany@appsatori.eu">Vladimir Orany</a>
 * @see ThreadManager
 * 
 */
public class AppEnginePool {

    private static final int DEFAULT_POOL_SIZE = 10;

    private AppEnginePool() {
    }

    /**
     * Creates new {@link ExecutorService} which can be used in
     * {@link GParsExecutorsPool#withExistingPool(ExecutorService, Closure)}
     * methods or just for executing {@link Runnable} classes.
     * 
     * @param poolSize
     *            the desired pool size. Note that 10 is maximum of available
     *            threads per request on frontend instance.
     * @return new instance of {@link ExecutorService} using App Engine specific
     *         logic
     */
    public static ThreadPoolExecutor getExecutor(int poolSize) {
        return new ThreadPoolExecutor(0, poolSize, 500, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(), AppEngineThreadFactory.INSTANCE);
    }

    /**
     * Creates new {@link ExecutorService} with default size which can be used
     * in {@link GParsExecutorsPool#withExistingPool(ExecutorService, Closure)}
     * methods or just for executing {@link Runnable} classes.
     * 
     * @return new instance of {@link ExecutorService} using App Engine specific
     *         logic
     */
    public static ThreadPoolExecutor getExecutor() {
        return getExecutor(DEFAULT_POOL_SIZE);
    }

    /**
     * Returns new {@link Pool} which can be for example used to create new
     * {@link DefaultPGroup}.
     * 
     * @param poolSize
     *            the desired pool size. Note that 10 is maximum of available
     *            threads per request on frontend instance.
     * @return new instance of {@link Pool} using App Engine specific logic
     */
    public static Pool getPool(int poolSize) {
        return new DefaultPool(getExecutor(poolSize));
    }

    /**
     * Returns new {@link Pool} with default size which can be for example used
     * to create new {@link DefaultPGroup}.
     * 
     * @return new instance of {@link Pool} using App Engine specific logic
     */
    public static Pool getPool() {
        return getPool(DEFAULT_POOL_SIZE);
    }

    /**
     * Creates a new instance of <i>ExecutorService</i>, binds it to the current
     * thread, enables the ExecutorService DSL and runs the supplied closure.
     * Within the supplied code block the <i>ExecutorService</i> is available as
     * the only parameter, objects have been enhanced with the
     * <i>eachParallel()</i>, <i>collectParallel()</i> and other methods from
     * the <i>GParsExecutorsPoolUtil</i> category class as well as closures can
     * be turned into asynchronous ones by calling the <i>async()</i> method on
     * them. E.g. <i>closure,async</i> returns a new closure, which, when run
     * will schedule the original closure for processing in the pool. Calling
     * <i>images.eachParallel{processImage(it}}</i> will call the potentially
     * long-lasting <i>processImage()</i> operation on each image in the
     * <i>images</i> collection in parallel.
     * 
     * <pre>
     *  def result = new ConcurrentSkipListSet()
     *  AppEnginePool.withPool(5) {ExecutorService service ->
     *      [1, 2, 3, 4, 5].eachParallel{Number number -> result.add(number * 10)}
     *      assertEquals(new HashSet([10, 20, 30, 40, 50]), result)
     * }
     * </pre>
     * 
     * @param numberOfThreads
     *            Number of threads in the newly created thread pool
     * @param cl
     *            The block of code to invoke with the DSL enabled
     */
    @SuppressWarnings("unchecked")
    public static <V> V withPool(int numberOfThreads, Closure<V> cl) {
        return (V) GParsExecutorsPool.withExistingPool(getExecutor(numberOfThreads), cl);
    }

    /**
     * Creates a new instance of <i>ExecutorService</i>, binds it to the current
     * thread, enables the ExecutorService DSL and runs the supplied closure.
     * Within the supplied code block the <i>ExecutorService</i> is available as
     * the only parameter, objects have been enhanced with the
     * <i>eachParallel()</i>, <i>collectParallel()</i> and other methods from
     * the <i>GParsExecutorsPoolUtil</i> category class as well as closures can
     * be turned into asynchronous ones by calling the <i>async()</i> method on
     * them. E.g. <i>closure,async</i> returns a new closure, which, when run
     * will schedule the original closure for processing in the pool. Calling
     * <i>images.eachParallel{processImage(it}}</i> will call the potentially
     * long-lasting <i>processImage()</i> operation on each image in the
     * <i>images</i> collection in parallel.
     * 
     * <pre>
     *  def result = new ConcurrentSkipListSet()
     *  AppEnginePool.withPool() {ExecutorService service ->
     *      [1, 2, 3, 4, 5].eachParallel{Number number -> result.add(number * 10)}
     *      assertEquals(new HashSet([10, 20, 30, 40, 50]), result)
     * }
     * </pre>
     * 
     * @param cl
     *            The block of code to invoke with the DSL enabled
     */
    @SuppressWarnings("unchecked")
    public static <V> V withPool(Closure<V> cl) {
        return (V) GParsExecutorsPool.withExistingPool(getExecutor(), cl);
    }

    /**
     * Creates new instance of {@link PGroup} to let you start working with
     * actors.
     * 
     * @param poolSize
     *            the desired pool size. Note that 10 is maximum of available
     *            threads per request on frontend instance.
     * @return the new instance of {@link PGroup} using App Engine specific
     *         logic
     * @see PGroup
     */
    public static PGroup getPGroup(int poolSize) {
        return new DefaultPGroup(getPool(poolSize));
    }

    /**
     * Creates new instance of {@link PGroup} to let you start working with
     * actors.
     * 
     * @return the new instance of {@link PGroup} using App Engine specific
     *         logic
     * @see PGroup
     */
    public static PGroup getPGroup() {
        return new DefaultPGroup(getPool());
    }

}
