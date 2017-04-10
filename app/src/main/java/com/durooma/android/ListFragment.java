package com.durooma.android;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.durooma.android.api.ApiLoader;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import java.util.ArrayList;
import java.util.List;

public abstract class ListFragment<T>
        extends Fragment
        implements LoaderManager.LoaderCallbacks<List<T>>,
        AbsListView.MultiChoiceModeListener,
        SwipeRefreshLayout.OnRefreshListener {

    protected static final int STATUS_EMPTY = 0;
    protected static final int STATUS_LIST = 1;
    protected static final int STATUS_ERROR = 2;

    protected static final int LOAD_ITEMS = 0;

    ListView list;
    View empty;
    View error;
    FloatingActionButton fab;
    SwipeRefreshLayout swipeRefresh;

    private ArrayAdapter<T> listAdapter;

    private View[] statusViews;

    protected abstract ArrayAdapter<T> onCreateAdapter(Context context);

    protected abstract Observable<List<T>> getItems();

    protected abstract Observable<Void> removeItem(long id);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        list = (ListView)view.findViewById(R.id.list);
        empty = view.findViewById(R.id.empty);
        error = view.findViewById(R.id.error);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        statusViews = new View[]{empty, list, error};
        getLoaderManager().initLoader(LOAD_ITEMS, null, this);
        listAdapter = onCreateAdapter(getContext());
        list.setAdapter(listAdapter);
        list.setMultiChoiceModeListener(this);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                menuRefresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setStatus(int status) {
        list.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        statusViews[status].setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        return new ApiLoader<>(getContext(), getItems());
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        swipeRefresh.setRefreshing(false);
        listAdapter.clear();
        if (data != null) {
            listAdapter.addAll(data);
            setStatus(data.isEmpty() ? STATUS_EMPTY : STATUS_LIST);
        } else {
            setStatus(STATUS_ERROR);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    protected abstract String getEditModeTitle();

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.edit_items, menu);
        mode.setTitle(getEditModeTitle());
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
        List<Observable<Void>> deleteOperations = new ArrayList<>();
        for (Long id : list.getCheckedItemIds()) {
            deleteOperations.add(removeItem(id));
        }
        Observable.merge(deleteOperations)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getLoaderManager().restartLoader(LOAD_ITEMS, null, ListFragment.this);
                        mode.finish();
                    }
                });
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }

    void refresh() {
        getLoaderManager().restartLoader(LOAD_ITEMS, null, this);
    }

    void menuRefresh() {
        swipeRefresh.setRefreshing(true);
        refresh();
    }


    @Override
    public void onRefresh() {
        refresh();
    }
}
