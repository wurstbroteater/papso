
(def plotfile "resources/plot.gnuplot")

(defn plot_swarm
  [swarm]
  (def resource_path "resources/")
  (def pointfile (str "plotdata/" (first swarm) ".txt"))
  (spit plotfile (str "plot \"" pointfile "\",'cont.dat' w l lt -1 lw 0.5\n" ) :append true)
  (spit (str resource_path pointfile)
        (reduce
         str
         (map (fn [string] (apply str (remove #(#{\[,\]} %) (str string "\n"))))
              (map
               str
               (map vec (map :position (rest swarm))))))))

(defn plot_swarms
  "generates a gnuplot program to plot the swarm points"
  [swarms]
  (spit plotfile "set term pdf\nset output \"plot.pdf\"\nset xrange [0:150]\nset yrange [0:150]\n")
  (map plot_swarm
       (map cons (range 0 (count swarms)) swarms)))
