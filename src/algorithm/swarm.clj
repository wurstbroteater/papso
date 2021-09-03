(ns algorithm.swarm)
(require '[utility.core :as util])
(require '[testfunction.core :as atf])

;;global vars
(def dimensions 2)
(def groupCount 20)
(def running true)
(def swarmSize 1024)
(def spawnRange 600)

(defn fitness [position]                                    ;; calculates fitness for a point
  (- (atf/h3 position)))


(defn randomPosition [] (repeatedly dimensions #(-(rand (* spawnRange 2)) spawnRange)))

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
  (alter-var-root #'running (constantly true))
  (doall (map (fn [a] (send a updateParticle)) swarm)))

(defn stopPs []
  (alter-var-root #'running (constantly false)))

(defn resetPs [] "resets swarm"
  (alter-var-root #'running (constantly false))
  (Thread/sleep 1000)
  (alter-var-root #'running (constantly true))
  (alter-var-root #'groupBest (constantly (map agent (repeatedly groupCount randomPosition))))
  (alter-var-root #'swarm (constantly (map agent (createRandomSwarm swarmSize)))))

(defn setSwarmProperties [dim gCount sSize sRange fFun]
  "dim: swarm dimensions"
  "gCount: group count (1 for global best)"
  "sSize: swarm particle count"
  "sRange: swarm spawn range (uniform)"
  "fFun: fitness evaluate function"
  (alter-var-root #'dimensions (constantly dim))
  (alter-var-root #'groupCount (constantly gCount))
  (alter-var-root #'swarmSize (constantly sSize))
  (alter-var-root #'spawnRange (constantly sRange))
  (alter-var-root 
   (var fitness)                  ; var to alter
   (fn [f]                    ; fn to apply to the var's value
     (fn [n]                  ; returns a new fn wrapping old fn
       (fFun n)))))

(defn psSync [iter mapFun]
  "Synchronous psa with either pmap (parallel) or map (non-parallel) "
  (alter-var-root #'running (constantly false))
  (loop [swarmSync (createRandomSwarm swarmSize)]
    (if (not= (:iterations (first swarmSync)) iter)
      (recur (doall (mapFun updateParticle swarmSync)))
      swarmSync)))
