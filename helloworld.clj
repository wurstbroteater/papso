
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
