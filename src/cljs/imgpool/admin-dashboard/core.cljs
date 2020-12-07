(ns imgpool.admin-dashboard.core
  (:require
   [reagent.core :refer [atom]]
   [imgpool.admin-dashboard.tag-form :refer [tag-form]]
   [imgpool.admin-dashboard.user-form :refer [user-form]]
   [imgpool.utility :refer [loader]]))

(defonce users (atom [{:active true :bio "test" :favorited-posts [] :id 4 :join-date "2019-11-06T16:35:04.452Z" :post [] :username "jonny" :valid true}
                      {:active true :bio "test" :favorited-posts [] :id 5 :join-date "2019-11-06T16:35:04.452Z" :post [] :username "jane" :valid true}]))

(defonce tags (atom [{:id 20 :name "bla"}
                     {:id 20 :name "bla123"}
                     {:id 20 :name "bla321"}]))

(defonce show-user-form (atom false))

(defonce show-tag-form (atom false))

(defonce can-sign-up (atom true))

(defn toggle-signup [event]
  (.preventDefault event))

(defn handle-tag-submit [url tag-ids]
  (.log js/console [url tag-ids]))

(defn handle-select-user [id]
  (.log js/console id))

(defn admin-dashboard [{:keys [user flags]}]
  [:section#account-dashboard.container.dashboard
   (comment "TastContainer")
   (when (and (:is-init user) (:logged-in user))
     [:div.inner
      [:h1
       [:span "Admin"]]
      [:div.left
       [:h2 "Flags"]
       [:div.row
        [:p (if (> (count flags) 0) (count flags) 0)]
        (when (< (count flags) 0)
          [:a#show-flags {:href "/flags"}])]
       [:h2 "Users"]
       [:div.row
        [:p (if (> (count @users) 0) (count @users) 0)]
        (when (> (count @users) 0)
          [:button#show-users {:on-click #(reset! show-user-form true)} "manage"])]
       [:h2 "Tags"]
       [:div.row
        [:p (if (> (count @tags) 0) (count @tags) 0)]
        (when (> (count @tags) 0)
          [:button#show-tags {:on-click #(reset! show-tag-form true)} "manage"])]]
      [:div.right
       [:button#toggle-signup.border-button {:on-click toggle-signup} (if @can-sign-up "Disable" "Enable") " Signups"]]
      [tag-form {:tags @tags :show @show-tag-form :toggle-show #(reset! show-tag-form (not @show-tag-form)) :handle-submit handle-tag-submit}]
      [user-form {:users @users :show @show-user-form :toggle-show #(reset! show-user-form (not @show-user-form)) :handle-select-user handle-select-user}]])
   [loader (not (:is-init user))]])