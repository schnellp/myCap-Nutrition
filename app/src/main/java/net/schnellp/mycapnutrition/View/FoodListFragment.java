package net.schnellp.mycapnutrition.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import net.schnellp.mycapnutrition.presenter.FoodSearchAdapter;
import net.schnellp.mycapnutrition.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodListFragment extends Fragment {

    private static final String ARG_FOOD_TYPE = "foodType";

    private int foodType;

    private OnFragmentInteractionListener mListener;

    private ListView listView;
    public FoodSearchAdapter adapter;

    public FoodListFragment() {
        // Required empty public constructor
    }

    public static FoodListFragment newInstance(int foodType) {
        FoodListFragment fragment = new FoodListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FOOD_TYPE, foodType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            foodType = getArguments().getInt(ARG_FOOD_TYPE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new FoodSearchAdapter(this, foodType);

        EditText searchBar = (EditText) getActivity().findViewById(R.id.editTextFoodSearch);
        if (searchBar != null) {
            adapter.search(searchBar.getText().toString());
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);
        listView.setOnItemLongClickListener(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.listViewFoodResults);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
