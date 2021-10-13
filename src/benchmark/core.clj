(ns benchmark.core)
(require '[utility.core :as util])
(require '[testfunction.core :as atf])
(require '[algorithm.swarm :as psa])
(require '[clojure.pprint :as pp])

(defn getBest []
  (last (sort-by psa/fitness (map deref psa/groupBest))))

;;---------------------------- Benchmark for analytical test function h1
(def dim 2)                                                 ;; Dimension
(def gCount 10)                                             ;; 10 neighbourhoods, 1 for global best only
(def sSize 512)                                             ;; Number of particles
(def nIter 100)                                             ;; Max iterations
(def sRange 100)                                            ;; Range of x, e.g. 100 = [-100,100]

(psa/setSwarmProperties dim gCount sSize sRange (fn [a] (atf/h1 (first a) (last a)))) ;;h1 only works with 2 dim,
;;(psa/setSwarmProperties dim gCount sSize sRange (fn [a] (- (atf/h3 a)))) ;;h1 only works with 2 dim
(psa/resetPs)
(println (str "test function h1 with particle swarm optimization with " nIter " iterations and " sSize " for " dim " dimensions particles took: "))
(def startTime (System/currentTimeMillis))
(def outSwarm (psa/psSync nIter map))
(def deltaTime (- (System/currentTimeMillis) startTime))
(println (str deltaTime " ms"))
;;(pp/pprint outSwarm)
(print "Global maxima position is at (8.6998 6.7665). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")

(println (str "test function h1 with parallel particle swarm optimization with " nIter " iterations and " sSize " for " dim " dimensions particles took: "))
(def startTime (System/currentTimeMillis))
(def outSwarm (psa/psSync nIter pmap))
(def deltaTime (- (System/currentTimeMillis) startTime))
(println (str deltaTime " ms"))
;;(pp/pprint outSwarm)
(print "Global maxima position is at (8.6998 6.7665). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")

(println (str "test function h1 with parallel asynchronous particle swarm optimization with " deltaTime " ms yielded"))
(first (psa/ps))
(Thread/sleep deltaTime)
(psa/stopPs)
(println (str (apply + (map :iterations (map deref psa/swarm)))) " particle updates")
(print "Global maxima position is at (8.6998 6.7665). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
;;(println (apply + (map :iterations outSwarm)))
(println "--------------------------------------------------------------------------------------")

;;---------------------------- Benchmark for analytical test function h2
(def dim 2)                                                 ;; Dimension
(def gCount 10)                                             ;; 10 neighbourhoods, 1 for global best only
(def sSize 512)                                             ;; Number of particles
(def nIter 100)                                             ;; Max iterations
(def sRange 100)                                            ;; Range of x, e.g. 100 = [-100,100]

(psa/setSwarmProperties dim gCount sSize sRange (fn [a] (atf/h2 (first a) (last a)))) ;;h2 only works with 2 dim,
(psa/resetPs)
(println (str "test function h2 with particle swarm optimization with " nIter " iterations and " sSize " for " dim " dimensions particles took: "))
(def startTime (System/currentTimeMillis))
(def outSwarm (psa/psSync nIter map))
(def deltaTime (- (System/currentTimeMillis) startTime))
(println (str deltaTime " ms"))
(print "Global maxima position is at (0 0). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")

(println (str "test function h2 with parallel particle swarm optimization with " nIter " iterations and " sSize " for " dim " dimensions particles took: "))
(def startTime (System/currentTimeMillis))
(def outSwarm (psa/psSync nIter pmap))
(def deltaTime (- (System/currentTimeMillis) startTime))
(println (str deltaTime " ms"))
(print "Global maxima position is at (0 0). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")

(println (str "test function h2 with parallel asynchronous particle swarm optimization with " deltaTime " ms yielded"))
(first (psa/ps))
(Thread/sleep deltaTime)
(psa/stopPs)
(println (str (apply + (map :iterations (map deref psa/swarm)))) " particle updates")
(print "Global maxima position is at (0 0). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")

;;---------------------------- Benchmark for analytical test function h3
(def dim 2)                                                 ;; Dimension
(def gCount 10)                                             ;; 10 neighbourhoods, 1 for global best only
(def sSize 512)                                             ;; Number of particles
(def nIter 100)                                             ;; Max iterations
(def sRange 100)                                            ;; Range of x, e.g. 100 = [-100,100]

(psa/setSwarmProperties dim gCount sSize sRange (fn [a] (- (atf/h3 a))))
(psa/resetPs)
(println (str "test function h3 with particle swarm optimization with " nIter " iterations and " sSize " for " dim " dimensions particles took: "))
(def startTime (System/currentTimeMillis))
(def outSwarm (psa/psSync nIter map))
(def deltaTime (- (System/currentTimeMillis) startTime))
(println (str deltaTime " ms"))
(print "Global minima position is at (0.00 +- 0.005, 0.00 +- 0.005). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")

(println (str "test function h3 with parallel particle swarm optimization with " nIter " iterations and " sSize " for " dim " dimensions particles took: "))
(def startTime (System/currentTimeMillis))
(def outSwarm (psa/psSync nIter pmap))
(def deltaTime (- (System/currentTimeMillis) startTime))
(println (str deltaTime " ms"))
(print "Global minima position is at (0.00 +- 0.005, 0.00 +- 0.005). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")

(println (str "test function h3 with parallel asynchronous particle swarm optimization with " deltaTime " ms yielded"))
(first (psa/ps))
(Thread/sleep deltaTime)
(psa/stopPs)
(println (str (apply + (map :iterations (map deref psa/swarm)))) " particle updates")
(print "Global minima position is at (0.00 +- 0.005, 0.00 +- 0.005). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")

;;---------------------------- Benchmark for analytical test function h4
(def dim 2)                                                 ;; Dimension
(def gCount 10)                                             ;; 10 neighbourhoods, 1 for global best only
(def sSize 512)                                             ;; Number of particles
(def nIter 100)                                             ;; Max iterations
(def sRange 100)                                            ;; Range of x, e.g. 100 = [-100,100]

(psa/setSwarmProperties dim gCount sSize sRange (fn [a] (- (atf/h4 a))))
(psa/resetPs)
(println (str "test function h4 with particle swarm optimization with " nIter " iterations and " sSize " for " dim " dimensions particles took: "))
(def startTime (System/currentTimeMillis))
(def outSwarm (psa/psSync nIter map))
(def deltaTime (- (System/currentTimeMillis) startTime))
(println (str deltaTime " ms"))
(print "Global minima position is at (0 0). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")

(println (str "test function h4 with parallel particle swarm optimization with " nIter " iterations and " sSize " for " dim " dimensions particles took: "))
(def startTime (System/currentTimeMillis))
(def outSwarm (psa/psSync nIter pmap))
(def deltaTime (- (System/currentTimeMillis) startTime))
(println (str deltaTime " ms"))
(print "Global maxima position is at (0 0). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")

(println (str "test function h4 with parallel asynchronous particle swarm optimization with " deltaTime " ms yielded"))
(first (psa/ps))
(Thread/sleep deltaTime)
(psa/stopPs)
(println (str (apply + (map :iterations (map deref psa/swarm)))) " particle updates")
(print "Global minima position is at (0 0). Swarm calculated ")
(pp/pprint (getBest))
(psa/resetPs)
(println "--------------------------------------------------------------------------------------")
