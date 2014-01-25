(defproject mouthpiece "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.4"]
                 [ring-server "0.3.0"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [markdown-clj "0.9.41"]]
  :plugins [[lein-ring "0.8.7"]]
  :ring {:handler mouthpiece.handler/app
         :init mouthpiece.handler/init
         :destroy mouthpiece.handler/destroy}
  :aot :all
  :profiles {:production {:ring {:open-browser? false
                                 :stacktraces? false
                                 :auto-reload? false}}
             :dev {:dependencies [[ring-mock "0.1.5"]
                                  [ring/ring-devel "1.2.0"]]}})
