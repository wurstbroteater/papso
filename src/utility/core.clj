(ns utility.core)

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

(defn ddiv [n d] (float (/ n d)))                           ;; division in double wrapper
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

(defn equal?
  ([x y]
   "This method is used for equality checks with acceptable error eps.
   However in error case, the output is not meaningful. Therefore in error case replace equal? with = for testing."
   (equal? x y 0.001))
  ([x y eps]
   (< (Math/abs (- x y)) eps)))
