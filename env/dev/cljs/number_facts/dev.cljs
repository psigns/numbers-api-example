(ns ^:figwheel-no-load number-facts.dev
  (:require
    [number-facts.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
