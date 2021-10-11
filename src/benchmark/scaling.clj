(ns benchmark.scaling
  (:gen-class))
(require '[utility.core :as util])
(require '[testfunction.core :as atf])
(require '[algorithm.swarm :as psa])
(require '[clojure.pprint :as pp])


(defn -main [& args]
  (def dTime (Integer/parseInt (first args)))
  (first (psa/ps))
  (Thread/sleep dTime)
  (psa/stopPs)
  (println (str (apply + (map :iterations (map deref psa/swarm)))))
  (System/exit 0))
