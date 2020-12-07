(ns imgpool.paginator)

(defn paginator [change page last-page loading]
  [:div.paginator
   [:button.previous {:disabled (or (< page 2) loading) :on-click #(change "prev")} "←"]
   (when (>= page 6) [:button.number {:disabled (true? loading) :on-click #(change "prev")} "1"])
   (map (fn [page-link]
          (when (> page-link 0)
            [:button.number {:disabled (true? loading) :on-click #(change page-link)} page-link])) (range (- page 5) page))
   [:button.number.active {:disabled (true? loading) :on-click #(change page)} page]
   (map (fn [page-link]
          (when (< page-link last-page)
            [:button.number {:disabled (true? loading) :on-click #(change page-link)} page-link])) (range page (+ page 5)))
   (when (<= page (- last-page 6)) [:button.number {:disabled (true? loading) :on-click #(change last-page)} last-page])
   [:button.next {:disabled (true? loading) :on-click #(change "next")} "→"]])
