(ns mouthpiece.routes.home
  (:require [compojure.core :refer :all]
            [mouthpiece.views.layout :as layout]
            [hiccup.form :refer :all]
            [mouthpiece.models.db :as db]))

(defn format-time [timestamp]
  (-> "dd/MM/yyyy HH:mm"
      (java.text.SimpleDateFormat.)
      (.format timestamp)))

(defn show-messages []
  [:ul.messages
   (for [{:keys [message timestamp]}
         (db/read-messages)]
     [:li
      [:blockquote message]
      [:time (format-time timestamp)]])])

(defn home [& [message error]]
  (layout/common [:h1 "mouthpiece"]
                 [:p "Welcome, say whatever you want."]
                 [:p error]
                 (show-messages)
                 [:hr]
                 (form-to [:post "/"]
                          [:p "Message: "]
                          (text-area {:rows 10 :cols 40} "message" message)
                          [:br]
                          (submit-button "comment"))))

(defn save-message [message]
  (cond (empty? message) (home message "Don't you have something to say?")
        :else (do
                (db/save-message message)
                (home))))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [message] (save-message message)))
