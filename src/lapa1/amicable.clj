(ns lapa1.amicable)


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


;;     - рекурсии (вариант с хвостовой рекурсией не является примером рекурсии);

(defn sum-divisors-recursive
  ([n]
   (sum-divisors-recursive n 2 1))
  ([n i acc]
   (let [sqrt-n (Math/sqrt n)]
     (if (> i (inc sqrt-n))
       acc
       (if (zero? (mod n i))
         (if (= sqrt-n (/ n i))  
           (sum-divisors-recursive n (inc i) (+ acc i))
           (sum-divisors-recursive n (inc i) (+ acc i (/ n i))))
         (sum-divisors-recursive n (inc i) acc))))))


(defn sum-amicable-numbers-recursive
  ([limit]
   (sum-amicable-numbers-recursive 1 limit 0))
  ([n limit acc]
   (if (> n limit)
     acc
     (if (has-amicable-pare? n sum-divisors-recursive)
       (sum-amicable-numbers-recursive (inc n) limit (+ acc n))
       (sum-amicable-numbers-recursive (inc n) limit acc)))))





;; 2. модульной реализации, где явно разделена генерация последовательности, фильтрация и свёртка (должны использоваться функции reduce/fold, filter и аналогичные);

(defn get-numbers
  [limit]
  (range 2 limit))

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


;; 5. работа с бесконечными списками для языков, поддерживающих ленивые коллекции или итераторы как часть языка (к примеру Haskell, Clojure);

(defn amicable-numbers-seq []
  (filter #(has-amicable-pare? % sum-divisors-m) (range 1 10000)))

(defn sum-amicable-numbers-lazy
  [limit]
  (reduce + (take-while #(< % limit) (amicable-numbers-seq))))

