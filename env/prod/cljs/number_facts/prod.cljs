(ns number-facts.prod
  (:require
    [number-facts.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
