
(defn sqrt [n] (java.lang.Math/sqrt n)) ;; sqrt wrapper
(defn abs [n] (java.lang.Math/abs n)) ;; abs wrapper
(defn pow [b n] (java.lang.Math/pow b n)) ;; pow wrapper


(defn setup []
  (q/frame-rate 1)                    ;; Set framerate to 1 FPS
  (q/background 200))                 ;; Set the background colour to
                                      ;; a nice shade of grey.
(defn draw []
  (q/stroke (q/random 255))             ;; Set the stroke colour to a random grey
  (q/stroke-weight (q/random 10))       ;; Set the stroke thickness randomly
  (q/fill (q/random 255))               ;; Set the fill colour to a random grey

  (let [diam (q/random 100)             ;; Set the diameter to a value between 0 and 100
        x    (q/random (q/width))       ;; Set the x coord randomly within the sketch
        y    (q/random (q/height))]     ;; Set the y coord randomly within the sketch
    (q/ellipse x y diam diam)))         ;; Draw a circle at x y with the correct diameter

(q/defsketch example                  ;; Define a new sketch named example
  :title "Oh so many grey circles"    ;; Set the title of the sketch
  ;;:settings #(q/smooth 2)             ;; Turn on anti-aliasing
  :setup setup                        ;; Specify the setup fn
  :draw draw                          ;; Specify the draw fn
  );;:size [1080 720])                    ;; You struggle to beat the golden ratio


(defn setup []
  (q/frame-rate 30))

(defn t []
  (* 0.001 (q/millis)))

(defn calc-y [x mid amp]
  (+ mid (* (q/sin (+ (t) x)) amp)))

(defn wave [step mid-y amp]
  (let [w (q/width)
        h (q/height)
        mult (q/map-range w
                          700 200
                          0.01 0.03)]
    (q/begin-shape)
    (q/vertex 0 h) ; lower left corner
    (doseq [x (range (- w) (+ step w) step)]
      (let [t (* x mult)
            y (calc-y t mid-y amp)]
        (q/vertex x y)))
    (q/vertex w h) ; lower right corner
    (q/end-shape)))

(defn draw []
  (q/background 250)
  (q/stroke 255 250)
  (q/fill 50 230 (+ (* 20 (q/sin (t))) 230) 40)
  (q/)
  (let [h (q/height)
        move-down (/ h 5)
        amp (/ h 8)]
    (doseq [y (range move-down (+ amp h) 8)]
      (let [x-step (- (* y 0.8) move-down)]
        (wave x-step y amp)))))

(q/defsketch waves
   :host "host"
   :size [500 500]
   :setup setup
   :draw draw)
