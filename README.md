<h1>MVIExample</h1>
An alternative client for the <a href="https://anilist.gitbook.io/anilist-apiv2-docs/">Anilist API</a>.
<h2>Description</h2>
The app was designed for educational purposes. It consists of 3 screens: Login, List of animes, Anime details.

<h2>Implementation details</h2>
<h3>High level</h3>
The app based on multi-module and clean architecture approaches. App divided on 4 layers: domain, use cases, presentation, frameworks and adapters.
<h3>Data layer</h3>
The app receives data from GraphQL API (AppolloGraphQL library was used). Room persistance library is using for caching purposes.
<h3>Presentation layer</h3>
App's presentation layer is based on the MVI architectural pattern which is implemented on RxJava2. There is Coordinator pattern with Jetpack Navigation Component under the hood for navigation purposes. UI is based on conductors library. Also we've integrated epoxy to display lists of data.
<h3>QA</h3>
Here are some unit tests examples for presentaion layer.
