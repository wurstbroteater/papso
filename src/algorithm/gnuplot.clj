(require '[quil.core :as q] '[quil.middleware :as m])
(load-file "./src/algorithm/vector.clj")
(load-file "./src/algorithm/basic_swarm.clj")

(defn setup [] ;; returns state
  (q/resize-sketch 675 675) 
  (q/frame-rate -1)
  (q/background 0)
  (q/stroke 255 255 255)
  (createRandomSwarm 64))

(defn update-state [state]
  (fly state))

(defn draw-state [state]
  "
  draw function
  - executed for side-effects
  - draws frame onto the screen
  "
  (q/background 0)
  (doseq [[x y] (map :position state)]
    (q/point (* 1.5 (+ x 150)) (* 1.5(+ y 150)))))



(defn -main [& args]
  (q/sketch
   :title "PSO"
   :size [10 10] ;; set size in setup, prevents a window manager bug
   :setup #'setup
   :update #'update-state
   :draw #'draw-state
   :middleware [m/fun-mode]))

(-main)
