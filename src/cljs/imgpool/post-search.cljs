(ns imgpool.post-search)

(defn handle-submit [event]
  (.preventDefault event))

(defn handle-change [event]
  (.preventDefault event))

(defn post-search []
  (fn []
    [:form.search {:on-submit handle-submit}
     [:input.search-field {:type "text" :placeholder "Search..." :value "" :on-change handle-change}]]))