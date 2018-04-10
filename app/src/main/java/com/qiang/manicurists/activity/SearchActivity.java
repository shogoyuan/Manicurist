package com.qiang.manicurists.activity;

import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.qiang.manicurists.R;
import com.qiang.manicurists.adapter.SearchHotKeyGridviewAdapter;
import com.qiang.manicurists.adapter.SuggestionDataHelper;
import com.qiang.manicurists.bean.Suggestion;
import com.qiang.manicurists.util.BaseUtil;
import com.qiang.manicurists.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FloatingSearchView searchview;
    private boolean mIsDarkSearchTheme = false;

    private ArrayList<String> key_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initToolbar();
        initSearchView();
        initGridview();
    }

    private void initGridview() {
        key_list = new ArrayList<>();
        key_list.add("琉璃");key_list.add("美甲");key_list.add("绚丽");key_list.add("想不出了");
        key_list.add("真得想不出了");key_list.add("没了");
        GridView gridview = (GridView) this.findViewById(R.id.search_gridview_id);
        SearchHotKeyGridviewAdapter adapter = new SearchHotKeyGridviewAdapter(this,key_list);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                BaseUtil.ShowToast(SearchActivity.this, (String) parent.getAdapter().getItem(position));
            }
        });
    }

    private void initSearchView() {
        searchview = (FloatingSearchView) this.findViewById(R.id.search_floatingsearchview_id);
       final ArrayList<Suggestion> suggestion_list = new ArrayList<>();
        for (int i=0;i<20;i++ ){
            Suggestion suggestions = new Suggestion(i+"");
            suggestion_list.add(suggestions);
        }
        SuggestionDataHelper.setoldlist(suggestion_list);
        //监听输入框
        searchview.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                //get suggestions based on newQuery
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchview.clearSuggestions();
                } else {
                    searchview.showProgress();
                    //pass them on to the search view
                    SuggestionDataHelper.findSuggestions(SearchActivity.this, newQuery, 5, 250, new SuggestionDataHelper.OnFindSuggestionsListener() {
                        @Override
                        public void onResults(List<Suggestion> results) {
                            searchview.swapSuggestions(results);
                            searchview.hideProgress();
                        }
                    });

                }
            }
        });
        //监听选择了那个
        searchview.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                BaseUtil.ShowToast(getApplicationContext(),searchSuggestion.getBody()+"    onSuggestionClicked");
            }

            @Override
            public void onSearchAction(String currentQuery) {
            }
        });
        //监听焦点
        searchview.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                searchview.clearQuery();
                //show suggestions when search bar gains focus (typically history suggestions)
                searchview.swapSuggestions(SuggestionDataHelper.getHistory(SearchActivity.this,4));
            }

            @Override
            public void onFocusCleared() {
            }
        });
        //监听左边的控件
        searchview.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                searchview.setSearchFocused(true);
            }

            @Override
            public void onMenuClosed() {

            }
        });
        //美化suggestion
        searchview.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                Suggestion Suggestion = (Suggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (Suggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = Suggestion.getBody()
                        .replaceFirst(searchview.getQuery(),
                                "<font color=\"" + textLight + "\">" + searchview.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });
    }

    private void initToolbar() {
        Toolbar search_toolebar = (Toolbar) this.findViewById(R.id.back_toolbar_id);
        ViewUtil.initAfterSetContentView(this,search_toolebar);
        TextView title = (TextView) this.findViewById(R.id.toolbar_back_title_id);
        title.setText("搜索商品");
        Button back = (Button) this.findViewById(R.id.toolbar_back_btn_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
