(ns algorithm.core_eric)
(load-file "src/algorithm/gnuplot.clj")
(require '[clojure.string :as str])
(def landscape (map (fn [line] (map read-string (str/split line #"\t"))) (str/split (slurp "resources/landscape.tsv") #"\n")))
;; TODO: delete or move dummies to test when done
(def dummy1
  {:velocity [0.1 0.1]
   :position [1.0 2.0]
   :best [1.5 1.0]
   :neighboursBest [0 0]})
(def dummy2
  {:velocity [0.1 0.1]
   :position [1.0 0.5]
   :best [1.0 0.0]
   :neighboursBest [0 0]})
(def dummies (list dummy1 dummy2))

;;                              Wrapper and utility methods
(defn ddiv [n d] (float (/ n d)))                           ;; division in double wrapper
(defn sqrt [n] (Math/sqrt n))                               ;; sqrt wrapper
(defn subV [v1 v2] (vec (map - v1 v2)))                     ;; vector subtraction
(defn sumV [v] (apply + v))                                 ;; sum over a single vector
(defn square [n] (* n n))                                   ;; square a number
(defn squareV [v] (vec (map square v)))                     ;; square a vector
(defn mulSV [scalar vector]
  (vec (map (partial * scalar) vector)))                    ;; scalar mul vector
(defn euclidDis [v1 v2]
  "Calculate the euclidean distance of two vectors with the same dimension or throws exception otherwise"
  (if (not= (count v1) (count v2))
    (throw (Exception. "euclidDis received vectors with different dimensions!"))
    (sqrt (sumV (squareV (subV v1 v2))))
    ))                                                      ;; euclidean distance of two vectors


;;                              Particle Swarm methods
(defn createParticle
  ([] (createParticle 2))
  ([dimension]
     (def initValue (take dimension (repeatedly #(rand 150))))
     {:velocity (take dimension (repeatedly #(rand 150)))          ;; v vector
      :position initValue                                   ;; x vector
      :best initValue                                       ;; p vector
      :neighboursBest initValue}
   )
)
(defn createPopulation [popSize]
  (concat (take popSize (repeatedly createParticle))))


(defn fitness
  "calculates fitness for a point"
  [position]
  (nth (nth landscape (int (last position) ) '(-100)) (int (first position)) -100 ))

(defn updateParticleBest [particle]
  (if (< (fitness (particle :position)) (fitness (particle :best)))
    (particle :position)
    (particle :best)
  ))

;;                              TODO: This method
(defn updateNeighboursBest [neighbours]
  (first (sort-by fitness (map :best neighbours))))

(defn updateVelocity
  ([particle] (updateVelocity particle (rand) (rand)))
  ([particle p1 p2]
   "Update velocity according to update rule with random values"
   (def cognitive (mulSV p1 (subV (particle :best) (particle :position))))
   (def social (mulSV p2 (subV (particle :neighboursBest) (particle :position))))
   (vec (map + (particle :velocity) cognitive social)))
  )

(defn updatePosition
  ([particle] (updatePosition particle 0.01))
  ([particle timeDelta]
   (vec (map + (particle :position) (mulSV timeDelta (particle :velocity)))))
  )

(defn updateParticle [particle]
  {:velocity (updateVelocity particle)
   :position (updatePosition particle)
   :best (updateParticleBest particle)
   :neighboursBest (particle :neighboursBest)})

(defn start
  ([iterations] (start iterations 32))
  ([iterations popSize] (start iterations popSize (createPopulation popSize)))
  ([iterations popSize population]
     ;; fill swarm
   (if (<= (dec iterations) 0)
      population
     (cons population (start (dec iterations) popSize (map updateParticle population))))))


(comment
     ;;
     (loop [iteration 0]
       ;;(println "Step: " iteration)
       ;;(println (first population))
       (def population (map #(update % :best (fn [a] (updateParticleBest %))) population))
       (def population (map #(update % :velocity (fn [a] (updateVelocity %))) population))
       (def population (map #(update % :position (fn [a] (updatePosition %))) population))


       (if (< iteration (- iterations 1))
         (recur (inc iteration))
         (identity population)
         )
       )
     )
;;                              init call
;;(time (println (plotSwarms (start 600))))

