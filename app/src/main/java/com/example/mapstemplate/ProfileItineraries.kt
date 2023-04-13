package com.example.mapstemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mapstemplate.activities.ItineraryActivity
import com.example.travelapp.adapters.ItineraryListAdapter
import com.example.travelapp.itineraries.Itinerary
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class ProfileItineraries : AppCompatActivity() {

    lateinit var listViewItinerary: ListView
    lateinit var itineraryListAdapter: ItineraryListAdapter

    val itineraryList: ArrayList<Itinerary> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_itineraries)

        itineraryList.addAll(HomeActivity.globalItineraryList)

        listViewItinerary = findViewById(R.id.global_itineraries_list_item)

        val friendJson = intent.getStringExtra("friend")
        if (friendJson != null) {
            try {
                val friend = Gson().fromJson<User>(friendJson, User::class.java)
                val titleTextView = findViewById<TextView>(R.id.title)
                titleTextView.text = friend.nick
            } catch (e: Exception) {
                Log.i("ProfilesTag", "Error deserializing friend: ${e.message}")
            }
        }
        else {
            // Handle the case where the friendJson variable is null or empty
            Toast.makeText(this, "No friend data found", Toast.LENGTH_SHORT).show()
        }



        setupItineraryListView()

        val searchButton = findViewById<Button>(R.id.btnSearch)
        val inputField = findViewById<EditText>(R.id.itineraryName)
        var enteredSearch = ""

        searchButton.setOnClickListener {
            val newItineraryList = ArrayList<Itinerary>()
            enteredSearch = inputField.text.toString()
            for (i in HomeActivity.globalItineraryList) {
                if (i.name.contains(enteredSearch, true)) {
                    newItineraryList.add(i)
                } else {
                    Toast.makeText(
                        this,
                        "Please, enter an itinerary name",
                        Toast.LENGTH_SHORT).show()
                }
            }
            itineraryList.clear()
            itineraryList.addAll(newItineraryList)
            itineraryListAdapter.notifyDataSetChanged()

        }
    }

    fun setupItineraryListView() {
        itineraryListAdapter = ItineraryListAdapter(this, itineraryList)
        listViewItinerary.isClickable = true
        listViewItinerary.adapter = itineraryListAdapter

        listViewItinerary.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ItineraryActivity::class.java)
            intent.putExtra("itinerary_index", position)
            intent.putExtra("is_global", true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // update the data in the listView
        itineraryListAdapter.notifyDataSetChanged()
    }
}
