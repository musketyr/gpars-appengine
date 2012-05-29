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

import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

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
enum AppEngineThreadFactory implements ThreadFactory {
    
    
    /**
     * The single instance of this {@link ThreadFactory}.
     */
    INSTANCE;

    private static final Logger log = Logger.getLogger(AppEngineThreadFactory.class.getName());

    @Override
    public Thread newThread(Runnable r) {
        try {
            log.fine("New thread requested to run  " + r.getClass().getName());
            if (BackendServiceFactory.getBackendService().getCurrentBackend() == null) {
                    return ThreadManager.currentRequestThreadFactory().newThread(r);
            }
            return ThreadManager.backgroundThreadFactory().newThread(r);
        } catch (Throwable ies) {
            log.info("Got " + ies.getClass().getName() + ": " + ies.getMessage());
            return null;
        }
        
    }

}