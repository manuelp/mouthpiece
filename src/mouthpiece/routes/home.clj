(ns mouthpiece.routes.home
  (:require [compojure.core :refer :all]
            [mouthpiece.views.core :refer [home page-size]]
            [mouthpiece.models.db :as db]
            [ring.util.response :refer [redirect]]))

;; Configuration
(def auth-tokens
  "Ordered sequence of valid authentication tokens for administrative tasks."
  (atom [(System/getenv "MOUTHPIECE_TOKEN")]))

(defn save-message [message]
  (cond (empty? message) (home 1 page-size message "Don't you have something to say?")
        :else (do
                (db/save-message message)
                (redirect "/"))))

(defn- required-token
  "Returns the first valid (not nil and not empty) authorization token configured."
  [tokens]
  (letfn [(nothing? [v]
                    (or (nil? v) (= "" v)))]
    (first (drop-while nothing? tokens))))

(defn delete-message [token id]
  (when (= token (required-token @auth-tokens))
    (println "Deleting message with ID " id)
    (db/delete-message id))
  (redirect "/"))

(defroutes home-routes
  (GET "/" [] (home 1 page-size))
  (POST "/" [message] (save-message message))
  (GET "/page/:n" [n] (home (Integer/parseInt n) page-size))
  (GET "/delete/:token/:id" [token id] (delete-message token id)))
