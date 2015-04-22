(ns hiccup-templating.views.layout
  (:use [hiccup.page]))

(defn application [title & content]
  (html5
    [:head
    [:title title]
    (include-css "//cdnjs.cloudflare.com/ajax/libs/semantic-ui/1.12.0/semantic.min.css")
    (include-css "main.css")
    (include-js "//cdnjs.cloudflare.com/ajax/libs/semantic-ui/1.12.0/semantic.min.js")
    [:body
      [:div {:class "container"} content]
    ]
    ]
  )
)
