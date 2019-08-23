package hr.dostavifrende.dostavifrende.util;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import hr.dostavifrende.dostavifrende.R;
import hr.dostavifrende.dostavifrende.core.FragmentExtension;
import hr.dostavifrende.dostavifrende.fragments.ActiveUsersFragment;
import hr.dostavifrende.dostavifrende.fragments.OfferServiceFragment;
import hr.dostavifrende.dostavifrende.fragments.ProfileFragment;
import hr.dostavifrende.dostavifrende.fragments.UsersFragment;

public class BottomNavigationManager {

    private static BottomNavigationManager sInstance;

    private List<FragmentExtension> bottomNavigationItems;

    private AppCompatActivity activity;
    private BottomNavigationView bottomNavigationView;
    private int dynamicGroupId;

    private BottomNavigationManager() {

        bottomNavigationItems = new ArrayList<>();
        bottomNavigationItems.add(new ActiveUsersFragment());
        bottomNavigationItems.add(new OfferServiceFragment());
        bottomNavigationItems.add(new UsersFragment());
        bottomNavigationItems.add(new ProfileFragment());

    }

    public static BottomNavigationManager newInstnace() {
        if (sInstance == null){
            sInstance = new BottomNavigationManager();
        }
        return sInstance;
    }

    public void setBottomNavigationDependencies(AppCompatActivity activity, BottomNavigationView bottomNavigationView, int dynamicGroupId) {
        this.activity = activity;
        this.bottomNavigationView = bottomNavigationView;
        this.dynamicGroupId = dynamicGroupId;

        setUpBottomNavigationDrawer();
    }

    public void addFragment(FragmentExtension fragment){
        bottomNavigationItems.add(fragment);
    }

    public void selectNavigationItem(MenuItem menuItem) {
        if (!menuItem.isChecked()) {
            menuItem.setChecked(true);

            FragmentExtension selectedItem = bottomNavigationItems.get(menuItem.getItemId());

            startModule(selectedItem);
        }
    }

    private void startModule(FragmentExtension module) {
        FragmentManager mFragmentManager = activity.getSupportFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, module.getFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void startMainModule() {

        FragmentExtension mainModule = bottomNavigationItems != null ? bottomNavigationItems.get(0) : null;
        if (mainModule != null)
            startModule(mainModule);
    }

    private void setUpBottomNavigationDrawer()
    {
        for (int i = 0; i < bottomNavigationItems.size(); i++) {
            FragmentExtension item = bottomNavigationItems.get(i);
            bottomNavigationView.getMenu()
                    .add(dynamicGroupId, i, i+1, item.getName())
                    .setCheckable(true)
                    .setIcon(item.getIcon());
        }
    }

}
