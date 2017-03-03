package io.github.easyintent.quickref;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.github.easyintent.quickref.fragment.BookmarkListFragment;
import io.github.easyintent.quickref.fragment.MessageDialogFragment;
import io.github.easyintent.quickref.fragment.ReferenceListFragment;

@EActivity
public class MainActivity extends AppCompatActivity
        implements
            NavigationView.OnNavigationItemSelectedListener,
            MessageDialogFragment.Listener {

    @ViewById
    protected Toolbar toolbar;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            mayPopFragment();
        }
    }

    private void mayPopFragment() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) {
            navigationView.setCheckedItem(R.id.nav_all);
            showMainFragment();
        } else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        showMainFragment();

        switch (item.getItemId()) {
            case R.id.nav_all:
                break;
            case R.id.nav_bookmark:
                showBookmark();
                break;
            case R.id.nav_about:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showBookmark() {
        FragmentManager manager = getSupportFragmentManager();
        BookmarkListFragment fragment = BookmarkListFragment.newInstance();
        manager.beginTransaction()
                .replace(R.id.content_frame, fragment, "bookmark_list")
                .addToBackStack("bookmark")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        ReferenceListFragment fragment = (ReferenceListFragment) manager.findFragmentByTag("reference_list");
        if (fragment != null) {
            return;
        }

        // show main category
        fragment = ReferenceListFragment.newInstance(null);
        manager.beginTransaction()
                .replace(R.id.content_frame, fragment, "reference_list")
                .addToBackStack("main")
                .commit();
    }

    @Override
    public void onOkClicked(MessageDialogFragment dialogFragment) {
        // nothing to do
    }

    private void showMainFragment() {
        setTitle(getString(R.string.app_name));
        getSupportFragmentManager()
                .popBackStack("main", 0);
    }
}
