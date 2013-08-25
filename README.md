FeedBackYourSummerCamp
=============================================

A "hello world" application for Google Cloud Endpoints in Java.
Produced for the Jug Summer Camp conference.

## Products
- [App Engine]
- [AngularJS]

## Language
- [Java]
- [Javascript]

## APIs
- [Google Cloud Endpoints]
- [Google App Engine Maven plugin] (need Maven 3.1)

## Setup Instructions
1. Update the value of `application` in `appengine-web.xml` to the app ID you
   have registered in the App Engine admin console and would like to use to host
   your instance of this sample.
1. `mvn clean install`
1. Run the application with `mvn appengine:devserver`, and ensure it's running
   by visiting your local server's address (by default [localhost:8080].)
1. Get the client library with `mvn appengine:endpoints_get_client_lib` (it will generate a zip file named `helloworld-v1-java.zip` in the root of your project.)
1. Deploy your application with `mvn appengine:update`

