(ns mouthpiece.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to mouthpiece"]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1.0"}]
     (include-css "/css/foundation.css")
     (include-css "/css/mouthpiece.css")
     (include-js "/js/vendor/modernizr.js")]
    [:body body]))
