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