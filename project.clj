(defproject photos "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.4.0"]
                 [clj-http "3.9.1"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [java-jdbc/dsl "0.1.3"]
                 [org.xerial/sqlite-jdbc "3.23.1"]
                 [clj-time "0.14.4"]
                 ]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler photos.handler/app :port 5000}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]
                        ]}})
