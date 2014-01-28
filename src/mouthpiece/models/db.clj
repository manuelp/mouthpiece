(ns mouthpiece.models.db
  (:require [clojure.java.jdbc :as sql]
            [clojure.math.numeric-tower :as math])
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

(defn read-messages [page size]
  (sql/with-connection
   db
   (sql/with-query-results
    res
    ["SELECT * FROM mouthpiece ORDER BY timestamp DESC LIMIT ?, ?"
     (* (dec page) size)
     size]
    (doall res))))

(defn total-number []
  (-> (sql/with-connection
       db
       (sql/with-query-results
        res
        ["SELECT count(*) as num from mouthpiece"]
        (doall res)))
      first
      :num))

(defn num-pages
  "Calculates the number of pages with `size` messages given
  the total number of messages saved in the DB."
  [size]
  (-> (/ (total-number) size)
      math/ceil
      int))

(defn save-message [message]
  (sql/with-connection
   db
   (sql/insert-values :mouthpiece
                      [:message :timestamp]
                      [message (new java.util.Date)])))

(defn delete-message [id]
  (sql/with-connection
   db
   (sql/do-commands (str "DELETE FROM mouthpiece WHERE ID=" id))))

(comment

  (create-mouthpiece-table)

  (save-message "Something very interesting.")

  (read-messages)

  (delete-message 12)

  )

