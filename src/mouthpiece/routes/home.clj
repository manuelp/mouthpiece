(ns mouthpiece.routes.home
  (:require [compojure.core :refer :all]
            [mouthpiece.views.layout :as layout]
            [hiccup.form :refer :all]
            [hiccup.element :refer [image]]
            [mouthpiece.models.db :as db]))

(defn format-time [timestamp]
  (-> "dd/MM/yyyy HH:mm"
      (java.text.SimpleDateFormat.)
      (.format timestamp)))

(defn show-message [message id timestamp]
  [:div {:class "panel"}
   [:p message]
   [:span {:class "round label"} id]
   [:span {:class "secondary label timestamp"}
    (format-time timestamp)]])

(defn show-messages []
  [:div
   (for [{:keys [message id timestamp]}
         (db/read-messages)]
     (show-message message id timestamp))])

(defn comment-box [message]
  [:div {:class "panel"}
   (form-to [:post "/"]
            [:label "Message: "]
            (text-area {:rows 20 :cols 40} "message" message)
            [:br]
            (submit-button {:class "small round success button"} "Comment"))])

(defn home [& [message error]]
  (layout/common [:div {:class "row"}
                  [:div {:class "small-1 columns"}
                   (image {:style "height:60px;"} "/img/megafono_256.png" "Megaphone icon")]
                  [:div {:class "small-11 columns"}
                   [:h1 "Mouthpiece"]]
                  [:div {:class "small-12 columns"}
                   [:p "Welcome, say whatever you want."]]]
                 [:hr]
                 [:div {:class "row"}
                  [:div {:class "small-12 large-6 columns"}
                   (show-messages)]
                  [:div {:class "small-12 large-6 columns"}
                   (when error
                     [:div {:class "alert-box warning round"}
                      error])
                   (comment-box message)]]))

(defn save-message [message]
  (cond (empty? message) (home message "Don't you have something to say?")
        :else (do
                (db/save-message message)
                (home))))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [message] (save-message message)))
