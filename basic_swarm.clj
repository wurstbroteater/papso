(load-file "gnuplot.clj")
(require '[clojure.string :as str])
;; load landscape data
(def landscape (map (fn [line] (map read-string (str/split line #"\t"))) (str/split (slurp "landscape.tsv") #"\n")))
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
  {:velocity (take 2 (repeatedly #(random (- size) size)))
   :position (take 2 (repeatedly #(random (- size) size)))
   :best (take 2 (repeatedly #(random (- size) size)))})

(defn create_random_swarm
  "creates a random swarm"
  [population_size]
  (take population_size (repeatedly create_random_particle)))

(defn fitness
  "calculates fitness for a point"
  [position]
  (nth (nth landscape (int (first position) ) '(0)) (int (last position)) -100 ))

(def global_best '(-10000 -10000))
(defn update_global_best
  "trys to update the global best position"
  [position]
  (alter-var-root #'global_best (constantly (last (sort-by fitness [position global_best])))))

(defn update_particle
  "updates velocity and position of particle"
  [particle]
  ;(println particle)
  (def velocity (vec_add
                 (:velocity particle)
                 (vec_mul (repeatedly rand) (vec_sub  (:best particle) (:position particle)))
                 (vec_mul (repeatedly rand) (vec_sub   global_best (:position particle)))))
  (def position (vec_add (:position particle) (vec_mul (repeat 0.001) velocity)))
  (def best (last (sort-by fitness [(:best particle) position])))
  (update_global_best best)
  {:velocity velocity
   :position position
   :best best})


(def swarm (create_random_swarm 32))

(defn ps
  [swarm count]
  (if (= 0 count)
    '()
    (cons swarm (ps (map update_particle swarm) (dec count)))))


(last(plot_swarms (ps swarm 1024)))
