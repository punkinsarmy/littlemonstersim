(ns little-monster-simulation.core-test
  (:require [clojure.test :refer :all]
            [little-monster-simulation.core :as c]))

(defn identity-behaviour
  [monster time]
  monster)

(defn halvit-behaviour
  [stat]
  (fn [monster _]
    (update-in monster [:stats stat] (partial * 0.5))))

(deftest monsters
  (testing "a monster with no stats is dead"
    (let [monster (c/monster)]
      (is (not (c/alive? monster)))))

  (testing "a monster with life some life is alive"
    (let [monster (c/monster)
          monster' (assoc-in monster [:stats :life] 100)]
      (is (c/alive? monster'))))

  (testing "can get value of a stat"
    (let [monster (c/monster)
          monster' (assoc-in monster [:stats :life] 100)
          monster'' (update-in monster' [:behaviours]
                               #(conj % identity-behaviour))]
      (is (= 100 (c/value-of monster'' :life)))))

  (testing "life can change with time"
    (let [monster (c/monster)
          monster' (assoc-in monster [:stats  :life] 100)
          monster'' (update-in monster' [:behaviours]
                               #(conj % (halvit-behaviour :life)))
          monster''' (c/tick monster'' 100)]
      (is (= 50.0 (c/value-of monster''' :life))))))
