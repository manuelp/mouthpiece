(ns mouthpiece.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [mouthpiece.routes.home :refer [home-routes]]
            [mouthpiece.models.db :as db]))

(defn init []
  (println "mouthpiece is starting")
  (if-not (.exists (java.io.File. "./db.sq3"))
    (db/create-mouthpiece-table)))

(defn destroy []
  (println "mouthpiece is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes app-routes)
      (handler/site)
      (wrap-base-url)))


