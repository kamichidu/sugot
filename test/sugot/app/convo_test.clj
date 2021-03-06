(ns sugot.app.convo-test
  (:require [clojure.test :refer :all]
            [sugot.app.convo :refer :all]
            [sugot.lib :as l]
            [sugot.models])
  (:import [org.bukkit.craftbukkit Main]
           [org.bukkit Bukkit]
           [org.bukkit.entity Player]
           [sugot.models Loc P]))

(deftest replacefirst-go-test
  (testing "a word"
    (is (= [nil "benri"]
           (@#'sugot.app.convo/replacefirst-go "n" "ん" "benri")))
    (is (= ["べ" "nri"]
           (@#'sugot.app.convo/replacefirst-go "be" "べ" "benri")))))

(deftest AsyncPlayerChatEvent-test
  (testing "english->hiragana"
    (is (= 1 1)))

  (testing "notifies to lingr, after converting"
    (with-redefs [l/post-lingr (fn [msg] {:msg msg})]
      (is (= {:msg "<dummy-player> あ"}
             (AsyncPlayerChatEvent (org.bukkit.event.player.AsyncPlayerChatEvent. true nil "a" (java.util.HashSet.))
                                   (P. "dummy-player" nil nil)))))))

#_ (defn fixture [f]
  (future (Main/main (make-array String 0)))

  ; call `f` once server is ready.
  (loop [server nil]
    (Thread/sleep 100)
    (if server
      (f)
      (recur (try (Bukkit/getServer) (catch Exception e nil)))))

  ; dirty hack: wait until report completes.
  (future
    (Thread/sleep 100)
    (System/exit 0)))

#_ (use-fixtures :once fixture)
