(ns little-monster-simulation.core-test
  (:require [clojure.test :refer :all]
            [little-monster-simulation.core :as c]))

(defn identity-behaviour
  [value time]
  value)

(deftest monsters
  (testing "a monster with no stats is dead"
    (let [monster (c/monster)]
      (is (not (c/alive? monster)))))
  (testing "a monster with life some life is alive"
    (let [monster (c/monster)
          monster' (assoc monster :life [100 identity-behaviour])]
      (is (c/alive? monster')))))
