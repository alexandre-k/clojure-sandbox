(ns photos.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [clj-http.client :as client]
            [clojure.string :as str]
            [clojure.java.jdbc :as jdbc]
            [java-jdbc.ddl :as ddl]
            [java-jdbc.sql :as sql]
            [clojure.pprint :as pp]
            [clojure.walk :as walk]
            [clj-time.core :as time]
            ))

;; (def photos
;;   (:body (client/get "https://jsonplaceholder.typicode.com/photos")))

(def git-db {:classname "org.sqlite.JDBC"
              :subprotocol "sqlite"
              :subname "git.db"})


(def sql-data
  (jdbc/query git-db (sql/select * :git)))


;; (jdbc/db-do-commands git-db false
;;                      (ddl/create-table
;;                       :git
;;                       [:login "TEXT NOT NULL"]
;;                       [:email "TEXT NOT NULL"]
;;                       [:updatedAt "DATETIME"]
;;                       [:createdAt "DATETIME"]))

(defn insert-data
  [git-data]
  (jdbc/insert! git-db :git git-data))

(defn filter-data
  [mutation]
  (drop 2
  (map #(str/split % #"\":")
  (filter #(str/includes? % ":")
  (str/split-lines mutation)))))
  ;; (str/split-lines "{
  ;; \"data\": {
;;     \"viewer\": {
;;       \"login\": \"alexandre-k\",
;;       \"email\": \"k.m.alexandre@gmail.com\",
;;       \"updatedAt\": \"2018-08-15T15:30:31Z\",
;;       \"createdAt\": \"2010-09-16T12:49:50Z\"
;;     }
;;   }
;; }")))))

(defn formated-keys
  [keys]
  (map #(str/replace % "\"" "")
  (map str/trim
  (map #(first %) keys))))

(defn formated-values
  [values]
  (map #(str/replace % "\"" "")
  (map #(str/replace % "," "")
  (map str/trim
  (map #(second %) values)))))

(defn format-data
  [fetched-data]
  (walk/keywordize-keys (zipmap
                         (formated-keys fetched-data)
                         (formated-values fetched-data))))

;; (insert-data data)

(defroutes app-routes
  (GET "/" [] "Here come the related graphs")
  ;; (GET "/photos" photos)
  (GET "/new" [query] (insert-data (format-data (filter-data query))))
  (GET "/git" [] (response sql-data))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults (wrap-json-response app-routes) site-defaults))
