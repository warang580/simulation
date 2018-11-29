(ns simulation.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [simulation.components :as c]
            [simulation.systems :as s]))

(defn setup []
  (q/frame-rate 100)
  (q/color-mode :hsb)
  {:entities (vec (repeatedly 5 make-entity))})

; @TODO: add a mouse entity that "follows" (is) the mouse

(defn make-entity []
  (-> {}
      (c/position)
      (c/can-move [0 0])
      ; @TODO: be able to add seek behaviours and target entities
      (c/seek [(q/mouse-x) (q/mouse-y)] 3 0.2 100 -1)
      (c/seek [(/ (q/width) 2) (/ (q/height) 2)])))

(defn update-entities [state entities]
  (->> entities
    (mapv #(s/seek state %))
    (mapv #(s/move state %))))

(defn update-state [state]
  (let [entities (:entities state)]
    (assoc state :entities (update-entities state entities))))

; @TODO: renderable as a system/component ?
(defn draw-entity [entity]
  (q/fill 10 100)
  (q/stroke 20)
  (q/push-matrix)
  (let [[x y] (:position entity)]
    (q/translate x y)
    (let [[vx vy] (:velocity entity)]
      (q/rotate (Math/atan2 vy vx)))
    (q/triangle 0 -2, 5 0, 0 2))
  (q/pop-matrix))

(defn draw-state [state]
  ; Clear screen
  (q/background 240)
  ; Show entities
  (doseq [e (:entities state)]
    (draw-entity e))
  ; Show FPS
  (q/fill 0)
  (q/stroke 0)
  (q/text (str "FPS: " (int (q/current-frame-rate))) 10 10))

(defn on-key-pressed [state event]
  (case (:key-code event)
    32 (println (str "state = " state))
    (println event))
  state)

(q/defsketch simulation
  :title "Simulation"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :key-pressed on-key-pressed
  :features [:keep-on-top :resizable]
  :middleware [m/fun-mode m/pause-on-error])
