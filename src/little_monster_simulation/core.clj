(ns little-monster-simulation.core)

(defn monster [] {:stats {} :behaviours []})

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
          (:behaviours monster)))
