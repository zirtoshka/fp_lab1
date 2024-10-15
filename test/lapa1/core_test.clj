(ns lapa1.core-test
  (:require [clojure.test :refer [deftest is]]
            [lapa1.primes :as primes]
            [lapa1.amicable :as amicable]))

(def prime-limit 2000000)
(def amicable-limit 10000)
(def expected-prime-sum 142913828922)
(def expected-amicable-sum 31626)

(deftest test-primes-tail
  (is (= (primes/sum-primes-tail prime-limit) expected-prime-sum)))
(deftest test-primes-m
  (is (= (primes/sum-primes-m prime-limit) expected-prime-sum)))
(deftest test-primes-map
  (is (= (primes/sum-primes-map prime-limit) expected-prime-sum)))
(deftest test-primes-loop
  (is (= (primes/sum-primes-loop prime-limit) expected-prime-sum)))
(deftest test-primes-for
  (is (= (primes/sum-primes-for prime-limit) expected-prime-sum)))
(deftest test-primes-lazy
  (is (= (primes/sum-primes-lazy prime-limit) expected-prime-sum)))

(deftest test-amicable-tail
  (is (= (amicable/sum-amicable-numbers-tail amicable-limit) expected-amicable-sum)))
(deftest test-amicable-m
  (is (= (amicable/sum-amicable-numbers-m amicable-limit) expected-amicable-sum)))
(deftest test-amicable-map
  (is (= (amicable/sum-amicable-numbers-map amicable-limit) expected-amicable-sum)))
(deftest test-amicable-loop
  (is (= (amicable/sum-amicable-numbers-loop amicable-limit) expected-amicable-sum)))
(deftest test-amicable-lazy
  (is (= (amicable/sum-amicable-numbers-lazy amicable-limit) expected-amicable-sum)))

(deftest test-amicable-recursive
  (is (= (amicable/sum-amicable-numbers-recursive 1000) 504)))