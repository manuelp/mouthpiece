(ns mouthpiece.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname "db.sq3"})

(defn create-mouthpiece-table []
  (sql/with-connection
   db
   (sql/create-table :mouthpiece
                     [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
                     [:timestamp "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
                     [:message "TEXT"])
   (sql/do-commands "CREATE INDEX timestamp_index ON mouthpiece (timestamp)")))

(defn read-messages []
  (sql/with-connection
   db
   (sql/with-query-results
    res
    ["SELECT * FROM mouthpiece ORDER BY timestamp DESC"]
    (doall res))))

(defn save-message [message]
  (sql/with-connection
   db
   (sql/insert-values :mouthpiece
                      [:message :timestamp]
                      [message (new java.util.Date)])))

(comment

  (create-mouthpiece-table)

  (save-message "Something very interesting.")

  (read-messages)

  )

