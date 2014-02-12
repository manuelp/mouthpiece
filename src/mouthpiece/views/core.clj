(ns mouthpiece.views.core
  (:require [mouthpiece.views.layout :as layout]
            [mouthpiece.views.pagination :as pagination]
            [mouthpiece.models.db :as db]
            [hiccup.form :refer :all]
            [hiccup.element :refer [image link-to]]
            [markdown.core :as md]))

;; Configuration: a page is at most (val) messages
(def page-size 15)

(defn format-time [timestamp]
  (-> "dd/MM/yyyy HH:mm"
      (java.text.SimpleDateFormat.)
      (.format timestamp)))

(defn show-message [message id timestamp]
  [:div {:class "panel"}
   (md/md-to-html-string message)
   [:a {:class "round label id-label"
        :href (str "/message/" id)} id]
   [:span {:class "secondary label"}
    (format-time timestamp)]])

(defn show-messages [page size]
  [:div
   (for [{:keys [message id timestamp]}
         (db/read-messages page size)]
     (show-message message id timestamp))])

(defn comment-box [message]
  [:div {:class "panel"}
   (form-to [:post "/"]
            [:div {:class "row"}
             [:label "Message: "]
             (text-area {:rows 6 :cols 40} "message" message)]
            [:div {:class "row"}
             [:p "Messages can be written in "
              (link-to "https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet"
                       "Markdown") "."]]
            [:div {:class "row"}
             (submit-button {:class "small round success button"} "Comment")])])

(defn home [page size & [[message error]]]
  (layout/common [:div {:class "row"}
                  [:div {:class "small-12 large-6 columns"}
                   (when error
                     [:div {:class "alert-box warning round"}
                      error])
                   (comment-box message)]
                  [:div {:class "small-12 large-6 columns"}
                   (if (> (db/num-pages page-size) 1)
                     (pagination/pagination page size))
                   (show-messages page size)]]))

(defn message-page [id]
  (layout/common (let [{:keys [message id timestamp]}
                       (db/read-message id)]
                   [:div {:class "row"}
                    [:div {:class "small-12 medium-10 large-8"}
                     (show-message message id timestamp)]])))
