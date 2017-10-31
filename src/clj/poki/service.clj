(ns poki.service
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [poki.game :as game]
            [ring.middleware.cors :refer [wrap-cors]]))

(defroutes my-routes
           (GET "/" []
                (str (game/random-dice)))
           (route/not-found "<h1>Page not found</h1>"))

(def app
  (wrap-cors my-routes :access-control-allow-origin [#"http://localhost:3449"]
             :access-control-allow-methods [:get]))