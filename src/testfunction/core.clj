(ns testfunction.core)
(require '[utility.core :as util])
(require '[clojure.string :as str])
;;----------------------------- Analytical Test Problem Functions
;; x_i in [-100, 100]
(defn h1 [x1 x2]
  (def numeratorH1
    (+ (util/square (Math/sin (- x1 (util/ddiv x2 8)))) (util/square (Math/sin (+ x2 (util/ddiv x1 8))))))
  (def denominatorH1
    (+ (util/sqrt (+ (util/square (- x1 8.6998)) (util/square (- x2 6.7665)))) 1))
  (util/ddiv numeratorH1 denominatorH1))

;; x_i in [-100, 100]
(defn h2 [x1 x2]
  (def numeratorH2
    (- (util/square (Math/sin (util/sqrt (+ (util/square x1) (util/square x2))))) 0.5))
  (def denominatorH2
    (util/square (+ 1 (* 0.001 (+ (util/square x1) (util/square x2))))))
  (- 0.5 (util/ddiv numeratorH2 denominatorH2)))

(defn itemH3Z [xi s]
  (* (Math/round (Math/abs (util/ddiv xi s))) (Math/signum (double xi)) s))

(defn itemH3D [i] (case (mod i 4.0)
                    1.0 1.0
                    2.0 1000.0
                    3.0 10.0
                    100.0))

(defn itemH3 [xi zi t c di]
  (if (< (Math/abs (double (- xi zi))) t)
    (* (util/square (+ (* t (Math/signum (double zi))) zi)) c di)
    (* di (util/square xi))))

;; x_i in [-1000, 1000]
(defn h3
  ([xs] (h3 xs 0.05 0.2 0.15))
  ([xs t s c]
   (apply + (map-indexed (fn [i x]
                           (itemH3 x (itemH3Z x s) t c (itemH3D (inc i)))) xs)))) ;;inc i because papers starts at 1

(defn sumH4 [xs d]
  (apply + (map (fn [x]
                  (util/ddiv (util/square x) d)) xs)))

(defn productH4 [xs]
  (apply * (map-indexed (fn [i x]
                          (Math/cos (util/ddiv x (util/sqrt (inc i))))) xs))) ;;inc i because papers starts at 1

;; x_i in [-600, 600]
(defn h4
  ([xs] (h4 xs 4000))
  ([xs d]
   (def sumOut (sumH4 xs d))
   (def productOut (productH4 xs))
   (+ (- sumOut productOut) 1)))

(defn log2 [n]
  (/ (Math/log n) (Math/log 2)))

(defn pow2 [n]
  (bit-shift-left 1 n))

(defn isDigit [char]
  (.contains [\- \1 \2 \3 \4 \5 \6 \7 \8 \9 \0] char))

(defn applyAssignment [formula assignment]
  "resturns how many clauses are sat (range from 0 to 1)"
  (/ (reduce (fn [result term]
               ((if (reduce (fn [termResult literal]
                              (or termResult
                                  ((if (> literal 0) identity not)
                                   (nth assignment  (- (Math/abs literal) 1) false))))
                            false
                            term)
                  inc
                  identity)
                result))
          0
          formula)
     (count formula)))

(defn getAssignmentFromCoordinate [pos]
  "calculates assignment based on position"
  (def pos (map int pos))
  (def dimensions (* 2(Math/ceil(log2(inc (apply max pos))))))
  (map
   (fn [dim]
     (>= (mod (+ (nth pos (mod dim 2))
                 (pow2 (int (/ dim 2)))
                 -1)
              (pow2 (+ 2 (int (/ dim 2)))))
         (pow2 (inc (int (/ dim 2))))))
   (range dimensions)))

;; solve sat problem on a k-v plane
(defn kv-fitness [formula pos]
  "calculates formula at position xs"
  "formula is the first argument to allow for currying :D"
  (if (and (> (first pos) 0) (>  (last pos) 0))
    (applyAssignment formula (getAssignmentFromCoordinate pos))
    -1))

(defn parseDimacs [url]
  (doall(map (fn [line] (map #(Integer/parseInt %) (drop-last(str/split line #" ")))) (filter (fn [line] (isDigit (first line))) (str/split (slurp url) #"\n")))))
