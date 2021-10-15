(ns algorithm.swarm)
(require '[utility.core :as util])
(require '[testfunction.core :as atf])
;;global vars
(def dimensions 2)
(def groupCount 4)
(def running true)
(def swarmSize 3)
(def spawnRange 600)
(def groupMode :standard)
;;(def groupMode :partition) ;; uncomment to partition space in groupSize^dimensions many regions; recommended that #regions << #particles (low dimensionality)

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


(defn createGroupBestPartition [] ;; creates a groupsize x .. x  groupsize grid over the fitness landscape
  (def groupRange (/ (* 2 spawnRange) groupCount))
  (assoc
   (reduce ;; create groups over all coordinate combinations
    (fn [a b]
      (assoc a b
             (do ;; create group data
               (def position (map
                              (fn [index] (-(+ (rand groupRange) (* index groupRange)) spawnRange))
                                    b))
               (atom {:position position :fitness (fitness position)}))))
          (hash-map)
          (util/cart (repeat dimensions (range groupCount)))) ;; all coordinate combinations
   0 ;; append group id "0" for individuals outside of the landscape
   (do
     (def position (randomPosition))
     (atom {:position position :fitness (fitness position)}))))

(defn createGroupBest [] ;; create random group best values
  (if (= groupMode :partition)
    (createGroupBestPartition)
    (do
      (def positions (repeatedly groupCount randomPosition))
      (map atom (map (fn [position] {:position position :fitness (fitness position)}) positions)))))

(def groupBest (createGroupBest))

(defn updateGroupBest [id position]
  (def groupId (if (= groupMode :partition)
                 (do
                   (def groupRange (/ (* 2 spawnRange) groupCount))
                   (map (fn [x] (int( quot (+ x spawnRange) groupRange))) position))
                 id))
  (def score (fitness position))
  (def gBest ((if (= groupMode :partition) get nth) groupBest groupId (get groupBest 0)))
  (def dgBest @gBest)
  (if (> score (:fitness dgBest))
    (do (reset! gBest {:position position :fitness score});;(fn [a] {:position position :fitness score}))
        position)
    (:position dgBest)))

(defn updateParticle
  [particle]
  (when running
    (send *agent* updateParticle))                      ;; "recursive" call
  (def velocity (util/addV
                  (util/mulV (repeat 0.995) (:velocity particle)) ;; repeat 1's allow easy "on-the-fly" modification
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

(defn ps []
  (alter-var-root #'running (constantly true))
  (doall (map (fn [a] (send a updateParticle)) swarm)))

(defn stopPs []
  (alter-var-root #'running (constantly false)))

(defn resetPs [] "resets swarm"
  (alter-var-root #'running (constantly false))
  (Thread/sleep 1000)
  (alter-var-root #'running (constantly true))
  (alter-var-root #'groupBest (constantly (createGroupBest)))
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
