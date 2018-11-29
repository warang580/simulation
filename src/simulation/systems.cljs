(ns simulation.systems
  (:require [quil.core :as q]
            [simulation.vector :as v]))

; @TODO: systems shouldn't be accessing state globally

; @TODO: compose position+move
; @TODO: make checks outside system
(defn move [state entity]
  (if (contains? entity :velocity)
    (let [a (:acceleration entity)
          v (mapv + (:velocity entity) a)
          p (mapv + (:position entity) v)]
      (assoc entity :position p
                    :velocity v
                    :acceleration [0 0]))
    entity))

; @TODO: compose position+move+seek(x)
; @TODO: make checks outside system
(defn seek [state entity]
  (if (contains? entity :target)
    (let [target (:target entity)
          targetm (first (filter #(contains? % target) (:entities state)))]
      (if targetm
         (let [{:keys [position
                       velocity
                       acceleration
                       maxspeed
                       maxforce
                       maxdist
                       weight]} entity
               target-pos (:position targetm)
               distance (v/distance position target-pos)
               speed (if (<= distance maxdist) (q/map-range distance 0 maxdist 0 maxspeed) maxspeed)
               desired (v/mult (mapv - target-pos position) (* speed weight))
               steer (v/limit (mapv - desired velocity) maxforce)
               a (mapv + acceleration steer)]
           (assoc entity :acceleration a))
         entity))
    entity))

(defn follow-mouse [state entity]
  (if (contains? entity :follow-mouse)
    (assoc entity :position [(q/mouse-x) (q/mouse-y)])
    entity))
