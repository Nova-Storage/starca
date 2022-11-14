# Starca

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview

### Description
Starca is a storage marketplace that allows users to either list their unusued space to make some extra income or look for listed places so they can store items that are taking up lots of space in their household.

### App Evaluation

- **Category:** P2P Marketplace
- **Mobile:** This app is being primarily developed for mobile but would perhaps be just as usable on a computer, similarly to Zillow, Apartments, etc. Mobile version would provide for better features and prove to be more convenient, as you would want to search/manage rental properties while not constricted to a computer.
- **Story:** Allow landlords to post unused storage spaces for tenants to search for. Connects people looking to rent their storage space and people who are looking for a storage space at a rate best for them.
- **Market:** Individuals that are looking to make some extra income by renting out unused space and those that need to clear up space in their house but don't want to pay the high prices for traditional storage facilities
- **Habit:** This app is not meant to be used consistently. Renters and rentees can "set it and forget it"
- **Scope:** Start by allowing users to post their storage spaces for rent and allowing users to search for storage spaces. Provide a platform for these two user groups to connect.  

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users will be able to register an account
* Users will be able to log in to their account
* Users will be able to view listings
* Users will be able to view listings of storage places to rent nearby
* Users will be able to view listings in other selected areas
* Users will be able to post listings
  * Users will be able to post pictures (implicit intent) for listings
  * User will be able post details on their storage listing
* Users will be able to update their profile
* Users will have a dashboard to select listings/rentals they own
* Users will be able to rate renters
* Renter will be able to rate rentees
* Users will be able to view previously rented storage listing
* Users will be able to delete their account
* Users will be able to chat with their prospective renters/rentees

**Optional Nice-to-have Stories**

* [fill in your optional user stories here]
* ...

### 2. Screen Archetypes

* Login
   * Users can log into their account
* Register
   * Users can register for an account
* Dashboard (Maps)
   * Users will see a maps screen that displays nearby listings to their current location, and a list of the listings below it with some more details for each property.
* Listing Creation
   * Users will be able to post listings to the app
   * Fragment 1
      * Users upload pictures of their space
   * Fragment 2
      * Users enter details of the property (address
   * Fragment 3
      * Users enter more details (amenaties, etc) into the listing
* Listing Detail
   * Show property details for a listing (Zip Code, photos, amenaties)
   * Show reviews for the property left by other users.
* Messaging (Stream)
   * Display all the users that have been messaged 
* Messaging Detail Page
   * Display the messages two users have sent to each other. Allow a user to send a message to another user.
* Profile
   * Users can add or change their profile photo
   * Users can see reviews about them left by other users.
* Settings
   * Users can delete their account
   * Users can log out of their account
* OPTIONAL STRETCH Stream Page (Search)
   * Users can search a zipcode to show all of the available storeage spaces in that zip code


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Dashboard
* Post
* Message
* Profile

**Flow Navigation** (Screen to Screen)

* Home
   * Login - Bottom Nav
     * Dashboard
   * Register - Bottom Nav
     * Dashboard
* Dashboard + Search
  * Search
    * Details
  * Details
  * Post - Bottom Nav
    * Post
  * Message - Bottom Nav
    * List of message
      * Message
    * Message
  * Profile - Bottom Nav
    * Settings

## Wireframes

<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/HandSketched.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Login.png" width=150> <img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Register.png" width=150>
<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Dashboard.png" width=150>
<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Detail.png" width=150>
<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Creation01.png" width=150>
<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Creation02.png" width=150>
<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Creation03.png" width=150>
<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Messaging.png" width=150>
<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Conversation.png" width=150>
<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Profile.png" width=150>
<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/Settings.png" width=150>

### [BONUS] Interactive Prototype

<img src="https://github.com/Nova-Storage/starca/blob/master/wireframes/StarcaPrototype.gif" width=300>

## Schema 
### Models
[Add table of models]

**User**
| Property      | Type    | Desecription                                           |
|  :---         | :---    |     :---                                               |
| objectID      | String  | Unique ID for the user                                 |
| emailVerified | Boolean | Boolean indicating if the account was verified by user |
| createdAt     | Date    | Date when user was created                             |
| username      | String  | User's username created during registration            |
| password      | String  | User's password created during registration            |
| email         | String  | User's email                                           |
| firstName     | String  | User's full name                                       |
| lastName      | String  | User's full name                                       |

**Image**
| Property      | Type               | Desecription                       |
|  :---         | :---               |     :---                           |
| objectID      | String             | Unique ID of the uploaded image    |
| image         | File               | Image that the user uploaded       |
| listingID     | Pointer to Listing | Listing the image was uploaded to  |

**Listing**
| Property      | Type               | Desecription                       |
|  :---         | :---               |     :---                           |
| objectID      | String             | Unique ID of the uploaded image    |
| createdAt     | Date               | Date when user was created         |
| userID        | Pointer to User    | User that created the Listing      |
| title         | String             | The title of the listing           |
| description   | String             | The description of the listing     |
| addressStreet | String             | The street address of the listing  |
| addressZip    | String             | The zip code of the address        |
| addressState  | String             | The state of the address           |
| addressCity   | String             | The city of the address            |
| amenities     | String Array       | Array of all the amenities         |


**Message**
*TBD*

### Networking
- Login
  *  (Read/GET) Query user
	  ```Kotlin
	  private fun loginUser(username: String, password: String) {
		ParseUser.logInInBackground(username, password, ({ user, e ->
			if (user != null) {
				Log.i(TAG, "Successfully logged in user")
				goToMainActivity()
			} else {
				e.printStackTrace()
				Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
			}
		}))
	  }
	  ```
- Register
  *  (Create/POST) Create user
	  ```Kotlin
	  private fun registerUser(username: String, 
							  password: String, 
							  firstName: String, 
							  lastName: String, 
							  email: String) {

		val user = ParseUser()

		user.username = username
		user.setPassword(password)
		user.firstName = username
		user.lastName = username
		user.email = username

		user.signUpInBackground { e ->
			if (e == null) {
				// User successfully created new account
				Log.i(TAG, "User successfully registered")
				goToMainActivity()
			} else {
				Toast.makeText(this, "Error registered", Toast.LENGTH_SHORT).show()
				e.printStackTrace()
			}
		}
	  }
	  ```
- Creation
  * (Create/POST) Create a new listing
	  ```Kotlin
	  fun createListing(title: String, 
					  description: String, 
					  user: ParseUser, 
					  file: File, 
					  addressStreet: String, 
					  addressCity: String, 
					  addressState: String, 
					  addressZip: String, 
					  amenities: String) {

		val post = Post()
		post.setTitle(title)
		post.setDescription(description)
		post.setUser(user)
		post.setAddressStreet(addressStreet)
		post.setAddressCity(addressCity)
		post.setAddressState(addressState)
		post.setAddressZip(addressZip)
		post.setImage(ParseFile(file))

		post.saveInBackground { exception ->
			if (exception != null) {
				Log.e(MainActivity.TAG, "Error while saving post")
				exception.printStackTrace()
				Toast.makeText(requireContext(), "Couldn't save post", Toast.LENGTH_SHORT).show()
			} else {
				Log.i(MainActivity.TAG, "Successfully saved post")
				progressBar?.visibility = View.GONE
				etDescription.text.clear()
				Toast.makeText(requireContext(), "Listing Created!", Toast.LENGTH_SHORT).show()
			}
		}
	  }

	  ```
  * (Create/POST) Upload image for a listing
	  ```Kotlin
	  fun uploadImage(listingID: String, file: File) {

		val image = Image()
		post.setListingID(listingID)
		post.setImage(ParseFile(file))

		image.saveInBackground { exception ->
			if (exception != null) {
				Log.e(MainActivity.TAG, "Error while uploading image")
				exception.printStackTrace()
				Toast.makeText(requireContext(), "Couldn't upload Image", Toast.LENGTH_SHORT).show()
			} else {
				Log.i(MainActivity.TAG, "Successfully uploaded image")
				progressBar?.visibility = View.GONE
				etDescription.text.clear()
				Toast.makeText(requireContext(), "Saved Image!", Toast.LENGTH_SHORT).show()
			}
		}
	  }
	  ```
- Maps Screen
  * (Read/GET) Query all warehouse location posts in database
```
    val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
    
    query.addDescendingOrder("createdAt")
    query.findInBackground { posts, e ->
        if (e != null) {
            Log.e(TAG, "Error fetching posts.")
        } else {
            if (posts != null) {
                for (post in posts) {
                   // retrieve post and populate map markers + recyclerView date.
                }
                feedPosts.addAll(posts)
                adapter.notifyDataSetChanged()
            }
        }
        swipeContainer.isRefreshing = false
    }
```
 * Messaging
   - (Read/GET) Query all Message requests addressed to user as well as their latest message.
   - (Delete) Delete existing Message
 * Conversation
   - (Create/POST) Create a new message
 * Detail
   - (Read/GET) Query Post data/info
 * Profile
   - (Read/GET) Query Profile data 
   - (Read/GET) Query Comments data
 * Settings
   - (Read/GET) Query Settings data 
   - (Update/PUT) Modify Settings data
   - (Delete) Delete Account data

- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
