(ns imgpool.handler
  (:require
   [reitit.ring :as reitit-ring]
   [imgpool.middleware :refer [middleware]]
   [hiccup.page :refer [include-js include-css html5]]
   [config.core :refer [env]]))

(def mount-target
  [:div#app
   [:h2 "Welcome to imgpool"]
   [:p "please wait while Figwheel is waking up ..."]
   [:p "(Check the js console for hints if nothing exciting happens.)"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    mount-target
    (include-js "/js/app.js")]))


(defn index-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})

(def app
  (reitit-ring/ring-handler
   (reitit-ring/router
    [["/" {:get {:handler index-handler}}]
     ["/flags" {:get {:handler index-handler}}]
     ["/admin" {:get {:handler index-handler}}]
     ["/account" {:get {:handler index-handler}}]
     ["/login" {:get {:handler index-handler}}]
     ["/user"
      ["/:user-id" {:get {:handler index-handler
                          :parameters {:path {:user-id int?}}}}]]
     ["/post"
      ["/:post-id" {:get {:handler index-handler
                          :parameters {:path {:post-id int?}}}}]]
     ["/about" {:get {:handler index-handler}}]])
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"})
    (reitit-ring/create-default-handler))
   {:middleware middleware}))
