(ns imgpool.admin-dashboard.tag-form
  (:require
   [reagent.core :as r :refer [atom]]
   [reagent-material-ui.lab.autocomplete :refer [autocomplete]]
   [imgpool.utility :refer [modal]]))

(def selected-tags (atom []))

(defn handle-submit [event]
  (.preventDefault event)
  (.log js/console selected-tags))

(defn tag-form [{:keys [tags show toggle-show]}]
  [modal {:show show :toggle-modal toggle-show}
   [:form#admin-manage-form.form-light {:key 0}
    [:div.field-container
     [autocomplete {:style {:width 326 :max-width "100%" :margin "auto"}
                    :multiple true
                    :id "admin-manage-select"
                    :options tags
                    :get-option-label #(-> % .-name)
                    :on-change #(reset! selected-tags %2)
                    :render-input (fn [^js params]
                                    (doto params
                                      (-> .-style (set! {:border "none"}))
                                      (-> .-variant (set! "outlined"))
                                      (-> .-label (set! "Autocomplete"))
                                      (-> .-placeholder (set! "Placeholder")))
                                    (r/create-element (.-TextField js/MaterialUI) params))}]]
    [:button#toggle-state.border-button {:on-click handle-submit} "Toggle State"]
    [:button#delete.border-button-red {:on-click handle-submit} "Delete"]]])