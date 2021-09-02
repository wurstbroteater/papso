(ns algorithm.swarm)
(require '[utility.core :as util])
(require '[testfunction.core :as atf])

;;global vars
(def dimensions 2)
(def groupCount 20)
(def running true)
(defn evalFunction [x] (atf/h3 x))                          ;; default analytical test function is h3

(defn resetGlobals []
  (alter-var-root #'running (constantly true))
  (alter-var-root #'groupBest (constantly (map agent (repeatedly groupCount randomPosition))))
  (alter-var-root #'swarm (constantly (map agent (createRandomSwarm 1024)))))

(comment
(defn changeEvalFunction [f] (case f
      "h1" (alter-var-root
             (var evalFunction)                             ;; var to alter
             (fn [f]                                        ;; fn to apply to the var's value
               (fn [x]                                      ;; returns a new fn wrapping old fn
                (atf/h1 x))))
      "h2" (alter-var-root
             (var evalFunction)
             (fn [f]
               (fn [x]
                 (atf/h2 x))))
      (alter-var-root                                       ;; default case is h3
        (var evalFunction)
        (fn [f]
          (fn [x]
            (atf/h3 x))))))
)

;;(def landscape (map (fn [line] (map read-string (str/split line #"\t"))) (str/split (slurp "./resources/landscape.tsv") #"\n")))
(def size 300)


(defn fitness [position]                                    ;; calculates fitness for a point
  (- (evalFunction position)))
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
  ;;(Thread/sleep 24)                                         ;; gives priority to render thread - comment to get max performance
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
  ;;(Thread/sleep 2000)
  (alter-var-root #'running (constantly true))
  (doall (map (fn [a] (send a updateParticle)) swarm)))

(defn stopPs []
  (alter-var-root #'running (constantly false)))

(defn psSync [iter swarmSize mapFun]
  "Synchronous psa with either pmap (parallel) or map (non-parallel) "
  (alter-var-root #'running (constantly false))
  (loop [swarmSync (createRandomSwarm swarmSize)]
    (if (not= (:iterations (first swarmSync)) iter)
      (recur (doall (mapFun updateParticle swarmSync)))
      swarmSync)))
