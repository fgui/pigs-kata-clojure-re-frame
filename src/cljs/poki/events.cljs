(ns poki.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [poki.db :as db]
            [poki.game :as game]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))


(re-frame/reg-event-db
  ::roll
  (fn  [db _]
    (game/do-dice db game/random-dice)))

(re-frame/reg-event-fx ;; <-- note the `-fx` extension
  ::roll-remote        ;; <-- the event id
  (fn                  ;; <-- the handler function
    [{db :db} _]       ;; <-- 1st argument is coeffect, from which we extract db
    ;; we return a map of (side) effects
    {:http-xhrio {:method          :get
                  :uri             "http://localhost:3000"
                  :response-format (ajax/text-response-format)
                  :on-success      [::roll-received]
                  :on-failure      [::bad-response]}
     :db  (assoc db :loading? true)}))

(re-frame/reg-event-db
  ::roll-received
  (fn  [db [_ response]]
    (let [dice-value (js/parseInt response)]
      (game/roll-done db dice-value))))

(re-frame/reg-event-db
  ::bad-response
  (fn  [db [_]]
    db))

(re-frame/reg-event-db
  ::hold
  (fn  [db _]
    (game/do-hold db)))