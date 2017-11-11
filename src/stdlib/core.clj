(ns stdlib.core)


;;;;;;;;;;;;;;;;
;; COMPLEMENT ;;
;;;;;;;;;;;;;;;;


;; takes a function f and returns a new function wrapping f to produce its
;; complement: (not (f))
(def not-empty? (complement empty?))
(not-empty? [])
(not-empty? [1 2 3])

;; My implementation
(defn complement* [f]
  (fn [& args] (not (apply f args))))

;; Use it for instance when filtering a collection with the complement of a pred
(filter (complement zero?) [0 1 2 3 0 4 5 6])
;; compare with:
(filter #(not (zero? %)) [0 1 2 3 0 4 5 6])
;; or compare with:
(filter (fn [nr] (not (zero? nr))) [0 1 2 3 0 4 5 6])

;; Used in the standard lib for implementing remove (which is the complement of filter)
(defn remove* [pred coll]
  (filter (complement pred) coll))
(remove* neg? [1 2 -3 4 -5 6])


;;;;;;;;;;;;;;;;
;; CONSTANTLY ;;
;;;;;;;;;;;;;;;;


;; Returns a function that always returns the given argument
((constantly 42))

;; My implementation
(defn constantly* [x] (fn [& _] x))

(def always42 (constantly 42))
(always42)
(always42 :a)
(always42 nil [] false "blah")

;; Use it when a lib requires a function, but you want to provide a value
;; So instead of:
(update [0 1 2 3] 3 (fn [x] (+ x 72)))
;; You provide a function that returns the value:
(update [0 1 2 3] 3 (constantly 75))

;; Use it when you want the same value x times in a sequence
(map (constantly "MM") (range 10))

;; Use it as a stub
(defn some-hard-to-setup-library-fn [& _] "return something complex")
(some-hard-to-setup-library-fn :x :& :y)
(with-redefs [some-hard-to-setup-library-fn (constantly "simple")]
  (= "simple" (some-hard-to-setup-library-fn "with" "complex" "params")))


;;;;;;;;;;;;;;
;; IDENTITY ;;
;;;;;;;;;;;;;;


;; Returns its argument
(identity 1)

;; My implementation
(defn identity* [x] x)

;; Used to filter 'logical false' elements (`nil` or `false`)
(filter identity [1 2 nil 4 false true :a])

;; Get the first non 'logical false' element
(some identity [nil false 1 2 3])

;; Split a map into its entries
(map identity {:a 1 :b 2 :c 3})

;; Transform a map into a sequence
(mapcat identity {:a 1 :b 2 :c 3})

;; Transform a vec into a sequence
(map identity [1 2 3 4 5])

;; Partition by identity:
(partition-by identity "abraacadaabra")
(partition-by identity (sort "abraacadaabra"))

;; Group by identity:
(group-by identity "abraacadaabra")


;;;;;;;;;;;;;;;;;
;; WITH-REDEFS ;;
;;;;;;;;;;;;;;;;;
