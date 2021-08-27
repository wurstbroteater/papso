
(load-file "src/algorithm/vector.clj")
(require '[clojure.string :as str])
;; load landscape data
(def landscape (map (fn [line] (map read-string (str/split line #"\t"))) (str/split (slurp "./resources/landscape.tsv") #"\n")))
(def size (count landscape))

(defn random [lower upper]
  (+(rand (- upper lower)) lower))

(defn createRandomParticle
  "creates a particle for pso"
  []
  (def position (take 2 (repeatedly #(rand size))))
  {
   :stubborness (rand 3) ;; determines likelyhood to "listen" to his neightbour
   :velocity (take 2 (repeatedly #(rand size)))
   :position position
   :best position
   :neighbourBest position})

(defn createRandomSwarm [population_size] ;; create a random swarm
    (take population_size (repeatedly createRandomParticle)))

(defn fitness [position] ;; calculates fitness for a point
  (nth (nth landscape (int (last position) ) '(-100)) (int (first position)) -100 ))

(def globalBest (agent [0 0]))
(defn updateGlobalBest [position]
  (if (> (fitness position) (fitness (deref globalBest)))
    (do (send globalBest (fn [a] position))
        position)
    (deref globalBest)))
  

(defn updateParticle
  [particle]
  (when running
    (send-off *agent* updateParticle))
  (def velocity (addV
                 (mulV (repeat 1)(:velocity particle))
                 (mulV (repeatedly rand) (subV (:best particle) (:position particle)));;(repeat (:stubborness particle))
                 (mulV (repeatedly rand) (subV (updateGlobalBest(:position particle)) (:position particle))))) ;; (repeat (- 3 (:stubborness particle)))
  (def position (addV (:position particle) (mulV (repeat 0.01) velocity)))
  
  (def best (last (sort-by fitness [(:best particle) position])))
  {:stubborness (:stubborness particle)
   :velocity velocity
   :position position
   :best best})


(def swarm (map agent (createRandomSwarm 512)))
(def running true)
;;(defn fly [swarm]
;;  (map (fn [a] (send a (partial updateParticle swarm))) swarm))

(defn ps [foo]
  (Thread/sleep 2000)
  (doall(map (fn [a] (send a updateParticle)) swarm )))
