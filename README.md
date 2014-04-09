# clj-twillio

Note this began as a fork of github.com/owainlewis/twilio

Twilio wrapper library for Clojure.

```
[clj-twilio "0.0.1"]
```

## Usage

More to follow. Note that the API is not considered stable yet.

<!-- ### SMS MESSAGES -->

<!-- First require the Twilio sms ns -->

<!-- ``` -->
<!-- (:require [twilio.core :as twilio])) -->
<!-- ``` -->

<!-- Send an SMS message -->

<!-- ```clojure -->
<!-- (twilio/with-auth "your-sid" "your-auth-token" -->
<!--   (twilio/send -->
<!--     {:From "+442033222504" -->
<!--      :To "+447846012894" -->
<!--      :Body "Hello world"})) -->
<!-- ``` -->

<!-- Since uppercase key names hurt my eyes, there's a helper function to make things cleaner -->

<!-- ```clojure -->
<!-- (def my-sms-message -->
<!--   (sms "+442033222504" ;; from -->
<!--        "+447846012894" ;; to -->
<!--        "OH HAI!"))     ;; message -->

<!-- ;; Send the message -->

<!-- (twilio/with-auth "your-sid" "your-auth-token" -->
<!--   (twilio/send my-sms-message)) -->

<!-- ``` -->

<!-- Fetch all your sent messages -->

<!-- ```clojure -->
<!-- (twilio/with-auth "your-sid" "your-auth-token" -->
<!--   (twilio/get-messages)) -->
<!-- ``` -->

## License

Copyright © 2014 Trevis Elser

Distributed under the Eclipse Public License, the same as Clojure.


The code this was forked from:

Copyright © 2013 Twilio Owain Lewis

Distributed under the Eclipse Public License, the same as Clojure.
