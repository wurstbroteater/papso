(load-file "./src/algorithm/gnuplot.clj")
(load-file "./src/algorithm/vector.clj")
(require '[clojure.string :as str])
;; load landscape data
(def landscape (map (fn [line] (map read-string (str/split line #"\t"))) (str/split (slurp "./resources/landscape.tsv") #"\n")))
(def size (count landscape))

(defn random
  [lower upper]
  (+(rand (- upper lower)) lower))

(defn createRandomParticle
  "creates a particle for pso"
  []
  (def position (take 3 (repeatedly #(rand size))))
  {:velocity (take 3 (repeatedly #(rand size)))
   :position position
   :best position
   :neighbourBest position})

(defn createRandomSwarm
  "creates a random swarm"
  [population_size]
  (take population_size (repeatedly #(agent (createRandomParticle)))))

(defn fitness
  "calculates fitness for a point"
  [position]
  (nth (nth landscape (int (last position) ) '(-100)) (int (first position)) -100 ))

(defn getNeighbours
  [particle swarm range]
  (filter (fn [element] (> range (distV (:position element) (:position particle)))) (map (fn [a] @a ) swarm)))

(defn getNeighbourBest
  [neighbours]
  (last(sort-by fitness (map :best neighbours))))

(defn updateParticle
  [swarm particle]
  (def velocity (addV
                 (mulV (repeatedly #(rand 2))(:velocity particle))
                 (mulV (repeatedly rand) (subV (:best particle) (:position particle)))
                 (mulV (repeatedly rand) (subV (:neighbourBest particle) (:position particle)))))
  (def position (addV (:position particle) (mulV (repeat 0.01) velocity)))
  (def best (last (sort-by fitness [(:best particle) position])))
  {:velocity velocity
   :position position
   :best best
   :neighbourBest (getNeighbourBest (getNeighbours particle swarm 50))})


(def swarm (createRandomSwarm 32))

(defn fly [swarm]
  (map (fn [a] (send a (partial updateParticle swarm))) swarm))

(defn ps [swarm count]
  (if (= 0 count)
    (list swarm)
    (cons swarm (ps (map (partial updateParticle swarm) swarm) (dec count)))))


(time(plotSwarms (ps swarm 500)))

