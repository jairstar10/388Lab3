package com.codepath.nationalparks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


// --------------------------------//
// CHANGE THIS TO BE YOUR API KEY  //
// --------------------------------//
private const val API_KEY = "xR9Rx134w6g65B9pVeawmzvQLFjCfjIOxCJPMixi"

class NationalParksFragment : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_national_parks_list, container, false)
        val progressBar = view.findViewById<ContentLoadingProgressBar>(R.id.progress)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        updateAdapter(progressBar, recyclerView)

        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // Create AsyncHttpClient and set API key
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        // Make the GET request
        client["https://developer.nps.gov/api/v1/parks", params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                progressBar.hide()

                try {
                    // Extract the "data" JSON array
                    val dataJSON = json.jsonObject.get("data") as JSONArray
                    val parksRawJSON = dataJSON.toString()

                    // Parse JSON into Kotlin models using Gson
                    val gson = Gson()
                    val arrayParkType = object : TypeToken<List<NationalPark>>() {}.type
                    val models: List<NationalPark> = gson.fromJson(parksRawJSON, arrayParkType)

                    // Update RecyclerView adapter with parsed models
                    recyclerView.adapter =
                        NationalParksRecyclerViewAdapter(models, this@NationalParksFragment)

                    Log.d("NationalParksFragment", "response successful")
                } catch (e: Exception) {
                    Log.e("NationalParksFragment", "JSON parsing error", e)
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, t: Throwable?) {
                progressBar.hide()
                Log.e("NationalParksFragment", "Request failed: $errorResponse")
                t?.printStackTrace()
            }
        }]
    }

    override fun onItemClick(item: NationalPark) {
        Toast.makeText(context, "Park Name: ${item.name}", Toast.LENGTH_LONG).show()
    }
}