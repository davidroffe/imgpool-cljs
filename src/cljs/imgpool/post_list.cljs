(ns imgpool.post-list
  (:require
   [reagent.core :refer [atom]]
   [imgpool.tag-menu :refer [tag-menu]]
   [imgpool.paginator :refer [paginator]]
   [imgpool.utility :refer [loader]]
   [env-vars.core :refer [image-host]]))

(defn change-page [])

(def is-loading (atom false))

(defn post-list-item [{:keys [post key]}]
  (fn []
    [:a.post-item {:href (str "/post/" (:id post))}
     [:img {:src (str image-host (:thumb-url post)) :alt "tag names"}]]))

(defn post-list [{:keys [posts show-tag-menu toggle-menu]}]
  [:div
   [tag-menu {:show show-tag-menu :toggle-menu toggle-menu}]
   (when @is-loading [:section#post-list
                      [loader @is-loading]])
   [:section#post-list
    (map (fn [post] [post-list-item {:post post}]) posts)]
   [paginator change-page 1 1 false]])
