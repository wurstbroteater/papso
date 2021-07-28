(ns algorithm.core)

(defn sqrt [n] (java.lang.Math/sqrt n))                     ;; sqrt wrapper
(defn abs [n] (java.lang.Math/abs n))                       ;; abs wrapper
(defn pow [b n] (java.lang.Math/pow b n))                   ;; pow wrapper
(defn subV [v1 v2] (vec (map - v1 v2)))                     ;; vector subtraction
(defn sumV [v] (apply + v))                                 ; sum over a single vector

(defn zip [listeA listeB]
  (if (not= (count listeA) (count listeB))
    (throw (Exception. "zip received vectors with different dimensions!"))
    (map vector listeA listeB))
  )                                                         ;; zip wrapper

(defn square [n] (* n n))                                   ;; square a number
(defn squareV [v] (vec (map square v)))                     ;; square a vector

(defn createParticle []
  {:velocity (take 2 (repeatedly rand))                     ;; v vector
   :position (take 2 (repeatedly rand))                     ;; x vector
   :best (take 2 (repeatedly rand))}                        ;; p vector
  )


(defn getParticlePos [particle] (get particle :position))

(defn euclidDis [v1 v2]
  (if (not= (count v1) (count v2))
    (throw (Exception. "euclidDis received vectors with different dimensions!"))
    (sqrt (sumV (squareV (subV v1 v2))))
  ))

(defn distance [particle point]
  (def dis (euclidDis (getParticlePos particle) point))
  (if (= nil dis)
    (throw (Exception. "Distance was nil"))
    (identity dis))
  )

(defn start
  ([iterations] (start iterations 3))
  ([iterations popSize]
   (do
     ;; fill swarm
     (def population (concat (take popSize (repeatedly createParticle))))
     ;;
     (loop [iteration 0]
       (println "doing " iteration)
       (println "more")

       (if (< iteration (- iterations 1))
         (recur (inc iteration))
         (identity population)
         )
       )
     ))
 )
(println (start 2))






(comment
(load-file "../gnuplot.clj")
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

(defn create_random_particle
  "creates a particle for pso"
  []
  {:velocity (take 2 (repeatedly rand))
   :position (take 2 (repeatedly rand))
   :best (take 2 (repeatedly rand))})

(defn create_random_swarm
  "creates a random swarm"
  [population_size]
  (take population_size (repeatedly create_random_particle)))

(defn fitness
  "calculates fitness for a point"
  [position]
  (def points [[0 0]])
  (-(apply min (map (partial vec_dist position) points))))

(def global_best '(-1 -1))
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
                  (vec_sub  (:best particle) (:position particle))
                  (vec_sub   global_best (:position particle))))
  (def position (vec_add (:position particle) (vec_mul (repeat 0.1) velocity)))
  (def best (last (sort-by fitness [(:best particle) position])))
  (update_global_best best)
  {:velocity velocity
   :position position
   :best best})


(def swarm (create_random_swarm 2))

(defn ps
  [swarm count]
  (if (= 0 count)
    '()
    (cons swarm (ps (map update_particle swarm) (dec count)))))
)

;;(ps swarm 1000)
