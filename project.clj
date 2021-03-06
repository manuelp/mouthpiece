(defproject mouthpiece "0.2.2-SNAPSHOT"
  :description "Simple anonymous guestbook, to spread ideas."
  :url "https://github.com/manuelp/mouthpiece"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [markdown-clj "0.9.41"]
                 [lein-light-nrepl "0.0.17"]
                 [org.clojure/math.numeric-tower "0.0.4"]]
  :plugins [[lein-ring "0.8.7"]]
  :ring {:handler mouthpiece.handler/app
         :init mouthpiece.handler/init
         :destroy mouthpiece.handler/destroy}
  :aot :all
  :profiles {:production {:ring {:open-browser? false
                                 :stacktraces? false
                                 :auto-reload? false}}
             :dev {:dependencies [[ring-mock "0.1.5"]
                                  [ring/ring-devel "1.2.2"]]
                   :ring {:open-browser? false}}})
