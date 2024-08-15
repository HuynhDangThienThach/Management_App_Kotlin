package com.example.usermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    FloatingActionButton floatingActionButton;

    //--- Thiết lập giao diện người dùng và khởi tạo RecyclerView và MainAdapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo RecyclerView từ layout
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        // Thiết lập layout manager cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Cấu hình FirebaseRecyclerOptions để lấy dữ liệu từ Firebase và chuyển vào adapter
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students"), MainModel.class)
                        .build();
        // Khởi tạo MainAdapter với các options đã cấu hình
        mainAdapter = new MainAdapter(options);
        // Thiết lập adapter cho RecyclerView
        recyclerView.setAdapter(mainAdapter);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bắt đầu lắng nghe sự kiện Firebase khi ứng dụng bắt đầu
        mainAdapter.startListening();
    }



    //--Tạo phương thức để gọi menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Nạp menu từ tệp tin XML search.xml vào menu của hoạt động
        getMenuInflater().inflate(R.menu.search, menu);
        // Lấy đối tượng MenuItem từ menu với id là search
        MenuItem item = menu.findItem(R.id.search);
        // Lấy đối tượng SearchView từ MenuItem để thực hiện tìm kiếm
        SearchView searchView = (SearchView)item.getActionView();
        // Lắng nghe sự kiện khi người dùng thực hiện tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            // Gọi phương thức txtSearch khi người dùng nhấn nút tìm kiếm trên bàn phím
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }
            // Gọi phương thức txtSearch khi văn bản trong ô tìm kiếm thay đổi
            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void txtSearch(String str){
        // Cấu hình FirebaseRecyclerOptions để lấy dữ liệu từ Firebase và chuyển vào adapter
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students").orderByChild("name").startAt(str).endAt(str + "~"), MainModel.class)
                        .build();
        // Tạo adapter mới sử dụng FirebaseRecyclerOptions đã cấu hình
        mainAdapter = new MainAdapter(options);

        // Bắt đầu lắng nghe sự kiện thay đổi trên dữ liệu Firebase để cập nhật giao diện người dùng
        mainAdapter.startListening();

        // Gán adapter cho RecyclerView để hiển thị danh sách sinh viên
        recyclerView.setAdapter(mainAdapter);
    }
}