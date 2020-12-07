(ns imgpool.prod
  (:require [imgpool.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
