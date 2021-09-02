(ns utility.core-test
  (:require [clojure.test :refer :all]))
(require '[utility.core :as util])

;;---------------------------- Tests for general utility methods
(def vectors {:v1 [3 5 -4] :v2 [9 5 -2]})

(deftest euclidDisUsageTest
  (testing "Testing euclidDis: Correct usage"
    (def euclidDisExpectedValue 6.324555320336759)
    (is (util/equal? euclidDisExpectedValue (util/euclidDis (vectors :v1) (vectors :v2))))))

(deftest euclidDisIncorrectSizeTest
  (testing "Testing euclidDis: Incorrect vector dimension"
    (is (thrown? Exception (util/euclidDis (vectors :v1) (conj (vectors :v2) 1))))))
