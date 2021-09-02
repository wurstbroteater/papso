(ns benchmark.core)
(require '[utility.core :as util])
(require '[testfunction.core :as atf])
(require '[algorithm.swarm :as psa])
(require '[clojure.pprint :as pp])

(defmacro sectime
  [expr]
  "get elapsed time in seconds"
  `(let [start# (. System (currentTimeMillis))
         ret# ~expr]
     (prn (str "Elapsed time: " (/ (double (- (. System (currentTimeMillis)) start#)) 1000.0) " secs"))
     ret#))

(println "particle swarm optimization with 1000 iterations and 1000 particles took: ")
(def outSwarm (sectime (psa/psSync 1000 1000 map)))
;;(pp/pprint outSwarm)
(print "Best position should be (0.00 +- 0.005, 0.00 +- 0.005). Swarm calculated ")
(pp/pprint (first (sort-by psa/fitness (map :best outSwarm))))
;;(println (apply + (map :iterations outSwarm)))
(println "--------------------------------------------------------------------------------------")

(println "parallel synchronous particle swarm optimization with 1000 iterations and 1000 particles took: ")
(def outSwarm (sectime (psa/psSync 1000 1000 pmap)))
;;(pp/pprint outSwarm)
(print "Best position should be (0.00 +- 0.005, 0.00 +- 0.005). Swarm calculated ")
(pp/pprint (first (sort-by psa/fitness (map :best outSwarm))))
(println "--------------------------------------------------------------------------------------")

(println "parallel asynchronous particle swarm optimization with 1000 iterations and 1000 particles took: ")
;; needs dummy int because felix is dummy
(psa/ps 1.0)
(Thread/sleep 16000)
(shutdown-agents)
(print "iterations: ")
(println (apply + (map :iterations (map deref psa/swarm))))
(print "Best position should be (0.00 +- 0.005, 0.00 +- 0.005). Swarm calculated ")
(pp/pprint (first (sort-by psa/fitness (map :best (map deref psa/swarm)))))
;;(println (apply + (map :iterations outSwarm)))
