(ns stdlib.macros)

;; Read - Eval - Print : There is space between Read and Eval:
(read-string "(+ 3 4 5)")
(eval (read-string "(+ 3 4 5)"))
(eval (cons (read-string "*") (rest (read-string "(+ 3 4 5)"))))
(eval (cons (quote *) (rest (quote (+ 3 4 5)))))
(eval (cons '* (rest '(+ 3 4 5))))
