(ns imgpool.post-list
  (:require
   [reagent.core :refer [atom]]
   [imgpool.tag-menu :refer [tag-menu]]
   [imgpool.paginator :refer [paginator]]
   [imgpool.utility :refer [loader]]))

(defn change-page [])

(def is-loading (atom false))

(defn post-list-item [{:keys [key]}]
  (fn []
    [:a.post-item {:href (str "/post/" key)}
     [:img {:src "https://via.placeholder.com/200" :alt "tag names"}]]))

(defn post-list [{:keys [show-tag-menu toggle-menu]}]
  [:div
   [tag-menu {:show show-tag-menu :toggle-menu toggle-menu}]
   (when @is-loading [:section#post-list
                      [loader @is-loading]])
   [:section#post-list
    (map (fn [index] [post-list-item {:key index}]) (range 1 31))]
   [paginator change-page 1 1 false]])
