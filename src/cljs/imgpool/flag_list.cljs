(ns imgpool.flag-list
  (:require
   [reagent.core :refer [atom]]
   [reagent-material-ui.styles :refer [with-styles]]
   [reagent-material-ui.core.table :refer [table]]
   [reagent-material-ui.core.table-body :refer [table-body]]
   [reagent-material-ui.core.table-cell :refer [table-cell]]
   [reagent-material-ui.core.table-head :refer [table-head]]
   [reagent-material-ui.core.table-pagination :refer [table-pagination]]
   [reagent-material-ui.core.table-row :refer [table-row]]
   [reagent-material-ui.core.table-sort-label :refer [table-sort-label]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.typography :refer [typography]]
   [reagent-material-ui.core.paper :refer [paper]]
   [reagent-material-ui.core.checkbox :refer [checkbox]]
   [reagent-material-ui.core.icon-button :refer [icon-button]]
   [reagent-material-ui.core.tooltip :refer [tooltip]]
   [reagent-material-ui.icons.delete-forever :refer [delete-forever]]
   [imgpool.utility :refer [index-of]]))

(def order (atom "asc"))
(def order-by (atom "date"))
(def page (atom 0))
(def selected (atom []))
(defn custom-styles [theme]
  {:root {:width "100%"
          :margin-top ((:spacing theme) 3)}
   :papers {:width "100%"
            :margin-bottom ((:spacing theme) 2)
            :box-shadow "none"}
   :table {:min-width 750}
   :table-cell {:font-size "1.2rem"}
   :table-wrapper {:overflow-x "auto"}
   :visually-hidden {:border 0
                     :clip "rect(0 0 0 0)"
                     :height 1
                     :margin -1
                     :overflow "hidden"
                     :padding 0
                     :position "absolute"
                     :top 20
                     :width 1}})
(def with-custom-styles (with-styles custom-styles))

(defn desc [a b order-by]
  (case (> ((keyword order-by) b) ((keyword order-by) a)) true -1 false 1 0))

(defn stable-sort [array cmp]
  (let [stabilized-this (map-indexed (fn [index el] [el index]) array)
        sorted-stabilized-this (sort (fn [a b]
                                       (let [order (cmp (first a) (first b))]
                                         (if (not= order 0)
                                           order
                                           (- (second a) (second b))))) stabilized-this)]

    (map (fn [el] (first el)) sorted-stabilized-this)))

(defn get-sorting [order order-by]
  (if (= order "desc") (fn [a b] (desc a b order-by)) (fn [a b] (- (desc a b order-by)))))

(def head-cells [{:id "post-id" :numeric false :disable-padding true :label "Post"}
                 {:id "date" :numeric true :disable-padding false :label "Date"}
                 {:id "user" :numeric true :disable-padding false :label "User"}
                 {:id "status" :numeric true :disable-padding false :label "Status"}
                 {:id "reason" :numeric true :disable-padding false :label "Reason"}])

(defn enhanced-table-head [{:keys [classes order order-by on-request-sort]}]
  (let [create-sort-handler (fn [property]
                              (fn []
                                (on-request-sort property)))]

    [table-head
     [table-row
      [table-cell {:padding "checkbox"}]
      (map (fn [head-cell]
             [table-cell {:class (:table-cell classes)
                          :key (:id head-cell)
                          :align (if (:numeric head-cell) "right" "left")
                          :padding (if (:disable-padding head-cell) "none" "default")
                          :sort-direction (if (= order-by (:id head-cell)) order false)}
              [table-sort-label {:active (= order-by (:id head-cell))
                                 :direction order
                                 :on-click (create-sort-handler (:id head-cell))}
               (:label head-cell)
               (when (= order-by (:id head-cell))
                 [:span {:class (:visually-hidden classes)}
                  (if (= order "desc") "sorted descending" "sorted ascending")])]])
           head-cells)]]))

(defn flag-list-inner [{:keys [flags classes is-admin retrieve-flags]}]
  (let [rows-per-page 10

        handle-post-delete (fn [event]
                             (.preventDefault event)
                             (let [selected-flags (get flags @selected)]
                               (.log js/console (str "Delete selected flags " selected-flags))))

        handle-request-sort (fn [property]
                              (let [is-desc (and (= @order-by property) (= @order "desc"))]
                                (reset! order (if is-desc "asc" "desc"))
                                (reset! order-by property)))

        handle-click (fn [clicked-index]
                       (if (not is-admin) nil
                           (let [is-clicked-index-selected (index-of @selected clicked-index)]
                             (if (nil? is-clicked-index-selected)
                               (reset! selected [clicked-index])
                               (reset! selected (concat
                                                 (subvec @selected 0 is-clicked-index-selected)
                                                 (subvec @selected (inc is-clicked-index-selected))))))))

        handle-change-page (fn [event new-page]
                             (reset! page new-page))

        is-selected (fn [name] (not (nil? (index-of @selected name))))]

    [:section#flag-list
     (comment "ToastContainer")
     [:h1
      [:span "Flags"]]
     [:div {:class (:root classes)}
      [paper {:class (:paper classes)}
       [:div {:class (:table-wrapper classes)}
        [table {:class (:table classes)
                :aria-labelledby "tableTitle"
                :size "medium"
                :aria-label "enhanced table"}
         [enhanced-table-head {:classes classes
                               :num-selected (count @selected)
                               :order @order
                               :order-by @order-by
                               :on-select-all-click #()
                               :on-request-sort handle-request-sort
                               :row-count (count flags)}]
         [table-body (doall (map-indexed (fn [index row]
                                           (let [is-item-selected (is-selected index)
                                                 label-id (str "enhanced-table-checkbox-" index)]
                                             [table-row {:hover true
                                                         :on-click (fn [] (handle-click index))
                                                         :role "checkbox"
                                                         :aria-checked is-item-selected
                                                         :tab-index -1
                                                         :key index
                                                         :selected is-item-selected}
                                              [table-cell {:padding "checkbox"}
                                               (when is-admin
                                                 [checkbox {:style {:color "#757575"}
                                                            :checked is-item-selected
                                                            :input-props {:aria-labelledby label-id}}])]
                                              [table-cell {:class (:table-cell classes)
                                                           :component "th"
                                                           :id label-id
                                                           :scope "row"
                                                           :padding "none"}
                                               [:a {:href (str "/post/" (:post-id row))} (:post-id row)]]
                                              [table-cell {:class (:table-cell classes)
                                                           :align "right"}
                                               (:date row)]
                                              [table-cell {:class (:table-cell classes)
                                                           :align "right"}
                                               [:a {:href (str "/user/" (-> row :user :id))}
                                                (-> row :user :username)]]
                                              [table-cell {:class (:table-cell classes)
                                                           :align "right"}
                                               (if (:active row) "Active" "Deleted")]
                                              [table-cell {:class (:table-cell classes)
                                                           :align "right"}
                                               (:reason row)]]))
                                         (stable-sort flags (get-sorting @order @order-by))))]]]]]]))

(defn flag-list [{:keys [flags is-admin retrieve-flags]}]
  [(with-custom-styles flag-list-inner) {:flags flags
                                         :is-admin is-admin
                                         :retrieve-flags retrieve-flags}])