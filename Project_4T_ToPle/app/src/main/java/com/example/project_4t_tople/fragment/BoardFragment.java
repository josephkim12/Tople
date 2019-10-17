package com.example.project_4t_tople.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_4t_tople.R;
import com.example.project_4t_tople.activity.BoardDetailActivity;
import com.example.project_4t_tople.adapter.BoardAdapter;
import com.example.project_4t_tople.adapter.ShowAdapter;
import com.example.project_4t_tople.helper.FileUtils;
import com.example.project_4t_tople.helper.PhotoHelper;
import com.example.project_4t_tople.model.Board;
import com.example.project_4t_tople.model.Mypage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment
        implements OnClickListener, AdapterView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    LinearLayout fabLayout, fabLayout1, fabLayout2, boardLayout;
    LinearLayout binputLayout;
    TextView textFab1, textFab2;
    Button bbntBack, bbntWrite;
    EditText bSubject, bContent;
    ListView listNotice, listBoard;
    ArrayList<Board> list1, list2;
    ImageButton imageButton;
    ImageView imageView3;

    ShowAdapter adapter2;
    BoardAdapter adapter1;

    //통신용 객체 선언
    AsyncHttpClient client;
    HttpResponse_Boardlist response;

    String masterLev = "1";
    String lev = "";

    // 사진이 있는 게시글작성 URL
    String URLWithPhoto = "http://175.198.87.149:8080/moim.4t.spring/boardWrite.tople";
    String URLlist = "http://175.198.87.149:8080/moim.4t.spring/testselectMoimBoard.tople";

    // 데이터 가져올 객체 선언
    String user_id;
    Mypage item;
    Board board;

    // 업로드할 사진파일의 경로
    String filePathBig = null;

    public BoardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BoardFragment newInstance(String param1, String param2) {
        BoardFragment fragment = new BoardFragment();
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
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);

        client = new AsyncHttpClient();
        response = new HttpResponse_Boardlist(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // intent와 bundle로 가져온 데이터
        if (getArguments() != null) {
            user_id = getArguments().getString("user_id");
            item = (Mypage) getArguments().getSerializable("item");
        }

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_board, container, false);

        //플로팅 액션버튼
        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);

        textFab1 = view.findViewById(R.id.textFab1);
        textFab2 = view.findViewById(R.id.textFab2);

        fabLayout = view.findViewById(R.id.fabLayout);
        fabLayout1 = view.findViewById(R.id.fabLayout1);
        fabLayout2 = view.findViewById(R.id.fabLayout2);
        //게시판 레이아웃
        boardLayout = view.findViewById(R.id.boardLayout);
        //공지사항 레이아웃
        //일반게시글 레이아웃
        binputLayout = view.findViewById(R.id.binputLayout);

        // 글 상세 화면
        imageView3 = view.findViewById(R.id.imageView3);

        //공지사항 입력 후 리스트에 추가
        listNotice = view.findViewById(R.id.listNotice);
        listBoard = view.findViewById(R.id.listBoard);

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();

        adapter1 = new BoardAdapter(getActivity(), R.layout.list_notice, list1);
        adapter2 = new ShowAdapter(getActivity(), R.layout.list_board, list2);

        listNotice.setAdapter(adapter1);
        listBoard.setAdapter(adapter2);
        listBoard.setOnItemClickListener(this);
        listNotice.setOnItemClickListener(this);

        //게시글
        bbntBack = view.findViewById(R.id.bbntBack);
        bbntWrite = view.findViewById(R.id.bbntWrite);

        bSubject = view.findViewById(R.id.bSubject);
        bContent = view.findViewById(R.id.bContent);
        //게시글(이미지올리기)
        imageButton = view.findViewById(R.id.imageButton);

        imageButton.setOnClickListener(this);

        bbntBack.setOnClickListener(this);
        bbntWrite.setOnClickListener(this);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getlist();
    }

    //게시글 리스트 받아오는 통신
    public void getlist() {
        RequestParams params = new RequestParams();
        params.put("moimcode", item.getMoimcode());
        if (!adapter1.isEmpty())adapter1.clear();
        if (!adapter2.isEmpty()) adapter2.clear();
        client.post(URLlist, params, response);
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

    //이벤트설정
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 이미지 업로드 버튼
            case R.id.imageButton: // 작은이미지 업로드
                showLisDialogThumb();
                break;
            //플로팅 액션 버튼(+) 눌렀을때
            case R.id.fab:
                anim();
                break;
            case R.id.fab1://일반게시글 입력화면으로
                masterLev="2";
                anim();
                boardLayout.setVisibility(View.GONE);
                fabLayout.setVisibility(View.GONE);
                binputLayout.setVisibility(View.VISIBLE);
                imageView3.setVisibility(View.GONE);
                break;
            case R.id.fab2: //공지사항 입력화면으로  //+모임장 또는 관리자 권한을 가진사람만 보여주게해야함
                masterLev="1";
                anim();
                boardLayout.setVisibility(View.GONE);
                fabLayout.setVisibility(View.GONE);
                binputLayout.setVisibility(View.VISIBLE);
                imageView3.setVisibility(View.GONE);
                break;
            case R.id.bbntBack: //돌아가기 버튼
                bSubject.setText("");
                bContent.setText(""); //입력내용초기화
                binputLayout.setVisibility(View.GONE);
                boardLayout.setVisibility(View.VISIBLE);
                fabLayout.setVisibility(View.VISIBLE);
                break;
            case  R.id.bbntWrite: //공지사항입력
                String subject=bSubject.getText().toString().trim();
                String content=bContent.getText().toString().trim();

                //입력값이 있으면, 서버로 데이터 전송 및 요청
                RequestParams params = new RequestParams();

                if(masterLev.equals("1")){
                    lev = "1"; //공지사항 -1 일반게시글-2
                }else if(masterLev.equals("2")){
                    lev="2";
                }
                params.put("id",user_id);
                params.put("subject",subject);
                params.put("content",content);
                params.put("moimcode",item.getMoimcode());
                params.put("lev",lev);

                if (filePathBig == null) {
                    params.setForceMultipartEntityContentType(true);
                    imageView3.setVisibility(View.INVISIBLE);
                } else{
                    try {
                        params.put("filename",new File(filePathBig));
                        //  params.put("thumb",new File(filePathThumb));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                client.post(URLWithPhoto, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strJson = new String(responseBody);
                        try {
                            JSONObject json = new JSONObject(strJson);
                            String rt  = json.getString("result");
                            if(rt.equals("OK")){
                                Toast.makeText(getActivity(),"저장성공",Toast.LENGTH_SHORT).show();

                                getlist();
                            }else {
                                Toast.makeText(getActivity(),"실패",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getActivity(), "통신실패 BoardFragment 1"+statusCode, Toast.LENGTH_SHORT).show();
                    }
                });
                filePathBig=null;
                bSubject.setText("");
                bContent.setText("");
                binputLayout.setVisibility(View.GONE);
                boardLayout.setVisibility(View.VISIBLE);
                fabLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    //플로팅액션버튼 동작 함수
    public void anim() {
        if (isFabOpen) {
            fab.animate().rotationBy(60);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fabLayout1.setVisibility(View.GONE);
            fabLayout2.setVisibility(View.GONE);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab.animate().rotationBy(-60);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fabLayout1.setVisibility(View.VISIBLE);

            if(item.getPermit()>2){
                fabLayout2.setVisibility(View.GONE);
            }else {
                fabLayout2.setVisibility(View.VISIBLE);
            }

            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int listnum = 0;
        switch (parent.getId()) {
            case R.id.listNotice:
                Board temp1 = adapter1.getItem(position);
                listnum=temp1.getListnum();
                break;
            case R.id.listBoard:
                Board temp = adapter2.getItem(position);
                listnum=temp.getListnum();
                break;
        }
        Intent intent = new Intent(getActivity(), BoardDetailActivity.class);
        intent.putExtra("item",item);
        intent.putExtra("board",board);
        intent.putExtra("user_id",user_id);
        intent.putExtra("listnum", listnum);
        getActivity().startActivity(intent);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //게시글 목록 조회 통신
    class HttpResponse_Boardlist extends AsyncHttpResponseHandler {
        Activity activity;

        public HttpResponse_Boardlist(Activity activity) {
            this.activity = activity;
        }

        //통신 성공
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray normal = json.getJSONArray("normal");
                JSONArray feel = json.getJSONArray("feel");
                for (int i = 0; i < normal.length(); i++) {
                    JSONObject temp = normal.getJSONObject(i);
                    Board normal_list = new Board();

                    normal_list.setListnum(temp.getInt("listnum"));
                    if (!temp.getString("filename").equals("")) {
                        normal_list.setFilename(temp.getString("filename"));
                    }
                    if (!temp.getString("thumb").equals("")) {
                        normal_list.setThumb(temp.getString("thumb"));
                    }
                    normal_list.setName(temp.getString("name"));
                    normal_list.setSubject(temp.getString("subject"));
                    normal_list.setId(temp.getString("id"));
                    normal_list.setMoimcode(temp.getInt("moimcode"));
                    normal_list.setLev(temp.getInt("lev"));
                    normal_list.setEditdate(temp.getString("editdate"));
                    normal_list.setContent(temp.getString("content"));

                    adapter2.add(normal_list);
                }
                for (int i = 0; i < feel.length(); i++) {
                    JSONObject temp = feel.getJSONObject(i);
                    Board feel_list = new Board();

                    feel_list.setListnum(temp.getInt("listnum"));

                    if (!temp.getString("filename").equals("")) {
                        feel_list.setFilename(temp.getString("filename"));
                    }
                    if (!temp.getString("thumb").equals("")) {
                        feel_list.setThumb(temp.getString("thumb"));
                    }
                    feel_list.setName(temp.getString("name"));
                    feel_list.setSubject(temp.getString("subject"));
                    feel_list.setId(temp.getString("id"));
                    feel_list.setMoimcode(temp.getInt("moimcode"));
                    feel_list.setLev(temp.getInt("lev"));
                    feel_list.setEditdate(temp.getString("editdate"));
                    feel_list.setContent(temp.getString("content"));

                    adapter1.add(feel_list);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //통신 실패
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 BoardFragment 2" + statusCode, Toast.LENGTH_SHORT).show();
        }
    }

    //사진
    private void showLisDialogThumb() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"}; // It should be Array
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 새로 촬영하기
                        filePathBig = PhotoHelper.getInstance().getNewPhotoPath();
                        // 카메라 앱 호출출 위한 암묵적 인텐트
                        File file = new File(filePathBig);
                        Uri uri = null;
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(getActivity(),
                                    getActivity().getPackageName() + ".fileprovider", file);
                            camera_intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            camera_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(camera_intent, 100);
                        Log.d("[INFO]", uri.toString());

                        break;
                    case 1: // 갤러리에서 가져오기
                        Intent galleryIntent = null;
                        if (Build.VERSION.SDK_INT >= 19) {
                            galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        } else  {
                            galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        }
                        // 임시 파일만 필터링
                        galleryIntent.setType("image/*");
                        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY,true );
                        startActivityForResult(galleryIntent, 101);
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 100: // 카메라 앱
                // 촬영 결과를 갤러리 앱에 등록
                Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + filePathBig));
                if (getActivity() != null) {
                    getActivity().sendBroadcast(photoIntent);
                }
                break;
            case 101: // 갤러리 앱
                if (resultCode == getActivity().RESULT_OK) { // resultCode
                    filePathBig = FileUtils.getPath(getActivity(), data.getData());
                }
                break;
        }

        Glide
            .with(imageView3)
            .load(filePathBig)
            .error(R.drawable.ic_error_w)
            .placeholder(R.drawable.ic_empty_b)
            .into(imageView3);

        imageView3.setVisibility(View.VISIBLE);
    }
}