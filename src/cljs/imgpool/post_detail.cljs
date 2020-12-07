(ns imgpool.post-detail
  (:require
   [reagent.core :as reagent :refer [atom]]
   [imgpool.tag-menu :refer [tag-menu]]
   [imgpool.utility :refer [loader]]))

(defonce is-loading (atom false))
(defonce flag-post (atom {:show false :reason ""}))
(def user-id false)
(def is-uploader false)
(def is-admin false)
(def options-menu false)

(defn set-is-loading [loading]
  (reset! is-loading loading))

(defn toggle-menu [event]
  (.preventDefault event))

(defn handle-menu-click [event]
  (.preventDefault event))

(defn is-favorited [event]
  (.preventDefault event))

(defn toggle-favorite [event]
  (.preventDefault event))

(defn delete-post [event]
  (.preventDefault event))

(defn post-options-menu []
  [:div
   [:button.toggle-options {:on-click toggle-menu} "options"
    [:span "+"]]
   [:ul {:class (str "options" (when options-menu " active")) :on-click handle-menu-click}
    [:li
     [:button {:class (str "toggle-fav" (when is-favorited "favorited")) :on-click toggle-favorite}
      [:span.icon "&hearts;"]
      [:span.text.add "add to favorites"]
      [:span.text.remove "remove from favorites"]]]
    [:li
     [:button.flag-post {:on-click (fn [] (swap! flag-post assoc :show true))}
      [:span.icon.flag "&#9873;"]
      [:span.text "flag post"]]]
    (when (or is-uploader is-admin) [:li
                                     [:button.delete-post {:on-click delete-post}
                                      [:span.icon.x "×"]
                                      [:span.text "delete post"]]])]])

(defn post-detail []
  (fn []
    [:section#post-single.container
     (when @is-loading [loader @is-loading])
     (comment "[toast-container]")
     [tag-menu]
     [:div.image-container
      [:div.inner
       (when @is-loading
         [:div.post-info
          (when user-id
            [post-options-menu])
          [:p.poster "posted by: "
           [:a {:href (str "/user/" (comment "post.user.username"))}]]])
       [:img {:src "https://via.placeholder.com/800" :on-load (fn [] (set-is-loading false))}]]]]))