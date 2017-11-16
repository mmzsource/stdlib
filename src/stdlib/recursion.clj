(ns stdlib.recursion)

;; Classic recursion
;; -----------------
;; 1. Base Case
;; 2. Advance through the datastructure (divide the problem)
;; 3. Initialisation
;;
;; Two types of recursion
;; ----------------------
;; 1. Tail recursion (recur) - Doesn't have to remember anything
;; 2. Non tail recursion - Has to remember where it's at. Stackoverflow risk.
;;

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

;;;;;;;;;;;;
;; FILTER ;;
;;;;;;;;;;;;

(filter even? (range 10))

;; My implementation
(defn filter* [pred coll]
  (if (empty? coll)
    ()
    (if (pred (first coll))
      (cons (first coll) (filter* pred (rest coll)))
      (filter* pred (rest coll)))))

(filter* even? (range 10))

;; Implementation with (partial) tail recursion:
(defn filter** [pred coll]
  (if (empty? coll)
    ()
    (if (pred (first coll))
      (cons (first coll) (filter* pred (rest coll)))
      (recur pred (rest coll)))))

(filter** even? (range 10))

;; Implementation with complete tail recursion:
(defn filter*** [pred coll]
  ((fn [pred coll acc]
      (if (empty? coll)
        acc
        (if (pred (first coll))
          (recur pred (rest coll) (conj acc (first coll)))
          (recur pred (rest coll) acc))))
   pred
   coll
   []))

(filter*** even? (range 10))
