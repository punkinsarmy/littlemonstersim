(ns little-monster-simulation.core-test
  (:require [clojure.test :refer :all]
            [little-monster-simulation.core :as c]))

(deftest monsters
  (testing "a monster with no stats is dead"
    (let [monster (c/monster)]
      (is (not (c/alive? monster))))))
