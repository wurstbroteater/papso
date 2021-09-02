(ns algorithm.swarm)
(require '[utility.core :as util])
(require '[testfunction.core :as atf])

(def dimensions 2)
(def groupCount 20)
(def running true)

;;(def landscape (map (fn [line] (map read-string (str/split line #"\t"))) (str/split (slurp "./resources/landscape.tsv") #"\n")))
(def size 300)


(defn fitness [position]                                    ;; calculates fitness for a point
  (- (atf/h3 position)))
;;  (nth (nth landscape (int (last position) ) '(-100)) (int (first position)) -100 ))


(defn randomPosition [] (repeatedly dimensions #(rand size)))

(defn createRandomParticle "creates a particle for pso" []
  (def position (randomPosition))
  {
   :groupId     (rand-int groupCount)
   :iterations  0
   :stubborness (rand)                                      ;; determines likelihood not to "listen" to his neighbour
   :velocity    (randomPosition)
   :position    (randomPosition)
   :best        (randomPosition)})

(defn createRandomSwarm [population_size]                   ;; create a random swarm
  (repeatedly population_size createRandomParticle))

(def groupBest (map agent (repeatedly groupCount randomPosition)))

(defn updateGroupBest [groupId position]
  (if (> (fitness position) (fitness (deref (nth groupBest groupId))))
    (do (send (nth groupBest groupId) (fn [a] position)) position)
    (deref (nth groupBest groupId))))

(defn updateParticle
  [particle]
  (Thread/sleep 24)                                         ;; gives priority to render thread - comment to get max performance
  (when running
    (send-off *agent* updateParticle))                      ;; "recursive" call
  (def velocity (util/addV
                  (util/mulV (repeat 1) (:velocity particle)) ;; repeat 1's allow "on-the-fly" modification
                  (util/mulV (repeat 1) (repeat (:stubborness particle)) (util/subV (:best particle) (:position particle)))
                  (util/mulV (repeat 1) (repeat (- 1 (:stubborness particle))) (util/subV (updateGroupBest (:groupId particle) (:position particle)) (:position particle)))))
  (def position (util/addV (:position particle) (util/mulV (repeat 0.01) velocity)))

  (def best (last (sort-by fitness [(:best particle) position])))
  {:groupId     (:groupId particle)
   :iterations  (inc (:iterations particle))
   :stubborness (:stubborness particle)
   :velocity    velocity
   :position    position
   :best        best})

(def swarm (map agent (createRandomSwarm 1024)))

(defn ps [foo]
  (Thread/sleep 2000)
  (doall (map (fn [a] (send a updateParticle)) swarm)))

(defn psSync [iter]
  (alter-var-root #'running (constantly false))
  (loop [swarmSync (createRandomSwarm 1024)]
    (if (not= (:iterations (first swarmSync)) iter)
      (do(println (:iterations (first swarmSync)))
         (recur (pmap updateParticle swarmSync)))
      swarmSync)))
