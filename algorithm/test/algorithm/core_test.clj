(ns algorithm.core-test
  (:require [clojure.test :refer :all]
            [algorithm.core :refer :all]))

(def dummy1 [3 5 -4])
(def dummy2 [9 5 -2])
(def euclidDisExpectedValue 6.324555320336759)

(deftest euclidDisTest
  (testing "Testing euclidDis"
    (is (= (euclidDis dummy1 dummy2) euclidDisExpectedValue))))
