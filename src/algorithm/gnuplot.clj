(use '[clojure.java.shell :only [sh]])
(defn sqrt [n] (java.lang.Math/sqrt n)) ;; sqrt wrapper
(defn abs [n] (java.lang.Math/abs n)) ;; abs wrapper
(defn pow [b n] (java.lang.Math/pow b n)) ;; pow wrapper

(defn plotSwarms

  ([swarms] "provide default bounds"
   (plotSwarms swarms [-50 200] [-50 200]))
  ([swarms xBounds yBounds]  "generates a gnuplot program to plot the swarm points"
  (def plotfile "resources/plot.gnuplot")
  (defn plotSwarm
    "clojure for plot_swarms"
    [swarm]
    (def pointfile (str "plotdata/" (first swarm) ".txt"))
    (spit plotfile (str "plot \"" pointfile "\",'cont.dat' w l lt -1 lw 0.5\n" ) :append true)
    (spit pointfile
          (reduce
           str
           (map (fn [string] (apply str (remove #(#{\[,\]} %) (str string "\n"))))
                (map
                 str
                 (map vec (map :position (rest swarm))))))))
  (def xRange (str "[" (first xBounds) ":" (last xBounds) "]"))
  (def yRange (str "[" (first yBounds) ":" (last yBounds) "]"))
  (spit plotfile (str "set term pdf\nset output \"plot.pdf\"\nset xrange " xRange "\nset yrange " yRange "\n"))
  (def ret (count(map plotSwarm
                      (map cons (range 0 (count swarms)) swarms))))
  (sh "gnuplot" "resources/plot.gnuplot")
  ret))
