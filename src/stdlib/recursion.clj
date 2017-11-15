(ns stdlib.recursion)

;; Classic recursion:
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
