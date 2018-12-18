package robertapengelly.dimensionconverter;

import  android.content.res.Resources;
import  android.os.Build;
import  android.os.Bundle;
import  android.support.design.widget.TabLayout;
import  android.support.v4.view.ViewPager;
import  android.support.v7.app.AppCompatActivity;
import  android.support.v7.widget.Toolbar;
import  android.util.TypedValue;
import  android.view.View;
import  android.view.ViewGroup;
import  android.view.ViewTreeObserver;

import  java.util.ArrayList;
import  java.util.Arrays;

import  robertapengelly.dimensionconverter.app.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> metrics;
    private ArrayList<String> tab_titles;
    
    private ViewPagerAdapter adapter;
    private Toolbar appbar;
    private TabLayout layout_tabs;
    private ViewPager viewpager;
    
    // Adjust any views obstructed by the translucent status bar.
    private void adjustIfNeeded(View[] views) {
    
        // We are only using windowTranslucentStatus on lollipop and newer.
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        
        TypedValue val = new TypedValue();
        
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(android.R.attr.windowTranslucentStatus, val, true);
        
        if (val.data == 0) {
            return;
        }
        
        // Get the status bar height identifier.
        final int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        
        if (resourceId == 0) {
            return;
        }
        
        // Get the status bar height.
        final int height = getResources().getDimensionPixelSize(resourceId);
        
        // Adjust our views padding.
        for (View view : views) {
        
            if (view != null) {
                view.setPaddingRelative(view.getPaddingStart(), view.getPaddingTop() + height, view.getPaddingEnd(), view.getPaddingBottom());
            }
        
        }
    
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);
        adjustIfNeeded(new View[] { appbar });
        
        metrics = new ArrayList<> (Arrays.asList(getResources().getStringArray(R.array.metrics)));
        tab_titles = new ArrayList<> (Arrays.asList(getResources().getStringArray(R.array.tab_titles)));
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), metrics, tab_titles);
        
        viewpager = findViewById(R.id.viewpager);
        viewpager.setAdapter(adapter);
        
        layout_tabs = findViewById(R.id.layout_tabs);
        layout_tabs.setupWithViewPager(viewpager);
    
    }

}