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
