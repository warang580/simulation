(ns simulation.components
  (:require [quil.core :as q]))

(defn position
  ([state]
   (assoc state :position [(q/random (q/width)) (q/random (q/height))]))
  ([state p]
   (assoc state :position p)))

(defn can-move
  ([state]
   (assoc state :velocity (q/random-2d)
                :acceleration [0 0]))
  ([state velocity]
   (assoc state :velocity velocity
                :acceleration [0 0]))
  ([state velocity acceleration]
   (assoc state :velocity velocity
                :acceleration acceleration)))

; @TODO: Movable target
(defn seek
  ([state target]
   ; Default seek behaviour
   (assoc state :target target
                :maxspeed 3
                :maxforce 0.2
                :maxdist  100
                :weight   1))
  ([state target speed force dist]
   (assoc state :target   target
                :maxspeed speed
                :maxforce force
                :maxdist  dist
                :weight   1))
  ([state target speed force dist weight]
   (assoc state :target   target
                :maxspeed speed
                :maxforce force
                :maxdist  dist
                :weight   weight)))
