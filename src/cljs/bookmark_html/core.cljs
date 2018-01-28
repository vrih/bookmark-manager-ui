;;; If this namespace requires macros, remember that ClojureScript's
;;; macros are written in Clojure and have to be referenced via the
;;; :require-macros directive where the :as keyword is required, while in Clojure is optional. Even
;;; if you can add files containing macros and compile-time only
;;; functions in the :source-paths setting of the :builds, it is
;;; strongly suggested to add them to the leiningen :source-paths.
(ns bookmark-html.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [goog.style :as style]
            [cljs.core.async :refer [put! chan <!]]
            [clojure.string :as string]))

(extend-type js/NodeList
  ISeqable
(-seq [array] (array-seq array 0)))

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type
                   (fn [e] (put! out e)))
    out))

(defn prepare-ddg [x]
  (str "https://duckduckgo.com/?q=" x "&ia=web"))

(defn search-for-input "Search DDG for x"
  [x]
  (-> js.document
      .-location
      (set! (prepare-ddg x))))


(let [keypresses (listen (dom/getElement "search") "keypress")]
  (go (while true
        (let [key-event (<! keypresses)]
          (when (= (.-charCode key-event) 13)
            (do
              (.-preventDefault key-event)
              (let [filterVal (.-value (dom/getElement "search"))
                    mediaElements (dom/getElementsByClass "bm")]
                (if (= filterVal "all")
                  (doseq [el mediaElements] (style/showElement el true))
                  (let [filterElements (dom/getElementsByClass filterVal)]
                    (if (= 0 (count filterElements))
                      (search-for-input filterVal)
                      (do
                        (doseq [el mediaElements] (style/showElement el false))
                        (doseq [el (dom/getElementsByClass filterVal)] (style/showElement el true)))))))))))))
  


