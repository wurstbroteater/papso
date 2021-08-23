(ns algorithm.core)
(require '[quil.core :as q])


(defn sqrt [n] (java.lang.Math/sqrt n)) ;; sqrt wrapper
(defn abs [n] (java.lang.Math/abs n)) ;; abs wrapper
(defn pow [b n] (java.lang.Math/pow b n)) ;; pow wrapper




(defn setup []
  "
  Setup function
  - only called once
  "
  (q/resize-sketch 640 420) 
  (q/background 0))

#_ (defn update []
     "
     Update function
     - Called before draw
     - Updates state for drawing
     "
     nil)

(defn draw []
  "
  draw function
  - executed for side-effects
  - draws frame onto the screen
  "  
  (q/stroke
    (rand-int 256)
    (rand-int 256)
    (rand-int 256))
  
  (let [width  (q/width)
        height (q/height)]

    (q/line
      (rand-int width)
      (rand-int height)
      (rand-int width)
      (rand-int height))))


(defn -main [& args]
  (q/sketch
    :title         "Quil Drawing Tutorial"
    :size          [10 10] ;; set size in setup, prevents a window manager bug
    :setup         setup
    :draw          draw))
