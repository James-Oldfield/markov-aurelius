(ns markov-aurelius.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  :sample
  (fn [db]
    (:sample db)))
