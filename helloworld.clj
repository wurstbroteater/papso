
(defn hello-world []
  (println "Hello World"))
(hello-world)
(if false
  "erdick"
  "ed dick'nt")
(defn foobar
  [boo]
  (if boo
    "lolwhut"
    123))
(foobar false)
(foobar nil)

(or nil  nil :large_I_mean_venti :why_cant_I_just_say_large)

(or (= "" 1) (= "no" "yes"))

(or nil)
(:super_cool 3)
(:super_cool (println 60))


(def failed-protagonist-names
  ["Larry Potter" "Doreen the Explorer" "The Incredible Bulk"])

(defn error-message
  [severity]
  (str "OH GOD! IT'S A DISASTER! WE'RE "
       (if (= severity :mild)
         "MILDLY INCONVENIENCED!"
         "DOOOOOOOMED!")))
(error-message :mild)
(def name "Chewbacca")
(str "\"Uggllglglglglglglglll\" - " name)

(def myMap {:first-name "Charlie"
            :last-name "McFishwich"})
(println myMap)
(def map_fun {"string-key" +})
((get map_fun "string-key") 1 2 3)
((get map_fun "string-keyasd" -) 1 2 3)
(get-in {:a 0 :b{:c {:c "ho hum"}}} [:b :c :c])
((map_fun "string-key") 4)
(myMap :first-name)
(get {:a "a" :b "b" :c "c"} :a)
(=(:a {:a 1 :b 2 :c 3})
  ({:a 1 :b 2 :c 3} :a))
([3 2 1] 0)
(conj [1 2 3] 1)
(:d {:a 1 :b 2 :c 3} "No gnome knows that scotty doesn't know")

(or nil + -)
(and + -)

(defn weird-arity
  ([]
   "Destiny dressed you this morning, my friend, and now Fear is
     trying to pull off your pants. If you give up, if you give in,
     you're gonna end up naked with Fear just standing there laughing
     at your dangling unmentionables! - the Tick")
  ([fun & values]
   (fun values)))
(weird-arity println 1 2 3 4)
(defn weird-list
  [[foo] [bar] [baz]]
  (- foo bar baz))
(weird-list [1 2 3 4 5] [6 7] [10])

(defn weird-list-2
  [[foo bar baz]]
  (+ foo bar baz))
(weird-list-2 [1 2 3 4 5])
;; function body
