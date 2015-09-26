(ns little-monster-simulation.core)

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

(defn behaviour [monster name value]
  (assoc-in monster [:behaviours name] value))

(defn remove-behaviour [monster name]
  (dissoc-in monster [:behaviours name]))

(defn stat [monster name value]
  (assoc-in monster [:stats name] value))

(defn remove-stat [monster name]
  (dissoc-in monster [:stats name]))

(defn monster [] {:stats {} :behaviours (array-map)})

(defn value-of [monster stat]
  (stat (:stats monster)))

(defn alive?
  [monster]
  (if-let [life (value-of monster :life)]
    (< 0 life)
    false))

(defn tick
  [monster dt]
  (reduce #(%2 % dt)
          monster
          (vals (:behaviours monster))))
