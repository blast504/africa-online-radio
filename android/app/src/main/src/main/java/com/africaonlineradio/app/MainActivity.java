package com.africaonlineradio.app;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import com.google.common.util.concurrent.ListenableFuture;

public class MainActivity extends Activity {

    private WebView webView;
    private ListenableFuture<MediaController> mediaControllerFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient());

        SessionToken sessionToken =
                new SessionToken(
                        this,
                        new ComponentName(this, PlaybackService.class)
                );

        mediaControllerFuture =
                MediaController.Builder(this, sessionToken).buildAsync();

        webView.loadUrl(
                "https://blast504.github.io/africa-online-radio/"
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaControllerFuture != null) {
            MediaController.releaseFuture(mediaControllerFuture);
        }

        if (webView != null) {
            webView.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
