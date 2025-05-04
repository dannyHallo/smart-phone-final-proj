package com.example.simpledictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpledictionary.Adapter.SearchAdapter;
import com.example.simpledictionary.Database.ReadOriginalDatabase;
import com.example.simpledictionary.Database.ReadPersonalDatabase;
import com.example.simpledictionary.Database.ReadTutorialDatabase;
import com.example.simpledictionary.Database.WritePersonalDatabase;
import com.example.simpledictionary.Database.WriteTutorialDatabase;
import com.example.simpledictionary.Looper.ExampleLooperThread;
import com.example.simpledictionary.Model.words;
import com.example.simpledictionary.Utils.KeyboardUtils;
import com.example.simpledictionary.VideoView.CustomVideoView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchAdapter adapter;

    private CustomVideoView videoView;
    private ExampleLooperThread looperThread = new ExampleLooperThread();

    EditText searchBar;
    //List<String> suggestList = new ArrayList<>();
    public List<words> current_list;
    public List<words> current_list_for_history;

    public String current_searching_word;

    ReadOriginalDatabase readOriginalDatabase;
    WritePersonalDatabase writePersonalDatabase;
    ReadPersonalDatabase readPersonalDatabase;
    WriteTutorialDatabase writeTutorialDatabase;
    ReadTutorialDatabase readTutorialDatabase;

    int current_position;
    int former_position = -1;
    long last_click_time = 0;

    List<String> menuWords = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MainActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//设置绘画模式
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        menuWords.add("welcome");
        menuWords.add("love");
        menuWords.add("nice");
        menuWords.add("lime");
        menuWords.add("mint");
        menuWords.add("vanilla");
        menuWords.add("lightyear");
        menuWords.add("flapper");
        menuWords.add("summer");
        menuWords.add("flower");
        menuWords.add("sweet");
        menuWords.add("sunset");
        menuWords.add("anew");

        videoView = (CustomVideoView) findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bgvid));
        videoView.start();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_search);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        searchBar = (EditText) findViewById(R.id.search_bar);

        searchBar.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        looperThread.start();
        SystemClock.sleep(50);
        looperThread.handler.post(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    long time = System.currentTimeMillis();
                    final Calendar mCalendar = Calendar.getInstance();
                    mCalendar.setTimeInMillis(time);
                    int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                    if (mHour < 6) {
                        searchBar.setHint("THE MOON IS DOWN");

                    } else if (mHour < 7) {
                        searchBar.setHint("SUMMERTIME");

                    } else if (mHour < 8) {
                        searchBar.setHint("BLACK EYES");

                    } else if (mHour < 9) {
                        searchBar.setHint("SILVER LINING");

                    } else if (mHour < 10) {
                        searchBar.setHint("BRICK WALLS");

                    } else if (mHour < 11) {
                        searchBar.setHint("THE WOODS");

                    } else if (mHour < 12) {
                        searchBar.setHint("COASTLINE");

                    } else if (mHour < 13) {
                        searchBar.setHint("HARD OF HEARING");

                    } else if (mHour < 14) {
                        searchBar.setHint("FEELIN' ALRIGHT");

                    } else if (mHour < 15) {
                        searchBar.setHint("SWEET LOUISE");

                    } else if (mHour < 16) {
                        searchBar.setHint("SPIRITS");

                    } else if (mHour < 17) {
                        searchBar.setHint("HOMESICK");

                    } else if (mHour < 18) {
                        searchBar.setHint("SUNSETZ");

                    } else if (mHour < 19) {
                        searchBar.setHint("COUDS");

                    } else if (mHour < 20) {
                        searchBar.setHint("GLORY");

                    } else if (mHour < 21) {
                        searchBar.setHint("CHASIN' HONEY");

                    } else if (mHour < 22) {
                        searchBar.setHint("THE FLY");

                    } else if (mHour < 23) {
                        searchBar.setHint("WIDE EYES");

                    } else {
                        searchBar.setHint("LUA");
                    }
                    SystemClock.sleep(60000);
                }

            }
        });

        //init database
        readOriginalDatabase = new ReadOriginalDatabase(this);
        readPersonalDatabase = new ReadPersonalDatabase(this);
        writePersonalDatabase = new WritePersonalDatabase(this);
        readTutorialDatabase = new ReadTutorialDatabase(this);
        writeTutorialDatabase = new WriteTutorialDatabase(this);

        writePersonalDatabase.createTable();
        writeTutorialDatabase.createTable();

        if(readTutorialDatabase.getProcess().size() == 4){
            normalMenuSearching();
        }else{
            tutorialMenuSearching();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //recyclerView.setAdapter(null);

        //Video Loop
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoView.start(); //need to make transition seamless.
            }
        });

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyboardUtils.hideKeyboard(MainActivity.this);
                    searchBar.setVisibility(View.GONE);
                    startSearch(searchBar.getText().toString());
                }
                return false;
            }
        });


        //init adapter default set all result, and it is a waste of time
        //adapter = new SearchAdapter(this, database.getWords());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //clear double tap position, not that meaningful
                    former_position = -1;
                }
            }
        });


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                //Remove swiped item from list and notify the RecyclerView
                removeItem(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void tutorialMenuSearching() {
        current_searching_word = "tutorial";
        current_list = readOriginalDatabase.singleSearchForTutorial();
        adapter = new SearchAdapter(this, current_list);
        recyclerView.setAdapter(adapter);
    }

    private void normalMenuSearching() {
        current_searching_word = menuWords.get((int) (Math.random() * menuWords.size()));
        current_list = readOriginalDatabase.singleSearch(current_searching_word);
        adapter = new SearchAdapter(this, current_list);
        recyclerView.setAdapter(adapter);
    }

    protected void onPause() {
        super.onPause();
        current_position = videoView.getCurrentPosition();
        videoView.pause();

    }

    protected void onResume() {
        super.onResume();

        videoView.seekTo((int) current_position);
        videoView.start();

    }

    private void startSearch(String text) {
        current_searching_word = text;
        if (text.equals("")) {
            //searching into my list
            current_list_for_history = readPersonalDatabase.getWords();
            if(!readTutorialDatabase.getProcess().contains("enter_personal_db")){
                writeTutorialDatabase.addProcess("enter_personal_db");
            }
            adapter = new SearchAdapter(this, current_list_for_history);
        } else {
            //searching into words list
            current_list = readOriginalDatabase.getWordsByObj(text);
            adapter = new SearchAdapter(this, current_list);
        }
        recyclerView.setAdapter(adapter);

    }

    private void clearSearch() {
        List<words> empty_list = new ArrayList<>();
        adapter = new SearchAdapter(this, empty_list);
        recyclerView.setAdapter(adapter);
    }

    public void myItemClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        long currentTimeMillis = SystemClock.uptimeMillis();
        long deltaTime = currentTimeMillis - last_click_time;

        if (position == former_position && deltaTime < 300 && (!current_searching_word.equals("tutorial") || current_list.size() != 1)) {
            //Clicked twice
            if(!readTutorialDatabase.getProcess().contains("double_tap")){
                writeTutorialDatabase.addProcess("double_tap");
            }
            clearSearch();
            KeyboardUtils.showKeyboard(searchBar, MainActivity.this);
            searchBar.setText("");
            searchBar.setHint("TYPE HERE");
            searchBar.setVisibility(View.VISIBLE);
        }
        last_click_time = currentTimeMillis;
        former_position = position;
    }

    public void removeItem(int position) {
        if (!current_searching_word.equals("")) {
            //the card is in words list
            if (!current_list.get(position).getNum().equals("0")) {
                //add and remove a normal card
                if(!readTutorialDatabase.getProcess().contains("sweep")){
                    writeTutorialDatabase.addProcess("sweep");
                }
                writePersonalDatabase.addWord(current_list.get(position));
                current_list.remove(position);
                adapter.notifyItemRemoved(position);
            } else {
                //BACK card view in words list
                //refuse to move
                //showToast("cannot remove this into my words");
                adapter.notifyItemChanged(position);

            }

        } else {
            //the card is in my list
            if (current_list_for_history.get(position).getNum().equals("0")) {
                //BACK card view in my list
                //refuse to move
                adapter.notifyItemChanged(position);
            } else {
                //delete and remove a history card
                if(!readTutorialDatabase.getProcess().contains("sweep2")){
                    writeTutorialDatabase.addProcess("sweep2");
                }
                writePersonalDatabase.deleteWord(current_list_for_history.get(position));
                current_list_for_history.remove(position);
                adapter.notifyItemRemoved(position);
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //block the back func
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}