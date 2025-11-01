package com.donchacko.ieee754calc;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ConversionFragment();
            case 1:
                return new CalculatorFragment();
            default:
                return new ConversionFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

