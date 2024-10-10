(ns lapa1.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(-main)


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


(defn sum-primes
  "get sum of primes that less then n"
  ([n] (sum-primes 2 0 n))
  ([curr sum n]
   (if (>= curr n)
     sum
     (if (prime? curr)
       (recur (inc curr) (+ sum curr) n)
       (recur (inc curr) sum n)))))


(divisible? 10 3)
(prime? 11)
(sum-primes 2000)



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
  [primes]
  (reduce + primes))

(sum-primes-m (get-primes (get-numbers 2000)))


;; 3. генерация последовательности при помощи отображения (map);
(defn get-primes-map
  [limit]
  (map #(if (prime-m? %)
          %
          nil)
       (range 2 limit)))

(defn sum-primes-for-map
  [limit]
  (reduce + (filter some? (get-primes-map limit))))
(sum-primes-for-map 2000)


;; 4. работа со спец. синтаксисом для циклов (где применимо);

(defn sum-primes-for-loop
  [limit]
  (loop [n 2
         sum 0]
    (if (>= n limit)
      sum
      (recur (inc n)
             (if (prime? n)
               (+ sum n)
               sum)))))

(sum-primes-for-loop 2000)


(defn sum-primes-for
  [limit]
  (reduce + (for [n (range 2 limit)
                  :when (prime? n)]
              n)))
(sum-primes-for 2000)

;; 5. работа с бесконечными списками для языков, поддерживающих ленивые коллекции или итераторы как часть языка (к примеру Haskell, Clojure);

(def get-primes-lazy
  (filter prime-m? (iterate inc 2)))

(defn sum-primes-lazy
  [limit]
  (reduce + (take-while #(< % limit) get-primes-lazy)))


(sum-primes-lazy 2000)

 
 


