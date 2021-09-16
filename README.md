# Notification Server

### Info

 1.  feature - restapi, db connection(jpa), fcm web push server
 2.  dependancy - springboot(2.4.10), lombok, jpa, firebase-admin

---
### Build & Run
    ./gradlew clean build --exculde-task=test
    java -jar build/libs/fcm-0.0.1-SNAPSHOT.jar
---

### Preference
 1. Fire Base Admin Documentation : [FCM SDK](https://firebase.google.com/docs/reference/admin/java/reference/com/google/firebase/messaging/Message.Builder.html)
 2. Message Format : [Message Format](https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages)
