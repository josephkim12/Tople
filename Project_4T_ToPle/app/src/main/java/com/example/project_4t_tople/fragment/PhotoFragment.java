package com.example.project_4t_tople.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.adapter.PhotoAdapter;
import com.example.project_4t_tople.model.Board;
import com.example.project_4t_tople.model.Mypage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    GridView gridViewPhoto;
    ArrayList<Board> list;


    PhotoAdapter adapter;

    //통신용 객체 선언
    AsyncHttpClient client;
    HttpResponse response;

    // listView용 URL
    String URLlist= "http://175.198.87.149:8080/moim.4t.spring/testselectMoimBoard.tople";

    // 데이터 가져올 객체 선언
    String user_id;
    Mypage item;

    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoFragment newInstance(String param1, String param2) {
        PhotoFragment fragment = new PhotoFragment();
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

            client=new AsyncHttpClient();
            response= new HttpResponse(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // intent와 bundle로 가져온 데이터
        if(getArguments() != null){
            user_id = getArguments().getString("user_id");
            item = (Mypage) getArguments().getSerializable("item");
        }

        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_photo, container, false);

        gridViewPhoto = view.findViewById(R.id.gridViewPhoto);

        list=new ArrayList<>();

        adapter=new PhotoAdapter(getActivity(),R.layout.grid_photo,list);
        gridViewPhoto.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getlist();
        Log.d("[INFO]", "userPhoto =" + user_id);
        Log.d("[INFO]", "userPhoto =" + item.getMoimname());
    }

    //  파라미터 보내기
    private void getlist() {
        RequestParams params = new RequestParams();
        params.put("moimcode", item.getMoimcode());
        client.post(URLlist,params,response);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public HttpResponse(Activity activity) {
            this.activity = activity;
        }

        //통신 시작
        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려 주세요...");
            dialog.setCancelable(false);
            dialog.show();
        }
        // 통신 종료
        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }
        //통신 성공
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            String strJson = new String(responseBody);

            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray normal = json.getJSONArray("normal");
                JSONArray feel = json.getJSONArray("feel");
                for (int i=0; i<normal.length(); i++) {
                    JSONObject temp = normal.getJSONObject(i);
                    Board normal_list = new Board();

                    normal_list.setListnum(temp.getInt("listnum"));
                    if (!temp.getString("filename").equals("")) {
                        normal_list.setFilename(temp.getString("filename"));
                    }
                    if (!temp.getString("thumb").equals("")) {
                        normal_list.setThumb(temp.getString("thumb"));
                    }
                    normal_list.setSubject(temp.getString("subject"));
                    normal_list.setId(temp.getString("id"));
                    normal_list.setMoimcode(temp.getInt("moimcode"));
                    normal_list.setLev(temp.getInt("lev"));
                    normal_list.setEditdate(temp.getString("editdate"));
                    normal_list.setContent(temp.getString("content"));


                    if (normal_list.getThumb() != null) adapter.add(normal_list);
                }
                for (int i=0; i<feel.length(); i++) {
                    JSONObject temp = feel.getJSONObject(i);
                    Board feel_list = new Board();

                    feel_list.setListnum(temp.getInt("listnum"));

                    if (!temp.getString("filename").equals("")) {
                        feel_list.setFilename(temp.getString("filename"));
                    }
                    if (!temp.getString("thumb").equals("")) {
                        feel_list.setThumb(temp.getString("thumb"));
                    }
                    feel_list.setSubject(temp.getString("subject"));
                    feel_list.setId(temp.getString("id"));
                    feel_list.setMoimcode(temp.getInt("moimcode"));
                    feel_list.setLev(temp.getInt("lev"));
                    feel_list.setEditdate(temp.getString("editdate"));
                    feel_list.setContent(temp.getString("content"));

                    if (feel_list.getThumb() != null) adapter.add(feel_list);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //통신 실패
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신 실패 \n PhotoFragment,"+ "statusCode =" + statusCode, Toast.LENGTH_SHORT).show();
        }
    }
}
