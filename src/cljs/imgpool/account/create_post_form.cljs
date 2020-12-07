(ns imgpool.account.create-post
  (:require
   [imgpool.utility :refer [modal file-input]]))

(defn create-post-form [{:keys [clear-values handle-submit handle-change data]}]
  [modal {:show (:show data) :toggle-modal clear-values}
   [:form#post-form.form-light {:key 0 :on-submit handle-submit}
    [:div.field-container
     [file-input {:id "file"
                  :title "Post"
                  :name "post"
                  :value (-> data :file :name)
                  :on-change #(handle-change % "create-post-state" "file")}]
     [:input {:id "source"
              :auto-complete "off"
              :type "text"
              :title "Source"
              :name "source"
              :value (:source data)
              :placeholder "SOURCE URL"
              :on-change #(handle-change % "create-post-state" "source")}]
     [:input {:id "tags"
              :auto-complete "off"
              :type "text"
              :title "Source"
              :name "source"
              :value (:tags data)
              :placeholder "TAGS"
              :on-change #(handle-change % "create-post-state" "tags")}]]
    [:input.border-button {:type "submit"}]]])