(ns mouthpiece.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]))

(defn include-syntax-highlighter []
  (letfn [(append-prefix [dir f]
                         (str "/" dir "/syntax-highlighter/" f))]
    (concat (map (comp first include-js (partial append-prefix "js"))
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
                  "shBrushSql.js"])
            (map (comp first include-css (partial append-prefix "css"))
                 ["shCore.css"
                  "shThemeDefault.css"])
            [[:script {:type "text/javascript"}
              (str "SyntaxHighlighter.defaults['class-name'] = 'code-block';"
                   "SyntaxHighlighter.all();")]])))

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
   [:body body]))
