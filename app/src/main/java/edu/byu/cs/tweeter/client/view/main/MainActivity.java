package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.client.view.login.StatusDialogFragment;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements MainPresenter.MainView,
        StatusDialogFragment.Observer {

    private static final String LOG_TAG = "MainActivity";

    public static final String CURRENT_USER_KEY = "CurrentUser";

    private Toast infoToast;
    private Toast errorToast;
    private User selectedUser;
    private TextView followeeCount;
    private TextView followerCount;
    private Button followButton;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedUser = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        presenter = getMainPresenter(selectedUser);

        presenter.validateUser();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), selectedUser);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showStatusDialogFragment(getSupportFragmentManager());
            }
        });

        presenter.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(),
                selectedUser);

        TextView userName = findViewById(R.id.userName);
        userName.setText(selectedUser.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(selectedUser.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        Picasso.get().load(selectedUser.getImageUrl()).into(userImageView);

        followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, "..."));

        followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, "..."));

        followButton = findViewById(R.id.followButton);

        presenter.checkIsFollower(selectedUser, Cache.getInstance().getCurrUser(),
                Cache.getInstance().getCurrUserAuthToken());


        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.followButtonClick(v.getContext().getString(R.string.following),
                        followButton.getText().toString(),
                        Cache.getInstance().getCurrUserAuthToken(),
                        Cache.getInstance().getCurrUser(), selectedUser);
            }
        });
    }

    public MainPresenter getMainPresenter(User user) {
        return new MainPresenter(selectedUser, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutMenu) {
            presenter.logout(Cache.getInstance().getCurrUserAuthToken());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    // Login //
    @Override
    public void openLoginView() {
        //Revert to login screen.
        Intent intent = new Intent(this, LoginActivity.class);
        //Clear everything so that the main activity is recreated with the login page.
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Clear user data (cached data).
        Cache.getInstance().clearCache();
        startActivity(intent);
    }
    ////////////////

    // Follower & Followee Count //
    //// MainPresenter methods ///
    @Override
    public void setFolloweesCount(int followeesCount) {
        followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, String.valueOf(followeesCount)));
    }

    @Override
    public void setFollowersCount(int followersCount) {
        followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, String.valueOf(followersCount)));
    }
    ////   ////
    ////////////////

    // Follow Button //
    public void showFollowButton(boolean visible) {
        if (visible) {
            followButton.setVisibility(View.VISIBLE);
        } else {
            followButton.setVisibility(View.GONE);
        }
    }

    public void isFollowing(boolean following) {
        if (following) {
            followButton.setText(R.string.following);
            followButton.setBackgroundColor(getResources().getColor(R.color.white));
            followButton.setTextColor(getResources().getColor(R.color.lightGray));
        } else {
            followButton.setText(R.string.follow);
            followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

    }

    public void enableFollowButton(boolean enabled) {
        followButton.setEnabled(enabled);
    }
    ////////////////


    // Messages //
    public void showErrorMessage(String message) {
        errorToast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
        errorToast.show();
    }

    public void showInfoMessage(String message) {
        infoToast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
        infoToast.show();

    }

    public void hideInfoMessage() {
        if (infoToast != null) {
            infoToast.cancel();
            infoToast = null;
        }
    }

    public void hideErrorMessage() {
        if (errorToast != null) {
            errorToast.cancel();
            errorToast = null;
        }
    }

    //// StatusDialogFragment.Observer Methods ////
    @Override
    public void onStatusPosted(String post) {
        try {
            presenter.onStatusPosted(post, Cache.getInstance().getCurrUser(),
                    Cache.getInstance().getCurrUserAuthToken());
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            showErrorMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }
    /////////////////
}
