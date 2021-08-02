(load-file "./src/algorithm/gnuplot.clj")
(require '[clojure.string :as str])
;; load landscape data
(def landscape (map (fn [line] (map read-string (str/split line #"\t"))) (str/split (slurp "./resources/landscape.tsv") #"\n")))
(def size (count landscape))
(defn sqrt [n] (java.lang.Math/sqrt n)) ;; sqrt wrapper
(defn abs [n] (java.lang.Math/abs n)) ;; abs wrapper
(defn pow [b n] (java.lang.Math/pow b n)) ;; pow wrapper

(def vec_sub ;; subtracts n vectors component wiseq
  (partial map -))

(def vec_add ;; adds n vectors component wise
  (partial map +))

(def vec_mul ;; multiplies n vectors component wise
  (partial map *))

(def vec_div ;; divides n vectors component wise
  (partial map /))


(defn vec_dist
  "calculates the distance of 2 vectors"
  [vec_a vec_b]
  (sqrt (reduce
         (fn [a b] (+ a (* b b)))
         0
         (vec_sub vec_a vec_b))))

(defn normalize
  "normalizes the vector"
  [vec]
  (vec_div vec (repeat (reduce + vec))))

(defn random
  [lower upper]
  (+(rand (- upper lower)) lower))

(defn create_random_particle
  "creates a particle for pso"
  []
  (def position (take 2 (repeatedly #(rand size))))
  {:velocity (take 2 (repeatedly #(rand size)))
   :position position
   :best position
   :neighbour_best position})

(defn create_random_swarm
  "creates a random swarm"
  [population_size]
  (take population_size (repeatedly create_random_particle)))

(defn fitness
  "calculates fitness for a point"
  [position]
  (nth (nth landscape (int (last position) ) '(-100)) (int (first position)) -100 ))

(defn get_neighbours
  [particle swarm range]
  (filter (fn [element] (> range (vec_dist (:position element) (:position particle)))) swarm))

(defn get_neighbour_best
  [neighbours]
  (last(sort-by fitness (map :best neighbours))))

(defn update_particle
  "updates velocity and position of particle"
  [swarm particle]
  ;(println particle)
  (def velocity (vec_add
                 (vec_mul (repeat (+ (rand) 0.5)) (:velocity particle))
                 (vec_mul (repeatedly rand) (vec_sub (:best particle) (:position particle)))
                 (vec_mul (repeatedly rand) (vec_sub (:neighbour_best particle) (:position particle)))))
  (def position (vec_add (:position particle) (vec_mul (repeat 0.01) velocity)))
  (def best (last (sort-by fitness [(:best particle) position])))
  {:velocity velocity
   :position position
   :best best
   :neighbour_best (get_neighbour_best (get_neighbours particle swarm 50))})


(def swarm (create_random_swarm 64))

(defn ps
  [swarm count]
  (if (= 0 count)
    (list swarm)
    (cons swarm (ps (map (partial update_particle swarm) swarm) (dec count)))))


(plot_swarms (ps swarm 512))
