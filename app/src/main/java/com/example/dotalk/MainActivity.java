package com.example.dotalk;


import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String strNick, strProfileImg;
    EditText user_chat;
    Button user_next, add_chat_btn, btn_delete;
    Spinner chat_room_list;
    DatabaseReference dbref;



    ValueEventListener listener;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        TextView tv_nick = findViewById(R.id.tv_nickName);
        ImageView iv_profile = findViewById(R.id.iv_profile);
        user_chat = (EditText) findViewById(R.id.user_chat);
        chat_room_list = (Spinner) findViewById(R.id.chat_room_list);
        user_next = (Button) findViewById(R.id.user_next);
        add_chat_btn = (Button) findViewById(R.id.add_chat_btn);
        btn_delete = (Button) findViewById(R.id.btn_delete);



        dbref= FirebaseDatabase.getInstance().getReference().child("chatroom");



        list=new ArrayList<String>();
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, list);
        chat_room_list.setAdapter(adapter);

        Intent intent = getIntent();
        strNick = intent.getStringExtra("name");
        strProfileImg = intent.getStringExtra("profileImg");


        // 닉네임 set
        tv_nick.setText(strNick);
        // 프로필 이미지 사진 set
        Glide.with(this).load(strProfileImg).into(iv_profile);


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String del_chat = chat_room_list.getSelectedItem().toString();
                dbref.child("chat").child(del_chat).removeValue(); //선택한 chat_room 에서의 내용을 삭제
                dbref.child(del_chat).removeValue(); // chat_room을 삭제
                onRestart(); // 새로고침

            }

        });

        // onclick으로 데이터를 삽입 here
        user_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_chat.getText().toString().equals(""))//채팅방 생성에 값이 들어가있지 않으면 return
                    return;

                Intent intent = new Intent(MainActivity.this, ChatActivity.class); //메인에서 채팅방으로 값을 넘김
                intent.putExtra("chatName", user_chat.getText().toString()); //user_chat에 입력된 값을 넘김
                intent.putExtra("userName", strNick); //유저 이름을 넘김
                startActivity(intent);
                insertdata();
            }
        });


        add_chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_chat = chat_room_list.getSelectedItem().toString();
                Intent intent = new Intent(MainActivity.this, ChatActivity.class); //메인에서 채팅방으로 값을 넘김
                intent.putExtra("chatName", str_chat); //spinner에 selected 된 값을 넘김
                intent.putExtra("userName", strNick); //유저 이름을 넘김
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "성공적으로 입장 되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        fetchdata();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu1:
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback()
                {
                    @Override
                    public void onCompleteLogout()
                    {
                        /*Toast.makeText(getApplicationContext(), "성공적으로 로그아웃 되었습니다.", Toast.LENGTH_LONG).show();*/
                        finish(); // 현재 액티비티 종료
                    }
                });
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    //새로고침
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", strNick);   //새로고침시 프로필 값이 날라가기 때문에 다시 가져옴
        intent.putExtra("profileImg", strProfileImg);
        startActivity(intent);
        finish();

    }

    public void insertdata() { //데이타를 삽입하는 구문
        String data = user_chat.getText().toString().trim();
        dbref.child("chat").child(data).setValue(data) //child(data)로 chat 노드 아래에 채팅방 이름을 입력받음
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user_chat.setText(""); //user_chat에 들어갈 내용 나중에 spinner에 선택한 값이 여기에 나오도록 설정
                        list.clear();
                        fetchdata();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "성공적으로 생성되었습니다", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void fetchdata() {
        listener = dbref.child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mydata : snapshot.getChildren()){

                    list.add(mydata.getValue().toString());
                    adapter.notifyDataSetChanged();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}