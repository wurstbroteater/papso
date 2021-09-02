(ns algorithm.core_eric)
(require '[utility.core :as util])
(require '[clojure.string :as str])
(def landscape (map (fn [line] (map read-string (str/split line #"\t"))) (str/split (slurp "resources/landscape.tsv") #"\n")))

;;                              Particle Swarm methods
(defn createParticle
  ([] (createParticle 2))
  ([dimension]
   (def initValue (take dimension (repeatedly #(rand 150))))
   {:velocity       (take dimension (repeatedly #(rand 150))) ;; v vector
    :position       initValue                               ;; x vector
    :best           initValue                               ;; p vector
    :neighboursBest initValue}
   )
  )
(defn createPopulation [popSize]
  (concat (take popSize (repeatedly createParticle))))


(defn fitness
  "calculates fitness for a point"
  [position]
  (nth (nth landscape (int (last position)) '(-100)) (int (first position)) -100))

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
   (def cognitive (util/mulSV p1 (util/subV (particle :best) (particle :position))))
   (def social (util/mulSV p2 (util/subV (particle :neighboursBest) (particle :position))))
   (vec (map + (particle :velocity) cognitive social)))
  )

(defn updatePosition
  ([particle] (updatePosition particle 0.01))
  ([particle timeDelta]
   (vec (map + (particle :position) (util/mulSV timeDelta (particle :velocity)))))
  )

(defn updateParticle [particle]
  {:velocity       (updateVelocity particle)
   :position       (updatePosition particle)
   :best           (updateParticleBest particle)
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