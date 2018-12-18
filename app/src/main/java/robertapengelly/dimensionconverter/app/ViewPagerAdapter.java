package robertapengelly.dimensionconverter.app;

import  android.os.Bundle;
import  android.support.v4.app.Fragment;
import  android.support.v4.app.FragmentManager;
import  android.support.v4.app.FragmentPagerAdapter;

import  java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> metrics;
    private ArrayList<String> tab_titles;
    
    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> metrics, ArrayList<String> tab_titles) {
        super(fm);
        
        this.metrics = metrics;
        this.tab_titles = tab_titles;
    
    }
    
    @Override
    public int getCount() {
        return tab_titles.size();
    }
    
    @Override
    public Fragment getItem(int position) {
    
        PageFragment fragment = new PageFragment();
        position++;
        
        ArrayList<String> metrics = new ArrayList<> ();
        
        if (position == 1) {
            metrics.addAll(this.metrics);
        } else if (position == 2) {
        
            for (String metric : this.metrics) {
                metrics.add("Squared " + metric.toLowerCase());
            }
        
        } else if (position == 3) {
        
            for (String metric : this.metrics) {
                metrics.add("Cubic " + metric.toLowerCase());
            }
        
        }
        
        Bundle bundle = new Bundle();
        bundle.putString("edittext_from", "1");
        bundle.putStringArrayList("metrics", metrics);
        bundle.putInt("hide_from", 1);
        bundle.putInt("hide_to", 0);
        
        fragment.setArguments(bundle);
        return fragment;
    
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return tab_titles.get(position);
    }

}