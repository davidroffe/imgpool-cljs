(ns imgpool.account.core
  (:require
   [reagent.core :refer [atom]]
   [imgpool.utility :refer [loader]]
   [imgpool.account.edit-account :refer [edit-account-form]]
   [imgpool.account.create-post :refer [create-post-form]]))

(def edit-account-state (atom {:show false :field "" :email "" :username "" :password "" :password-confirm "" :bio ""}))

(def create-post-state (atom {:show false :file {:value {} :name ""} :source "" :tags ""}))

(def user-init true)

(def logged-in true)

(def username)

(def email)

(def password)

(def bio)

(defn logout [event]
  (.preventDefault event))

(defn clear-values []
  (reset! edit-account-state {:show false :field "" :email "" :username "" :password "" :password-confirm "" :bio ""})
  (reset! create-post-state {:show false :file {:value {} :name ""} :source "" :tags ""}))

(defn handle-change [event form field]
  (.preventDefault event)
  (case form
    "edit-account" (swap! edit-account-state assoc (keyword field) (-> event .-target .-value))
    "create-post" (if (= field "file")
                    (swap! create-post-state assoc (keyword field) {:name (-> event .-target .-value) :value (first (-> event .-target .-files))})
                    (swap! create-post-state assoc (keyword field) (-> event .-target .-value)))))

(defn handle-edit-submit [event]
  (.preventDefault event))

(defn handle-create-post-submit [event]
  (.preventDefault event))

(defn account [{:keys [user]}]
  [:section#account-dashboard.container.dashboard
   (comment "ToastContainer")
   (if (and user-init logged-in)
     [:div.inner
      [:h1
       [:span "Account"]]
      [:div.left
       [:h2 "Username"]
       [:div.row
        [:p (:username user)]
        [:button#edit-username {:on-click #(swap! edit-account-state assoc :show true :field "edit-username")} "edit"]]
       [:h2 "Email"]
       [:div.row
        [:p (:email user)]
        [:button#edit-email {:on-click #(swap! edit-account-state assoc :show true :field "edit-email")} "edit"]]
       [:h2 "Password"]
       [:div.row
        [:p "hidden"]
        [:button#edit-password {:on-click #(swap! edit-account-state assoc :show true :field "edit-password")} "edit"]]
       [:h2 "Bio"]
       [:div.row
        (when (:bio user) [:p (:bio user)])
        [:button#edit-bio {:on-click #(swap! edit-account-state assoc :show true :field "edit-bio")} "edit"]]]
      [:div.right
       [:button#create-post.border-button {:on-click #(swap! create-post-state assoc :show true)} "Create Post"]
       [:button#logout.border-button {:on-click logout} "Log Out"]]
      [edit-account-form {:data @edit-account-state}]
      [create-post-form {:data @create-post-state}]]
     [loader (and (not user-init) (not logged-in))])])