(ns simulation.vector)

; Vector magnitude (size)
(defn magnitude [v]
  (let [[x y] v]
    (Math/sqrt (+ (* x x) (* y y)))))

; Unit vector
(defn unit [v]
  (let [mag (magnitude v)]
    (if (== mag 0) [0 0] (mapv #(/ % mag) v))))

; Multiply vector by k
(defn mult [v k]
  (mapv #(* % k) (unit v)))

; Constrain max vector size
(defn limit [v max]
  (let [maxv (mapv #(* % max) (unit v))]
    (if (> (magnitude v) max) maxv v)))

; Distance between two points/vectors
(defn distance [a b]
  (let [[x1 y1] a
        [x2 y2] b
        x (- x2 x1)
        y (- y2 y1)]
    (Math/sqrt (+ (* x x) (* y y)))))
