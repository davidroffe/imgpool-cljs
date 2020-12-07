(ns imgpool.account.edit-account
  (:require
   [imgpool.utility :refer [modal]]))

(defn edit-account-form [{:keys [clear-values handle-submit handle-change data]}]
  [modal {:show (:show data) :toggle-modal clear-values}
   [:form#edit-form.form-light {:key 0 :on-submit handle-submit}
    [:div.field-container
     (when (= (:field data) "edit-username")
       [:input {:id "username"
                :auto-complete "off"
                :type "text"
                :title "Username"
                :name "username"
                :value (:username data)
                :placeholder "USERNAME"
                :on-change #(handle-change % "edit-account-state" "username")}])
     (when (= (:field data) "edit-email")
       [:input {:id "email"
                :auto-complete "off"
                :type "text"
                :title "Email"
                :name "email"
                :value (:email data)
                :placeholder "EMAIL"
                :on-change #(handle-change % "edit-account-state" "email")}])
     (when (= (:field data) "edit-password")
       [:div
        [:input {:id "password"
                 :auto-complete "off"
                 :type "password"
                 :title "Password"
                 :name "password"
                 :value (:password data)
                 :placeholder "PASSWORD"
                 :on-change #(handle-change % "edit-account-state" "password")}]
        [:input {:id "password-confirm"
                 :auto-complete "off"
                 :type "password"
                 :title "Password Confirm"
                 :name "password-confirm"
                 :value (:password-confirm data)
                 :placeholder "CONFIRM PASSWORD"
                 :on-change #(handle-change % "edit-account-state" "password-confirm")}]])
     (when (= (:field data) "edit-bio")
       [:textarea {:id "bio"
                   :title "Bio"
                   :name "bio"
                   :value (:bio data)
                   :placeholder "BIO"
                   :on-change #(handle-change % "edit-account-state" "bio")}])]
    [:input.border-button {:type "submit"}]]])