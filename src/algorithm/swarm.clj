(ns algorithm.core)
(load "vector")
(load "fitness")
(defn randomPosition [] (take 2 (repeatedly #(rand size))))
(def groupCount 20)
(defn createRandomParticle
  "creates a particle for pso"
  []
  (def position (randomPosition))
  {
   :groupId (rand-int groupCount)
   :iterations 0
   :stubborness (rand) ;; determines likelyhood not to "listen" to his neightbour
   :velocity (randomPosition)
   :position (randomPosition)
   :best (randomPosition)})

(defn createRandomSwarm [population_size] ;; create a random swarm
    (take population_size (repeatedly createRandomParticle)))

(def groupBest (map agent (repeatedly groupCount randomPosition)))

(defn updateGroupBest [groupId position]
  (if (> (fitness position) (fitness (deref (nth groupBest groupId))))
    (do (send (nth groupBest groupId) (fn [a] position)) position)
    (deref (nth groupBest groupId))))

(def running true)
(defn updateParticle
  [particle]
  (Thread/sleep 24) ;; gives priority to render thread - comment to get max performance
  (when running
    (send-off *agent* updateParticle));; "recursive" call
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


(def swarm (map agent (createRandomSwarm 1024)))

(defn ps [foo]
  (Thread/sleep 2000)
  (doall(map (fn [a] (send a updateParticle)) swarm )))
