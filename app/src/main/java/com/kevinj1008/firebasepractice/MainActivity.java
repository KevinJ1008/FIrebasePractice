package com.kevinj1008.firebasepractice;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mReference;
    private EditText mUserEmail;
    private EditText mUserName;
    private Button mAddBtn;
    private Button mUserBtn;
    private Button mPostBtn;
    private RadioButton mBeautyBtn;
    private RadioButton mJokeBtn;
    private RadioButton mGossipBtn;
    private RadioButton mLifeBtn;
    private RadioGroup mRadioGroup;
    private EditText mTitle;
    private EditText mContent;
    private Button mGetArticle;
    private Button mSearchBeauty;
    private Button mSearchGossip;
    private Button mSearchJoke;
    private Button mSearchLife;
    private EditText mSearchName;
    private Button mSearchBtn;
    private EditText mSearchFriend;
    private EditText mSearchTag;
    private Button mDoubleSearchBtn;
    private EditText mFriendName;
    private Button mAcceptBtn;
    private Button mRefuseBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserEmail = findViewById(R.id.user_email);
        mUserName = findViewById(R.id.user_name);
        mAddBtn = findViewById(R.id.add_user_btn);
        mUserBtn = findViewById(R.id.get_user);
        mPostBtn = findViewById(R.id.post_btn);
        mBeautyBtn = findViewById(R.id.beauty);
        mJokeBtn = findViewById(R.id.joke);
        mGossipBtn = findViewById(R.id.gossip);
        mLifeBtn = findViewById(R.id.life);
        mRadioGroup = findViewById(R.id.tag_group);
        mTitle = findViewById(R.id.title);
        mContent = findViewById(R.id.article);
        mSearchBtn = findViewById(R.id.search_btn);
        mGetArticle = findViewById(R.id.get_article);
        mSearchBeauty = findViewById(R.id.search_beauty);
        mSearchGossip = findViewById(R.id.search_gossip);
        mSearchJoke = findViewById(R.id.search_joke);
        mSearchLife = findViewById(R.id.search_life);
        mSearchName = findViewById(R.id.search);
        mSearchFriend = findViewById(R.id.search_name);
        mSearchTag = findViewById(R.id.search_tag);
        mDoubleSearchBtn = findViewById(R.id.search_double_btn);
        mFriendName = findViewById(R.id.friend_name);
        mAcceptBtn = findViewById(R.id.add_friend_btn);
        mRefuseBtn = findViewById(R.id.denied_friend_btn);

        mReference = FirebaseDatabase.getInstance().getReference();

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = mUserEmail.getText().toString();
                String nameText = mUserName.getText().toString();
                StringTokenizer tokens = new StringTokenizer(emailText, "@");
                String first = tokens.nextToken();
                String second = tokens.nextToken();
                StringTokenizer mailSplit = new StringTokenizer(second, ".");
                String mail = mailSplit.nextToken();
                String userId;
                if (!mail.contains("mail")) {
                    userId = first + "_" + mail + "mail";
                } else {
                    userId = first + "_" + mail;
                }

                InputMethodManager input = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if (!"".equals(emailText) && !"".equals(nameText)) {
                    addNewUser(userId, emailText, nameText);
                    mUserEmail.getText().clear();
                    mUserName.getText().clear();
                } else {
                    Toast.makeText(getApplicationContext(), "Pls enter Email and Name!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentText = mContent.getText().toString();
                String titleText = mTitle.getText().toString();
                String tag = mRadioGroup.getTag().toString();
                String author = "Kevin";
                String authorTag = author + "_" + tag;

                InputMethodManager input = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if (!"".equals(contentText) && !"".equals(titleText)) {
                    writeNewPost(author, contentText, titleText, tag, authorTag);
                    mContent.getText().clear();
                    mTitle.getText().clear();
                    mRadioGroup.clearCheck();
                } else {
                    Toast.makeText(getApplicationContext(), "Pls enter title and content!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RadioGroupListener listener = new RadioGroupListener();
        mRadioGroup.setOnCheckedChangeListener(listener);



        mGetArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mReference = database.getReference("article");
                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("Firebase", "Get all articles: \n" + value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Firebase", "Failed to get article " + databaseError.getMessage());
                    }
                });
            }
        });

        // Get user
        mUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mReference = database.getReference("user");
                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("Firebase", "Get user data: \n" + value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Firebase", "Failed to get user data " + databaseError.getMessage());
                    }
                });
            }
        });

        // Search Beauty
        mSearchBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mReference = database.getReference();
                Query query = mReference.child("article").orderByChild("tag").equalTo("表特");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("Firebase", "Get Beauty articles: \n" + value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Firebase", "Failed to get Beauty articles " + databaseError.getMessage());
                    }
                });
            }
        });

        // Search Gossip
        mSearchGossip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mReference = database.getReference();
                Query query = mReference.child("article").orderByChild("tag").equalTo("八卦");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("Firebase", "Get Gossip articles: \n" + value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Firebase", "Failed to get Gossip articles " + databaseError.getMessage());
                    }
                });
            }
        });

        // Search Joke
        mSearchJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mReference = database.getReference();
                Query query = mReference.child("article").orderByChild("tag").equalTo("就可");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("Firebase", "Get Joke articles: \n" + value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Firebase", "Failed to get Joke articles " + databaseError.getMessage());
                    }
                });
            }
        });

        // Search Life
        mSearchLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mReference = database.getReference();
                Query query = mReference.child("article").orderByChild("tag").equalTo("生活");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("Firebase", "Get Life articles: \n" + value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Firebase", "Failed to get Life articles " + databaseError.getMessage());
                    }
                });
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager input = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String authorText = mSearchName.getText().toString();
                if (!"".equals(authorText)) {
                    searchFriendArticle();
                    mSearchName.getText().clear();
                } else {
                    Toast.makeText(getApplicationContext(), "Enter something to search!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDoubleSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager input = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String searchText = mSearchFriend.getText().toString();
                String searchTag = mSearchTag.getText().toString();
                if (!"".equals(searchText) && !"".equals(searchTag)) {
                    searchArticle();
                    mSearchFriend.getText().clear();
                    mSearchTag.getText().clear();
                } else {
                    Toast.makeText(getApplicationContext(), "Enter something to search!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                String key = mReference.child("user").getKey();
//                mReference = db.getReference(key);

                InputMethodManager input = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), 0);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mReference = database.getReference();
//                String friendNameText = mFriendName.getText().toString();
//                Query query = mReference.child("user").orderByChild("name").equalTo(friendNameText);
//                query.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String friendNameText = mFriendName.getText().toString();
                        if (!"".equals(friendNameText) && friendNameText != null) {
                            addNewFriend();
                            mFriendName.getText().clear();
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter a name to search!", Toast.LENGTH_SHORT).show();
                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

            }
        });

        mRefuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager input = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), 0);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mReference = database.getReference();
                String friendName = mFriendName.getText().toString();
                if (!"".equals(friendName) && friendName != null) {
                    refuseFriend();
                    mFriendName.getText().clear();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchFriendArticle() {
        String authorText = mSearchName.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference();
        Query query = mReference.child("article").orderByChild("author").equalTo(authorText);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                Log.d("Firebase", "Get friend's articles: \n" + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase", "Failed to get friend's articles " + databaseError.getMessage());
            }
        });
    }

    private void searchArticle() {
        String searchText = mSearchFriend.getText().toString();
        String searchTag = mSearchTag.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference();
        Query query = mReference.child("article").orderByChild("author_tag").startAt(searchText).endAt(searchTag);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                Log.d("Firebase", "Get friend's articles by name and tag: \n" + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase", "Failed to get friend's articles " + databaseError.getMessage());
            }
        });

    }

    private void addNewUser(String userId, String name, String email) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mReference = db.getReference("user");
//        String key = mReference.child("user").push().getKey();

        User user = new User(name, email);
        Map<String, Object> addValues = user.toMap();
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(userId, addValues);
        mReference.child(userId).setValue(user);
    }

    private void writeNewPost(String author, String content, String title, String tag, String authorTag) {
        // Create new post at /article and get auto article ID
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mReference = db.getReference("article");
        String key = mReference.child("article").push().getKey();
        Article article = new Article(author, content, title, tag, authorTag);
        Map<String, Object> postValues = article.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);

        mReference.updateChildren(childUpdates);
    }

    private void addNewFriend() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String key = mReference.child("user").getKey();
        mReference = db.getReference(key);
        String user = "car4353666_gmail";
        String friend = mFriendName.getText().toString();
//        String friendId = friend.replace(friend, key);
        Map invite = new HashMap();
        invite.put(user + "/checkFriend/" + friend, true);
        invite.put(friend + "/checkFriend/" + user, false);
        mReference.updateChildren(invite);
        if (invite == null) {
            Toast.makeText(getApplicationContext(), "Can not find a user", Toast.LENGTH_SHORT).show();
        }
    }

    private void refuseFriend() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String key = mReference.child("user").getKey();
        mReference = db.getReference(key);
        String user = "car4353666_gmail";
        String friend = mFriendName.getText().toString();
//        String friendId = friend.replace(friend, key);
        Map invite = new HashMap();
        invite.put(friend + "/checkFriend/" + user, null);
        invite.put(user + "/checkFriend/" + friend, null);
        mReference.updateChildren(invite);
        if (invite == null) {
            Toast.makeText(getApplicationContext(), "Can not find a user", Toast.LENGTH_SHORT).show();
        }
    }

    public class RadioGroupListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
            if (checkId == R.id.beauty) {
                mRadioGroup.setTag("表特");
                mBeautyBtn.setTextColor(Color.WHITE);
                mBeautyBtn.setBackgroundColor(Color.BLACK);
                mJokeBtn.setTextColor(Color.BLACK);
                mJokeBtn.setBackgroundColor(Color.WHITE);
                mGossipBtn.setTextColor(Color.BLACK);
                mGossipBtn.setBackgroundColor(Color.WHITE);
                mLifeBtn.setTextColor(Color.BLACK);
                mLifeBtn.setBackgroundColor(Color.WHITE);
            } else if (checkId == R.id.joke) {
                mRadioGroup.setTag("就可");
                mJokeBtn.setTextColor(Color.WHITE);
                mJokeBtn.setBackgroundColor(Color.BLACK);
                mBeautyBtn.setTextColor(Color.BLACK);
                mBeautyBtn.setBackgroundColor(Color.WHITE);
                mGossipBtn.setTextColor(Color.BLACK);
                mGossipBtn.setBackgroundColor(Color.WHITE);
                mLifeBtn.setTextColor(Color.BLACK);
                mLifeBtn.setBackgroundColor(Color.WHITE);
            } else if (checkId == R.id.gossip) {
                mRadioGroup.setTag("八卦");
                mGossipBtn.setTextColor(Color.WHITE);
                mGossipBtn.setBackgroundColor(Color.BLACK);
                mBeautyBtn.setTextColor(Color.BLACK);
                mBeautyBtn.setBackgroundColor(Color.WHITE);
                mJokeBtn.setTextColor(Color.BLACK);
                mJokeBtn.setBackgroundColor(Color.WHITE);
                mLifeBtn.setTextColor(Color.BLACK);
                mLifeBtn.setBackgroundColor(Color.WHITE);
            } else if (checkId == R.id.life) {
                mRadioGroup.setTag("生活");
                mLifeBtn.setTextColor(Color.WHITE);
                mLifeBtn.setBackgroundColor(Color.BLACK);
                mBeautyBtn.setTextColor(Color.BLACK);
                mBeautyBtn.setBackgroundColor(Color.WHITE);
                mJokeBtn.setTextColor(Color.BLACK);
                mJokeBtn.setBackgroundColor(Color.WHITE);
                mGossipBtn.setTextColor(Color.BLACK);
                mGossipBtn.setBackgroundColor(Color.WHITE);
            }
        }
    }
}
