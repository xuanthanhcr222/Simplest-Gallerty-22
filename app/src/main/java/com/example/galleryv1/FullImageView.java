package com.example.galleryv1;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.os.ConditionVariable;
        import android.telecom.Call;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.ImageView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

public class FullImageView extends AppCompatActivity {
    ImageView mainImageView;
    int myPhoto;
    private String name, date, dimension, size, src;
    private String PREFNAME = "myPrefFile1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_view);

        mainImageView = (ImageView) findViewById(R.id.imageView);
        updateMeUsingSavedStateData();

        getData();
        setData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveStateData(myPhoto);
    }

    protected void OnStart() {
        super.onStart();
        updateMeUsingSavedStateData();
    }

    private void updateMeUsingSavedStateData() {
        SharedPreferences myPrefContainer = getSharedPreferences(PREFNAME, Activity.MODE_PRIVATE);
        if (( myPrefContainer != null ) && myPrefContainer.contains("my_photo")){
            int image = myPrefContainer.getInt("my_photo", 0);
            mainImageView.setImageResource(image);
        }
    }

    private void saveStateData(int value) {
        SharedPreferences myPrefContainer = getSharedPreferences(PREFNAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor myPrefEditor = myPrefContainer.edit();
        /*String key = "my_photo";
        String value = txtSpyBox.getText().toString();*/
        myPrefEditor.putInt("my_photo", value);
        myPrefEditor.commit();
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_full_image,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_ttct:
            {
                Intent intent = new Intent(FullImageView.this, ImageDetails.class);
                intent.putExtra("details_name", name);
                intent.putExtra("details_date", date);
                intent.putExtra("details_dimension", dimension);
                intent.putExtra("details_size", size);
                intent.putExtra("details_src", src);

                startActivity(intent);

                setData();

                break;
            }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        if(getIntent().hasExtra("Photo")) {
            myPhoto = getIntent().getIntExtra("Photo", 1);
        }
        else {
            Toast.makeText(this, "No Data!", Toast.LENGTH_SHORT).show();
        }

        if(getIntent().hasExtra("details_name")) {
            name = getIntent().getStringExtra("details_name");
        }

        if (getIntent().hasExtra("details_date")) {
            date = getIntent().getStringExtra("details_date");
        }

        if (getIntent().hasExtra("details_dimension")) {
            dimension = getIntent().getStringExtra("details_dimension");
        }

        if (getIntent().hasExtra("details_size")) {
            size = getIntent().getStringExtra("details_size");
        }

        if (getIntent().hasExtra("details_src")) {
            src = getIntent().getStringExtra("details_src");
        }
    }

    private void setData() {
        mainImageView.setImageResource(myPhoto);
    }
}