package com.example.usermanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    // Khởi tạo adapter với FirebaseRecyclerOptions đã cấu hình
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    // Gắn dữ liệu từ model vào các thành phần giao diện người dùng của RecyclerView item
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView")  int position, @NonNull MainModel model) {
        holder.name.setText("Name: " + model.getName());
        holder.stcode.setText("Code: " + model.getStcode());
        holder.course.setText("Course: " + model.getCourse());
        holder.email.setText("Email: " + model.getEmail());
        // Sử dụng thư viện Glide để tải hình ảnh từ URL và hiển thị trong CircleImageView
        Glide.with(holder.img.getContext())
                .load(model.getTurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 2000)
                        .create();

                //---Đưa dữ liệu vào popup
                View view = dialogPlus.getHolderView();

                EditText name = view.findViewById(R.id.txtName);
                EditText course = view.findViewById(R.id.txtCourse);
                EditText email = view.findViewById(R.id.txtEmailId);
                EditText turl = view.findViewById(R.id.txtImgUrl);
                EditText stcode = view.findViewById(R.id.txtStcode);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                name.setText(model.getName());
                course.setText(model.getCourse());
                email.setText(model.getEmail());
                turl.setText(model.getTurl());
                stcode.setText(String.valueOf(model.getStcode()));

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //---Ẩn bàn phím khi nhập xong
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        //---Lưu các giá trị vào Map
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put("course", course.getText().toString());
                        map.put("email", email.getText().toString());
                        map.put("turl", turl.getText().toString());
                        try {
                            long stcodeValue = Long.parseLong(stcode.getText().toString());
                            map.put("stcode", stcodeValue);
                        } catch (NumberFormatException e) {
                            Toast.makeText(holder.name.getContext(), "The data type is incorrect", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("FirebaseUpdate", "Update data: " + map.toString());
                        //--- Cập nhật lên Firebase
                        FirebaseDatabase.getInstance().getReference().child("students")
                                .child(Objects.requireNonNull(getRef(position).getKey())).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(),"Update Success", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.name.getContext(),"Update Fail", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are you Sure");
                builder.setMessage("Deleted data can't be Undo.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("students")
                                .child(Objects.requireNonNull(getRef(position).getKey())).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.name.getContext(),"Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }

    @NonNull
    @Override
    // Tạo mới ViewHolder và kết nối nó với layout của mỗi item trong RecyclerView
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }
    // ViewHolder chứa các thành phần giao diện người dùng của mỗi item trong RecyclerView
    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView name,course,email, stcode;

        Button btnEdit, btnDelete;

        // Khởi tạo ViewHolder và liên kết các thành phần với layout
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            img  = (CircleImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.nametext);
            course = (TextView) itemView.findViewById(R.id.coursetext);
            email = (TextView) itemView.findViewById(R.id.emailtext);
            stcode = (TextView) itemView. findViewById(R.id.stcodetext);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
