(ns imgpool.tag-menu)

(def show-menu)

(defn handle-menu-click [e]
  (.stopPropagation e))
  ;(close-all-menus-except "TAGS-MENU")

(defn handle-tag-click [e]
  (.stopPropagation e))
  ;(close-all-menus-except "TAGS-MENU")

(defn burger-button [{:keys [toggle-menu]}]
  [:button.tab {:on-click (fn [e] (.preventDefault e) (.stopPropagation e) (toggle-menu e "tags"))}
   [:span.burger
    [:span.line]
    [:span.line]
    [:span.line]]
   [:span.text "View Tags"]])

(defn tag-menu-item [{:keys [id active name]}]
  [:a {:href (str "post?tag=" id) :class (str "tag " active) :on-click handle-tag-click} name])

(defn tag-menu [{:keys [show toggle-menu]}]
  [:aside#tag-menu {:class (when show "active")}
   [:div.body {:on-click handle-menu-click}
    [:nav
     (map (fn [key] [tag-menu-item {:key key :id key :active false :name "tag"}]) (range 0 20))]]
   [burger-button {:toggle-menu toggle-menu}]])