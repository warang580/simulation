(ns simulation.systems
  (:require [quil.core :as q]
            [simulation.vector :as v]))

; @TODO: systems shouldn't be accessing state globally

; @TODO: compose position+move
(defn move [state entity]
  (let [a (:acceleration entity)
        v (mapv + (:velocity entity) a)
        p (mapv + (:position entity) v)]
    (assoc entity :position p
                  :velocity v
                  :acceleration [0 0])))

; @TODO: compose position+move+seek(x)
(defn seek [state entity]
  (let [{:keys [position
                velocity
                acceleration
                maxspeed
                maxforce
                maxdist
                weight
                target]} entity
        distance (v/distance position target)
        speed (if (<= distance maxdist) (q/map-range distance 0 maxdist 0 maxspeed) maxspeed)
        desired (v/mult (mapv - target position) (* speed weight))
        steer (v/limit (mapv - desired velocity) maxforce)
        a (mapv + acceleration steer)]
      (assoc entity :acceleration a)))
