(ns hiccup-templating.views.contents
  (:use [hiccup.page])
  (:use [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn index []
  [:div {:class "ui page grid"}
    [:div {:class "row"}
      [:div {:class "center aligned starter column"}
        [:h1 {:class "ui header"} "CSV to Illustrator variable export"]
        [:p "A handy tool to convert CSVs to XML files that Adobe Illustrator can use as variable imports."]
        [:p "For this to work properly, please upload the default comma-separated CSV format."]
        [:form {:method "POST" :enctype "multipart/form-data" :action "/process" :class "ui form"}
          (anti-forgery-field)
          [:input {:type "file" :name "file"}]
          [:button {:type "submit" :class "ui orange submit button"} "Generate Illustrator variables"]]]]])
