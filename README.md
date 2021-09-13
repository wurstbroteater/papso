# Parallel Asynchronous Particle Swarm Optimization  [![Build Status](https://app.travis-ci.com/wurstbroteater/papso.svg?token=p8UbHeQhFdd4xZrZhbxs&branch=main)](https://app.travis-ci.com/wurstbroteater/papso)

![visualized swarm using 8 dimenions](https://github.com/wurstbroteater/papso/blob/main/doc/logo.png?raw=true)

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
(defn tF [x] (atf/h1 x)) ;; Function used to evaluate fitness of a position

(psa/setSwarmProperties dim gCount sSize sRange tF) ;; Set new swarm properties
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
```clojure
...
(def viewSwarm (psa/ps))
```
This time we retrieve a reference to the swarm which we can use to monitor progress of the individuals.
Use `(psa/stopPs)` to stop papso.

You can use `(sort-by psa/fitness (map deref psa/groupBest))` to retrieve a list of swarm best positions

More examples can be found in `src/benchmark/core.clj`

#### Visual Run example
Initialize the swarm as described above, but don't execute the PSO algorithm. Instead you call `visualRun` as follows:
```clojure
(require '[algorithm.swarm :as psa])
(require '[testfunction.core :as atf])
(require '[visualize.core :as v])
(psa/setSwarmProperties 16 10 512 600 (fn [a] (-(atf/h3 a)))) ;;dim gCount sSize nIter sRange tF
(psa/resetPs)
(v/visualRun)
```
Since the "render thread" is in competition with all those swarm particles you can uncomment `(Thread/sleep 24)` in the `updateParticle` method in `src/algorithm/swarm.clj`. This will cause the swarm to slow down and give a bit more priority to the "render thread" (tbh: this is more or less a hacky way than a good way)

**Legend:**
- Green dot: A single particle
- Red dot: Particle best position
- White dot: Neighbourhood best position

**Dimension reduction:**
Given all particles are of dimensionality d > 1, the plotting routine splits the dimensions into d/2 parts and plots every part in a 2-D plane.
For example, if the dimension is set to 8 the plotting routine will show 4 windows with 2 dimensions each.

## Known Problems
If the number of dimension is odd, the last dimension won't be visualized.

## SEND COFFEE!
