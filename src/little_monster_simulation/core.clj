(ns little-monster-simulation.core)

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
