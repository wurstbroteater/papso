(ns algorithm.core-test
  (:require [clojure.test :refer :all]
            [algorithm.core_eric :refer :all]))
(require '[utility.core :as util])

(deftest startReturnAmount0Test
  (testing "Testing the number of returned populations by start with 0 iterations and 0 particles"
    (is (util/equal? 0 (count (start 0 0))))))

(deftest startReturnAmount1Test
  (testing "Testing the number of returned populations by start with 1 iterations and 0 particles"
    (is (util/equal? 0 (count (start 1 0))))))

(deftest startReturnAmount2Test
  (testing "Testing the number of returned populations by start with 0 iterations and 1 particles"
    (is (util/equal? 1 (count (start 0 1))))))

(deftest startReturnAmount3Test
  (testing "Testing the number of returned populations by start with 1 iterations and 1 particles"
    (is (util/equal? 1 (count (start 1 1))))))

(deftest startReturnAmount4Test
  (testing "Testing the number of returned populations by start with 2 iterations and 1 particles"
    (is (util/equal? 2 (count (start 2 1))))))

(deftest startReturnAmount5Test
  (testing "Testing the number of returned populations by start with 2 iterations and 2 particles"
    (is (util/equal? 3 (count (start 2 2))))))
