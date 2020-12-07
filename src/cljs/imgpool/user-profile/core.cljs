(ns imgpool.user-profile.core
  (:require
   [reagent.core :refer [atom]]
   [imgpool.user-profile.edit-account :refer [edit-account-form]]
   [imgpool.utility :refer [loader]]))

(defonce edit-account-state (atom {:show false :field "" :email "" :username "" :bio ""}))

(defonce user-state (atom {:email "jondoe@gmail.com" :username "jonnydoe123" :join-date "12-12-12" :favorites "" :post "" :bio "I am jonson doe."}))

(defn clear-values []
  (reset! edit-account-state {:show false :field "" :email "" :username "" :bio ""}))

(defn handle-change [event field]
  (swap! edit-account-state assoc (keyword field) (-> event .-target .-value)))

(defn handle-edit-submit [event]
  (.preventDefault event))

(defn reset-password [])

(defn handle-toggle-account-submit [event]
  (.preventDefault event))

(defn handle-favorites-click [event]
  (.preventDefault event))

(defn handle-posts-click [event]
  (.preventDefault event))

(defn user-profile [{:keys [is-admin is-init]}]
  [:section#account-dashboard.container.dashboard
   (comment "ToastContainer")
   [:div.inner
    [:h1
     [:span "User"]]
    [:div.left
     [:div.row
      [:h2 "Join Date"]
      [:p (:join-date @user-state)]]
     [:div.row
      [:h2 "Username"]
      [:p (:username @user-state)]
      (when is-admin
        [:button#edit-username {:on-click #(swap! edit-account-state assoc :show true :field "edit-username")} "edit"])]
     (if is-admin
       [:div.row
        [:h2 "Email"]
        [:p (:email @user-state)]
        [:button#edit-email {:on-click #(swap! edit-account-state assoc :show true :field "edit-email")} "edit"]]
       [:div.row
        [:h2 "Password"]
        [:p "hidden"]
        [:button#edit-password {:on-click reset-password} "reset"]])
     [:div.row.favorites
      [:h2 "Favorites"]
      [:p (str (count (:favorites @user-state)) " favorite" (when (> (count (:favorites @user-state)) 0) "s"))]
      [:button {:on-click handle-favorites-click} "view"]]
     [:div.row.posts
      [:h2 "Posts"]
      [:p (str (count (:posts @user-state)) " post" (when (> (count (:posts @user-state)) 0) "s"))]
      [:button {:on-click handle-posts-click} "view"]]
     [:div.row
      [:h2 "Bio"]
      [:p (:bio @user-state)]
      (when is-admin
        [:button#edit-bio {:on-click #(swap! edit-account-state assoc :show true :field "edit-bio")} "edit"])]]
    (when is-admin
      [:div.right
       [:button#toggle-account
        {:class (if (:active @user-state) "border-button-red" "border-button-green")
         :on-click handle-toggle-account-submit}
        (if (:active @user-state) "Disable" "Enable") " Account"]])
    (when is-admin [edit-account-form {:data @edit-account-state}])]
   [loader (not is-init)]])