(ns algorithm.core)
(load "swarm")
(require '[quil.core :as q] '[quil.middleware :as m])
(def hack (agent 0)) ;; allows delayed execution of ps
(defn setup [] ;; returns state
  (q/resize-sketch 675 675) 
  (q/frame-rate -1)
  (q/background 0)
  (q/stroke 255 255 255)
  (send hack ps))


(defn draw-state []
  "
  draw function
  - executed for side-effects
  - draws frame onto the screen
  "
  (q/background 0)
  (q/stroke 0 255 0)
  (doseq [[x y] (map :position (map deref swarm))]
    (q/point (* 1.5 (+ x 150)) (* 1.5(+ y 150))))
  (q/stroke 255 0 0)
  (doseq [[x y] (map :best (map deref swarm))]
    (q/point (* 1.5 (+ x 150)) (* 1.5(+ y 150))))
  (q/stroke 255 255 255)
  (doseq [[x y] (map deref groupBest)]
    (q/point (* 1.5 (+ x 150)) (* 1.5(+ y 150)))))



(defn visualRun [& args]
  (q/sketch
   :title "PSO"
   :size [10 10] ;; set size in setup, prevents a window manager bug
   :setup setup
   :draw draw-state))

