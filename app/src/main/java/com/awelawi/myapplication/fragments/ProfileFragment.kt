package com.awelawi.myapplication.fragments

import android.util.Log
import com.awelawi.myapplication.MainActivity
import com.awelawi.myapplication.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment:HomeFragment() {

    override fun queryPosts(){
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //find all post objects
        query.include(Post.KEY_USER)
//        Only returns posts from currently signed in user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
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
