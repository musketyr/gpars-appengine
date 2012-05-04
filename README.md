GPars App Engine
================

[GPars](http://gpars.codehaus.org/) support for [Google App Engine](https://developers.google.com/appengine/).

# Installation

Before you use any [GPars](http://gpars.codehaus.org/) feature on [Google App Engine](https://developers.google.com/appengine/) 
you need to call `AppEnginePool.install()` which will configure [GPars](http://gpars.codehaus.org/) properly.
The call is needed only once for instace so you should put it somewhere into application initializer.


# Parallel Collections

To enable [concurrent collection processing](http://gpars.codehaus.org/Parallelizer) 
use `AppEnginePool.withPool(Closure)` method instead of `GParsPool.withPool(Closure)`.
Then you can run any [GPars](http://gpars.codehaus.org/) code as usual. For example following one:

```groovy
AppEnginePool.withPool {
    def selfPortraits = images.findAllParallel{it.contains me}.collectParallel {it.resize() }

    //a map-reduce functional style
    def smallestSelfPortrait = images.parallel
        .filter{it.contains me}
        .map{it.resize()}
        .min{it.sizeInMB}
}
```

# Parallel Groups and Actors

To obtain parallel group (`PGroup`) just call `AppEnginePool.getPGroup()`. You can than for example do some work with
actors.

```groovy
def group = AppEnginePool.getPGroup()
final actor = group.actor { loop { react { msg -> report 'Actor printing: ' + msg } } }
actor << 'Hi'
[1, 2, 3, 4, 5, 6, 7, 8, 9].each {actor << it}
```

# Gaelyk Integration

GPars App Engine is [Gaelyk](http://gaelyk.appengine.com) 1.2+ compliant binary plugin. As soon as it is present on the classpath it configures 
[GPars](http://gpars.codehaus.org/) automatically so there is no need to call `AppEnginePool.install()` manually.

It adds two following new methods to each [groovlet and template](http://gaelyk.appspot.com/tutorial/views-and-controllers)

## `withPool`

Shortcut for `AppEnginePool.withPool(Closure)`. 

```groovy
withPool {
    def selfPortraits = images.findAllParallel{it.contains me}.collectParallel {it.resize() }
}
```

## `withPGroup`

Shortcut for `AppEnginePool.getPGroup()`. Runs closure with `PGroup`. 

```groovy
withPGroup { group ->
    final actor = group.actor { loop { react { msg -> report 'Actor printing: ' + msg } } }
    actor << 'Hi'
    [1, 2, 3, 4, 5, 6, 7, 8, 9].each {actor << it}
}
```


