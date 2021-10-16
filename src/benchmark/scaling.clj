(ns benchmark.scaling
  (:gen-class))
(require '[utility.core :as util])
(require '[testfunction.core :as atf])
(require '[algorithm.swarm :as psa])
(require '[clojure.pprint :as pp])


(defn -main [& args]
  (def dTime (Integer/parseInt (first args)))
  (def dim (Integer/parseInt (second args)))
  (psa/setSwarmProperties dim 10 512 600 (fn [a] (-(atf/h3 a))))
  ;;(psa/setSwarmProperties dim 8 512 600 (fn [position] (- (atf/h3 position))))
  (psa/resetPs)
  (first (psa/ps))
  (Thread/sleep dTime)
  (psa/stopPs)
  (println (str (apply + (map :iterations (map deref psa/swarm)))))
  (System/exit 0))

;;(-main "10000" "8")
