package com.example.liberty_instagram.Fragments;

import android.util.Log;

import com.example.liberty_instagram.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment  extends PostsFragment {
    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting Posts ", e );
                    return;
                }

                for (Post post: posts){
                    Log.i(TAG,"Post:  "+ post.getDescription() + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                Log.i(TAG,"Post was successful !!");
//                    etDescription.setText(""); These are for the loading parts
//                    ivPostImage.setImageResource(0);
            }
        });
    }
}
