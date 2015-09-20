(ns little-monster-simulation.core)

(defn monster [] {})

(defn alive?
  [monster]
  (if-let [life (first (:life monster))]
    (< 0 life)
    false ))
