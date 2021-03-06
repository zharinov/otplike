(ns otplike.proc-util
  (:require [clojure.core.async :as async]
            [clojure.core.match :refer [match]]
            [otplike.process :as process]))

;; ====================================================================
;; API

(defmacro ^:no-doc current-line-number []
  (:line (meta &form)))

(defmacro execute-proc
  "Executes `body` in a newly created process context. Returns channel
  which will receive the result."
  [& body]
  `(let [done# (async/chan)]
     (process/spawn-opt
       (process/proc-fn
         []
         (try
           (let [res# (do ~@body)]
             (async/put! done# [:ok res#]))
           (catch Throwable t#
             (async/put! done# [:ex t#]))))
       {:name (str "execute-proc:" (.getName *ns*) ":" (current-line-number))})
     done#))

(defmacro execute-proc!
  "Executes `body` in a newly created process context. Parks waiting
  for the result."
  [& body]
  `(match (async/<! (execute-proc ~@body))
     [:ok res#] res#
     [:ex e#] (throw e#)))

(defmacro execute-proc!!
  "The same as `execute-proc!` but blocks."
  [& body]
  `(match (async/<!! (execute-proc ~@body))
     [:ok res#] res#
     [:ex e#] (throw e#)))

(defmacro defn-proc
  "Defines function with name `fname`, arguments `args`. `body` is
  executed in a newly created process context."
  [fname args & body]
  `(defn ~fname []
     (let [done# (async/chan)]
       (process/spawn
         (process/proc-fn
           ~args
           (try
             (let [res# (do ~@body)]
               (when (some? res#) (async/>! done# res#)))
             (finally
               (async/close! done#)))))
       (async/<!! done#))))
