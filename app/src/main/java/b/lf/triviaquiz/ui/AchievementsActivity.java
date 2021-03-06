package b.lf.triviaquiz.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.ui.achievements_fragments.CurrentAchievementsFragment;
import b.lf.triviaquiz.ui.achievements_fragments.TotalAchievementsFragment;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.TriviaQuizBaseViewModel;

public class AchievementsActivity extends TriviaQuizBaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private TriviaQuizBaseViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievemnts);

        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //tabs
        ViewPager viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new CurrentAchievementsFragment(), getString(R.string.current_frag));
        adapter.addFragment(new TotalAchievementsFragment(), getString(R.string.total_frag));
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        setupViewModel();
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(TriviaQuizBaseViewModel.class);
        mViewModel.setUserLiveDataFilter(SharedPreferencesUtils.retrieveCurrentUserId(this));
        mViewModel.getUserWithAchievements().observe(this, usr -> processGettingCurrentUserFromDb());
    }

    private void processGettingCurrentUserFromDb(){
        setNavigationViewUserInfo(((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0),mViewModel);
        mViewModel.getUser().removeObservers(this);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
