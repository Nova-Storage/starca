package com.example.starca

import android.graphics.Color
import android.os.Bundle
import android.os.IBinder.DeathRecipient
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.starca.models.Conversation
import com.example.starca.models.Listing
import com.google.gson.Gson
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.SaveCallback
import org.json.JSONArray

private const val LISTING_BUNDLE = "LISTING_BUNDLE"

enum class FLAGS(val code: Int) {
    DENIED(0),
    REQUESTED(1),
    APPROVED(2),
    BOUGHT(3)
}

class DetailFragment : Fragment() {
    private var listing: Listing? = null

    lateinit var button_bottomLeft: Button
    lateinit var button_bottomRight: Button
    lateinit var tv_requestDenied: TextView

    lateinit var conversationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listing = it.getParcelable(LISTING_BUNDLE)
        }

        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(R.transition.shared_image)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }
/*
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
    */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTv = view.findViewById<TextView>(R.id.detail_title_tv)
        val cityTv = view.findViewById<TextView>(R.id.detail_city_tv)
        val stateTv = view.findViewById<TextView>(R.id.detail_state_tv)
        val ratingRb = view.findViewById<RatingBar>(R.id.detail_rating_rb)
        val descriptionTv = view.findViewById<TextView>(R.id.detail_description_tv)
        val imageIv = view.findViewById<ImageView>(R.id.detail_image_iv)
        ViewCompat.setTransitionName(imageIv, "transition_detail_image")
        ViewCompat.setTransitionName(titleTv, "transition_detail_title")

        titleTv.text = listing?.getString("title")
        cityTv.text = listing?.getString("addressCity") + ", "
        stateTv.text = listing?.getString("addressState")
        ratingRb.rating = listing?.getDouble("listingRating")!!.toFloat()
        descriptionTv.text = listing?.getString("description")

        Glide.with(requireContext()).load(listing?.getParseFile("PictureOfListing")?.url)
            .into(imageIv)

        startPostponedEnterTransition()

        // okay. start simple. get the 2 buttons and textview
        button_bottomLeft = view.findViewById(R.id.button_bottomLeft)
        button_bottomRight = view.findViewById(R.id.button_bottomRight)
        tv_requestDenied = view.findViewById(R.id.tv_request_denied)

        getRequest()
    }


    fun getRequest() {

        //read flag. get request array from this listing
        val requests_arrayJSON = listing?.getJSONArray("listingRequests")

        if (requests_arrayJSON == null) {
            // listing array default to undefined.
//            Toast.makeText(context, "No Array Exists", Toast.LENGTH_SHORT).show() // confirmed. it defaults to null
            //
            val newArray = JSONArray()
            listing?.put("listingRequests", newArray)
            listing?.saveInBackground {
                nnn(newArray)
            }
        } else {
            nnn(requests_arrayJSON)
        }

    }

    fun nnn(requests_arrayJSON: JSONArray) {

        val requests = requests_arrayJSON?.let { listing?.let { it1 -> ListingRequest.fromJsonArray(it, it1.objectId) } }
        //i have requests now. it's a list. lets get the 1 request.
        if (requests != null) {
            for (request in requests) {
                if (request.objectId == ParseUser.getCurrentUser().objectId) {
                    // we now have the request for this user at this listing.

                    //get the status of that request
                    when (request.status) {
                        FLAGS.DENIED.code -> {
                            //show the denied message
                            tv_requestDenied.visibility = View.INVISIBLE

                            // show the left button only. it'll already be here by default.
                            // change color to black
                            button_bottomLeft.setBackgroundColor(Color.BLACK)
                            button_bottomLeft.text = "Okay"
                            button_bottomLeft.setOnClickListener(cancelRequest(requests))
                        }
                        FLAGS.REQUESTED.code -> {
                            // should just display text: "Awaiting Approval"
                            Toast.makeText(context, "Awaiting Approval", Toast.LENGTH_SHORT).show()
                            //show cancel button
                            button_bottomLeft.visibility=View.GONE
                            button_bottomRight.visibility=View.VISIBLE
                            button_bottomRight.text="Cancel Request"

                            tv_requestDenied.visibility=View.VISIBLE
                            tv_requestDenied.setTextColor(Color.BLACK)
                            tv_requestDenied.text="Awaiting Approval..."
                        }
                        FLAGS.APPROVED.code -> {

                        }
                        FLAGS.BOUGHT.code -> {

                        }
                    }
                    return
                }
            }
        }
        // if you get here, this mean you have no requests under this listing.
        button_bottomLeft.setOnClickListener(
            requests?.let { requestListing(it) }
        )
    }

    fun requestListing(requestArray: MutableList<ListingRequest>): View.OnClickListener {
        sendToConversationTable()
        return View.OnClickListener { _ ->
            // here you will create a request. you will add it to the listing array.
            Toast.makeText(context, "Request Sent", Toast.LENGTH_SHORT).show()

            // create an listingRequest object
            val newRequest =
                ListingRequest(ParseUser.getCurrentUser().objectId, FLAGS.REQUESTED.code, listing!!.objectId)

            //add to request array
            requestArray.add(newRequest)

            val gson = Gson()

            val stArr = ArrayList<String>()
            for(request in requestArray){
                stArr.add(gson.toJson(request))
            }

            //Toast.makeText(context, "$jsonArray", Toast.LENGTH_LONG).show()

            listing?.put("listingRequests", stArr)

            listing?.saveInBackground { e ->
                if (e == null) {
                    Toast.makeText(context, "Request Submitted", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "requestListing: $e")
                }
            }

        }
    }

    fun cancelRequest(requestArray: MutableList<ListingRequest>): View.OnClickListener {
        return View.OnClickListener { _ ->
            //return to original request view.
            tv_requestDenied.visibility = View.INVISIBLE
            button_bottomLeft.setBackgroundColor(Color.parseColor("#0C825F"))
            button_bottomLeft.text = "Rent"

            // you are supposed to remove the listingRequest here.
        }



    }

    //////////////////
    fun sendToConversationTable()
    {


        //Logic
        //  Create the Converstation table - DONE
        // Link to message table Pointer points to Conversation ObjectID column -DONE


        // Get the user id of the poster - DONE
        // Save the listingPoster, currentUser to Conversation Fragment  database
            //Get callback to retrieve   conversation pointer
        // send the message automatically
             // Save message, conversationPointer

        // Chat fragment
             // Pull
            //  if userId = currentLoginInuser, display, the data(listingPosterFirstname, messages,)
        // That's it
        //ParseObject message = ParseObject.create("Message");
        //message.put(Message.USER_ID_KEY, userId);
        //message.put(Message.BODY_KEY, data);
        // Using new `Message` Parse-backed model now



        Log.d(TAG,listing!!.getUser()!!.objectId)
        Log.d(TAG, ParseUser.getCurrentUser().objectId)


        val listingPoster = listing!!.getUser()!!
        val currentLoginInUser = ParseUser.getCurrentUser()
        val initMessage = "Hi I am interested in the listing";

        val directMessage = listOf(listingPoster, currentLoginInUser)



        val conversation = Conversation()
        conversation.setUser(currentLoginInUser)
        conversation.setRecipient(listingPoster)
        conversation.saveInBackground(SaveCallback {
            Toast.makeText(
                requireContext(), "Successfully added data to Conversation table.",
                Toast.LENGTH_SHORT
            ).show()
        })

        // Get the conversation Id
        compareConversation(currentLoginInUser, listingPoster)

        //Retrieve conversation pointer



    }

  /*  fun sendMessage()
    {
        val message = Message()
        message.setUserId(ParseUser.getCurrentUser().objectId)
        message.setBody(initMessage)
        message.conversationPointer(currentLoginInUser)
        message.saveInBackground(SaveCallback {
            Toast.makeText(
                this@DetailFragment, "Successfully created message on Parse",
                Toast.LENGTH_SHORT
            ).show()
            //refreshMessages();

        })

    }
*/

    fun compareConversation(user: ParseUser, recipient: ParseUser){
        val query: ParseQuery<Conversation> = ParseQuery(Conversation::class.java)

        query.include(Conversation.KEY_USER)
        query.include(Conversation.KEY_RECIPIENT)
        query.whereEqualTo(Conversation.KEY_USER, user)
        query.findInBackground { conversation, e ->
            if (e != null) {
                Log.d(TAG, "No conversation found: $e")
            } else {
                if (conversation != null) {
                    Log.d(TAG, conversation.toString())
                    conversationId = conversation[0].objectId
                    Log.d(TAG, conversationId)
                }
            }
        }
    }

    companion object {
        const val TAG = "DetailFragment"
    }
}