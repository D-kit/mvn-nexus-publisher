(defproject mvn-nexus-publisher "0.3.1-SNAPSHOT"
  :description "Tool for publishing project dependencies tree to local Nexus repository."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [me.raynes/conch "0.8.0"]]
  :main ^:skip-aot mvn-nexus-publisher.core
  :target-path "target/%s"
  :plugins [[lein-deps-tree "0.1.2"]]
  :profiles {:uberjar {:aot :all}})
