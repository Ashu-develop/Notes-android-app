package com.example.powernotes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class notesActivity extends AppCompatActivity {

    private FloatingActionButton mcreatenotesfab;
    private FirebaseAuth firebaseAuth;

    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<FirebaseModel,noteViewAdapter> noteAdapter;















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        getSupportActionBar().setTitle("Make Notes");

        mcreatenotesfab = findViewById(R.id.createnotefab);

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();//this will take the data from the current user
        firebaseFirestore = FirebaseFirestore.getInstance();

        mcreatenotesfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(notesActivity.this,CreateNote.class));

            }
        });


        Query query =firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("mynotes").orderBy("title",Query.Direction.ASCENDING);//this is path for storing the data in firebase firestore
        FirestoreRecyclerOptions<FirebaseModel> allusernotes = new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query,FirebaseModel.class).build();//the dat is sored in variable named allusernote to set in adapter
        noteAdapter = new FirestoreRecyclerAdapter<FirebaseModel, noteViewAdapter>(allusernotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull noteViewAdapter noteViewAdapter, int position, @NonNull  FirebaseModel firebaseModel) {


                ImageView popupbutton = noteViewAdapter.itemView.findViewById(R.id.menupopupbutton);

                noteViewAdapter.notetitle.setText(firebaseModel.getTitle());//getting data to recycler view in adapter
                noteViewAdapter.notecontent.setText(firebaseModel.getTitle());

                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();//this docid can be used to delete and update note


                int colorcode = getRandomColor();
                noteViewAdapter.mnote.setBackgroundColor(noteViewAdapter.itemView.getResources().getColor(colorcode, null));


                noteViewAdapter.itemView.setOnClickListener(new View.OnClickListener() {// when clicked on note view adapter
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                        intent.putExtra("title", firebaseModel.getTitle());//this will pass the title and content to editnote activity
                        intent.putExtra("content", firebaseModel.getContent());
                        intent.putExtra("noteid", docId);
                        v.getContext().startActivity(intent);


                    }
                });

                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {//this will add options to popupmenu
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                                intent.putExtra("title", firebaseModel.getTitle());//this will pass the title and content to editnote activity
                                intent.putExtra("content", firebaseModel.getContent());
                                intent.putExtra("noteid", docId);
                                v.getContext().startActivity(intent);

                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("mynotes").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "your note is deleted", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "your note is not deleted", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                return false;


                            }

                        });


                        popupMenu.show();
                    }

                });





            }

            @NonNull
            @Override
            public noteViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new noteViewAdapter(view);
            }
        };

        mrecyclerview = findViewById(R.id.recyclerview);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview.setAdapter(noteAdapter);


    }


    public class noteViewAdapter extends RecyclerView.ViewHolder{

        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;

        public noteViewAdapter(@NonNull  View itemView) {
            super(itemView);

            notetitle = itemView.findViewById(R.id.notetitle);
            notecontent = itemView.findViewById(R.id.notecontent);
            mnote = itemView.findViewById(R.id.note);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);//this will show the menu bar in this activity
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                fileList();
                startActivity(new Intent(notesActivity.this,MainActivity.class));


        }

        switch (item.getItemId()){
            case R.id.AboutApp:
                fileList();
                startActivity(new Intent(notesActivity.this,AboutApp.class));
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onStart() {

        noteAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {

        if(noteAdapter!=null){//if note adapter is empty
            noteAdapter.stopListening();
        }
        super.onStop();
    }
    private int getRandomColor(){
        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.color3);
        colorcode.add(R.color.color4);
        colorcode.add(R.color.color5);
        colorcode.add(R.color.color6);
        colorcode.add(R.color.color7);
        colorcode.add(R.color.color8);
        colorcode.add(R.color.color9);
        colorcode.add(R.color.color10);

        Random random = new Random();//this will select the color randomly from arraylist
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);



    }
}