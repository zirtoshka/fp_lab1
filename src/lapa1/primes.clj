(ns lapa1.primes)

;; 1. монолитные реализации с использованием:
;;     - хвостовой рекурсии;
;;     - рекурсии (вариант с хвостовой рекурсией не является примером рекурсии);
;; 2. модульной реализации, где явно разделена генерация последовательности, фильтрация и свёртка (должны использоваться функции reduce/fold, filter и аналогичные);
;; 3. генерация последовательности при помощи отображения (map);
;; 4. работа со спец. синтаксисом для циклов (где применимо);
;; 5. работа с бесконечными списками для языков, поддерживающих ленивые коллекции или итераторы как часть языка (к примеру Haskell, Clojure);


;;     - хвостовой рекурсии;
(defn divisible?
  "Check a%b==0"
  [a b]
  (= 0 (mod a b)))

(defn prime?
  "Check a is prime?"
  ([a] (prime? a 2))
  ([a divisor]
   (cond
     (> (* divisor divisor) a) true
     (divisible? a divisor) false
     :else (recur a (inc divisor)))))

(defn sum-primes-tail
  "get sum of primes that less then n"
  ([n] (sum-primes-tail 2 0 n))
  ([curr sum n]
   (if (>= curr n)
     sum
     (if (prime? curr)
       (recur (inc curr) (+ sum curr) n)
       (recur (inc curr) sum n)))))


;; 2. модульной реализации, где явно разделена генерация последовательности, фильтрация и свёртка (должны использоваться функции reduce/fold, filter и аналогичные);

(defn get-numbers
  [limit]
  (range 2 limit))

(defn prime-m?
  [a]
  (cond
    (< a 2) false
    (= a 2) true
    :else (not-any? #(divisible? a %)
                    (range 2 (inc (Math/sqrt a))))))

(defn get-primes
  [numbers]
  (filter prime-m? numbers))

(defn sum-primes-m
  [limit]
  (reduce + (get-primes (get-numbers limit))))


;; 3. генерация последовательности при помощи отображения (map);

(defn get-primes-map
  [limit]
  (map #(if (prime-m? %)
          %
          nil)
       (range 2 limit)))

(defn sum-primes-map
  [limit]
  (reduce + (filter some? (get-primes-map limit))))


;; 4. работа со спец. синтаксисом для циклов (где применимо);

(defn sum-primes-loop
  [limit]
  (loop [n 2
         sum 0]
    (if (>= n limit)
      sum
      (recur (inc n)
             (if (prime? n)
               (+ sum n)
               sum)))))


(defn sum-primes-for
  [limit]
  (reduce + (for [n (range 2 limit)
                  :when (prime? n)]
              n)))


;; 5. работа с бесконечными списками для языков, поддерживающих ленивые коллекции или итераторы как часть языка (к примеру Haskell, Clojure);

(def get-primes-lazy
  (filter prime-m? (iterate inc 2)))

(defn sum-primes-lazy
  [limit]
  (reduce + (take-while #(< % limit) get-primes-lazy)))