package com.govinda.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int MAROON = Color.rgb(74, 16, 24);
    private static final int SAFFRON = Color.rgb(222, 139, 25);
    private static final int GOLD = Color.rgb(246, 202, 105);
    private EditText content;
    private ClipboardManager clipboard;
    private boolean handlingClipboard = false;
    private ClipboardManager.OnPrimaryClipChangedListener clipListener;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(MAROON);
        getWindow().setNavigationBarColor(MAROON);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        buildUi();
        clipListener = () -> {
            if (handlingClipboard || !hasWindowFocus()) return;
            runOnUiThread(() -> {
                if (!hasWindowFocus() || clipboard == null || !clipboard.hasPrimaryClip()) return;
                ClipData clip = clipboard.getPrimaryClip();
                if (clip != null && clip.getItemCount() > 0 && clip.getItemAt(0).coerceToText(this) != null) {
                    CharSequence text = clip.getItemAt(0).coerceToText(this);
                    if (text != null && text.length() > 0 && !text.toString().contentEquals(content.getText())) {
                        content.setText(text.toString());
                        content.setSelection(content.length());
                    }
                }
            });
        };
    }

    @Override protected void onResume() {
        super.onResume();
        if (clipboard != null && clipListener != null) clipboard.addPrimaryClipChangedListener(clipListener);
    }

    @Override protected void onPause() {
        if (clipboard != null && clipListener != null) clipboard.removePrimaryClipChangedListener(clipListener);
        super.onPause();
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(26), dp(22), dp(24));
        page.setBackgroundColor(Color.rgb(255, 248, 235));
        scroll.addView(page);

        TextView symbol = new TextView(this);
        symbol.setText("ॐ"); symbol.setTextColor(GOLD); symbol.setTextSize(42); symbol.setGravity(Gravity.CENTER);
        page.addView(symbol, matchWrap());
        TextView title = new TextView(this);
        title.setText("GOVINDA"); title.setTextColor(MAROON); title.setTextSize(30); title.setTypeface(Typeface.DEFAULT, Typeface.BOLD); title.setGravity(Gravity.CENTER);
        page.addView(title, matchWrap());
        TextView subtitle = new TextView(this);
        subtitle.setText("A simple space for your copied text and links"); subtitle.setTextColor(Color.rgb(112, 75, 56)); subtitle.setTextSize(14); subtitle.setGravity(Gravity.CENTER); subtitle.setPadding(0, dp(6), 0, dp(24));
        page.addView(subtitle, matchWrap());

        TextView label = new TextView(this);
        label.setText("YOUR TEXT OR LINK"); label.setTextColor(MAROON); label.setTextSize(13); label.setTypeface(null, Typeface.BOLD);
        page.addView(label, matchWrap());
        content = new EditText(this);
        content.setHint("Paste or type something here…");
        content.setGravity(Gravity.TOP | Gravity.START);
        content.setMinLines(8); content.setPadding(dp(14), dp(14), dp(14), dp(14));
        content.setTextSize(16); content.setTextColor(MAROON); content.setHintTextColor(Color.GRAY);
        content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        GradientDrawable box = new GradientDrawable(); box.setColor(Color.WHITE); box.setCornerRadius(dp(14)); box.setStroke(dp(1), Color.rgb(230, 204, 164));
        content.setBackground(box);
        LinearLayout.LayoutParams editParams = matchWrap(); editParams.topMargin = dp(8); editParams.bottomMargin = dp(16);
        page.addView(content, editParams);

        addButton(page, "PASTE FROM CLIPBOARD", SAFFRON, Color.WHITE, v -> pasteClipboard());
        addButton(page, "COPY TEXT", MAROON, Color.WHITE, v -> copyText());
        addButton(page, "SHARE TEXT / LINK", Color.rgb(255, 232, 190), MAROON, v -> shareText());

        TextView note = new TextView(this);
        note.setText("Clipboard reading works only while GOVINDA is open. Android may restrict background clipboard access.");
        note.setTextColor(Color.rgb(120, 92, 70)); note.setTextSize(12); note.setGravity(Gravity.CENTER); note.setPadding(dp(8), dp(18), dp(8), 0);
        page.addView(note, matchWrap());
        setContentView(scroll);
    }

    private void addButton(LinearLayout parent, String text, int bg, int fg, View.OnClickListener action) {
        Button button = new Button(this); button.setText(text); button.setTextColor(fg); button.setTextSize(14); button.setTypeface(null, Typeface.BOLD); button.setAllCaps(false);
        GradientDrawable shape = new GradientDrawable(); shape.setColor(bg); shape.setCornerRadius(dp(12)); button.setBackground(shape);
        button.setOnClickListener(action);
        LinearLayout.LayoutParams p = matchWrap(); p.topMargin = dp(8); p.height = dp(52);
        parent.addView(button, p);
    }

    private void pasteClipboard() {
        if (clipboard == null || !clipboard.hasPrimaryClip()) { toast("Clipboard is empty"); return; }
        ClipData clip = clipboard.getPrimaryClip();
        if (clip == null || clip.getItemCount() == 0) { toast("Clipboard is empty"); return; }
        CharSequence text = clip.getItemAt(0).coerceToText(this);
        if (text == null || text.length() == 0) { toast("No text found in clipboard"); return; }
        content.setText(text); content.setSelection(content.length()); toast("Pasted from clipboard");
    }

    private void copyText() {
        String text = content.getText().toString();
        if (text.trim().isEmpty()) { toast("Enter or paste text first"); return; }
        handlingClipboard = true;
        clipboard.setPrimaryClip(ClipData.newPlainText("GOVINDA text", text));
        handlingClipboard = false;
        toast("Copied to clipboard");
    }

    private void shareText() {
        String text = content.getText().toString();
        if (text.trim().isEmpty()) { toast("Enter or paste text first"); return; }
        Intent intent = new Intent(Intent.ACTION_SEND); intent.setType("text/plain"); intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, "Share with"));
    }

    private LinearLayout.LayoutParams matchWrap() { return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); }
    private int dp(int value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }
    private void toast(String message) { Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); }
}
