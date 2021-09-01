(ns algorithm.core-test
  (:require [clojure.test :refer :all]
            [algorithm.core :refer :all]))

(defn equal?
  ([x y] (equal? x y 0.001))
  ([x y eps]
   (< (Math/abs (- x y)) eps)))

(def vectors {:v1 [3 5 -4] :v2 [9 5 -2]})
(deftest euclidDisUsageTest
  (testing "Testing euclidDis: Correct usage"
    (def euclidDisExpectedValue 6.324555320336759)
    (is (equal? euclidDisExpectedValue (euclidDis (vectors :v1) (vectors :v2))))))

(deftest euclidDisIncorrectSizeTest
  (testing "Testing euclidDis: Incorrect vector dimension"
    (is (thrown? Exception (euclidDis (vectors :v1) (conj (vectors :v2) 1))))))

(deftest startReturnAmount0Test
  (testing "Testing the number of returned populations by start with 0 iterations and 0 particles"
    (is (equal? 0 (count (start 0 0))))))

(deftest startReturnAmount1Test
  (testing "Testing the number of returned populations by start with 1 iterations and 0 particles"
    (is (equal? 0 (count (start 1 0))))))

(deftest startReturnAmount2Test
  (testing "Testing the number of returned populations by start with 0 iterations and 1 particles"
    (is (equal? 1 (count (start 0 1))))))

(deftest startReturnAmount3Test
  (testing "Testing the number of returned populations by start with 1 iterations and 1 particles"
    (is (equal? 1 (count (start 1 1))))))

(deftest startReturnAmount4Test
  (testing "Testing the number of returned populations by start with 2 iterations and 1 particles"
    (is (equal? 2 (count (start 2 1))))))

(deftest startReturnAmount5Test
  (testing "Testing the number of returned populations by start with 2 iterations and 2 particles"
    (is (equal? 3 (count (start 2 2))))))

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
  (* (Math/round (Math/abs (ddiv xi s))) (Math/signum (double xi)) s))

(defn itemH3 [xi zi t c di]
  (if (< (Math/abs (- xi zi)) t)
    (* (square (+ (* t (Math/signum (double zi))) zi)) c di))
  (* di (square xi)))


(defn itemH3D [i] (case (mod i 4.0)
                    1.0 1.0
                    2.0 1000.0
                    3.0 10.0
                    100.0))


(deftest itemH31Test
  (testing "Testing itemH3 function"
    (def h3TestX 0.5)
    (def h3TestS 0.2)
    (def h3TestZ (itemH3Z h3TestX h3TestS))
    (def h3TestT 0.05)
    (def h3TestC 0.15)
    (def h3TestD (itemH3D 3))
    (is (equal? 2.5 (itemH3 h3TestX h3TestZ h3TestT h3TestC h3TestD)))))

(defn h3
  ([xs] (h3 xs 0.05 0.2 0.15))
  ([xs t s c]
   (map-indexed (fn [i x]
                  (itemH3 x (itemH3Z x s) t c (itemH3D (inc i)))) xs)))

(comment
  (deftest analyticalTestProblemH3Test
    (testing "Testing H3 function"
      (def initC 0.15)
      (def initS 0.2)
      (def initT 0.05)
      (def initX '(1.0 2.0 3.0 4.0))

      (is (equal? 0.026215463876724243 (h3 initX initT initS initC)))))
  )

;; evaluated the expected test values with wolfram alpha
(deftest analyticalTestProblemH1Test
  (testing "Testing H1 function"
    (is (equal? 0.11755694 (h1 1 -1)))))

(deftest analyticalTestProblemH2Test
  (testing "Testing H2 function"
    (is (equal? 0.026215463876724243 (h2 1 -1)))))

(deftest itemH3Z1Test
  (testing "Testing itemH3Z function with parameter 1 -1"
    (is (equal? -1.0 (itemH3Z 1 -1)))))

(deftest itemH3Z2Test
  (testing "Testing itemH3Z function with parameter -1 1"
    (is (equal? -1.0 (itemH3Z -1 1)))))

(deftest itemH3Z2Test
  (testing "Testing itemH3Z function with parameter -1 -1"
    (is (equal? 1.0 (itemH3Z -1 -1)))))

(deftest itemH3Z3Test
  (testing "Testing itemH3Z function with parameter 1 0.2"
    (is (equal? 1.0 (itemH3Z 1 0.2)))))

(deftest itemH3D1Test
  (testing "Testing itemH3D function with parameter 1"
    (is (equal? 1.0 (itemH3D 1)))))

(deftest itemH3D2Test
  (testing "Testing itemH3D function with parameter 2"
    (is (equal? 1000.0 (itemH3D 2)))))

(deftest itemH3D3Test
  (testing "Testing itemH3D function with parameter 3"
    (is (equal? 10.0 (itemH3D 3)))))

(deftest itemH3D4Test
  (testing "Testing itemH3D function with parameter 4"
    (is (equal? 100.0 (itemH3D 4)))))

(deftest itemH3D5Test
  (testing "Testing itemH3D function with parameter 5"
    (is (equal? 1.0 (itemH3D 5)))))


