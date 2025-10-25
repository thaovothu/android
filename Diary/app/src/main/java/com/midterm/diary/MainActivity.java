package com.midterm.diary;
import com.midterm.diary.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.midterm.diary.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseFirestore firestore;
    private RecyclerView rvNotes;
    private FloatingActionButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("posts");
        firestore = FirebaseFirestore.getInstance();

        rvNotes = findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        btnAdd = findViewById(R.id.btn_add);


        //login("test@gmail.com","123456");
        //register("thao@gmail.com", "123456");
        //postDataToRealTimeDB();
        //readDataFromRealTimeDB();
        //postDataToFiresStore();
        //addPostData(new Post("thuthao","ne"));
        //addNote();
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addNote();
            }
        });
    }

    public void addNote(){

        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.add_note, null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);

        Button save = mView.findViewById(R.id.btn_save);
        EditText editTitle = mView.findViewById(R.id.edt_title);
        EditText editContent = mView.findViewById(R.id.edt_content);



        save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String id = myRef.push().getKey();
                                        String title = editTitle.getText().toString();
                                        String content = editContent.getText().toString();
                                        String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                                                .format(new java.util.Date());
                                        myRef.child(id).setValue(new Post(id, title, content, getRandomColor(),date)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) Toast.makeText(MainActivity.this, "Add note success", Toast.LENGTH_SHORT).show();
                                                else Toast.makeText(MainActivity.this, "Add note fail", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                        dialog.dismiss();
                                    }
                                });
        dialog.show();


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(myRef, Post.class)
                .build();

        FirebaseRecyclerAdapter<Post, PostHolder> adapter = new FirebaseRecyclerAdapter<Post, PostHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull Post model) {

                ImageView ivAction = holder.itemView.findViewById(R.id.iv_action);

//                ivAction.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
//                        popupMenu.setGravity(Gravity.END);
//                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem menuItem) {
//                                return false;
//                            }
//
//                        });
//
//                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem menuItem) {
//                                return false;
//                            }
//
//                        });
//
//                        popupMenu.show();
//                    }});

                ivAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                        popupMenu.setGravity(Gravity.END);

                        // --- EDIT ---
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(menuItem -> {
                            showEditDialog(model);  // gọi hàm sửa
                            return true;
                        });

                        // --- DELETE ---
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(menuItem -> {
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Xóa ghi chú")
                                    .setMessage("Bạn có chắc muốn xóa ghi chú này không?")
                                    .setPositiveButton("Xóa", (dialog, which) -> {
                                        myRef.child(model.getId()).removeValue()
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(view.getContext(), "Đã xóa ghi chú", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(view.getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    })
                                    .setNegativeButton("Hủy", null)
                                    .show();
                            return true;
                        });

                        popupMenu.show();
                    }
                });



                holder.tvTitle.setText(model.getTitle());
                holder.tvContent.setText(model.getContent());
                holder.tvDate.setText(model.getDate());
                holder.layoutNote.setBackgroundColor(Color.parseColor(model.getColor()));
            }

            @NonNull
            @Override
            public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.note_items, parent, false);
                return new PostHolder(view);
            }


        };

        rvNotes.setAdapter(adapter);
        adapter.startListening();
    }
    private String getRandomColor(){
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#35ad68");
        colors.add("#c27ba0");
        colors.add("#baa9aa");
        colors.add("#bfbd97");
        colors.add("#fc8eac");
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvContent;
        public TextView tvDate;
        public LinearLayout layoutNote;
        public PostHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
            tvContent = view.findViewById(R.id.tv_content);
            tvDate = view.findViewById(R.id.tv_date);
            layoutNote = view.findViewById(R.id.layout_note);
        }

    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("DEBUG", "Login sucess");
                        } else {
                            Log.d("DEBUG", "Login failed");
                        }
                    }
                });
    }

    private void register(String registerMail, String registerPass) {
        mAuth.createUserWithEmailAndPassword(registerMail, registerPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("DEBUG", "register succesfull");
                        } else Log.d("DEBUG", "register fail");
                    }
                });
    }

    private void resetPass(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("DEBUG", "reset succesfull");
                        } else Log.d("DEBUG", "reset fail");
                    }
                });
    }

    private void sigOut(){
        mAuth.signOut();
    }
    private void postDataToRealTimeDB( String data){
        myRef.setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("DEBUG", "post succesfull");
                        } else Log.d("DEBUG", "post fail");
                    }
                });
    }
    private void readDataFromRealTimeDB() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Log.d("DEBUG", "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DEBUG", "Failed to read value.", error.toException());
            }
        });
    }

    public void postDataToFiresStore(){
        Map<String,Object> user = new HashMap<>();
        user.put("first","Ada");
        user.put("last","Lovelace");
        user.put("born",1815);
        firestore.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DEBUG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }

                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                        public void onFailure(@NonNull Exception e){
                        Log.w("DEBUG","Error adding document",e);
                    }
    });
    }
    public void addPostData (Post data){
        DatabaseReference myRefRoot = database.getReference();
        myRefRoot.child("post").setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("DEBUG", "post succesfull");
                        } else Log.d("DEBUG", "post fail");
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();

        if (itemId == R.id.mi_logout) {
            mAuth.signOut();

            // Quay lại LoginActivity sau khi logout
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // đóng MainActivity để tránh quay lại bằng nút Back

            Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showEditDialog(Post post) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.add_note, null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);

        EditText editTitle = mView.findViewById(R.id.edt_title);
        EditText editContent = mView.findViewById(R.id.edt_content);
        Button save = mView.findViewById(R.id.btn_save);

        // Gán dữ liệu cũ vào form
        editTitle.setText(post.getTitle());
        editContent.setText(post.getContent());

        save.setOnClickListener(v -> {
            String newTitle = editTitle.getText().toString();
            String newContent = editContent.getText().toString();

            if (newTitle.isEmpty() || newContent.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật dữ liệu trong Firebase
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("title", newTitle);
            updateData.put("content", newContent);

            myRef.child(post.getId()).updateChildren(updateData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    });
        });

        dialog.show();
    }

}
