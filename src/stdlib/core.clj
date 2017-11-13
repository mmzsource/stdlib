(ns stdlib.core)


;;;;;;;;;;
;; COMP ;;
;;;;;;;;;;


;; Function COMPosition
;; Produces a function which is the composition of the given functions
(filter (comp not zero?) [0 1 0 2 0 3 0 4])
((comp str +) 8 8 8)
((comp :c :b :a) {:a {:b {:c "leaf"}}})

;; My implementation (for only 2 functions)
(defn comp* [f g]
  (fn [& args] (f (apply g args))))

;; Use it to travel a path in a map. So:
((comp :c :b :a) {:a {:b {:c "leaf"}}})
;; instead of
(:c (:b (:a {:a {:b {:c "leaf"}}})))

;; Use it to combine any 2 functions
((comp - /) 1 2)

;; Use it in a filter
(filter (comp not nil?) [1 nil 2 nil 3 nil nil])

;; Or more sophisticated:
(def countif (comp count filter))
(countif even? [1 2 3 4 5])
;; Or:
(def count-if-even (comp count (partial filter even?)))
(count-if-even [1 2 3 4 5])


;;;;;;;;;;;;;;;;
;; COMPLEMENT ;;
;;;;;;;;;;;;;;;;


;; Takes a function f and returns a new function wrapping f to produce its
;; complement: (not (f))
(def not-empty? (complement empty?))
(not-empty? [])
(not-empty? [1 2 3])

;; My implementation
(defn complement* [f]
  (fn [& args] (not (apply f args))))

;; Use it for instance when filtering a collection with the complement of a pred
(filter (complement zero?) [0 1 2 3 0 4 5 6])
;; Compare with:
(filter #(not (zero? %)) [0 1 2 3 0 4 5 6])
;; Or compare with:
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


;;;;;;;;;;
;; FNIL ;;
;;;;;;;;;;


;; Decorates another function:
;; replaces first argument(s) which have value `nil` with a default value
(def inc* (fnil inc 0))
(inc nil)
(inc* nil)

(def plus (fnil + 0 0 0))
(+ 2 3 nil 4)
(plus 2 3 nil 4)

;; My (one default arg) implementation
(defn fnil* [f default]
  (fn [x & xs]
    (if (nil? x)
      (apply f default xs)
      (apply f x xs))))

;; Use it to handle a nil value coming from some input form:
(defn say-hello [name] (str "Hello " name))
(def say-hello+ (fnil say-hello "World"))
(say-hello+ nil)

;; Use it for specifying a default value when updating maps
;; for instance for a map containing counters of keys:
(update-in {:a 1} [:a] inc)
(update-in {:a 2} [:b] (fnil inc 0))

;; Use it to overwrite unwanted default nil behaviour
;; for instance `conj` with nil produces a list:
(conj nil 1)
;; but I want it to be a vector:
((fnil conj []) nil 1)


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


;;;;;;;;;;
;; JUXT ;;
;;;;;;;;;;

;; Apply multiple functions (left-to-right) to the args
;; ((juxt a b c) x) => [(a x) (b x) (c x)]
((juxt :a :c) {:a "A" :b "B" :c "C"})

;; My implementation (only for '1 arg' function)
(defn juxt* [& fs]
  (fn [x] (mapv (fn [f] (f x)) fs)))

;; Use it to easily pull out elements from a sequence:
((juxt first second last) (range 10))

;; Or pull values from a map:
((juxt :first :last) {:first "Maarten" :last "Metz" :email "bla" :etc "etc"})

;; Use it to setup a map easily
;; First function arg will create the keys, second will create the values
(into {} (map (juxt identity str) (range 10)))

;; Expanding on the previous example, in general, with juxt you can maintain
;; an unaltered version of a value along with its transformations:
(def words ["The" "Clojure" "Standard" "Library" "is" "amazing"])
(map (juxt identity count) words)

;; Very elegant solution I found in The Clojure Standard Lib book for finding
;; neighbours in a 2D grid:
(def dim #{0 1 2 3 4});

(defn up    [[x y]] [x (dec y)])
(defn down  [[x y]] [x (inc y)])
(defn left  [[x y]] [(dec x) y])
(defn right [[x y]] [(inc x) y])

(defn valid? [[x y]]
  (and (contains? dim x)
       (contains? dim y)))

(defn neighbours [cell]
  (filter valid?
          ((juxt up down left right) cell)))

(neighbours [2 1])
(neighbours [0 0])

;; You can also use juxt for sorting on multiple criteria
(sort-by count ["ah" "this" "is" "super" "awesome"])
(sort-by str   ["ah" "this" "is" "super" "awesome"])
(sort-by (juxt count str) ["ah" "this" "is" "super" "awesome"])
(sort-by (juxt :a :b) [{:a 1 :b 3} {:a 1 :b 2} {:a 2 :b 1}])

;; Use it to create lookup maps
(def seq-to-index [{:id 1 :name "one"} {:id 2 :name "two"} {:id 3 :name "three"}])
(defn index-by [keyname coll]
  (into {} (map (juxt keyname identity) coll)))
(index-by :id   seq-to-index)
(index-by :name seq-to-index)

;; Use it to calculate 'stats' on a sequence in one go:
((juxt + * min max) 3 5 7)

;; Use it to segregate values in a collection
((juxt (partial filter even?) (partial filter odd?)) (range 10))



;;;;;;;;;;;;;
;; PARTIAL ;;
;;;;;;;;;;;;;


;; Partial application of a function
;; Also called currying in some languages
;; Partial is 'positional' and captures arguments starting from the left
(def one-over (partial / 1))
(one-over 5)

(def incrementer (partial + 1))
(incrementer 2 3)

;; My implementation
(defn partial* [f & xs]
  (fn [& ys]
    (apply f (concat xs ys))))

;; Use it when you know the value of the first argument(s) to a function
(map (partial / 1) [1 2 3 4 5 6])

;; Supercool usage example on clojuredocs:
(def to-english (partial clojure.pprint/cl-format nil "~@(~@[~R~]~^ ~A.~)"))
(to-english 1234567890)


;;;;;;;;;;;;;
;; SORT-BY ;;
;;;;;;;;;;;;;





;;;;;;;;;;;;;;;;;
;; WITH-REDEFS ;;
;;;;;;;;;;;;;;;;;
