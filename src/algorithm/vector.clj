
(defn sqrt [n] (java.lang.Math/sqrt n)) ;; sqrt wrapper
(defn abs [n] (java.lang.Math/abs n)) ;; abs wrapper
(defn pow [b n] (java.lang.Math/pow b n)) ;; pow wrapper



(def subV ;; subtracts n vectors component wiseq
  (partial map -))

(def addV ;; adds n vectors component wise
  (partial map +))

(def mulV ;; multiplies n vectors component wise
  (partial map *))

(def divV ;; divides n vectors component wise
  (partial map /))

(defn distV
  "calculates the distance of 2 vectors"
  [vec_a vec_b]
  (sqrt (reduce
         (fn [a b] (+ a (* b b)))
         0
         (subV vec_a vec_b))))

(defn normalize
  "normalizes the vector"
  [v]
  (divV v (repeat (reduce + v))))
