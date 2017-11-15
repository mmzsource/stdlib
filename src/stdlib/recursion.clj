(ns stdlib.recursion)

;; Classic recursion
;; -----------------
;; 1. Base Case
;; 2. Advance through the datastructure (divide the problem)
;; 3. Initialisation

;;;;;;;;;;;
;; COUNT ;;
;;;;;;;;;;;

(count nil)
(count [1 2 3])
(count {:a 1 :b 2})

;; My implementation
(defn count* [coll]
  (if (empty? coll)
    0
    (inc (count* (rest coll)))))

(count* nil)
(count* [1 2 3])
(count* {:a 1 :b 2})
(count* (range 7))


;;;;;;;;;
;; MAP ;;
;;;;;;;;;

(map inc [2 3 4])

;; My implementation
(defn map* [f coll]
  (if (empty? coll)
    ()
    (cons (f (first coll)) (map* f (rest coll)))))

(map* inc [1 2 3 4 5 6])
