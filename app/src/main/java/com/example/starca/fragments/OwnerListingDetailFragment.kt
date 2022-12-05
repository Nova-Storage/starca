package com.example.starca.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.starca.fragments.FLAGS
import com.example.starca.ListingRequest
import com.example.starca.R
import com.example.starca.adapters.RequestsAdapter
import com.example.starca.models.Listing
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

class OwnerListingDetailFragment : Fragment() {



    lateinit var rvListingRequests : RecyclerView
    lateinit var adapter: RequestsAdapter

    var allRequests: ArrayList<ListingRequest> = arrayListOf()

    var listing : Listing? = null

    lateinit var swipeContainer : SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_owner_listing_detail, container, false)

        arguments?.let {
            listing = it.getParcelable("LISTING_BUNDLE")
        }

//        rvListingRequests = view.findViewById(R.id.rvListingRequests)
//        swipeContainer = view.findViewById(R.id.swipeContainer)
//
//        adapter = RequestsAdapter(requireContext(), allRequests, listing!!)
//        rvListingRequests.adapter = adapter
//        rvListingRequests.layoutManager = LinearLayoutManager(requireContext())
//
//        swipeContainer.setOnRefreshListener {
//            queryRequests()
//        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeContainer = view.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        rvListingRequests = view.findViewById(R.id.rvListingRequests)

        adapter = RequestsAdapter(requireContext(), allRequests, listing!!)
        rvListingRequests.adapter = adapter
        rvListingRequests.layoutManager = LinearLayoutManager(requireContext())

        swipeContainer.setOnRefreshListener {
            queryRequests()
        }

        queryRequests()

    }

    fun queryRequests() {
        // Specify which class to query
        val query : ParseQuery<Listing> = ParseQuery.getQuery(Listing::class.java)

        Log.i(TAG, listing?.getJSONArray("listingRequests").toString())

        // Add the query constraints: Equal to the selected listing and has requests
        query.whereEqualTo("objectId", listing?.objectId)
        query.whereNotEqualTo("listingRequests", null)

        query.findInBackground(object: FindCallback<Listing> {
            override fun done(listings: MutableList<Listing>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error fetching listing")
                    e.printStackTrace()
                } else {
                    if (listings != null) {
                        val queriedListing : Listing = listings[0]

                        val temp : MutableList<ListingRequest>? = listing!!.getJSONArray("listingRequests")
                            ?.let { ListingRequest.fromJsonArray(it, listing!!.objectId) }
                        val temp2 : MutableList<ListingRequest> = mutableListOf()


                        if (temp != null) {
                            for (req in temp) {
                                if (req.status == FLAGS.DENIED.code)
                                    temp2.add(req)
                            }
                        }

                        if (temp2.isNotEmpty()) {
                            temp?.removeAll(temp2)
                        }

                        if (temp.isNullOrEmpty()) {
                            adapter.clear()
                            view?.findViewById<TextView>(R.id.tvNoRequests)?.visibility = View.VISIBLE
                        } else {
                            view?.findViewById<TextView>(R.id.tvNoRequests)?.visibility = View.INVISIBLE
                            adapter.clear()
                            allRequests.addAll(temp)
                            adapter.notifyDataSetChanged()
                        }

                        swipeContainer.isRefreshing = false
                    }
                }
            }
        })
    }
    companion object {
        const val TAG = "OwnerListingDetailFragment"
    }
}