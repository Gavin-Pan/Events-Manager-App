package com.example.a1_gavinpan_fit2081;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.example.a1_gavinpan_fit2081.provider.Category;
import com.example.a1_gavinpan_fit2081.provider.EmaViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListCategory extends Fragment {

    ArrayList<Category> listCategories = new ArrayList<>();
    MyCategoryRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Gson gson = new Gson();

    private EmaViewModel emaViewModel;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListCategory newInstance(String param1, String param2) {
        FragmentListCategory fragment = new FragmentListCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_list_category, container, false);
        // initialise ViewModel


        // Initialize views
        recyclerView = fragmentView.findViewById(R.id.categoryRecycler);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        //SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(KeyStore.CATEGORY_FILE_NAME, MODE_PRIVATE);
        //String readArrayListString = sharedPreferences.getString("data_key", "[]");

        // Convert the restored string back to ArrayList
        //Type type = new TypeToken<ArrayList<Category>>(){}.getType();
        //listCategories = gson.fromJson(readArrayListString, type);

        recyclerAdapter = new MyCategoryRecyclerAdapter();
        //recyclerAdapter.setData(listCategories);
        recyclerView.setAdapter(recyclerAdapter);

        emaViewModel = new ViewModelProvider(this).get(EmaViewModel.class);
        emaViewModel.getAllCategories().observe(getViewLifecycleOwner(), newData -> {
            // cast List<Item> to ArrayList<Item>
            recyclerAdapter.setData(new ArrayList<Category>(newData));
            recyclerAdapter.notifyDataSetChanged();
        });

        return fragmentView;
    }


}
