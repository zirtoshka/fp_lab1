# Лабораторная работа №1

## Титульный лист

**Студент**: Зайцева Ирина Сергеевна

**Группа**: P3309

**ИСУ**: 367222

## Описание проблемы

В лабораторной работе реализуются задачи Эйлера различными способами, используя язык программирования Clojure. Задачи включают:

1. **Нахождение суммы простых чисел меньше определённого предела [(задача 10)](https://projecteuler.net/problem=10)**.
2. **Нахождение суммы дружественных чисел меньше определённого предела [(задача 21)](https://projecteuler.net/problem=21)**.



## Ключевые элементы реализации

### Задача 10
1. **Решение с использованием хвостовой рекурсии**

Хвостовая рекурсия позволяет реализовать эффективные рекурсивные функции, где последнее действие функции — это вызов самой себя. 

```clojure
(defn sum-primes-tail
  ([n] (sum-primes-tail 2 0 n))
  ([curr sum n]
   (if (>= curr n)
     sum
     (if (prime? curr)
       (recur (inc curr) (+ sum curr) n)
       (recur (inc curr) sum n)))))
```

2. **Модульная реализация**

Модульная реализация разделяет задачу на несколько этапов: генерация последовательности, фильтрация и свёртка. 

```clojure
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
```

3. **Использование отображения (`map`)**

Функция `map` применяется для генерации последовательности значений.

```clojure
(defn get-primes-map
  [limit]
  (map #(if (prime-m? %)
          %
          nil)
       (range 2 limit)))
```

4. **Специальные синтаксические конструкции для циклов**

Использование конструкций `loop` и `for` для циклических операций:

```clojure
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
```
```clojure
(defn sum-primes-for
  [limit]
  (reduce + (for [n (range 2 limit)
                  :when (prime? n)]
              n)))
```

5. **Работа с бесконечными последовательностями (ленивые коллекции)**

Clojure поддерживает ленивые коллекции, что позволяет работать с бесконечными последовательностями.

Пример:

```clojure
(def get-primes-lazy
  (filter prime-m? (iterate inc 2)))

(defn sum-primes-lazy
  [limit]
  (reduce + (take-while #(< % limit) get-primes-lazy)))
```

### Задача 21
1. **Решение с использованием хвостовой рекурсии**

```clojure
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

(defn sum-amicable-numbers-tail
  ([limit]
   (sum-amicable-numbers-tail limit 1 0))
  ([limit curr acc]
   (if (> curr limit)
     acc
     (if (has-amicable-pare? curr sum-divisors-tail)
       (recur limit (inc curr) (+ acc curr))
       (recur limit (inc curr) acc)))))
```
2. **Решение с использованием рекурсии**
```clojure
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
````       

3. **Модульная реализация**

Модульная реализация разделяет задачу на несколько этапов: генерация последовательности, фильтрация и свёртка. 

```clojure
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
```

3. **Использование отображения (`map`)**

Функция `map` применяется для генерации последовательности значений.

```clojure
(defn generate-sum-divisors
  [limit]
  (map (fn [n] [n (sum-divisors-tail n)]) (range 1 limit)))
```

4. **Специальные синтаксические конструкции для циклов**

Использование конструкции `loop` для циклических операций:

```clojure
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
```

5. **Работа с бесконечными последовательностями (ленивые коллекции)**

Clojure поддерживает ленивые коллекции, что позволяет работать с бесконечными последовательностями.

Пример:

```clojure
(defn amicable-numbers-seq []
  (filter #(has-amicable-pare? % sum-divisors-m) (range 1 10000)))

(defn sum-amicable-numbers-lazy
  [limit]
  (reduce + (take-while #(< % limit) (amicable-numbers-seq))))
```

## Выводы

В ходе данной лабораторной работы я освоила основы языка Clojure применила полученные знаниея на практике, реализовав решение задач разными способами. Хочется отметить, что хвостовая рекурсия и ленивые коллекций позволили обрабатывать большие объемы данных, избегая переполнения стека и чрезмерного потребления памяти. Очень удобно работать с REPL и видеть результаты работы функций сразу после их написания.