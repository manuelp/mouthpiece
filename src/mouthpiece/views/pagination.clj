(ns mouthpiece.views.pagination
  (:require [mouthpiece.models.db :as db]
            [hiccup.element :refer [link-to]]))

(defn compressed-page-list
  "Produces a seq with the page numbers (starting from one) given the page size
  and the current page number, retaining only the first and last three
  (plus the current one) and inserting ellipses for omitted pages intervals."
  [num-pages page]
  (let [start (range 1 (inc 3))
        end (range (- num-pages 2) (inc num-pages))
        middle (if (or (< page 4) (> page (- num-pages 3)))
                 "..."
                 [(when (> page 4) "...")
                  page
                  (when (< page (- num-pages 3))
                    "...")])]
    (remove nil? (flatten [start middle end]))))

(defn pages-list
  "Produces the full page list if there are at most 10 pages, else produces
  a compressed page list omitting some of them."
  [page size]
  (let [num-pages (db/num-pages size)]
    (if (< num-pages 10)
      (range 1 num-pages)
      (compressed-page-list num-pages page))))

;; Here comes the view generation functions

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
  (if (not= n "...")
    {:link (link-to (str "/page/" n) n)
     :classes (cond (= n page) {:class "current"}
                    :else {})}
    {:link "&hellip;"
     :classes {:class "unavailable"}}))

(defn pagination
  "Produces an HTML data structure for Hiccup & Foundation of the pagination component,
  taking the page number and the page size (number of messages per page)."
  [page size]
  (let [prev-page (previous-page page size)
        next-page (next-page page size)]
    [:ul {:class "pagination"}
     [:li {:class (:classes prev-page)}
      (link-to (:link prev-page) "&laquo;")]
     (for [n (pages-list page size)]
       (let [page (current-page n page)]
         [:li (:classes page) (:link page)]))
     [:li {:class (:classes next-page)}
      (link-to (:link next-page) "&raquo;")]]))
