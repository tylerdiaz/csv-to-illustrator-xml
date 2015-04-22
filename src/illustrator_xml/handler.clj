(ns illustrator-xml.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure-csv.core :as csv]
            [clojure.data.xml :as xml]
            [hiccup-templating.views.layout :as layout]
            [hiccup-templating.views.contents :as contents]
            [ring.util.response :as r]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn create-variable
  [name]
  [:variable {:varName name :trait "textcontent" :category "&ns_flows;"}])

(defn create-data-row [k v] [k [:p v]])

(defn create-data-set
  [headers rows]
  ["v:sampleDataSet" {:dataSetName (first rows)}
    (map create-data-row headers rows)])

(defn csv->illustrator-xml
  [csv]
  (xml/indent-str
    (xml/sexp-as-element
      [:svg
        [:variableSets {:xmlns "&ns_vars;"}
          [:variableSet {:locked "none" :varSetName "binding1"}
            [:variables (map create-variable (first csv)) ]]
          ["v:sampleDataSets" {"xmlns" "&ns_custom;" "xmlns:v" "&ns_vars;"}
            (map #(create-data-set (first csv) %1) (rest csv))]]])))

(defn unescape-amp
  [str]
  (clojure.string/replace str #"&amp;" "&"))

(defn add-illutrator-metadata
  "janky solution incoming. Adobe Illustrator needs these or it refuses to parse the file."
  [xml-str]
  (clojure.string/replace
    xml-str
    #"(\<\?xml*.+\?\>)"
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20001102//EN\"    \"http://www.w3.org/TR/2000/CR-SVG-20001102/DTD/svg-20001102.dtd\" [ <!ENTITY ns_graphs \"http://ns.adobe.com/Graphs/1.0/\"> <!ENTITY ns_vars \"http://ns.adobe.com/Variables/1.0/\"> <!ENTITY ns_imrep \"http://ns.adobe.com/ImageReplacement/1.0/\"> <!ENTITY ns_custom \"http://ns.adobe.com/GenericCustomNamespace/1.0/\"> <!ENTITY ns_flows \"http://ns.adobe.com/Flows/1.0/\"> <!ENTITY ns_extend \"http://ns.adobe.com/Extensibility/1.0/\"> ]>"
    ))

(defn process-csv
  [params]
    (->>
      (slurp (get-in params [:file :tempfile]))
      csv/parse-csv
      csv->illustrator-xml
      unescape-amp
      add-illutrator-metadata))

(defroutes app-routes
  (GET "/" [] (layout/application "CSV to Adobe Illustrator variable XML file" (contents/index)))
  (POST "/process" {params :params} (process-csv params))
  (route/not-found "404"))

(def app
  (wrap-defaults app-routes site-defaults))
