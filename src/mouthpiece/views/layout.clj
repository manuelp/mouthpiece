(ns mouthpiece.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.element :refer [image link-to]]))

(defn include-syntax-highlighter []
  (concat
   (apply include-js (map #(str "/js/syntax-highlighter/" %)
                          ["shCore.js"
                           "shBrushClojure.js"
                           "shBrushJScript.js"
                           "shBrushBash.js"
                           "shBrushJava.js"
                           "shBrushPython.js"
                           "shBrushRuby.js"
                           "shBrushXml.js"
                           "shBrushScala.js"
                           "shBrushSass.js"
                           "shBrushSql.js"]))
   (apply include-css (map #(str "/css/syntax-highlighter/" %)
                           ["shCore.css"
                            "shThemeDefault.css"]))
   [[:script {:type "text/javascript"}
     "SyntaxHighlighter.all();"]]))

(defn- header []
  [:div {:class "row"}
   [:div {:class "small-1 columns"}
    (link-to "/" (image {:style "height:60px;"}
                        "/img/megafono_256.png"
                        "Megaphone icon"))]
   [:div {:class "small-11 columns"}
    [:h1 "Mouthpiece"]]
   [:div {:class "small-12 columns"}
    [:p "Welcome, say whatever you want."]]])

(defn- footer []
  [:div {:class "row"}
   [:hr]
   [:p (link-to "https://github.com/manuelp/mouthpiece" "Mouthpiece")
    " &copy; 2014 Manuel Paccagnella &mdash; Released under the "
    (link-to "http://www.eclipse.org/legal/epl-v10.html"
             "Eclipse Public License 1.0")]])

(defn common [& body]
  (html5
   [:head
    [:title "Welcome to mouthpiece"]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    (include-css "/css/foundation.css")
    (include-js "/js/vendor/modernizr.js")
    (include-syntax-highlighter)
    (include-css "/css/mouthpiece.css")]
   [:body
    (header)
    [:hr]
    body
    (footer)]))
