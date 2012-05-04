GPars App Engine
================

[GPars](http://gpars.codehaus.org/) support for [Google App Engine](https://developers.google.com/appengine/).

# Introduction

This little handy library allows you to use most of the [GPars](http://gpars.codehaus.org/) concurrency functionality
on the Google App Engine. High-level concurrency concepts, such as actors, parallel Groovy collections, agents and dataflow,
which aim at making concurrent programming easy and intuitive, will now run seamlessly on top of the Google App Engine services

GPars App Engine integrates well with both Groovy and Java applications and can also be used as a [Gaelyk](http://gaelyk.appengine.com) plugin.


# Example

The following example illustrates a simple GPars actor use:

```groovy
 import groovyx.gpars.actor.DynamicDispatchActor

 /**
  * Demonstrates use of the DynamicDispatchActor class, which leverages Groovy dynamic method dispatch to invoke
  * the appropriate onMessage() method.
  */

 final class MyActor extends DynamicDispatchActor {

     def MyActor(final closure) { become(closure); }

     void onMessage(String message) {
         println 'Received string'
     }

     void onMessage(Integer message) {
         println 'Received integer'
     }

     void onMessage(Object message) {
         println 'Received object'
     }

     void onMessage(List message) {
         println 'Received list'
         stop()
     }
 }

 final def actor = new MyActor({
     when {BigDecimal num -> println 'Received BigDecimal'}
 }).start()

 actor 1
 actor ''
 actor 1.0
 actor([1, 2, 3, 4, 5])

 actor.join()
```


# Installation

Before you use any of the [GPars](http://gpars.codehaus.org/) features in your code inside [Google App Engine](https://developers.google.com/appengine/),
you need to call `AppEnginePool.install()`, which will configure [GPars](http://gpars.codehaus.org/) properly for GAE.
The initialization call is necessary in order to hook-in the GAE services underneath GPars and is only required once for a running application instance.
A good strategy is to put the initialization call somewhere into the application initializer.

# Parallel Collections

To enable [concurrent collection processing](http://gpars.codehaus.org/Parallelizer) 
use `AppEnginePool.withPool(Closure)` method instead of `GParsPool.withPool(Closure)`.
Then you can run any [GPars](http://gpars.codehaus.org/) code as usual. For example:

```groovy
groovyx.gpars.appengine.AppEnginePool.withPool {
    def selfPortraits = images.findAllParallel{it.contains me}.collectParallel {it.resize() }
}
```

# Parallel Groups and Actors

To create a new parallel group (`PGroup`) just call `AppEnginePool.getPGroup()` with the optional _poolSize_ argument.
You can than for example do some work with actors.

```groovy
def group = groovyx.gpars.appengine.AppEnginePool.getPGroup()
final actor = group.actor { loop { react { msg -> report 'Actor printing: ' + msg } } }
actor << 'Hi'
[1, 2, 3, 4, 5, 6, 7, 8, 9].each {actor << it}
```

Please bear in mind that GAE-specific restrictions may apply to the number of threads your application creates.

# Other concepts

Please refer to the [GPars documentation](http://gpars.codehaus.org) and [the user guide](http://gpars.org/SNAPSHOT/guide/index.html)
 for details on the individual concurrency concepts and examples of their use.

# Gaelyk Integration

GPars App Engine is also a [Gaelyk](http://gaelyk.appengine.com) 1.2+ compliant binary plugin. As soon as it is present on the classpath
of your Gaelyk application, it will configure [GPars](http://gpars.codehaus.org/) for GAE automatically and so there is no need to call
`AppEnginePool.install()` manually.

Additionally, the plugin adds the two following new methods to each [groovlet and template](http://gaelyk.appspot.com/tutorial/views-and-controllers)

## `withPool`

A shorthand for `AppEnginePool.withPool(Closure)`.

```groovy
withPool {
    def selfPortraits = images.findAllParallel{it.contains me}.collectParallel {it.resize() }
}
```

## `withPGroup`

A shorthand for `AppEnginePool.getPGroup()`. Runs the supplied closure with a `PGroup`.

```groovy
withPGroup { group ->
    final actor = group.actor { loop { react { msg -> report 'Actor printing: ' + msg } } }
    actor << 'Hi'
    [1, 2, 3, 4, 5, 6, 7, 8, 9].each {actor << it}
}
```


# Integration

## Maven

The GPars App Engine library can be obtained from maven using the following descriptor:
```xml
        <dependency>
            <groupId>org.codehaus.gpars</groupId>
            <artifactId>gpars-appengine</artifactId>
            <version>0.1-SNAPSHOT</version>
        </dependency>
```

To save you from looking up the GPars library itself, you can get it as:
```xml
        <dependency>
            <groupId>org.codehaus.gpars</groupId>
            <artifactId>gpars</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

They both sit in the Codehaus snapshot repositories, but GA releases will eventually get into maven central:
```xml
        <repository>
            <id>codehausSnapshots</id>
            <name>Codehaus Snapshots</name>
            <url>http://snapshots.repository.codehaus.org/</url>
            <layout>default</layout>
        </repository>
```

## Gradle

You can also use the snapshots repositories in your Gradle build. You only need to specify the repositories
and dependencies in particular sections.


```groovy
repositories {
    mavenRepo name: 'codehaus-snapshots', url: 'http://snapshots.repository.codehaus.org/'
    // if you want Gaelyk integration
    // mavenRepo name: 'sonatype-snapshots', url: 'https://oss.sonatype.org/content/repositories/snapshots'
}

dependencies {
    compile 'org.codehaus.gpars:gpars-appengine:0.1-SNAPSHOT', {
       // if you don't want Gaelyk integration
       exclude group: 'org.gaelyk', module: 'gaelyk'
    }
    
    // if you want Gaelyk integration
    // compile 'org.gaelyk:gaelyk:1.2-SNAPSHOT'
}

```

