(defproject illustrator_xml "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [hiccup "1.0.5"]
                 [clojure-csv/clojure-csv "2.0.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [ring/ring-defaults "0.1.2"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler illustrator-xml.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
