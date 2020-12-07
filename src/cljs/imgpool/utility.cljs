(ns imgpool.utility)

(defn index-of [vct value]
  (loop [index 0]
    (when (< index (count vct))
      (if (= (vct index) value)
        index
        (recur (inc index))))))

(defn loader [show]
  (when show [:div.loader-container
              [:div.preloader2
               [:span]
               [:span]
               [:span]
               [:span]
               [:span]]]))

(defn file-input [])

(defn modal [{:keys [show toggle-modal]} & children]
  (when show
    [:div#modal-container {:on-click toggle-modal}
     [:div#modal {:on-click #(.stopPropagation %)} children]]))