(ns markov-aurelius.views
  (:require [re-frame.core :refer [subscribe dispatch]]))

;; Button component to sample from chain
(defn sample-button
  []
  (fn []
    [:button {:on-click #(dispatch [:sample-markov])} "generate"]))

(defn main-panel []
  (let [sample (subscribe [:sample])]
    (fn []
      [:div {:class "container"}
       [:h1 "Markov Aurelius"]
       [:p "Markov Chain built from Marcus Aurelius' "
        [:em "'Meditations'"]]
       [:hr]
       [sample-button]
       (if (< 0 (count @sample))
         [:blockquote {:style {:margin-top "25px"}}
          @sample]
         nil)])))
