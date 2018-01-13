package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.MyListGridViewAdapter;
import com.cookery.models.CuisineMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.MessageMO;
import com.cookery.models.MyListMO;
import com.cookery.models.RecipeMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_MY_LIST;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LIST_ID;
import static com.cookery.utils.Constants.MASTER;
import static com.cookery.utils.Constants.MY_LISTS_EXISTS;

/**
 * Created by vishal on 13/12/17.
 */
public class AddMyListFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.mylist_add_ll)
    LinearLayout mylist_add_ll;

    @InjectView(R.id.mylist_add_fab)
    FloatingActionButton mylist_add_fab;

    @InjectView(R.id.mylist_add_tv)
    TextView mylist_add_tv;

    @InjectView(R.id.user_lists_gv)
    GridView user_lists_gv;

    @InjectView(R.id.mylist_no_list_tv)
    TextView mylist_no_list_tv;

    //components

    private FragmentManager fragmentManager;
    private List<IngredientMO> myIngredients;
    private RecipeMO recipe;
    private MasterDataMO masterData;
    private boolean mylistexists;
    private List<MyListMO> mylists;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mylist_add, container);
        ButterKnife.inject(this, view);


        getDataFromBundle();
        setupAddMyListFragment();

        return view;
    }

    private void setupAddMyListFragment() {

        if (mylistexists) {
            mylist_no_list_tv.setVisibility(View.GONE);
            user_lists_gv.setVisibility(View.VISIBLE);
            setUpUserAllListCards(mylists);
        } else {
            mylist_no_list_tv.setVisibility(View.VISIBLE);
            user_lists_gv.setVisibility(View.GONE);
        }

        mylist_add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddListFragment();
            }
        });

        mylist_add_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddListFragment();
            }
        });


        user_lists_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int listid = (Integer) view.getTag();
                if(R.id.grid_view_mylist_delete_iv == view.getId())
                {
                    deleteList(listid);
                }
                openViewListFragment(listid);
            }
        });
    }

    private void deleteList(int listid)
    {
        MessageMO message = new MessageMO();
        message.setErr_message("Are you sure you want to delete your list");
        message.setPurpose("CANCEL_DELETE_MYLIST");
        message.setError(false);

        Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_MY_LIST);
        Utility.showMessageDialog(getFragmentManager(), currentFrag, message);

        //new AddMyListFragment.AsyncTaskerDeleteMyList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listid);
    }

    class AsyncTaskerDeleteMyList extends AsyncTask<Object, Void, Object> {
        Fragment frag = null;
        @Override
        protected void onPreExecute() {
            frag = Utility.showWaitDialog(getFragmentManager(), "Deleting your List ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            int listid = (Integer) objects[0];

            if (listid == 0) {
                Log.e(CLASS_NAME, "Error ! listid object is null");
                return null;
            }

            // TODO: Delete List method
            //return InternetUtility.deleteList(mylistObj);
            MyListMO mylistObj = new MyListMO();
            return InternetUtility.saveList(mylistObj);
        }

        @Override
        protected void onPostExecute(Object object) {

            MessageMO message = (MessageMO) object;
            message.setPurpose("ADD_LIST");

            Utility.closeWaitDialog(getFragmentManager(), frag);

            Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_MY_LIST);
            Utility.showMessageDialog(getFragmentManager(), currentFrag, message);

            if(message.isError()){
                Log.e(CLASS_NAME, "Error : "+message.getErr_message());
            }
            else{
                Log.i(CLASS_NAME, message.getErr_message());
                dismiss();
            }
        }
    }

    private void setUpUserAllListCards(List<MyListMO> mylists)
    {
        MyListGridViewAdapter adapter = new MyListGridViewAdapter(mContext, mylists, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CuisineMO cuisine = (CuisineMO) view.getTag();

                if(getTargetFragment() instanceof AddRecipeFragment){
                    dismiss();
                    ((AddRecipeFragment)getTargetFragment()).setCuisine(cuisine);
                }
                else{
                    // Utility.showUnimplemetedActionSnacks(common_selection_rl);
                }
            }
        });
        user_lists_gv.setAdapter(adapter);
    }

    private void getDataFromBundle() {
        masterData = (MasterDataMO) getArguments().get(MASTER);
        if(masterData == null || masterData.getCuisines() == null || masterData.getCuisines().isEmpty()
                || masterData.getFoodTypes() == null || masterData.getFoodTypes().isEmpty()
                || masterData.getQuantities() == null || masterData.getQuantities().isEmpty()
                || masterData.getTastes() == null || masterData.getTastes().isEmpty()){

            Log.e(CLASS_NAME, "Error ! Master data is null or required data in master data is not found !");
            dismiss();
        }

        mylistexists = (Boolean) getArguments().get(MY_LISTS_EXISTS);
        mylists = (ArrayList<MyListMO>) getArguments().get(GENERIC_OBJECT);
    }

    private void openAddListFragment()
    {
        Bundle bundle = new Bundle();

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(MASTER, masterData);
        paramsMap.put(GENERIC_OBJECT, new MyListMO());

        for(Map.Entry<String, Object> iterMap : paramsMap.entrySet()){
            bundle.putSerializable(iterMap.getKey(), (Serializable) iterMap.getValue());
        }

        MyListFragment fragment = new MyListFragment();
        fragment.setArguments(bundle);
        String fragmentNameStr = FRAGMENT_MY_LIST;
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        fragment.show(manager, fragmentNameStr);
    }

    private void openViewListFragment(int listid)
    {
        Bundle bundle = new Bundle();

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(MASTER, masterData);
        paramsMap.put(GENERIC_OBJECT, new MyListMO());
        paramsMap.put(LIST_ID, listid);

        for(Map.Entry<String, Object> iterMap : paramsMap.entrySet()){
            bundle.putSerializable(iterMap.getKey(), (Serializable) iterMap.getValue());
        }

        MyListFragment fragment = new MyListFragment();
        fragment.setArguments(bundle);
        String fragmentNameStr = FRAGMENT_MY_LIST;
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        fragment.show(manager, fragmentNameStr);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d!=null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }
}
