(ns imgpool.admin-dashboard.user-form
  (:require
   [reagent-material-ui.core.form-control :refer [form-control]]
   [reagent-material-ui.core.select :refer [select]]
   [reagent-material-ui.core.input-label :refer [input-label]]
   [reagent-material-ui.core.menu-item :refer [menu-item]]
   [imgpool.utility :refer [modal]]))

(defn user-form [{:keys [users show toggle-show handle-select-user]}]
  [modal {:show show :toggle-modal toggle-show}
   [:form#admin-manage-form.form-light {:key 0}
    [form-control {:style {:width "100%"}}
     [input-label {:id "user-select"} "User"]
     [select {:label-id "user-select"
              :value ""
              :on-change #(handle-select-user (-> % .-target .-value))}
      [menu-item [:em "Select a user"]]
      (map (fn [user] [menu-item {:key (:id user) :value (:id user)} (:username user)]) users)]]]])