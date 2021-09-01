(defn square [x] (* x x))

(defn itemH3Z [xi s]
  (* (Math/floor (+ (Math/abs (/ (+ xi 0.000001) s)) 0.4999999999999)) (Math/signum (double xi)) s))

(defn itemH3 [xi zi t c di]
  (if (< (Math/abs (- xi zi)) t)
    (* (square (+ (* t (Math/signum (double zi))) zi)) c di))
  (* di (square xi)))

(defn itemH3D [i] (case (mod i 4)
                    1 1
                    2 1000
                    3 10
                    100))

(defn h3
  ([xs] (h3 xs 0.05 0.2 0.15))
  ([xs t s c]
  (apply + (map-indexed (fn [i x]
                 (itemH3 x (itemH3Z x s) t c (itemH3D i))) xs))))
