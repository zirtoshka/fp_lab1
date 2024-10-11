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










;; 1. монолитные реализации с использованием:
;;     - хвостовой рекурсии;
;;     - рекурсии (вариант с хвостовой рекурсией не является примером рекурсии);
;; 2. модульной реализации, где явно разделена генерация последовательности, фильтрация и свёртка (должны использоваться функции reduce/fold, filter и аналогичные);
;; 3. генерация последовательности при помощи отображения (map);
;; 4. работа со спец. синтаксисом для циклов (где применимо);
;; 5. работа с бесконечными списками для языков, поддерживающих ленивые коллекции или итераторы как часть языка (к примеру Haskell, Clojure);

;;     - хвостовой рекурсии;

(defn sum-divisors-tail
  ([n]
   (sum-divisors-tail n 2 1))

  ([n i acc]
   (if (> i (inc (Math/sqrt n)))
     acc
     (if (zero? (mod n i))
       (if (== (Math/sqrt n) (/ n i))
         (recur n (inc i) (+ acc i))
         (recur n (inc i) (+ acc i (/ n i))))
       (recur n (inc i) acc)))))


(defn has-amicable-pare?
  [n sum-devisors-fn]
  (let [a (sum-devisors-fn n)]
    (and (not= n a)
         (= n (sum-devisors-fn a)))))

(defn sum-amicable-numbers-tail
  ([limit]
   (sum-amicable-numbers-tail limit 1 0))
  ([limit curr acc]
   (if (> curr limit)
     acc
     (if (has-amicable-pare? curr sum-divisors-tail)
       (recur limit (inc curr) (+ acc curr))
       (recur limit (inc curr) acc)))))

(sum-amicable-numbers-tail 10000)


;;     - рекурсии (вариант с хвостовой рекурсией не является примером рекурсии);

(defn sum-divisors-recursive
  ([n]
   (+ 1 (sum-divisors-recursive n 2)))
  ([n i]
   (if (> i (inc (Math/sqrt n)))
     0
     (if (zero? (mod n i))
       (if (== (Math/sqrt n) (/ n i))
         (+ i (sum-divisors-recursive n (inc i)))
         (+ i (/ n i) (sum-divisors-recursive n (inc i))))
       (sum-divisors-recursive n (inc i))))))


(defn sum-amicable-numbers-recursive
  ([n limit acc]
   (if (> n limit)
     acc
     (if (has-amicable-pare? n sum-divisors-recursive)
       (sum-amicable-numbers-recursive (inc n) limit (+ acc n))
       (sum-amicable-numbers-recursive (inc n) limit acc))))
  ([limit]
   (sum-amicable-numbers-recursive 1 limit 0)))

(sum-amicable-numbers-recursive 10000)



;; 2. модульной реализации, где явно разделена генерация последовательности, фильтрация и свёртка (должны использоваться функции reduce/fold, filter и аналогичные);
(defn sum-divisors-m
  [n]
  (reduce (fn [acc i]
            (if (zero? (mod n i))
              (if  (== (Math/sqrt n) (/ n i))
                (+ acc i)
                (+ acc i (/ n i)))
              acc))
          1
          (range 2 (inc (Math/sqrt n)))))

(defn get-amicable-numbers
  [seq]
  (filter  #(has-amicable-pare? % sum-divisors-m) seq))

(defn sum-amicable-numbers-m
  [limit]
  (reduce + (get-amicable-numbers (get-numbers limit))))

(sum-amicable-numbers-m 10000)


;; 3. генерация последовательности при помощи отображения (map);

(defn generate-sum-divisors
  [limit]
  (map (fn [n] [n (sum-divisors-tail n)]) (range 1 limit)))

(defn amicable-pair?
  [[a b] pairs]
  (and (not= a b)
       (= a (second (nth pairs (dec b) [b 0])))))

(defn sum-amicable-numbers-map
  [limit]
  (let [pairs (generate-sum-divisors limit)]
    (->> pairs
         (filter #(amicable-pair? % pairs))
         (map first)
         (distinct)
         (reduce +))))

(sum-amicable-numbers-map 10000)

;; 4. работа со спец. синтаксисом для циклов (где применимо);

(defn sum-divisors-loop
  [n]
  (loop [i 2
         sum 1]
    (if  (< i (inc (Math/sqrt n)))
      (if (zero? (mod n i))
        (if  (== (Math/sqrt n) (/ n i))
          (recur (inc i) (+ sum i))
          (recur (inc i) (+ sum i (/ n i))))
        (recur (inc i) sum))
      sum)))

(defn sum-amicable-numbers-loop
  [limit]
  (loop [n 1
         sum 0]
    (if (< n limit)
      (if (has-amicable-pare? n sum-divisors-loop)
        (recur (inc n) (+ sum n))
        (recur (inc n) sum))
      sum)))

(sum-amicable-numbers-loop 10000)


;; 5. работа с бесконечными списками для языков, поддерживающих ленивые коллекции или итераторы как часть языка (к примеру Haskell, Clojure);
