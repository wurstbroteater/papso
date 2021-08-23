(ns algorithm.core-test
  (:require [clojure.test :refer :all]
            [algorithm.core :refer :all]))


(def vectors {:v1 [3 5 -4] :v2 [9 5 -2]})
(deftest euclidDisUsageTest
  (testing "Testing euclidDis: Correct usage"
    (def euclidDisExpectedValue 6.324555320336759)
    (is (= euclidDisExpectedValue (euclidDis (vectors :v1) (vectors :v2))))))

(deftest euclidDisIncorrectSizeTest
  (testing "Testing euclidDis: Incorrect vector dimension"
    (is (thrown? Exception (euclidDis (vectors :v1) (conj (vectors :v2) 1))))))

(deftest startReturnAmount0Test
  (testing "Testing the number of returned populations by start with 0 iterations and 0 particles"
    (is (= 0 (count (start 0 0))))))

(deftest startReturnAmount1Test
  (testing "Testing the number of returned populations by start with 1 iterations and 0 particles"
    (is (= 0 (count (start 1 0))))))

(deftest startReturnAmount2Test
  (testing "Testing the number of returned populations by start with 0 iterations and 1 particles"
    (is (= 1 (count (start 0 1))))))

(deftest startReturnAmount3Test
  (testing "Testing the number of returned populations by start with 1 iterations and 1 particles"
    (is (= 1 (count (start 1 1))))))

(deftest startReturnAmount4Test
  (testing "Testing the number of returned populations by start with 2 iterations and 1 particles"
    (is (= 2 (count (start 2 1))))))

(deftest startReturnAmount5Test
  (testing "Testing the number of returned populations by start with 2 iterations and 2 particles"
    (is (= 3 (count (start 2 2))))))

;;----------------------------- Analytical Test Problem Functions

;; x_i in [-100, 100]
(defn h1 [x1 x2]
  (def numeratorH1
    (+ (square (Math/sin (- x1 (ddiv x2 8)))) (square (Math/sin (+ x2 (ddiv x1 8))))))
  (def denominatorH1
    (+ (sqrt (+ (square (- x1 8.6998)) (square (- x2 6.7665)))) 1))
  (ddiv numeratorH1 denominatorH1))

;; x_i in [-100, 100]
(defn h2 [x1 x2]
  (def numeratorH2
    (- (square (Math/sin (sqrt (+ (square x1) (square x2))))) 0.5))
  (def denominatorH2
    (square (+ 1 (* 0.001 (+ (square x1) (square x2))))))
  (- 0.5 (ddiv numeratorH2 denominatorH2)))

(defn itemH3Z [xi s]
  (* (Math/floor (+ (Math/abs (ddiv xi s)) 0.4999999999999)) (Math/signum (double xi)) s))

(defn itemH3 [xi zi t c di]
  (if (< (Math/abs (- xi zi)) t)
    (* (square (+ (* t (Math/signum zi)) zi)) c di))
  (* di (square xi)))

(comment
  (defn h3 [& inputs]
    "use it like this (h3 1 2.4 3.0 ...) "
    (letfn [(item [element]
              (itemH3 element (itemH3Z element)))]))



  (deftest analyticalTestProblemH3Test
    (testing "Testing H3 function"
      (is (= 0.026215463876724243 (itemH3Z 1 -1)))))
  )

;; evaluated the expected test values with wolfram alpha
(deftest analyticalTestProblemH1Test
  (testing "Testing H1 function"
    (is (= 0.11755694 (h1 1 -1)))))

(deftest analyticalTestProblemH2Test
  (testing "Testing H2 function"
    (is (= 0.026215463876724243 (h2 1 -1)))))