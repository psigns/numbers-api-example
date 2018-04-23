(ns number-facts.core
    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require
      [cljs-http.client :as http]
      [cljs.core.async :refer [<!]]
      [reagent.core :as r]))

(defonce input-value (r/atom ""))
(defonce submitted-value (r/atom nil))
(defonce is-loading (r/atom false)) 
(defonce trivia (r/atom "")) 

(defn get-random-url
  "Gets the random api url"
  []
  (clojure.string/join (list "http://numbersapi.com/" "random/trivia?json")))

(defn get-number-url
  "Gets the numbers api url for a given number"
  [number, type]
  (clojure.string/join (list "http://numbersapi.com/" number "/" type "?json")))

(defn number-input [{ :keys [ on-change on-random on-submit value ]}]
  [:div
    [:input {
      :on-change on-change
      :type "number"
      :value value }]
    [:button { :disabled (= @input-value "") :on-click on-submit } "Submit"]
    [:button { :on-click on-random } "Random"]])

(defn number-facts [{ :keys [ number trivia ]}]
  [:div
    [:h3 number (if-not (= number nil) '(" Trivia") '(""))]
    [:p trivia]])

(defn load-url [url] (do 
  (reset! is-loading true)
  (go (let [response (<! (http/get url {:with-credentials? false}))]
         (do
           (reset! trivia (:text (:body response)))
           (reset! submitted-value (:number (:body response)))
           (reset! is-loading false))))))

(defn handle-number-submit [number] (do 
    (load-url (get-number-url number "trivia"))
    (reset! submitted-value number)
  ))

(defn number-facts-container []
  [:div
    [:h2 "Welcome to Number Facts"]
    [:p "Enter a number and click search, or click random."]
    [number-input {
      :on-change #(reset! input-value (-> % .-target .-value))
      :on-random #(load-url (get-random-url))
      :on-submit #(handle-number-submit @input-value)
      :value @input-value }]
    [number-facts { :number @submitted-value :trivia @trivia }]])
    
(defn mount-root []
  (r/render [number-facts-container] (.getElementById js/document "app")))

(defn init! [] (do 
  (mount-root)
))
