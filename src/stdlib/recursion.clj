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

;; My non tail recursive implementation
(defn count* [coll]
  (if (empty? coll)
    0
    (inc (count* (rest coll)))))

(count* nil)
(count* [1 2 3])
(count* {:a 1 :b 2})
(count* (range 7))

;; My tail recursive implementation
(defn count* [coll]
  ((fn [coll acc]
      (if (empty? coll)
        acc
        (recur (rest coll) (inc acc))))
   coll
   0))

(count* (range 10))

;;;;;;;;;
;; MAP ;;
;;;;;;;;;

(map inc [2 3 4])

;; My non tail recursion implementation
(defn map* [f coll]
  (if (empty? coll)
    ()
    (cons (f (first coll)) (map* f (rest coll)))))

(map* inc [1 2 3 4 5 6])

;; My tail recursion solution
(defn map* [f coll]
  ((fn [f coll acc]
      (if (empty? coll)
        acc
        (recur f (rest coll) (conj acc (f (first coll))))))
   f
   coll
   []))

(map* inc [1 2 3 4 5 6])

;; My cleaned up tail recursive version using loop, and no lambda
(defn map* [f coll]
  (loop [coll coll acc []]
    (if (empty? coll)
      acc
      (recur (rest coll) (conj acc (f (first coll)))))))

(map* inc (range 10))


;; My lazy implementation (lazy and don't consume the stack completely)
(defn map* [f coll]
  (lazy-seq
    (if (empty? coll)
      ()
      (cons (f (first coll)) (map*** f (rest coll))))))

(map* inc (range 10))

;; My map implementation, using reduce
(defn map* [f coll]
  (reduce
   (fn [acc v] (conj acc (f v))) ; acc(umulator) and v(alue)
   []
   coll))

(map* inc (range 10))

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
(defn filter* [pred coll]
  (if (empty? coll)
    ()
    (if (pred (first coll))
      (cons (first coll) (filter* pred (rest coll)))
      (recur pred (rest coll)))))

(filter* even? (range 10))

;; Implementation with complete tail recursion:
(defn filter* [pred coll]
  ((fn [pred coll acc]
      (if (empty? coll)
        acc
        (if (pred (first coll))
          (recur pred (rest coll) (conj acc (first coll)))
          (recur pred (rest coll) acc))))
   pred
   coll
   []))

(filter* even? (range 10))

;; My cleaned up tail recursive function, using no lambda
(defn filter* [pred coll]
  (loop [coll coll acc []]
    (if (empty? coll)
      acc
      (if (pred (first coll))
        (recur (rest coll) (conj acc (first coll)))
        (recur (rest coll) acc)))))

(filter* even? (range 10))

;; My lazy filter implementation
(defn filter* [pred coll]
  (lazy-seq
   (if (empty? coll)
     ()
     (if (pred (first coll))
       (cons (first coll) (filter**** pred (rest coll)))
       (filter**** pred (rest coll))))))

(set! *print-length* 100)
(filter* even? (range))

;; My filter implementation using reduce

(defn filter* [pred coll]
  (reduce
   (fn [acc v]
     (if (pred v)
       (conj acc v)
       acc))
   []
   coll))

(filter* even? (range 10))

;;;;;;;;;;;;
;; REDUCE ;;
;;;;;;;;;;;;

(reduce + 0 [1 2 3])

;; My reduce implementation (only working for very base case easy scenarios)
(defn reduce* [f init coll]
  (loop [coll coll acc init]
    (if (empty? coll)
      acc
      (recur (rest coll) (f acc (first coll))))))

(reduce* + 0 [1 2 3])

;; Implementing map using reduce
(defn map* [f coll]
  (reduce
   (fn [acc v] (conj acc (f v)))
   []
   coll))

(map* inc (range 10))

;; Implementing filter using reduce
(defn filter* [pred coll]
  (reduce
   (fn [acc v]
     (if (pred v)
       (conj acc v)
       acc))
   []
   coll))

(filter* even? (range 10))


;;;;;;;;;;;;;;;
;; REDUCE-KV ;;
;;;;;;;;;;;;;;;


;; Increment all values of a map with reduce-kv
(reduce-kv
 (fn [m k v] (assoc m k (inc v)))
 {}
 {:a 1 :b 2})

;; Count all characters in the keys and in the values
(reduce-kv
 (fn [[kcount vcount] k v]
   [(+ kcount (count k))
    (+ vcount (count v))])
 [0 0]
 {"Foo" "Food"
  "Bar" "Bars"
  "Baz" "Bazaar"})

;; reduce-kv will call the function you provide with:
;; 1. The accumulator (a map in the first example, a tuple in the second)
;; 2. The next key in the map
;; 3. The accompanying value in the map

;; Reduce-kv with a vector as collection, will call your function with the
;; index of the vector as key and the value as value:
(reduce-kv
 (fn [m k v] (assoc m k v))
 {}
 (vec "ClojureRocks"))

(reduce-kv
 (fn [m k v] (assoc m k v))
 {}
 (vec (range 11 100 11)))


;;;;;;;;;;;;;;;;
;; REDUCTIONS ;;
;;;;;;;;;;;;;;;;

;; Reimplement the key- and value char counter with reduce
;; Note that the acc(umulator) is destructured into [kcount vcount]
;; and the v(alue) is destructured into [k v] since it's a map and not a seq.
(reduce
 (fn [[kcount vcount] [k v]]
   [(+ kcount (count k))
    (+ vcount (count v))])
 [0 0]
 {"foo" "food"
  "bar" "bars"
  "baz" "bazaar"})

;; Replace reduce with reductions and see the intermediate results:
(reductions
 (fn [[kcount vcount] [k v]]
   [(+ kcount (count k))
    (+ vcount (count v))])
 [0 0]
 {"foo" "food"
  "bar" "bars"
  "baz" "bazaar"})
