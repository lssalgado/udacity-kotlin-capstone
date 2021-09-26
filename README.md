## Political Preparedness

PolitcalPreparedness is an example application built to demonstrate core Android Development skills as presented in the Udacity Android Developers Kotlin curriculum. 

This app demonstrates the following views and techniques:

* [Retrofit](https://square.github.io/retrofit/) to make api calls to an HTTP web service.
* [Moshi](https://github.com/square/moshi) which handles the deserialization of the returned JSON to Kotlin data objects. 
* [Glide](https://bumptech.github.io/glide/) to load and cache images by URL.
* [Room](https://developer.android.com/training/data-storage/room) for local database storage.
  
It leverages the following components from the Jetpack library:

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) with binding adapters
* [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) with the SafeArgs plugin for parameter passing between fragments


## How to setup your API Key
Open `local.properties` located at project's root and add a variable named `API_KEY` with your key like so:

`API_KEY=THATSNOTAVALIDAPIKEY`

After that all you need to do is build and run the project :)

## Setting up the Repository

To get started with this project, simply pull the repository and import the project into Android Studio. From there, deploy the project to an emulator or device. 

* NOTE: In order for this project to pull data, you will need to add your API Key to the project as a value in the CivicsHttpClient. You can generate an API Key from the [Google Developers Console](https://console.developers.google.com/)

## Getting Started

* For the most part, the TODOs in the project will guide you through getting the project completed. There is a general package architecture and *most* files are present. 
* Hints are provided for tricky parts of the application that may extend beyond basic Android development skills.
* As databinding is integral to the project architecture, it is important to be familiar with the IDE features such s cleaning and rebuilding the project as well as invalidating caches. 

## Suggested Workflow

- [x] It is recommend you save all beautification until the end of the project. Ensure functionality first, then clean up UI. While UI is a component of the application, it is best to deliver a functional product.
- [x] Start by getting all screens in the application to navigate to each other, even with dummy data. If needed, comment out stub code to get the application to compile. You will need to create actions in *nav_graph.xml* and UI elements to trigger the navigation. 
- [x] Create an API key and begin work on the Elections Fragment  and associated ViewModel. 
  - [x] Use the elections endpoint in the Civics API and requires no parameters.
  - [x] You will need to create a file to complete the step.
  - [x] This will require edits to the Manifest.
  - [x] Link the election to the Voter Info Fragment.
- [x] Begin work on the Voter Info Fragment and associated ViewModel.
- [x] Begin work on the Representative Fragment and associated ViewModel.
  - [x] This will require edits to Gradle.
  - [x] You will need to create a file to complete the step.
- [x] Remove all TODOs from the code.
- [x] Remove all non error debug logs.
- [x] Fix Representatives fragment on landscape mode.
- [x] Fix VoterInfo links.
- [x] Extract resources.


## Rubric
- [x] Android UI/UX
  - [x] Build a navigable interface consisting of multiple screens of functionality and data.
    - [x] Application includes at least three screens with distinct features using either the Android Navigation Controller or Explicit Intents.
    - [x] The Navigation Controller is used for Fragment-based navigation and intents are utilized for Activity-based navigation.
    - [x] An application bundle is built to store data passed between Fragments and Activities.
  - [x] Construct interfaces that adhere to Android standards and display appropriately on screens of different size and resolution.
    - [x] Application UI effectively utilizes ConstraintLayout to arrange UI elements effectively and efficiently across application features, avoiding nesting layouts and maintaining a flat UI structure where possible.
    - [x] Data collections are displayed effectively, taking advantage of visual hierarchy and arrangement to display data in an easily consumable format.
    - [x] Resources are stored appropriately using the internal res directory to store data in appropriate locations including string* values, drawables, colors, dimensions, and more.
    - [x] Every element within ConstraintLayout should include the id field and at least 1 vertical constraint.
    - [x] Data collections should be loaded into the application using ViewHolder pattern and appropriate View, such as RecyclerView.
  - [x] Animate UI components to better utilize screen real estate and create engaging content.
    - [x] Application contains at least 1 feature utilizing MotionLayout to adapt UI elements to a given function. This could include animating control elements onto and off screen,   displaying and hiding a form, or animation of complex UI transitions.
    - [x] MotionLayout behaviors are defined in a MotionScene using one or more Transition nodes and ConstraintSet blocks.
    - [x] Constraints are defined within the scenes and house all layout params for the animation.
- [x] Local and Network data
  - [x] Connect to and consume data from a remote data source such as a RESTful API.
    - [x] The Application connects to at least 1 external data source using Retrofit or other appropriate library/component and retrieves data for use within the application.
    - [x] Data retrieved from the remote source is held in local models with appropriate data types that are readily handled and manipulated within the application source. Helper libraries such as Moshi may be used to assist with this requirement.
    - [x] The application performs work and handles network requests on the appropriate threads to avoid stalling the UI.
  - [x] Load network resources, such as Bitmap Images, dynamically and on-demand.
    - [x] The Application loads remote resources asynchronously using an appropriate library such as Glide or other library/component when needed.
    - [x] Images display placeholder images while being loaded and handle failed network requests gracefully.
    - [x] All requests are performed asynchronously and handled on the appropriate threads.
  - [x] Store data locally on the device for use between application sessions and/or offline use.
    - [x] The application utilizes storage mechanisms that best fit the data stored to store data locally on the device. Example: SharedPreferences for user settings or an internal database for data persistence for application data. Libraries such as Room may be utilized to achieve this functionality.
    - [x] Data stored is accessible across user sessions.
    - [x] Data storage operations are performed on the appropriate threads as to not stall the UI thread.
    - [x] Data is structured with appropriate data types and scope as required by application functionality.
- [x] Android system and hardware integration
  - [x] Architect application functionality using MVVM.
    - [x] Application separates responsibilities amongst classes and structures using the MVVM Pattern:
      - [x] Fragments/Activities control the Views
      - [x] Models houses the data structures,
      - [x] ViewModel controls business logic.
    - [x] Application adheres to architecture best practices, such as the observer pattern, to prevent leaking components, such as Activity Contexts, and efficiently utilize system resources.
  - [x] Implement logic to handle and respond to hardware and system events that impact the Android Lifecycle.
    - [x] Beyond MVVM, the application handles system events, such as orientation changes, application switching, notifications, and similar events gracefully including, but not limited to:
      - [x] Storing and restoring state and information
      - [x] Properly handling lifecycle events in regards to behavior and functionality
        - [x] Implement bundles to restore and save data
      - [x] Handling interaction to and from the application via Intents
      - [x] Handling Android Permissions
  - [x] Utilize system hardware to provide the user with advanced functionality and features.
    - [x] Application utilizes at least 1 hardware component to provide meaningful functionality to the application as a whole. Suggestion options include:
      - [ ] Camera
      - [x] Location
      - [ ] Accelerometer
      - [ ] Microphone
      - [ ] Gesture Capture
      - [ ] Notifications
    - [x] Permissions to access hardware features are requested at the time of use for the feature.
    - [x] Behaviors are accessed only after permissions are granted.
- [ ] Extras
  - [ ] As with any mobile application, attention to detail within the UI, including animations within the screens and/or while navigating will elevate the application presentation as a whole. Proper use of visual hierarchy and consistent implementation with Styles can assist in elevating the experience. Ensuring screen real estate is properly utilized, but not overburdened will provide a positive user experience.
  - [x] Caching data, when possible, to provide some level of application functionality when offline and/or to reduce the network burden of the application can help demonstrate and mirror real-world application goals found in many enterprise applications at scale. As such, elevate your project by utilizing local storage and caching on network requests when it would not deter from the application experience. Providing users with choice and customization through Shared Preferences is a great way to balance real-time data vs possible performance gains by giving power to the user.
  - [ ] The mobile experience is all about personal needs and convenient access. The features of the application should reflect a personal need and provide functionality and features that reflect the solution to that need. When possible, think about the following considerations:
    - [x] Does the application work for multiple users?
    - [ ] Does the application provide value over a website or similar static content?
    - [x] Does the application provide a coherent user experience that effectively and intuitively guides the user’s behavior?

## Report Issues
Notice any issues with a repository? Please file a github issue in the repository.
