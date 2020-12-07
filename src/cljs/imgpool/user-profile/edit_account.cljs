(ns imgpool.user-profile.edit-account
  (:require
   [imgpool.utility :refer [modal]]))

(defn edit-account-form [{:keys [data clear-values handle-change handle-submit]}]
  [modal {:show (:show data) :toggle-modal clear-values}
   [:form#edit-form.form-light {:key 0 :on-submit handle-submit}
    [:div.field-container
     (when (= (:field data) "edit-username")
       [:input#username {:auto-complete "off"
                         :type "text"
                         :title "Username"
                         :name "username"
                         :value (:username data)
                         :placeholder "USERNAME"
                         :on-change #(handle-change % "username")}])
     (when (= (:field data) "edit-email")
       [:input#email {:auto-complete "off"
                      :type "text"
                      :title "Email"
                      :name "email"
                      :value (:email data)
                      :placeholder "EMAIL"
                      :on-change #(handle-change % "email")}])
     (when (= (:field data) "edit-bio")
       [:textarea#bio {:title "Bio"
                       :name "bio"
                       :value (:bio data)
                       :placeholder "BIO"
                       :on-change #(handle-change % "bio")}])]
    [:input.border-button {:type "submit"}]]])