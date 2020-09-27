# Django-REST-Framework---Android

Android Integration for Django REST Framework

[ ![Download](https://api.bintray.com/packages/civilmachines/Django-REST-Framework/DRFAPI/images/download.svg?version=0.0.2) ](https://bintray.com/civilmachines/Django-REST-Framework/DRFAPI/0.0.2/link)

## Gradle

In your module (app-level) Gradle file (usually `app/build.gradle`), Add this
line inside the `dependencies` block.

```java
dependencies {
    ...
    implementation 'com.civilmachines.drfapi:drfapi:0.0.2'
}
```

## Usage

Register your Volley `SingletonFile` to your `AndroidManifest.xml`. You can copy
the sample code from
[VolleySingleton Gist](https://gist.github.com/iamvivekkaushik/b0608ff18902696051856c41f3e7e332)

```xml
<application
  android:name=".VolleySingleton"
  ...
    <activity>
    ...
    </activity>
</application>
```

## JAVA

For JSONObject, you can use `DjangoJSONObjectRequest`

```java
DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(Request.Method.POST, "url", requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Response Received
                    }
                }, new DjangoErrorListener() {
                // Override Methods here
            @Override
            public void defaultErrorListener(String message) {
                super.defaultErrorListener(message);
            }
        }, this);

request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
VolleySingleton.getInstance(this).getRequestQueue().add(request);
```

For JSONArray, you can use `DjangoJSONArrayResponseRequest`

```java
DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(Request.Method.GET, "url", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Response Received
                    }
                }, new DjangoErrorListener() {
                // Override Methods here
                @Override
                public void defaultErrorListener(String message) {
                    super.defaultErrorListener(message);
              }
        }, this);
request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
VolleySingleton.getInstance(this).getRequestQueue().add(request);
```

If you need to pass some data with request, you can create a JSONObject and pass
it in the request.

```java
JSONObject requestData = new JSONObject();
requestData.put("key", "value");
```

This library uses shared preferences for Authentication Token, just save your
token using the below code and it will be set to header automatically.

```java
UserSharedPreferenceAdapter usrAdapter = new UserSharedPreferenceAdapter(this);
usrAdapter.saveToken("token");

// Use the get token method to retrieve the token
String token = usrAdapter.getToken();
```

## Author

- [Himanshu Shankar](https://himanshus.com)
