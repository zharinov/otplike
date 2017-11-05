(def core-async-version "0.3.443")

(defproject otplike/otplike "0.3.0-alpha-SNAPSHOT"
  :description "Erlang/OTP like processes and behaviours on top of core.async"
  :license {:name "Eclipse Public License - v1.0"
            :url  "https://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async ~core-async-version]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [clojure-future-spec "1.9.0-alpha14"]]

  :source-paths  ["src"]

  ;:main otplike.example.benchmarks
  ;:source-paths  ["src" "examples"]

  :profiles {:parallel-test
             {:dependencies [[org.clojure/core.async ~core-async-version]
                             [org.clojure/math.combinatorics "0.1.4"]]}

             :test
             {:dependencies [[org.clojure/math.combinatorics "0.1.4"]]}

             ;:uberjar {:aot :all}

             :repl
             {:source-paths  ["src" "examples"]}}

  :codox {:source-paths ["src"]
          :namespaces [#"^(?!otplike.spec-util)"]}

  :test-selectors {:parallel :parallel
                   :serial :serial})
