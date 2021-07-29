# Parallel Asynchronous Particle Swarm Optimization  [![Build Status](https://app.travis-ci.com/wurstbroteater/papso.svg?token=p8UbHeQhFdd4xZrZhbxs&branch=main)](https://app.travis-ci.com/wurstbroteater/papso)


The implementation of the pseudo code written in the paper of Koh et al. (see literature folder)

## Usage

```clojure
(def particle 
    {:velocity [0.1 0.1]
     :position [1.0 0.5]
     :best [1.0 0.0]
     :neighboursBest [0 0]}) ;; single particle

(def swarm (list particle)) ;; list of particle(s)
(def numberOfIterations 42)
(start numberOfIterations swarm) ;; returns list
;; or
(start numberOfIterations) ;; inits a swarm with 32 particles
```

## SEND COFFEE!
