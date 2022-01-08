package com.example.galleryv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SectionPageAdapter sectionPageAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sectionPageAdapter=new SectionPageAdapter(getSupportFragmentManager());
        viewPager=(ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }


    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter=new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentImage(),"Image");
        adapter.addFragment(new FragmentAlbum(),"Album");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_xml,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_setting:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about:
                Toast.makeText(this,"2",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
