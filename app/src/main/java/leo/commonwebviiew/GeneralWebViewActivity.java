package leo.commonwebviiew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * 公用的WebView页面
 */
public class GeneralWebViewActivity extends Activity {
    WebView webView;



    //算法服务器地址
    private final static String Url = "http://211.99.98.169:20001/";

    /**
     * 视频全屏参数
     */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.wv);

        initWebView();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        webView.reload();
    }



    /**
     * 展示网页界面
     **/
    public void initWebView() {

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 关键点
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        webView.addJavascriptInterface(new Interface(), "JSInterface");
//        webView.setWebChromeClient(wvcc);
        //不打开内置浏览器
        webView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        CookieManager cookieManager = CookieManager.getInstance();
                        String CookieStr = cookieManager.getCookie(url);
                        super.onPageFinished(view, url);
                    }

                    @Nullable
                    @Override
                    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                        Log.d("console", "拦截" + request.getUrl());
                        return super.shouldInterceptRequest(view, request);
                    }

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        Log.d("加载失败", request.getUrl() + "==");
//                         if ((request.getUrl()+"").contains(".flv")||(request.getUrl()+"").contains(".mp4")){
//                             relNowork.setVisibility(View.GONE);
//                         }
//                         else{
//                             relNowork.setVisibility(View.VISIBLE);
//                         }
//                        ToastUtil.notic(GeneralWebViewActivity.this,"加载网络失败");
//
                    }
                });
        webView.setWebChromeClient(new WebChromeClient() {
            /*** 视频播放相关的方法 **/
            @Override
            public View getVideoLoadingProgressView() {
                FrameLayout frameLayout = new FrameLayout(GeneralWebViewActivity.this);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return frameLayout;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                Log.d("隐藏全屏", "全屏");
                showCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                Log.d("隐藏全屏", "隐藏");
                hideCustomView();
            }

            @Override
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Log.d("console", message + "(" + sourceID + ":" + lineNumber + ")");
                super.onConsoleMessage(message, lineNumber, sourceID);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("console", "[" + consoleMessage.messageLevel() + "] " + consoleMessage.message() + "(" + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber() + ")");
                return super.onConsoleMessage(consoleMessage);
            }
        });

        webView.loadUrl(Url);
        Log.d("URL地址", Url + "");
    }


    /**
     * js调用安卓
     */
    class Interface {
        @JavascriptInterface
        public void back(String json) {
            Log.d("收到消息", "22收到消息" + json);
            if ("true".equals(json)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        imgBaseBack.setEnabled(true);
//                                ToastUtil.notic(GeneralWebViewActivity.this,"true");
                    }
                });

            } else if ("false".equals(json)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        imgBaseBack.setEnabled(false);
//                                ToastUtil.notic(GeneralWebViewActivity.this,"false");
                    }
                });
//                        ToastUtil.notic(GeneralWebViewActivity.this,"false");
            } else if ("back".equals(json)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        finish();
//                                ToastUtil.notic(GeneralWebViewActivity.this,"false");
                    }
                });
//
            }
        }

        @JavascriptInterface
        public void tologin(String json) {
            Log.d("收到消息", "收到消息" + json);
            if ("true".equals(json)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finishAffinity();
//                        ComUtils.goAct(GeneralWebViewActivity.this, StartActivity.class, true, null);
                    }
                });
            }
        }

        @JavascriptInterface
        public void back_finish() {
            finish();
        }

        @JavascriptInterface
        public void webviewBack(){
            finish();
        }
    }

    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        GeneralWebViewActivity.this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(GeneralWebViewActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
    }


    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
                if (customView != null) {
                    hideCustomView();
                } else if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }
}
