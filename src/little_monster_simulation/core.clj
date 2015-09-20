(ns little-monster-simulation.core)

(defn monster [] {})

(defn value-of [monster stat]
  (first (stat monster)))

(defn alive?
  [monster]
  (if-let [life (value-of monster :life)]
    (< 0 life)
    false))

(defn tick
  [monster dt]
  (into {} (for [[key [value behaviour]] monster]
             [key [(behaviour value dt) behaviour]])))
