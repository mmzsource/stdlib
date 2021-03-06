# stdlib

Playing around with functions from the clojure standard library to improve my
clojure programs and increase my speed of development.

## Installation

- JVM installed
- Leiningen installed

## Usage

Meant to be used with an interactive repl. Send the examples to an interactive
REPL and play with them to increase / refresh understanding and knowledge of a
function.

Also possible: read the examples (on github or in your IDE) which are mainly in
the core.clj file.

## Main things I learned

### functions

- using `comp` to travel a path in a map `(comp :c :b :a)`
- using `comp` in a filter to combine functions 'on the fly'
- using `complement` in a filter `(filter (complement zero?) [0 1 2])`
- using `constantly` to provide a value when a function is requested
- using `constantly` to produce the same value x times
- using `constantly` as a stub together with `with-redefs`
- using `fnil` to handle nil values coming from some external input
- using `fnil` when updating maps with keys that don't yet exist
- using `fnil` to overwrite unwanted default nil behaviour
- using `identity` to filter 'logically false' elements (`nil` or `false`)
- using `identity` to transform a map or vec into a sequence
- using `juxt` to easily create a map out of a sequence of values
- using `juxt` to maintain an unaltered version of a value along with its
  transformations
- using `juxt` for sorting on multiple criteria
- using `juxt` to easily create lookup maps
- using `juxt` to calculate stats on a sequence of numbers in one go
- using `juxt` for splitting a seq into 2 seqs: one with the values satisfying
  a pred and the other with the values that don't
- using `reduce-kv` to reduce an associative structure (like a map or a vec)
- using `reductions` to view intermediate results of `reduce`

### macros

Clojure 'verbs'

- functions (Defined with `defn`)
- macros (Defined with `defmacro`)
- special forms (Cannot be defined by user. Are defined by language. (e.g. def,
  if, do, let, quote, var, fn, loop, recur, throw, try)

Key difference between function and macros & special forms: all expressions
within the body are evaluated before the function body is evaluated.


## License

Use the code the way you want it at your own risk. It is not copyrighted.
