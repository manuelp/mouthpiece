(ns mouthpiece.routes.home
  (:require [compojure.core :refer :all]
            [mouthpiece.views.layout :as layout]
            [hiccup.form :refer :all]
            [hiccup.element :refer [image link-to]]
            [mouthpiece.models.db :as db]
            [markdown.core :as md]
            [ring.util.response :refer [redirect]]))

(def auth-tokens
  "Ordered sequence of valid authentication tokens for administrative tasks."
  (atom [(System/getenv "MOUTHPIECE_TOKEN")]))

(defn format-time [timestamp]
  (-> "dd/MM/yyyy HH:mm"
      (java.text.SimpleDateFormat.)
      (.format timestamp)))

(defn show-message [message id timestamp]
  [:div {:class "panel"}
   ;[:p message]
   (md/md-to-html-string message)
   [:span {:class "round label"} id]
   [:span {:class "secondary label"}
    (format-time timestamp)]])

(defn previous-page [page size]
  {:link (if (> page 1)
           (str "/page/" (dec page))
           "")
   :classes (str "arrow" (when (= page 1) " unavailable"))})

(defn next-page [page size]
  {:link (if (< page (db/num-pages size))
           (str "/page/" (inc page))
           "")
   :classes (str "arrow" (when (= page (db/num-pages size))
                           " unavailable"))})

(defn current-page [n page]
  {:link (link-to (str "/page/" n) n)
   :classes (cond (= n page) {:class "current"}
                  :else {})})

(defn pagination [page size]
  (let [prev-page (previous-page page size)
        next-page (next-page page size)]
    [:ul {:class "pagination"}
     [:li {:class (:classes prev-page)}
      (link-to (:link prev-page) "&laquo;")]
     (for [n (rest (range (inc (db/num-pages size))))]
       (let [page (current-page n page)]
         [:li (:classes page) (:link page)]))
     [:li {:class (:classes next-page)}
      (link-to (:link next-page) "&raquo;")]]))

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
                  [:div {:class "small-1 columns"}
                   (image {:style "height:60px;"} "/img/megafono_256.png" "Megaphone icon")]
                  [:div {:class "small-11 columns"}
                   [:h1 "Mouthpiece"]]
                  [:div {:class "small-12 columns"}
                   [:p "Welcome, say whatever you want."]]]

                 [:hr]

                 [:div {:class "row"}
                  [:div {:class "small-12 large-6 columns"}
                   (when error
                     [:div {:class "alert-box warning round"}
                      error])
                   (comment-box message)]
                  [:div {:class "small-12 large-6 columns"}
                   (pagination page size)
                   (show-messages page size)]]

                 [:div {:class "row"}
                  [:hr]
                  [:p (link-to "https://github.com/manuelp/mouthpiece" "Mouthpiece")
                   " &copy; 2014 Manuel Paccagnella &mdash; Released under the "
                   (link-to "http://www.eclipse.org/legal/epl-v10.html"
                            "Eclipse Public License 1.0")]]))

(defn save-message [message]
  (cond (empty? message) (home 1 15 message "Don't you have something to say?")
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
  (GET "/" [] (home 1 15))
  (POST "/" [message] (save-message message))
  (GET "/page/:n" [n] (home (Integer/parseInt n) 15))
  (GET "/delete/:token/:id" [token id] (delete-message token id)))
