# Food_DeliveryApp
An E-Commerce android application that makes users can demand theire food easly
## Architecture

MVVM stands for Model-View-ViewModel, which is a design pattern used in software development, particularly in the context of user interfaces (UI). MVVM separates the concerns of presentation logic from the UI, resulting in a more modular and maintainable codebase.

![mvvm-architecture-app-in-android](https://github.com/InfoGenies/Food_DeliveryApp/assets/133220437/09a368a4-a565-430e-b49d-c364b41b1d46)

## Built With ![technical-service](https://github.com/InfoGenies/Food_DeliveryApp/assets/133220437/17f6b7f6-2e0f-453f-bb0c-1be8fa0fa464)

* [kotlin](https://kotlinlang.org/) : A modern programming language for Android development
* [Android Architecture Components](https://developer.android.com/topic/architecture) : Collection of libraries that help you design robust, testable, and maintainable apps .
   - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) : Stores UI-related data that isn't destroyed on UI changes .
   - [Navigation component](https://developer.android.com/guide/navigation) : Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app.
   - [Room Database](https://developer.android.com/training/data-storage/room) : Room is a library to save data in a local database .
   - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) : Data objects that notify views when the underlying database changes .
   - [Data Binding](https://developer.android.com/topic/libraries/architecture/livedata) : It helps in declaratively binding UI elements to in our layout to data sources of our app.
* [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) : Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
* [Kotlin coroutines](https://developer.android.com/kotlin/coroutines) : Coroutine is a concurrency design pattern that you can use on Android to simplify code that executes asynchronously. 
* [SDP Library](https://github.com/intuit/sdp) : An android lib that provides a new size unit - sdp (scalable dp). This size unit scales with the screen size. It can help Android developers with supporting multiple screens.
* [Glide](https://github.com/bumptech/glide) : Glide is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface.
* [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk) : Build dynamic, interactive, customized maps, location, and geospatial experiences for your Android apps.
* [Firebase](https://firebase.google.com/) : Firebase is a Backend-as-a-Service (Baas). It provides developers with a variety of tools and services to help them develop quality apps, grow their user base, and earn profit. It is built on Google’s infrastructure
* [Lottie](https://github.com/airbnb/lottie-android) : Lottie is a mobile library for Android and iOS that parses Adobe After Effects animations exported as json with Bodymovin and renders them natively on mobile.
## Screens
<p float="left">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Fwelcome.jpg?alt=media&token=66ee4ca3-a2fb-42ef-879b-7f4abbbe9a11" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/food%2Flogine.jpg?alt=media&token=b4f19b7a-ba11-4d21-a2a6-902d824f9776" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Fphone_authv2.jpg?alt=media&token=e35ea3f9-2a0d-4f9f-8cab-1daef78b0e08" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Fphone_auth.jpg?alt=media&token=db5b7912-276f-47c8-902d-126b868d9e23" width="200">
</p>
<p float="left">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Finfo_user.jpg?alt=media&token=064a304d-e7d0-428f-97f3-b330a86da5fc" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Faddress.jpg?alt=media&token=ed8103fc-01b5-4236-a860-58cb8921cb82" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Fsaving_info.jpg?alt=media&token=a21da4ed-c4f1-469c-8045-673e1831cc38" width="200">
   <img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/food%2Fhome2.jpg?alt=media&token=f4001b39-27a1-43bc-aefd-cdb9a5823009" width="200">
</p>
<p float="left">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/food%2Fhome3.jpg?alt=media&token=dabe507f-7d48-4138-8922-6707be303981" width="200">
   <img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/food%2Fhome1.jpg?alt=media&token=be81d512-bcc1-4ff5-964a-0c9a27cabeea" width="200">
   <img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Fdetail_food.jpg?alt=media&token=82af4e05-8ef5-4ab8-8e06-2499bdca0612" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Fcart.jpg?alt=media&token=7d6a81ce-4a03-436f-ba98-4d5edf3c5b5f" width="200">
</p>
<p float="left">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Fcheck_out.jpg?alt=media&token=a1ddb0f3-8121-4b92-90c8-dcf94d9a2b31" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Faccept_order.jpg?alt=media&token=5588e14a-d8e8-42a0-98f9-af7877cca4e3" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Ftrack_order.jpg?alt=media&token=e8967db0-b5e3-41f7-9ad0-bfc5fdc689f2" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Ffav2.jpg?alt=media&token=708962c7-8996-4318-8033-df1f752a8eb4" width="200">
</p>

<p float="left">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Ffav1.jpg?alt=media&token=1c301578-c8f2-4ed2-8069-29d388effbfb" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Fprofil.jpg?alt=media&token=f540c28c-c242-4af4-b97e-8d892eb352a9" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Fmy_orders.jpg?alt=media&token=6f92f5da-c1c8-4871-ae4d-7c01c39ca719" width="200">
<img src="https://firebasestorage.googleapis.com/v0/b/fooddelivery-ab491.appspot.com/o/Screen%2Fmy_specific_order.jpg?alt=media&token=60ccfe43-a5f8-4582-8046-cf3813c33291" width="200">
</p>
