# Parallel Asynchronous Particle Swarm Optimization  [![Build Status](https://app.travis-ci.com/wurstbroteater/papso.svg?token=p8UbHeQhFdd4xZrZhbxs&branch=main)](https://app.travis-ci.com/wurstbroteater/papso)


The implementation of the algorithm written in the paper of Koh et al. (see literature folder or click [here](https://www.ncbi.nlm.nih.gov/pmc/articles/PMC1769316/)). This project was created using IntelliJ 2021.2 with Cursive Plugin 1.10.3

## Requirements
 - Clojure 1.10.3 or higher
 - Lein [https://leiningen.org/](https://leiningen.org/)
 - Optional: Java 1.8 or higher

## Usage
### Algorithm

#### Init example:
```clojure
(require '[algorithm.swarm :as psa])
(require '[testfunction.core :as atf])

(def dim 2)      ;; Dimension
(def gCount 10)  ;; 10 neighbourhoods, 1 for global best only
(def sSize 512)  ;; Number of particles
(def nIter 100)  ;; Max iterations
(def sRange 100) ;; Range of x, e.g. 100 = [-100,100]
(defn testFunction [x] (atf/h1 x)) ;; Function used to evaluate fitness of a position

(psa/setSwarmProperties dim gCount sSize sRange testFunction) ;; Set new swarm properties
(psa/resetSwarm) ;; Create swarm with new properties
...
```
Until this point you haven't started the algorithm. Start with one of the following methods:

Starts algorithm single threaded:
```clojure
...
(def outSwarm (psa/psSync nIter map))
```

Starts algorithm multi-threaded aka parallel:
```clojure
...
(def outSwarm (psa/psSync nIter pmap))
```

Starts algorithm multi-threaded with asynchronous updates (papso):
This time we retrieve a reference to the swarm which we can use to monitor progress of the individuals.
```clojure
...
(def viewSwarm (psa/ps))
```
Use `(psa/stopPs)` to stop papso.

You can use `(sort-by psa/fitness (map deref psa/groupBest))` to retrieve a list of swarm best positions


or examples can be found in `src/benchmark/core.clj`

## Troubleshooting
In case the visulalisation does not show, you can try to add `(Thread/sleep 24)` as first command in the `updateParticle` method in `src/algorithm/swarm.clj
`
## SEND COFFEE!
