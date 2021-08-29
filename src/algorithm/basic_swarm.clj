
(load-file "src/algorithm/vector.clj")
(require '[clojure.string :as str])
;; load landscape data
(def landscape (map (fn [line] (map read-string (str/split line #"\t"))) (str/split (slurp "./resources/landscape.tsv") #"\n")))
(def size (count landscape))

(defn random [lower upper]
  (+(rand (- upper lower)) lower))


(def groupCount 24)
(defn createRandomParticle
  "creates a particle for pso"
  []
  (def position (take 2 (repeatedly #(rand size))))
  {
   :groupId (rand-int groupCount)
   :iterations 0
   :stubborness (rand) ;; determines likelyhood not to "listen" to his neightbour
   :velocity (take 2 (repeatedly #(rand size)))
   :position position
   :best position
   :neighbourBest position})

(defn createRandomSwarm [population_size] ;; create a random swarm
    (take population_size (repeatedly createRandomParticle)))

(defn fitness [position] ;; calculates fitness for a point
  (nth (nth landscape (int (last position) ) '(-100)) (int (first position)) -100 ))

(def groupBest (map agent (repeat groupCount [0 0])))

(defn updateGroupBest [groupId position]
  (if (> (fitness position) (fitness (deref (nth groupBest groupId))))
    (do (send (nth groupBest groupId) (fn [a] position)) position)
    (deref (nth groupBest groupId))))

(def running true)
(defn updateParticle
  [particle]
  (Thread/sleep 50) ;; gives priority to render thread - comment to get max performance
  (when running
    (send-off *agent* updateParticle))
  (def velocity (addV
                 (mulV (repeat 1) (:velocity particle)) ;; repeat 1's allow "on-the-fly" modification
                 (mulV (repeat 1) (repeat (:stubborness particle)) (subV (:best particle) (:position particle)))
                 (mulV (repeat 1) (repeat (- 1 (:stubborness particle))) (subV (updateGroupBest (:groupId particle) (:position particle)) (:position particle)))))
  (def position (addV (:position particle) (mulV (repeat 0.01) velocity)))
  
  (def best (last (sort-by fitness [(:best particle) position])))
  {:groupId (:groupId particle)
   :iterations (inc (:iterations particle))
   :stubborness (:stubborness particle)
   :velocity velocity
   :position position
   :best best})


(def swarm (map agent (createRandomSwarm 512)))

;;(defn fly [swarm]
;;  (map (fn [a] (send a (partial updateParticle swarm))) swarm))

(defn ps [foo]
  (Thread/sleep 2000)
  (doall(map (fn [a] (send a updateParticle)) swarm )))
