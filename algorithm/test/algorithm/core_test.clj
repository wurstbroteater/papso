(ns algorithm.core-test
  (:require [clojure.test :refer :all]
            [algorithm.core :refer :all]))


(def vectors {:v1 [3 5 -4] :v2 [9 5 -2]})
(deftest euclidDisUsageTest
  (testing "Testing euclidDis: Correct usage"
    (def euclidDisExpectedValue 6.324555320336759)
    (is (= (euclidDis (vectors :v1) (vectors :v2)) euclidDisExpectedValue))))

(deftest euclidDisIncorrectSizeTest
  (testing "Testing euclidDis: Incorrect vector dimension"
    (is (thrown? Exception (euclidDis (vectors :v1) (conj (vectors :v2) 1)) euclidDisExpectedValue))))