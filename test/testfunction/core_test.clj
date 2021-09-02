(ns testfunction.core-test
  (:require [clojure.test :refer :all]))
(require '[utility.core :as util])
(require '[testfunction.core :as atf])


;;----------------------------- Show usage and tests for analytical test problems
(deftest analyticalTestProblemH1Test
  (testing "Testing H1 function"
    (is (util/equal? 0.11755694 (atf/h1 1 -1)))))

(deftest analyticalTestProblemH2Test
  (testing "Testing H2 function"
    (is (util/equal? 0.026215463876724243 (atf/h2 1 -1)))))

(deftest analyticalTestProblemH3Test
  (testing "Testing H3 function"
    (def initC 0.15)
    (def initS 0.2)
    (def initT 0.05)
    (def initX '(1.0 2.0))
    ;;0.165375 +  630.375
    (is (util/equal? 630.540375 (atf/h3 initX initT initS initC)))))

(deftest analyticalTestProblemH4Test
  (testing "Testing H4 function"
    (def initH4X '(1 1))
    (def initH4D 4000)
    ;;0.026215463876724243 + 0.56352262307559496
    (is (= 0.5897380869523192 (atf/h4 initH4X initH4D)))))

;;---------------------------- Tests for help method for analytical test problems
;;-------------- Tests for itemH3
(deftest itemH31Test
  (testing "Testing itemH3 with x = 2, d = 1"
    (def itemH3TestX 2.0)
    (def itemH3TestZ 2.0)
    (def itemH3TestS 0.2)
    (def itemH3TestT 0.05)
    (def itemH3TestC 0.15)
    (def itemH3TestD 1.0)
    (is (= 0.6303749999999999 (atf/itemH3 itemH3TestX itemH3TestZ itemH3TestT itemH3TestC itemH3TestD)))))

(deftest itemH32Test
  (testing "Testing itemH3 function with x = 1, d = 1"
    (def itemH3TestX 1.0)
    (def itemH3TestZ 1.0)
    (def itemH3TestS 0.2)
    (def itemH3TestT 0.05)
    (def itemH3TestC 0.15)
    (def itemH3TestD 1.0)
    (is (= 0.165375 (atf/itemH3 itemH3TestX itemH3TestZ itemH3TestT itemH3TestC itemH3TestD)))))

(deftest itemH33Test
  (testing "Testing itemH3 with x = 2, d = 1000"
    (def itemH3TestX 2.0)
    (def itemH3TestZ 2.0)
    (def itemH3TestS 0.2)
    (def itemH3TestT 0.05)
    (def itemH3TestC 0.15)
    (def itemH3TestD 1000.0)
    (is (util/equal? 630.375 (atf/itemH3 itemH3TestX itemH3TestZ itemH3TestT itemH3TestC itemH3TestD)))))

;;-------------- Tests for itemH3Z
(deftest itemH3Z1Test
  (testing "Testing itemH3Z function with parameter 1 -1"
    (is (util/equal? -1.0 (atf/itemH3Z 1 -1)))))

(deftest itemH3Z2Test
  (testing "Testing itemH3Z function with parameter -1 1"
    (is (util/equal? -1.0 (atf/itemH3Z -1 1)))))

(deftest itemH3Z2Test
  (testing "Testing itemH3Z function with parameter -1 -1"
    (is (util/equal? 1.0 (atf/itemH3Z -1 -1)))))

(deftest itemH3Z3Test
  (testing "Testing itemH3Z function with parameter 0.3 1"
    (is (= 0.0 (atf/itemH3Z 0.3 1)))))

(deftest itemH3Z4Test
  (testing "Testing itemH3Z function with parameter 2.0 0.2"
    (is (= 2.0 (atf/itemH3Z 2.0 0.2)))))

;;-------------- Tests for itemH3D
(deftest itemH3D1Test
  (testing "Testing itemH3D function with parameter 1"
    (is (util/equal? 1.0 (atf/itemH3D 1)))))

(deftest itemH3D2Test
  (testing "Testing itemH3D function with parameter 2"
    (is (util/equal? 1000.0 (atf/itemH3D 2)))))

(deftest itemH3D3Test
  (testing "Testing itemH3D function with parameter 3"
    (is (util/equal? 10.0 (atf/itemH3D 3)))))

(deftest itemH3D4Test
  (testing "Testing itemH3D function with parameter 4"
    (is (util/equal? 100.0 (atf/itemH3D 4)))))

(deftest itemH3D5Test
  (testing "Testing itemH3D function with parameter 5"
    (is (util/equal? 1.0 (atf/itemH3D 5)))))

;;-------------- Tests for sumH4
(deftest sumH41Test
  (testing "Testing sumH4 wit parameter xs = '(1), d = 4000"
    (is (util/equal? 0.0025 (atf/sumH4 '(1) 400)))))

(deftest sumH42Test
  (testing "Testing sumH4 wit parameter xs = '(1 1), d = 4000"
    (is (util/equal? 0.005 (atf/sumH4 '(1 1) 400)))))

;;-------------- Tests for productH4
(deftest productH41Test
  (testing "Testing productH4 wit parameter xs = '(1)"
    (is (= (Math/cos 1) (atf/productH4 '(1))))))

(deftest productH42Test
  (testing "Testing productH4 wit parameter xs = '(1 1)"
    (is (= (* (Math/cos 1) (Math/cos (util/ddiv 1 (util/sqrt 2)))) (atf/productH4 '(1 1))))))
