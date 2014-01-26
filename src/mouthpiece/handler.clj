(ns mouthpiece.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [mouthpiece.routes.home :refer [home-routes]]
            [mouthpiece.models.db :as db]
            [clojure.tools.nrepl.server :refer [start-server stop-server default-handler]]
            [lighttable.nrepl.handler :refer [lighttable-ops]]))

(def system
  "Maps that contains system components that have to be started and stopped along with the application."
  (atom {}))

(defn start-nrepl-server
  "Start an nREPL server at a specified port and assoc it in the `system` atom."
  [port]
  (do
    (swap! system
           #(assoc % :nrepl-server (start-server :port port
                                                 :handler (default-handler #'lighttable-ops)))))
  (println "Started nREPL server on port " port))

(defn stop-nrepl-server
  "Stop the nREPL server binded in the `system` atom and dissoc it."
  []
  (do (swap! system #(do (stop-server (:nrepl-server %))
                       (dissoc % :nrepl-server))))
  (println "Stopped nREPL server."))

(defn init []
  (println "mouthpiece is starting")
  (if-not (.exists (java.io.File. "./db.sq3"))
    (db/create-mouthpiece-table))
  (start-nrepl-server 7888))

(defn destroy []
  (println "mouthpiece is shutting down")
  (stop-nrepl-server))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes app-routes)
      (handler/site)
      (wrap-base-url)))
