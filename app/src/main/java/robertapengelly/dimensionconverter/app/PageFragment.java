package robertapengelly.dimensionconverter.app;

import  android.app.Activity;
import  android.os.Bundle;
import  android.support.annotation.NonNull;
import  android.support.v4.app.Fragment;
import  android.text.Editable;
import  android.text.TextWatcher;
import  android.view.KeyEvent;
import  android.view.LayoutInflater;
import  android.view.View;
import  android.view.ViewGroup;
import  android.view.ViewTreeObserver;
import  android.view.inputmethod.EditorInfo;
import  android.view.inputmethod.InputMethodManager;
import  android.widget.AdapterView;
import  android.widget.ArrayAdapter;
import  android.widget.EditText;
import  android.widget.Spinner;
import  android.widget.TextView;

import  java.math.BigDecimal;
import  java.util.ArrayList;

import  robertapengelly.dimensionconverter.R;

public class PageFragment extends Fragment implements
    AdapterView.OnItemSelectedListener,
    TextView.OnEditorActionListener,
    View.OnFocusChangeListener {
    
    private String prev_from, prev_to;
    
    private ArrayList<String> metrics;
    
    private EditText edittext_from;
    private EditText edittext_to;
    private Spinner spinner_from;
    private Spinner spinner_to;
    
    public PageFragment() {}
    
    private void calculate(EditText src, String src_metric, EditText dst, String dst_metric) {
    
        String txt_val = src.getText().toString();
        
        if (txt_val.equals("")) {
            return;
        }
        
        src_metric = src_metric.toLowerCase();
        
        if (src_metric.endsWith("s")) {
            src_metric = src_metric.substring(0, src_metric.length() - 1);
        }
        
        dst_metric = dst_metric.toLowerCase();
        
        if (dst_metric.endsWith("s")) {
            dst_metric = dst_metric.substring(0, dst_metric.length() - 1);
        }
        
        BigDecimal val = new BigDecimal(txt_val);
        
        if (src_metric.startsWith("cubic")) {
        
            src_metric = src_metric.substring("cubic".length() + 1, src_metric.length());
            dst_metric = dst_metric.substring("cubic".length() + 1, dst_metric.length());
            
            val = calculateVolume(val, src_metric, dst_metric);
        
        } else if (src_metric.startsWith("squared")) {
        
            src_metric = src_metric.substring("squared".length() + 1, src_metric.length());
            dst_metric = dst_metric.substring("squared".length() + 1, dst_metric.length());
            
            val = calculateArea(val, src_metric, dst_metric);
        
        } else {
            val = calculatemetric(val, src_metric, dst_metric);
        }
        
        txt_val = val.toPlainString();
        
        if (txt_val.contains(".")) {
            txt_val = txt_val.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
        
        if (!dst.getText().toString().equals(txt_val) && !dst.hasFocus()) {
            dst.setText(txt_val);
        }
    
    }
    
    private BigDecimal calculateArea(BigDecimal val, String src_metric, String dst_metric) {
    
        if (src_metric.equals("millimeter")) {
        
            if (dst_metric.equals("centimeter")) {
                return val.divide(new BigDecimal(100), 17, BigDecimal.ROUND_HALF_UP);
            } else if (dst_metric.equals("meter")) {
                return val.divide(new BigDecimal(1000000), 17, BigDecimal.ROUND_HALF_UP);
            }
        
        } else if (src_metric.equals("centimeter")) {
        
            if (dst_metric.equals("millimeter")) {
                return val.multiply(new BigDecimal(100));
            } else if (dst_metric.equals("meter")) {
                return val.divide(new BigDecimal(10000), 17, BigDecimal.ROUND_HALF_UP);
            }
        
        } else if (src_metric.equals("meter")) {
        
            if (dst_metric.equals("millimeter")) {
                return val.multiply(new BigDecimal(1000000));
            } else if (dst_metric.equals("centimeter")) {
                return val.multiply(new BigDecimal(10000));
            }
        
        }
        
        return val;
    
    }
    
    private BigDecimal calculatemetric(BigDecimal val, String src_metric, String dst_metric) {
    
        if (src_metric.equals("millimeter")) {
        
            if (dst_metric.equals("centimeter")) {
                return val.divide(new BigDecimal(10), 17, BigDecimal.ROUND_HALF_UP);
            } else if (dst_metric.equals("meter")) {
                return val.divide(new BigDecimal(1000), 17, BigDecimal.ROUND_HALF_UP);
            }
        
        } else if (src_metric.equals("centimeter")) {
        
            if (dst_metric.equals("millimeter")) {
                return val.multiply(new BigDecimal(10));
            } else if (dst_metric.equals("meter")) {
                return val.divide(new BigDecimal(100), 17, BigDecimal.ROUND_HALF_UP);
            }
        
        } else if (src_metric.equals("meter")) {
        
            if (dst_metric.equals("millimeter")) {
                return val.multiply(new BigDecimal(1000));
            } else if (dst_metric.equals("centimeter")) {
                return val.multiply(new BigDecimal(100));
            }
        
        }
        
        return val;
    
    }
    
    private BigDecimal calculateVolume(BigDecimal val, String src_metric, String dst_metric) {
    
        if (src_metric.equals("millimeter")) {
        
            if (dst_metric.equals("centimeter")) {
                return val.divide(new BigDecimal(1000), 17, BigDecimal.ROUND_HALF_UP);
            } else if (dst_metric.equals("meter")) {
                return val.divide(new BigDecimal(1000000000), 17, BigDecimal.ROUND_HALF_UP);
            }
        
        } else if (src_metric.equals("centimeter")) {
        
            if (dst_metric.equals("millimeter")) {
                return val.multiply(new BigDecimal(1000));
            } else if (dst_metric.equals("meter")) {
                return val.divide(new BigDecimal(1000000), 17, BigDecimal.ROUND_HALF_UP);
            }
        
        } else if (src_metric.equals("meter")) {
        
            if (dst_metric.equals("millimeter")) {
                return val.multiply(new BigDecimal(1000000000));
            } else if (dst_metric.equals("centimeter")) {
                return val.multiply(new BigDecimal(1000000));
            }
        
        }
        
        return val;
    
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    
        final View view = inflater.inflate(R.layout.layout_page, container, false);
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        
            @Override
            public boolean onPreDraw() {
            
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                
                BigDecimal w = new BigDecimal(view.getMeasuredWidth());
                w = w.multiply(new BigDecimal(0.1));
                
                int padding = w.intValue();
                view.setPadding(padding, 0, padding, 0);
                
                return true;
            
            }
        
        });
        
        Bundle args = getArguments();
        
        if (args == null) {
            return view;
        }
        
        edittext_from = view.findViewById(R.id.edittext_from);
        edittext_from.addTextChangedListener(new TextWatcher() {
        
            @Override
            public void afterTextChanged(Editable s) {}
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int string, int before, int count) {
            
                boolean multiple = !s.toString().equals("1");
                int selection = spinner_from.getSelectedItemPosition();
                
                ArrayAdapter adapter = (ArrayAdapter) spinner_from.getAdapter();
                count = adapter.getCount();
                
                for (int i = 0; i < count; i++) {
                
                    String metric = (String) adapter.getItem(i);
                    
                    if (metric == null) {
                        continue;
                    }
                    
                    if (multiple && !metric.endsWith("s")) {
                        metric = metric.concat("s");
                    } else if (!multiple && metric.endsWith("s")) {
                        metric = metric.substring(0, metric.length() - 1);
                    }
                    
                    adapter.insert(metric, i);
                    adapter.remove(adapter.getItem(i + 1));
                
                }
                
                spinner_from.setSelection(selection);
                
                String from = (String) spinner_from.getSelectedItem();
                String to = (String) spinner_to.getSelectedItem();
                calculate(edittext_from, from, edittext_to, to);
            
            }
        
        });
        edittext_from.setOnEditorActionListener(this);
        edittext_from.setOnFocusChangeListener(this);
        
        edittext_to = view.findViewById(R.id.edittext_to);
        edittext_to.addTextChangedListener(new TextWatcher() {
        
            @Override
            public void afterTextChanged(Editable s) {}
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int string, int before, int count) {
            
                boolean multiple = !s.toString().equals("1");
                int selection = spinner_to.getSelectedItemPosition();
                
                ArrayAdapter adapter = (ArrayAdapter) spinner_to.getAdapter();
                count = adapter.getCount();
                
                for (int i = 0; i < count; i++) {
                
                    String metric = (String) adapter.getItem(i);
                    
                    if (metric == null) {
                        continue;
                    }
                    
                    if (multiple && !metric.endsWith("s")) {
                        metric = metric.concat("s");
                    } else if (!multiple && metric.endsWith("s")) {
                        metric = metric.substring(0, metric.length() - 1);
                    }
                    
                    adapter.insert(metric, i);
                    adapter.remove(adapter.getItem(i + 1));
                
                }
                
                spinner_to.setSelection(selection);
                
                String from = (String) spinner_to.getSelectedItem();
                String to = (String) spinner_from.getSelectedItem();
                calculate(edittext_to, from, edittext_from, to);
            
            }
        
        });
        edittext_to.setOnEditorActionListener(this);
        edittext_to.setOnFocusChangeListener(this);
        
        metrics = args.getStringArrayList("metrics");
        
        spinner_from = view.findViewById(R.id.spinner_from);
        spinner_from.setOnItemSelectedListener(this);
        populateSpinner(spinner_from, metrics, args.getInt("hide_from"));
        
        spinner_to = view.findViewById(R.id.spinner_to);
        spinner_to.setOnItemSelectedListener(this);
        populateSpinner(spinner_to, metrics, args.getInt("hide_to"));
        
        prev_from = (String) spinner_from.getSelectedItem();
        prev_to = (String) spinner_to.getSelectedItem();
        edittext_from.setText(args.getString("edittext_from"));
        
        String from = (String) spinner_from.getSelectedItem();
        String to = (String) spinner_to.getSelectedItem();
        calculate(edittext_from, from, edittext_to, to);
        
        return view;
    
    }
    
    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
    
        boolean handled = false;
        
        if (getView() != null && getActivity() != null && actionId == EditorInfo.IME_ACTION_DONE) {
        
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            
            getView().requestFocus();
            handled = true;
        
        }
        
        return handled;
    
    }
    
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
    
        if (hasFocus || !(view instanceof EditText)) {
            return;
        }
        
        EditText edittext = (EditText) view;
        String s = edittext.getText().toString();
        
        if (s.equals("")) {
            edittext.setText("1");
        } else {
            edittext.setText(!s.contains(".") ? s : s.replaceAll("0*$", "").replaceAll("\\.$", ""));
        }
    
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    
        ArrayAdapter adapter = (ArrayAdapter) parent.getAdapter();
        String metric = (String) adapter.getItem(position);
        
        if (metric == null) {
            return;
        }
        
        if (metric.endsWith("s")) {
            metric = metric.substring(0, metric.length() - 1);
        }
        
        if (parent == spinner_from) {
        
            if (!metric.equals(prev_from)) {
            
                int index = metrics.indexOf(prev_from);
                String selection = (String) spinner_to.getSelectedItem();
                
                if (!edittext_to.getText().toString().equals("1")) {
                    prev_from = prev_from.concat("s");
                }
                
                adapter = (ArrayAdapter) spinner_to.getAdapter();
                adapter.insert(prev_from, index);
                adapter.remove(adapter.getItem(metrics.indexOf(metric)));
                
                prev_from = metric;
                spinner_to.setSelection(adapter.getPosition(selection));
            
            }
        
        } else if (parent == spinner_to) {
        
            if (!metric.equals(prev_to)) {
            
                int index = metrics.indexOf(prev_to);
                String selection = (String) spinner_from.getSelectedItem();
                
                if (!edittext_from.getText().toString().equals("1")) {
                    prev_to = prev_to.concat("s");
                }
                
                adapter = (ArrayAdapter) spinner_from.getAdapter();
                adapter.insert(prev_to, index);
                adapter.remove(adapter.getItem(metrics.indexOf(metric)));
                
                prev_to = metric;
                spinner_from.setSelection(adapter.getPosition(selection));
            
            }
        
        }
        
        String from = (String) spinner_from.getSelectedItem();
        String to = (String) spinner_to.getSelectedItem();
        calculate(edittext_from, from, edittext_to, to);
    
    }
    
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
    
    private void populateSpinner(Spinner spinner, ArrayList<String> metrics, int index) {
    
        if (getContext() == null) {
            return;
        }
        
        int count = metrics.size();
        ArrayList<String> list = new ArrayList<> ();
        
        for (int i = 0; i < count; i++) {
        
            if (i == index) {
                continue;
            }
            
            String metric = metrics.get(i);
            list.add(metric);
        
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<> (getContext(), android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);
    
    }

}