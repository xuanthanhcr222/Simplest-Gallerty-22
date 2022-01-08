package com.example.galleryv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ImageDetails extends Activity {
    TextView name, date, dimension, size, src;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details);

        name = (TextView) findViewById(R.id.details_name);
        date = (TextView) findViewById(R.id.details_date);
        dimension = (TextView) findViewById(R.id.details_dimension);
        size = (TextView) findViewById(R.id.details_size);
        src = (TextView) findViewById(R.id.details_src);

        getData();
    }

    public void getData() {
        if (getIntent().hasExtra("details_name")) {
            name.setText("Tên: " + getIntent().getStringExtra("details_name"));
        }
        else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        if (getIntent().hasExtra("details_date")) {
            date.setText("Ngày: " + getIntent().getStringExtra("details_date"));
        }

        if (getIntent().hasExtra("details_dimension")) {
            dimension.setText("Kích thước: " + getIntent().getStringExtra("details_dimension"));
        }

        if (getIntent().hasExtra("details_size")) {
            size.setText("Kích cỡ: " + getIntent().getStringExtra("details_size"));
        }

        if (getIntent().hasExtra("details_src")) {
            src.setText("Nguồn: " + getIntent().getStringExtra("details_src"));
        }
    }
}
