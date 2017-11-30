(ns markov-aurelius.events
 (:require [re-frame.core :as re-frame]
           [clojure.string :as str]
           [clojure.set :as set]
           [markov-aurelius.data :refer [data]]
           [markov-aurelius.db :as db]))

;;
;; Tutorial from http://howistart.org/posts/clojure/1/
;;

(def output_char_count 300)

; Order of the marjov (number of previous words the next prediction is generated from)
(def markov_order 2)


(defn- seq->string
  "append a blank space to each element in the seq + concat"
  [coll]
  (apply str (interpose " " coll)))


(defn- partition_string
  "Take a string and return a partitioned-list,
  as a function of markov_order"
  [s]
  (partition-all (+ 1 markov_order) 1 (str/split s #"[\s|\n]")))


(defn- generate_chain
  "create and return a map of markov_order-length terms and their suffixes
  representing the possible state transitions"
  [string]
  (reduce
    (fn [suffixes terms]
      (merge-with set/union suffixes
                  (let [[t1 t2 t3] terms]
                    {[t1 t2] (if t3 #{t3} #{})})))
    {}
    string))


(defn- walk_chain
  "return the result when there are no suffixes,
  else pick a random prefix and recur with the new result"
  [prefix chain result]
  (let [suffixes (get chain prefix)]
    (if (empty? suffixes)
      result
      (let [suffix (first (shuffle suffixes))
            new_prefix [(last prefix) suffix]
            result_with_spaces (seq->string result)
            result_char_count (count result_with_spaces)
            suffix_char_count (inc (count suffix))
            new_result_char_count (+ result_char_count suffix_char_count)]
        (if (>= new_result_char_count 140)
          result
          (recur new_prefix chain (conj result suffix)))))))


(defn- data->chain
  "Parses the dataset as a markov chain"
  []
  (generate_chain (partition_string data)))


(defn- generate_text
  [start chain]
  (let [prefix (str/split start #" ")
        result_chain (walk_chain prefix chain prefix)
        result_text (seq->string result_chain)]
    (first (str/split result_text #"  "))))

(defn sample
  "Takes a seed and returns a sample from a random walk along the chain"
  [seed]
  (generate_text seed (data->chain)))

(re-frame/reg-event-db
  :initialize-db
  (fn  [_ _]
    db/default-db))


;; Grab a random seed and sample from the MC using it
(re-frame/reg-event-db
  :sample-markov
  (fn [db [_ _]]
    (let [seeds (:seeds db)
          random-seed (rand-nth (seq seeds))
          result (sample random-seed)]
      (assoc db :sample result))))
