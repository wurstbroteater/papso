
(def plotfile "plot.gnuplot")

(defn plot_swarm
  [swarm]
  (def pointfile (str "plotdata/" (first swarm) ".txt"))
  (spit plotfile (str "plot \"" pointfile "\"\n" ) :append true)
  (spit pointfile
        (reduce
         str
         (map (fn [string] (apply str (remove #(#{\[,\]} %) (str string "\n"))))
              (map
               str
               (map vec (map :position (rest swarm))))))))

(defn plot_swarms
  "generates a gnuplot program to plot the swarm points"
  [swarms]
  (spit plotfile "set term pdf\nset output \"plot.pdf\"\nset xrange [-1:1]\nset yrange [-1:1]\n")
  (map plot_swarm
       (map cons (range 0 (count swarms)) swarms)))
