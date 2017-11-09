(ns stdlib.core)

;;;;;;;;;;;;;;
;; IDENTITY ;;
;;;;;;;;;;;;;;

;; Returns its argument
(identity 1)

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
