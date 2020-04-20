package com.skku.userweb.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.skku.userweb.fragment.ContactFragment;
import com.skku.userweb.fragment.MapFragment;
import com.skku.userweb.fragment.StatusFragment;
import com.skku.userweb.fragment.TestFragment;
import com.skku.userweb.fragment.UserFragment;

import java.util.ArrayList;

public class MainFragmentAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragList = new ArrayList<>();
    public MainFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

        fragList.add(new MapFragment());
        fragList.add(new StatusFragment());
        fragList.add(new ContactFragment());
        fragList.add(new UserFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragList.size();
    }
}
