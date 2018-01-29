;;; ****************************** NOTES ******************************
;;; Defines four profiles:
;;;
;;; - :shared
;;; - :dev
;;; - :simple
;;; - :advanced
;;;
;;; the :dev, :simple and :advanced profiles are composite profiles,
;;; meaning that they share the content of :shared profile.
;;; *******************************************************************

{:shared {:clean-targets ["out" :target-path]
          :test-paths ["test/clj" "test/cljs"]
          :resources-paths ["dev-resources"]
;          :plugins [[com.cemerick/clojurescript.test "0.3.1"]]
          :cljsbuild
          {:builds {:bookmark-html
                    {:source-paths ["test/cljs"]
                     :compiler
                     {:output-dir "dev-resources/public/js"
                      :source-map "dev-resources/public/js/bookmark_html.js.map"}}}
           :test-commands {"phantomjs"
                           ["phantomjs" :runner "dev-resources/public/js/bookmark_html.js"]}}}
 :dev [:shared
       {
        :cljsbuild
        {:builds {:bookmark-html
                  {
                   :compiler
                   {:optimizations :whitespace
                    :pretty-print true}}}}
        
        }]
 ;; simple profile.
 :simple [:shared
          {:cljsbuild
           {:builds {:bookmark-html
                     {:compiler {:optimizations :simple
                                 :pretty-print false}}}}}]
 ;; advanced profile
 :advanced [:shared
            {:cljsbuild
             {:builds {:bookmark-html
                       {:source-paths ["test/cljs"]
                        :compiler
                        {:optimizations :advanced
                         :pretty-print false}}}}}]}

