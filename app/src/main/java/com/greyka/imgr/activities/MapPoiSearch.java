package com.greyka.imgr.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.greyka.imgr.R;

import java.util.ArrayList;
import java.util.List;

import overlay.PoiOverlay;

public class MapPoiSearch extends AppCompatActivity
        implements View.OnClickListener, Inputtips.InputtipsListener, PoiSearch.OnPoiSearchListener,
        TextWatcher, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter {

    private String keyWord = "";
    private MapView mapView;
    private AMap aMap;
    private AutoCompleteTextView searchText;
    private Button searchButton;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private PoiResult poiResult;
    private ProgressDialog progressDialog = null;
    private LatLng latLng;
    private String poiName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_poi_search);
        mapView = (MapView) findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
        searchText.addTextChangedListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<String> listString = new ArrayList<>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            throw new IllegalStateException("Error!");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchButton:
                searchButton();
                break;
            case R.id.set_poi_word:
                setPoiWordButton();
                break;
            default:
                throw new IllegalStateException("Error!");
        }
    }

    private void setPoiWordButton() {
        Intent intent = new Intent();
        intent.putExtra("latitude", latLng.latitude);
        intent.putExtra("longitude", latLng.longitude);
        intent.putExtra("name", poiName);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private String checkEditText(AutoCompleteTextView searchText) {
        if (searchText != null && searchText.getText() != null
                && !(searchText.getText().toString().trim().equals(""))) {
            return searchText.getText().toString().trim();
        } else {
            return "";
        }
    }

    private void searchButton() {
        keyWord = checkEditText(searchText);
        if (!"".equals(keyWord)) {
            doSearchQuery();
        }
    }

    private void doSearchQuery() {
        showProgressDialog();
        query = new PoiSearch.Query(keyWord, "", null);
        query.setPageSize(10);
        query.setPageNum(0);

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dismissProgressDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(query)) {
                    poiResult = result;
                    List<PoiItem> poiItems = poiResult.getPois();
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else {
                        throw new IllegalStateException("Error! No result.");
                    }
                }
            }
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在搜索:\n" + keyWord);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int rCode) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!IsEmptyOrNullString(newText)) {
            InputtipsQuery inputQuery = new InputtipsQuery(newText, null);
            inputQuery.setCityLimit(false);
            Inputtips inputTips = new Inputtips(MapPoiSearch.this, inputQuery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        latLng = marker.getPosition();
        poiName = marker.getTitle();
        return false;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());
        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        Button button = (Button) view.findViewById(R.id.set_poi_word);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}