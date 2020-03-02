package com.example.liberty_instagram.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liberty_instagram.Post;
import com.example.liberty_instagram.PostsAdapter;
import com.example.liberty_instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {
    public static final String TAG = "PostsFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    SwipeRefreshLayout swipeContainer;


    public PostsFragment() {
        // Required empty public constructor
    }
    //pull to refresh items
    public void clear() {
        allPosts.clear();
        adapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(),allPosts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        final  SwipeRefreshLayout swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts();
                swipeContainer.setRefreshing(false);
            }
        });
        queryPosts();


    }

        protected void queryPosts() {
            ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
            query.include(Post.KEY_USER);
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
//                    swipeContainer.setRefreshing(false);
                    Log.i(TAG,"Post was successful !!");
//                    etDescription.setText(""); These are for the loading parts
//                    ivPostImage.setImageResource(0);
                }
            });


        }

}

