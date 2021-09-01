(ns algorithm.core)
(require '[clojure.string :as str])
(def landscape (map (fn [line] (map read-string (str/split line #"\t"))) (str/split (slurp "./resources/landscape.tsv") #"\n")))
(def size (count landscape))

(defn fitness [position] ;; calculates fitness for a point
  (nth (nth landscape (int (last position) ) '(-100)) (int (first position)) -100 ))
