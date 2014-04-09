(ns clj-twilio.core
  (import [java.net URLEncoder])
  (:require [org.httpkit.client :as http]
            [clojure.string :as str]
            [clojure.core.typed :as t]))

(t/ann base String)
(def base "https://api.twilio.com/2010-04-01")

;; Authentication info
(t/tc-ignore
 (def ^:dynamic *sid*   ""))
(t/tc-ignore
 (def ^:dynamic *token* ""))

(t/tc-ignore
 (defn env [var] (System/getenv var)))

(t/ann load-auth-from-env (Fn [-> (t/Map clojure.lang.Keyword String)]))
(defn load-auth-from-env []
  (let [sid (env "TWILIO_SID")
        token (env "TWILIO_AUTH_TOKEN")]
   {:sid sid :token token}))

(t/tc-ignore
 (defn load-auth-from-config []
   (binding [*read-eval* false]
     (with-open [r (clojure.java.io/reader "config.clj")]
       (read (java.io.PushbackReader. r))))))

;; Helper macro

(t/tc-ignore
 (defmacro with-auth
   [account_sid auth_token & body]
   `(binding [*sid* ~account_sid
              *token* ~auth_token]
      (do ~@body))))

(t/tc-ignore
 (defn encode-url [url]
   (URLEncoder/encode url)))

(t/ann make-request-url (Fn [String -> String]))
(defn make-request-url [endpoint]
  (format "%s/Accounts/%s/SMS/%s.json"
    base
    *sid*
    endpoint))

;; HTTP requests
(t/tc-ignore
 (defn request
   "Make a simple POST request to the API"
   [method url & params]
   (assert ((complement every?) str/blank? [*sid* *token*]))
   (let [request-params (into {} params)]
     (try
       (http/request
        {:method method
         :url url
         :form-params request-params
         :basic-auth [*sid* *token*]})
       (catch Exception e
         {:error e})))))

(t/tc-ignore
 (deftype SMS [from to body]))

;; Utils
(t/tc-ignore
 (def twilio-format-key
   (comp keyword str/capitalize name)))

(t/tc-ignore
 (defn as-twilio-map [m]
   "Twilio defines ugly uppercase keys for the api i.e :from becomes :From
   so this helper transforms clojure keys without making my eyes bleed"
   (reduce
    (fn [acc [k v]]
      (conj acc {(twilio-format-key k) v})) {} m)))

(t/ann sms (Fn [String String String -> (t/Map clojure.lang.Keyword String)]))
(defn sms
  "Create an SMS message"
  [from to body]
  (as-twilio-map
    {:body body
     :to to
     :from from}))

;; (def msg
;;   (sms "+442033222504"
;;        "+447846012894"
;;        "OH HAI!"))

;; (def picture-msg
;;   {:From "+442033222504"
;;    :To "+447846012894"
;;    :Body "Hi"
;;    :MediaUrl "http://cdn.cutestpaw.com/wp-content/uploads/2011/12/Christmas-Balbinka-l.jpg"})

;; Send an SMS message via Twilio
;; *************************************************

(t/tc-ignore
 (defn send-sms
   "Send an SMS message which is a map in the form {:From x :To x :Body x}"
   [msg]
   (let [url (make-request-url "Messages")]
     (request :post url msg))))

;; *************************************************

(t/tc-ignore
 (defn get-messages
   "Fetch all messages sent from your account"
   []
   (if-let [response (request :get (make-request-url "Messages"))]
     (get-in response [:body :sms_messages]))))

(t/tc-ignore
 (defn verify-phone
   "Verify a phone number for making calls"
   [num]
   (assert ((complement every?) str/blank? [*sid* *token*]))
   (let [url (str base "/Accounts/" *sid* "/OutgoingCallerIds.json")
         params {:PhoneNumber num}]
     (try
       (http/request
        {:method :post
         :url url
         :form-params params
         :basic-auth [*sid* *token*]})
       (catch Exception e
         {:error e})))))

(t/tc-ignore
 (defn make-call
   "Make a phone call"
   [params]
   (assert ((complement every?) str/blank? [*sid* *token*]))
   (let [request-params (into {} params)
         url (str base "/Accounts/" *sid* "/Calls.json")]
     (try
       (http/request
        {:method :post
         :url url
         :form-params request-params
         :basic-auth [*sid* *token*]})
       (catch Exception e
         {:error e})))))
