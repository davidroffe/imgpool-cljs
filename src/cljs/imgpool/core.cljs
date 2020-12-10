(ns imgpool.core
  (:require
   [cljs.core.async :refer [<! go]]
   [cljs-http.client :as http]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]
   [reagent.session :as session]
   [reitit.frontend :as reitit]
   [accountant.core :as accountant]
   [imgpool.account.core :refer [account]]
   [imgpool.post-list :refer [post-list]]
   [imgpool.post-detail :refer [post-detail]]
   [imgpool.post-search :refer [post-search]]
   [imgpool.login :refer [login]]
   [imgpool.user-profile.core :refer [user-profile]]
   [imgpool.admin-dashboard.core :refer [admin-dashboard]]
   [imgpool.about :refer [about]]
   [imgpool.flag-list :refer [flag-list]]))

;; -------------------------
;; App State

(def state (atom
            {:user {:id 0
                    :email "jondoe@gmail.com"
                    :username "jonnydoe"
                    :bio "I am jonson doe."
                    :logged-in true
                    :is-admin true
                    :is-init true
                    :favorites []}
             :post {:id ""
                    :tag []
                    :user {:id ""
                           :username ""}}
             :posts {:list []
                     :page 1
                     :total-count 0
                     :loading false}
             :tags []
             :search ""
             :flags [{:created-at "2020-10-21T21:38:33.196Z"
                      :id 6
                      :post {:active true}
                      :post-id 22
                      :reason "This is a test reason."
                      :updated-at "2020-10-21T21:38:33.196Z"
                      :user {:username "jonnyboi"}
                      :user-id 2}
                     {:created-at "2020-10-25T21:38:33.196Z"
                      :id 7
                      :post {:active true}
                      :post-id 23
                      :reason "This is a test reason as well."
                      :updated-at "2020-10-25T21:38:33.196Z"
                      :user {:username "jane"}
                      :user-id 3}]
             :menus {:tags false
                     :post-options false}}))

;; -------------------------
;; Handlers

(defn toggle-menu [event menu]
  (.preventDefault event)
  (.stopPropagation event)
  (swap! state assoc-in [:menus (keyword menu)] (not (get-in @state [:menus (keyword menu)]))))

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/" :index]
    ["/flags" :flag-list]
    ["/admin" :admin]
    ["/account" :account]
    ["/login" :login]
    ["/user"
     ["/:user-id" :user-profile]]
    ["/post"
     ["/:post-id" :post-detail]]
    ["/about" :about]]))

(defn path-for [route & [params]]
  (if params
    (:path (reitit/match-by-name router route params))
    (:path (reitit/match-by-name router route))))

;; -------------------------
;; Page components

(defn item-page []
  (fn []
    (let [routing-data (session/get :route)
          item (get-in routing-data [:route-params :item-id])]
      [:span.main
       [:h1 (str "Item " item " of imgpool")]
       [:p [:a {:href (path-for :items)} "Back to the list of items"]]])))

(defn header []
  (fn [] [:header#main-header
          [:div.left
           [:a.logo {:href (path-for :index)}
            [:img {:src "https://via.placeholder.com/50" :alt "Imgpool Logo"}]]
           [:nav#main-nav
            [:a {:href (path-for :index)} "Posts"]
            [:a {:href (path-for :account)} "Account"]
            [:a {:href (path-for :about)} "About"]
            [:a {:href (path-for :admin)} "Admin"]]]
          [post-search]]))

;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index (fn [] [post-list {:posts (-> @state :posts :list) :show-tag-menu (-> @state :menus :tags) :toggle-menu toggle-menu}])
    :flag-list (fn [] [flag-list {:flags (:flags @state) :is-admin (-> @state :user :is-admin) :retrieve-flags #()}])
    :admin (fn [] [admin-dashboard {:user (:user @state) :flags (:flags @state)}])
    :account (fn [] [account {:user (:user @state)}])
    :user-profile (fn [] [user-profile (:user @state)])
    :login #'login
    :post-detail #'post-detail
    :about #'about))

;; -------------------------
;; Page mounting component

(defn current-page []
  (fn []
    (let [page (:current-page (session/get :route))]
      [:div
       [header]
       [page]])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (rdom/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (let [match (reitit/match-by-path router path)
            current-page (:name (:data  match))
            route-params (:path-params match)]
        (session/put! :route {:current-page (page-for current-page)
                              :route-params route-params
                              :test-val "blabla"})))
    :path-exists?
    (fn [path]
      (boolean (reitit/match-by-path router path)))})
  (accountant/dispatch-current!)
  (mount-root))

(go (let [response (<! (http/get "http://local.imgpool.com/api/post/list"
                                 {:with-credentials? false
                                  :query-params {"searchQuery" "" "page" "1"}}))]
      (swap! state assoc-in [:posts :list] (mapv (fn [post] {:updated-at (:updatedAt post)
                                                             :active (:active post)
                                                             :created-at (:createdAt post)
                                                             :height (:height post)
                                                             :id (:id post)
                                                             :source (:source post)
                                                             :tag (:tag post)
                                                             :thumb-url (:thumbUrl post)
                                                             :url (:url post)
                                                             :user-id (:userId post)
                                                             :width (:width post)}) (-> response :body :list)))))