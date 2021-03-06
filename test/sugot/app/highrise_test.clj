(ns sugot.app.highrise-test
  (:require [clojure.test :refer :all]
            [sugot.app.highrise :refer :all]
            [sugot.lib :as l])
  (:import [org.bukkit Location]
           [sugot.models SugotWorld SugotLocation]))

(defprotocol SugotCreatureSpawnEvent
  (getEntity [this])
  (getSpawnReason [this])
  (getLocation [this])
  (isCancelled [this])
  (setCancelled [this bool]))

(deftest a-test
  (testing "prevent spawning monster at high space"
    (let [entity nil
          reason org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason/NATURAL
          world (SugotWorld. "world")
          cancelled (ref false)
          event (reify SugotCreatureSpawnEvent
                  (getEntity [this] entity)
                  (getSpawnReason [this] reason)
                  (getLocation [this] (SugotLocation. world 0 120 0))
                  (setCancelled [this bool]
                    (dosync
                      (ref-set cancelled true))))]
      (is (= true
             (do
               (CreatureSpawnEvent event)
               @cancelled))))))
