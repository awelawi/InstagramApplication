package com.awelawi.myapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awelawi.myapplication.MainActivity
import com.awelawi.myapplication.Post
import com.awelawi.myapplication.PostAdapter
import com.awelawi.myapplication.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


open class HomeFragment : Fragment() {

    lateinit var postRecyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    var allPosts: MutableList<Post> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //sETUP VIEWS HERE AND ONCLICK LISTNERES
        postRecyclerView = view.findViewById(R.id.postRecyclerView)

        //Steps to populate RecyclerView
        //1. Create layout for each row in a list
        //2. Create data source for each row

        //3. Create adaper that will bridge the data and row data.(Post adapter class)
        //4. Set adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts)
        postRecyclerView.adapter = adapter
        //5. Set layout manager on RecyclerView
        postRecyclerView.layoutManager   = LinearLayoutManager(requireContext())
        queryPosts()



    }
    open fun queryPosts() {
        //Specify whoch class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //find all post objects
        query.include(Post.KEY_USER)
        //returns the post in descending otrder
        query.addDescendingOrder("createdAt")

        //TODO: ONly return the most recent 20 posts
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    //Sonmethings wrong
                    Log.e(MainActivity.TAG, "Error fetching posts")
                }
                else{
                    if (posts!= null){
                        for(post in posts){
                            Log.i(
                                MainActivity.TAG, "post: " + post.getDescription()
                                    + " username: " + post.getUser()?.username)
                        }
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
    companion object{
        const val TAG = "HomeFragment"
    }


}