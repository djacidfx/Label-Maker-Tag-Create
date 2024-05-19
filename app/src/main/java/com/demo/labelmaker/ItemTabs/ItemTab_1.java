package com.demo.labelmaker.ItemTabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.labelmaker.R;

import com.demo.labelmaker.Adapters.RecyclerItemClickListener;
import com.demo.labelmaker.Adapters.RecyclerViewCatAdapter;
import com.demo.labelmaker.OnComponentAddedListener;
import com.demo.labelmaker.Utility.Constants;


public class ItemTab_1 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int catPosition;
    RelativeLayout loadingLayout;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    RecyclerView mRecyclerView;
    OnComponentAddedListener onComponentAddedListener;

    
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public ItemTab_1() {
        this.catPosition = 0;
    }

    public ItemTab_1(int i) {
        this.catPosition = 0;
        this.catPosition = i;
    }

    public static ItemTab_1 newInstance(String str, String str2) {
        ItemTab_1 itemTab_1 = new ItemTab_1();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, str);
        bundle.putString(ARG_PARAM2, str2);
        itemTab_1.setArguments(bundle);
        return itemTab_1;
    }

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override 
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_item_tab_1, viewGroup, false);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recycleView);
        this.loadingLayout = (RelativeLayout) inflate.findViewById(R.id.loading);
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        this.mRecyclerView.setAdapter(new RecyclerViewCatAdapter(Constants.ALL_FRAMES[this.catPosition], getContext()));
        this.mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() { 
            @Override 
            public void onItemClick(View view, int i) {
                ItemTab_1.this.onComponentAddedListener.onAnyItemAdded(ItemTab_1.this.catPosition, i);
            }
        }));
        return inflate;
    }

    public void onButtonPressed(Uri uri) {
        OnFragmentInteractionListener onFragmentInteractionListener = this.mListener;
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.onFragmentInteraction(uri);
        }
    }

    @Override 
    public void onAttach(Context context) {
        super.onAttach(context);
        this.onComponentAddedListener = (OnComponentAddedListener) context;
        if (context instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) context;
            return;
        }
        throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    @Override 
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}
