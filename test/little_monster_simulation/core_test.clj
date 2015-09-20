(ns little-monster-simulation.core-test
  (:require [clojure.test :refer :all]
            [little-monster-simulation.core :as c]))

(defn identity-behaviour
  [value time]
  value)

(defn halvit-behaviour
  [value time]
  (/ value 2))

(deftest monsters
  (testing "a monster with no stats is dead"
    (let [monster (c/monster)]
      (is (not (c/alive? monster)))))
  
  (testing "a monster with life some life is alive"
    (let [monster (c/monster)
          monster' (assoc monster :life [100 identity-behaviour])]
      (is (c/alive? monster'))))
  
  (testing "can get value of a stat"
    (let [monster (c/monster)
          monster' (assoc monster :life [100 identity-behaviour])]
      (is (= 100 (c/value-of monster' :life)))))
  
  (testing "life can change with time"
    (let [monster (c/monster)
          monster' (assoc monster :life [100 halvit-behaviour])
          monster'' (c/tick monster' 100)]
      (is (= 50 (c/value-of monster'' :life))))))
