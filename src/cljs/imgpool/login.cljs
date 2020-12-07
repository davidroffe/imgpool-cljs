(ns imgpool.login
  (:require
   [reagent.core :as reagent :refer [atom]]))

(defonce form (atom "login"))
(defonce can-sign-up (atom true))
(defonce email (atom ""))
(defonce username (atom ""))
(defonce password (atom ""))
(defonce password-confirm (atom ""))

(defn switch-form [event]
  (.preventDefault event)
  (if (= (.. e -target -id) "forgot-password")
    (reset! form "forgot-password")
    (reset! form (if (= @form "login") "signup" "login"))))

(defn handle-submit [event]
  (.preventDefault event))

(defn login []
  (fn []
    [:div#account-center
     (comment "ToastContainer")
     [:div#center-box
      [:form.form-dark {:on-submit handle-submit}
       [:div.field-container
        [:input#email {:auto-complete "off" :type "text" :title "Full Name" :name "email" :value @email :placeholder "EMAIL" :on-change #(reset! email (-> % .-target .-value))}]
        (when (= @form "signup")
          [:input#username {:auto-complete "off" :type "text" :title "Username" :name "username" :value @username :placeholder "USERNAME" :on-change #(reset! username (-> % .-target .-value))}])
        (when (or (= @form "signup") (= @form "login"))
          [:input#password {:auto-complete "off" :type "password" :title "Password" :name "password" :value @password :placeholder "PASSWORD" :on-change #(reset! password (-> % .-target .-value))}])
        (when (= @form "signup")
          [:input#passwordConfirm {:auto-complete "off" :type "password" :title "Password Confirm" :name "password-confirm" :value @password-confirm :placeholder "CONFIRM PASSWORD" :on-change #(reset! password-confirm (-> % .-target .-value))}])]
       (when (= @form "signup")
         [:div#recaptcha])
       [:input.border-button {:type "submit" :value (case @form
                                                      "login" "LOGIN"
                                                      "signup" "SIGN UP"
                                                      "forgot-password" "SEND EMAIL")}]
       [:p (when can-sign-up
             [:span
              [:button.switch-form {:on-click switch-form}
               (if (= @form "login") "Sign Up" "Login")]
              [:span " | "]])
        [:button#forgot-password.switch-form {:on-click switch-form} "Forgot Password"]]]]]))