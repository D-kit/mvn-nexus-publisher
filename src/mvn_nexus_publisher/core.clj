(ns mvn-nexus-publisher.core
  (:gen-class)
  (:require [me.raynes.conch :refer [programs with-programs let-programs] :as sh]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(defn parse-deps-tree-file
  "lein classpath > deps.txt saves whole classpath for project.
  this fn takes deps.txt and parse it.
  returns vector of deps."
  [^String classpath-file]
  (try 
    (if-let [content (slurp classpath-file)]
      (if-let [jars-str (second (str/split content #"classes:"))]
        (mapv (fn [x] (str/trim x)) (str/split jars-str #":"))
        (println "can't find classpath begining using regex \"classes:\"")))
    (catch Exception e
      (throw (ex-info "can't parse deps file" {:path classpath-file :ex e})))))

(defn copy-file
  [source-path dest-path]
  (io/copy (io/file source-path) (io/file dest-path)))

(defn pom-jar-temp-copy
  "create temp copy of pom and jar for given jar file name from .m2
  return map with paths for created temp files {:jar _ :pom _}"
  [^String jar-file ^String tmp-folder]
  (try
    (let [tmp-jar-file (str tmp-folder (.getName (io/file jar-file)))
          pom-file (str (first (str/split jar-file #".jar$")) ".pom")
          tmp-pom-file (str tmp-folder (.getName (io/file pom-file)))]
      (copy-file jar-file tmp-jar-file)
      (copy-file pom-file tmp-pom-file)
      {:pom tmp-pom-file :jar tmp-jar-file})
    (catch Exception e
      (throw (ex-info "can't create jar temp copy" {:path jar-file :tmp-folder tmp-folder :ex e})))))

(defn pom-jar-temp-delete
  "delete previously created temp pom and jar files."
  [^String tmp-jar-file ^String tmp-pom-file]
  (try
    (.delete (io/file tmp-jar-file))
    (.delete (io/file tmp-pom-file))
    (catch Exception e
      (throw (ex-info "can't delete jar temp copy" {:ex e})))))

(defn publish-to-nexus
  "perform publishing jar to local nexus repo.
  mvn command should be available in PATH."
  [^String tmp-jar-file ^String tmp-pom-file ^String mvn-settings-file ^String repo-id ^String repo-uri]
  (try
    (let [writer (java.io.StringWriter.)]
      (with-programs [mvn]
        (mvn "deploy:deploy-file" "-s" mvn-settings-file (str "-Dfile=" tmp-jar-file)
             (str "-DpomFile=" tmp-pom-file) (str "-DrepositoryId=" repo-id)
             (str "-Durl=" repo-uri) "-DcreateChecksum=true" {:out writer})
        (println (str writer))))
    (catch Exception e
      (throw (ex-info "can't publish to nexus" {:tmp-jar tmp-jar-file  :tmp-pom tmp-pom-file :ex e
                                                :settings mvn-settings-file :repo-id repo-id
                                                :repo-uri repo-uri})))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (if (not= 5 (count args))
    (do
      (println "Usage: mvn-nexus-publisher <classpath-file> <path-to-settings.xml> <repo-id> <repo-uri> <tmp-folder>")
      (println "To create classpath file call: lein deps && lein classpath > deps.txt")
      (println "settings.xml should be with user and password for given repo-id")
      (println "tmp folder will be used to store temporary files.")
      (System/exit 1))
    (let [args         (into [] args)
          deps-file    (args 0)
          settings-xml (args 1)
          repo-id      (args 2)
          repo-uri     (args 3)
          tmp-folder   (args 4)]
      (println deps-file settings-xml repo-id repo-uri tmp-folder)
      (doseq [jar (parse-deps-tree-file deps-file)]
        (println "publishing: " jar)
        (when-let [temp-files (pom-jar-temp-copy jar tmp-folder)]
          (publish-to-nexus (:jar temp-files) (:pom temp-files) settings-xml repo-id repo-uri)
          (pom-jar-temp-delete (:jar temp-files) (:pom temp-files))))
      (System/exit 0))))
