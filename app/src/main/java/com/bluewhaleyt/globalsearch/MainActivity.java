package com.bluewhaleyt.globalsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.bluewhaleyt.common.DynamicColorsUtil;
import com.bluewhaleyt.common.PermissionUtil;
import com.bluewhaleyt.crashdebugger.CrashDebugger;
import com.bluewhaleyt.filemanagement.FileUtil;
import com.bluewhaleyt.globalsearch.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private SearchResultAdapter adapter;
    private List<SearchResult> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashDebugger.init(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvMadeBy.setText(getString(R.string.made_by, "❤️", "BlueWhaleYT"));

        if (isPermissionGranted()) setupSearchResultList();
        else PermissionUtil.requestAllFileAccess(this);

        binding.btnSearch.setOnClickListener(v -> performSearch(binding.etSearch.getText().toString()));

        binding.etFilePath.setText(FileUtil.getExternalStoragePath() + "/WhaleUtils/");

    }

    private boolean isPermissionGranted() {
        return PermissionUtil.isAlreadyGrantedExternalStorageAccess();
    }

    private void performSearch(String query) {
        if (!query.equals("")) {
            binding.progressBar.setVisibility(View.VISIBLE);
            results = new ArrayList<>();
            Map<String, Integer> fileCounts = new HashMap<>();
            runOnUiThread(() -> {
                adapter.setSearchResults(results);
                adapter.notifyDataSetChanged();
            });
            AsyncTask.execute(() -> {
                File dir = new File(binding.etFilePath.getText().toString());
                searchFiles(dir, query, results, fileCounts);
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    updateResultText(fileCounts.size(), results.size());
                    binding.progressBar.setVisibility(View.GONE);
                });
            });
        }
    }

    private void searchFiles(File dir, String query, List<SearchResult> results, Map<String, Integer> fileCounts) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line;
                        String content = "";
                        int lineNumber = 1;
                        while ((line = reader.readLine()) != null) {
                            content += line + "\n";
                            if (line.toLowerCase().contains(query.toLowerCase())) {
                                int startIndex = line.trim().toLowerCase().indexOf(query.toLowerCase());
                                int endIndex = startIndex + query.length();
                                SpannableString highlightedLine = new SpannableString(line.trim());

                                var color = new DynamicColorsUtil(this).getColorPrimary();
                                highlightedLine.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                highlightedLine.setSpan(new BackgroundColorSpan(ColorUtils.setAlphaComponent(color, 50)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                SearchResult searchResult = new SearchResult(
                                        file.getAbsolutePath(),
                                        file.getName(),
                                        content,
                                        highlightedLine,
                                        lineNumber
                                );
                                int count = 0;
                                if (fileCounts.containsKey(file.getAbsolutePath())) {
                                    count = fileCounts.get(file.getAbsolutePath());
                                }
                                fileCounts.put(file.getAbsolutePath(), count + 1);
                                runOnUiThread(() -> {
                                    results.add(searchResult);
                                    adapter.notifyItemInserted(results.indexOf(searchResult));
                                    updateResultText(fileCounts.size(), results.size());
                                });
                            }
                            lineNumber++;
                        }
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (file.isDirectory()) {
                    searchFiles(file, query, results, fileCounts);
                }
            }
        }
    }

    private void updateResultText(int numFiles, int numResults) {
        binding.tvResultCount.setText(
                getString(R.string.result, numResults) +
                " (" + getString(R.string.files, numFiles) + ")"
        );
    }

    private void setupSearchResultList() {
        adapter = new SearchResultAdapter();
        var rvList = binding.rvResult;
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
    }
}