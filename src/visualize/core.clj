(ns visualize.core)

(require '[quil.core :as q] '[quil.middleware :as m])
(require '[algorithm.swarm :as psa])
(require '[testfunction.core :as atf])
(require '[utility.core :as util])
(def hack (agent 0)) ;; allows delayed execution of ps
(def viewSize 768)
(def maxX (* 0.66 (.getWidth(.getScreenSize (java.awt.Toolkit/getDefaultToolkit))))) ;; 2560
(def viewsPerLine (Math/floor(/ maxX viewSize)))

(defn dimensionIndexToOffset [index]
  (util/mulV (repeat viewSize) [(mod index viewsPerLine)
                                (quot index viewsPerLine)]))

(defn transformPosition [xs] "transforms a position into multiple 2 dimensional vectors"
  (map-indexed (fn [i xs] (util/addV xs (dimensionIndexToOffset i)) )
               (map (fn [xs] (util/addV (repeat (/ viewSize 2)) (util/mulV xs (repeat (/ viewSize (* 2 psa/spawnRange))))))
                    (partition 2 xs))))
  

(defn delayedStart [a] "dummy parameter, delays the start of PAPSO"
  (Thread/sleep 2000)
  (psa/ps))

(defn setup [] ;; returns state
  (q/resize-sketch (* viewSize (Math/min viewsPerLine (double( quot psa/dimensions 2))))
                   (* viewSize (Math/ceil(/ ( quot psa/dimensions 2) viewsPerLine))))
  (q/frame-rate -1)
  (q/background 0)
  (q/stroke 255 255 255)
  (send hack delayedStart))

(defn draw-state []
  "
  draw function
  - executed for side-effects
  - draws frame onto the screen
  "
  (q/background 0)
  (def position (apply concat (map transformPosition (map :position (map deref psa/swarm)))))
  (def best (apply concat (map transformPosition (map :best (map deref psa/swarm)))))
  (def groupBest (apply concat (map transformPosition (map :position (if (= :partition psa/groupMode)
                                                                       (map (fn [a] (deref (last a))) psa/groupBest)
                                                                       (map deref psa/groupBest))))))
  (q/stroke 255 255 255)
  (q/stroke-weight 5) ;; set pointsize for groupbest to 5
  (doseq [[x y] groupBest]
    (q/point x y ))
  (q/stroke 0 255 0)
  (q/stroke-weight 3) ;; set pointsize for position and best
  (doseq [[x y] position]
    (q/point x y ))
  (q/stroke 255 0 0)
  (doseq [[x y] best]
    (q/point x y )))


(defn visualRun [& args]
  (q/sketch
    :title "PSO"
    :size [10 10] ;; set size in setup, prevents a window manager bug
    :setup setup
    :draw draw-state))
;; dim gCount sSize sRange fFun
;;(psa/setSwarmProperties 2 8 1024 600 (fn [a] (+(apply atf/h2 a))))
(psa/setSwarmProperties 8 4 256 1 (fn [a] (-( atf/h3 a))))
(psa/resetPs)

(visualRun)
