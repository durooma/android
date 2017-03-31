package com.durooma.android;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_transactions)
public class TransactionsFragment extends Fragment {

    @ViewById
    ViewPager pager;

    @ViewById
    TabLayout tabs;

    @AfterViews
    void init() {
        pager.setAdapter(new TransactionListPagerAdapter());
        tabs.setupWithViewPager(pager);
    }


    private class TransactionListPagerAdapter extends FragmentPagerAdapter {

        private String[] types = { "expense", "income", "transfer" };
        private int[] page_titles = { R.string.expense, R.string.income, R.string.transfer };

        TransactionListPagerAdapter() {
            super(getFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return new TransactionListFragment_.FragmentBuilder_()
                    .type(types[position])
                    .build();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getContext().getString(page_titles[position]);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
