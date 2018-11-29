(ns simulation.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [simulation.components :as c]
            [simulation.systems :as s]))

(defn center []
  (-> {}
    (assoc :center true)
    (c/position [(/ (q/width) 2) (/ (q/height) 2)])))

(defn mouse []
  (-> {}
    (assoc :mouse true)
    (c/position [(/ (q/width) 2) (/ (q/height) 2)])
    (c/follow-mouse)))

(defn bird []
  (-> {}
    (assoc :bird true)
    (c/position [0 0])
    (c/can-move)
    (c/seek :mouse)
    (c/render (fn [entity]
                (q/fill 10 100)
                (q/stroke 20)
                (q/push-matrix)
                (let [[x y] (:position entity)]
                  (q/translate x y)
                  (let [[vx vy] (:velocity entity)]
                    (q/rotate (Math/atan2 vy vx)))
                  (q/triangle 0 -2, 5 0, 0 2))
                (q/pop-matrix)))))

(defn setup []
  (q/frame-rate 100)
  (q/color-mode :hsb)
  {:entities [(mouse) (center) (bird)]})

(defn update-entities [state entities]
  (->> entities
     (mapv #(s/seek state %))
     (mapv #(s/move state %))
     (mapv #(s/follow-mouse state %))))

(defn update-state [state]
  (let [entities (:entities state)]
    (assoc state :entities (update-entities state entities))))

(defn draw-state [state]
  ; Clear screen
  (q/background 240)
  ; Show entities
  (let [entities   (:entities state)
        renderable (filterv #(contains? % :render) entities)]
    (doseq [e renderable]
      ((:render e) e)))
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
