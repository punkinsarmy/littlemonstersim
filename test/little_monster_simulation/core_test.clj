3(ns little-monster-simulation.core-test
   (:require [clojure.test :refer :all]
            [little-monster-simulation.core :as c]))

(defn identity-behaviour
  [monster time]
  monster)

(defn halvit-behaviour
  [stat]
  (fn [monster _]
    (update-in monster [:stats stat] (partial * 0.5))))

(defn dissoc-in
  "Dissociates an entry from a nested associative structure returning a new
  nested structure. keys is a sequence of keys. Any empty maps that result
  will not be present in the new structure."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))

(deftest monsters
  (testing "a monster with no stats is dead"
    (let [monster (c/monster)]
      (is (not (c/alive? monster)))))

  (testing "a monster with life some life is alive"
    (let [monster {:stats {:life 100}}]
      (is (c/alive? monster))))

  (testing "a monster with 0 life is dead"
    (let [monster {:stats {:life 0}}]
      (is (not (c/alive? monster)))))
    
  (testing "can get value of a stat"
    (let [monster {:stats {:life 100} :behaviours {}}]
      (is (= 100 (c/value-of monster :life)))))

  (testing "life can change with time"
    (let [monster {:stats {:life 100}
                   :behaviours {:life (halvit-behaviour :life)}}
          monster' (c/tick monster 100)]
      (is (= 50.0 (c/value-of monster' :life)))))

  (testing "behaviours can expire"
    (let [monster {:stats {:life 100}
                   :behaviours {:one-shot (fn [m _]
                                            (dissoc-in m [:behaviours :one-shot]))}}
          monster' (c/tick monster 1)]
      (is (empty? (:behaviours monster')))))
  
  (testing "behaviours can add stats"
    (let [monster {:stats {:life 100}
                   :behaviours {:walkies (fn [m _]
                                           (assoc-in m [:stats :walk] 100))}}
          monster' (c/tick monster 1)]
      (is (= 100 (c/value-of monster' :walk)))))
  
  (testing "behaviours can add other behaviours"
    (let [monster {:stats {:life 100}
                   :behaviours {:add-walk (fn [m _]
                                            (assoc-in m
                                                      [:behaviours :walk]
                                                      (fn [m _]
                                                        (assoc-in m [:stats :walk] 100))))}}
          monster' (c/tick (c/tick monster 1) 1)]
      (is (= 100 (c/value-of monster' :walk)))))

  (testing "behaviours can remove stats"
    (let [monster {:stats {:life 100 :foo 5}
                   :behaviours {:kill-foo (fn [m _]
                                             (dissoc-in m [:stats :foo]))}}
          monster' (c/tick monster 1)]
      (is (nil? ((comp :foo :stats) monster'))))))
