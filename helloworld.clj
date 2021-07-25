
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
;;------- start: the shire 
(def asym-hobbit-body-parts [
  {:name "head" :size 3}
  {:name "left-eye" :size 1}
  {:name "left-ear" :size 1}
  {:name "mouth" :size 1}
  {:name "nose" :size 1}
  {:name "neck" :size 2}
  {:name "left-shoulder" :size 3}
  {:name "left-upper-arm" :size 3}
  {:name "chest" :size 10}
  {:name "back" :size 10}
  {:name "left-forearm" :size 3}
  {:name "abdomen" :size 6}
  {:name "left-kidney" :size 1}
  {:name "left-hand" :size 2}
  {:name "left-knee" :size 2}
  {:name "left-thigh" :size 4}
  {:name "left-lower-leg" :size 3}
  {:name "left-achilles" :size 1}
  {:name "left-foot" :size 2}])

(defn matching-part
[part]
{:name (clojure.string/replace (:name part) #"^left-" "right-")
  :size (:size part)})

(defn symmetrize-body-parts
"Expects a seq of maps that have a :name and :size"
[asym-body-parts]
(loop [remaining-asym-parts asym-body-parts
        final-body-parts []]
  (if (empty? remaining-asym-parts)
    final-body-parts
    (let [[part & remaining] remaining-asym-parts]
      (recur remaining
              (into final-body-parts
                    (set [part (matching-part part)])))))))

;;(println symmetrize-body-parts asym-hobbit-body-parts) ;; prints content of asym-hobbit-body-parts
;;(println (symmetrize-body-parts asym-hobbit-body-parts)) ;; prints return value of symmetrize-body-parts(...)
;;(println symmetrize-body-parts)
;;------- end: the shire

;;----start: context stuff
(def x 0)
(let [x 1] x)
;; I want x to be 0 in the global context, but within the context of this let expression, it should be 1 
;;----end: context stuff
(into [] (set [:a :a])) ;; returns [:a]
;;(into [] #{:a :a});; throws duplicate exception
(into [] #{:b })

;;----start: exercise
;;Ex 2
(defn addIt
  "Description are optional: add 100 to input"
  [arg0]
  (+ arg0 100)
)

(loop [iteration 0]
  (println (addIt iteration))
  (if (< iteration 4)
    (recur (inc iteration))
  )
)
;; I am failing to translate this to something with map + '(0 1 2 3 4)