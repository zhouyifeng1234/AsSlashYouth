package com.slash.youth.data.api;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.orhanobut.logger.Logger;
import com.slash.youth.data.Global;
import com.slash.youth.data.api.cookie.ClearableCookieJar;
import com.slash.youth.data.api.cookie.PersistentCookieJar;
import com.slash.youth.data.api.cookie.cache.SetCookieCache;
import com.slash.youth.data.api.cookie.persistence.SharedPrefsCookiePersistor;
import com.slash.youth.data.util.AuthHeaderUtil;
import com.slash.youth.data.util.NetUtil;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;

/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/3/4
 */
public class ApiOption {

    private Map<Class, Object> apis;

    protected Retrofit retrofit;

    public ApiOption(Retrofit retrofit) {
        this.retrofit = retrofit;
        apis = new HashMap<>();
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * ApiOption 构建器，主要是构建Retrofit对象出来
     */
    public static final class Builder {

        protected Retrofit retrofit;

        protected OkHttpClient client;

        protected String url;

        // 将自身的实例对象设置为一个属性,并加上Static和final修饰符
        private static Builder instance;

        // 静态方法返回该类的实例
        public synchronized static Builder instance(Application application) {
            if (instance == null)
                instance = new Builder(application);
            return instance;
        }

        public Builder(Application application) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(application));
            client = new OkHttpClient()
                    .newBuilder()
                    .cookieJar(cookieJar)
                    .sslSocketFactory(getSSLContext(application).getSocketFactory())
                    .addInterceptor(new HeadInterceptor(application))
                    .addInterceptor(new CacheInterceptor(application))
                    .addInterceptor(logging)
//                    .addInterceptor(new CookieInterceptor(application))
                    .addNetworkInterceptor(new StethoInterceptor())
                    .cache(provideCache(application))
                    .build();
        }

        public Builder client(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public ApiOption build() {
            if (url == null || url.length() == 0) {
                throw new RuntimeException("url is null!");
            }
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            return new ApiOption(retrofit);
        }

        public ApiOption buildForXml() {
            if (url == null || url.length() == 0) {
                throw new RuntimeException("url is null!");
            }
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            return new ApiOption(retrofit);
        }


        public Cache provideCache(Application application) {
            return new Cache(application.getCacheDir(), 10240 * 1024);
        }

        /**
         * 获取Https的证书,zss
         *
         * @param context 上下文
         * @return SSL的上下文对象
         */
        public static SSLContext getSSLContext(Context context) {
            CertificateFactory certificateFactory = null;
            InputStream inputStream = null;
            Certificate cer = null;
            KeyStore keystore = null;
            TrustManagerFactory trustManagerFactory = null;
            try {
                certificateFactory = CertificateFactory.getInstance("X.509");
                inputStream = context.getAssets().open("213980825410312.pem");//这里导入SSL证书文件

                cer = certificateFactory.generateCertificate(inputStream);
                // LogManager.i(TAG, cer.getPublicKey().toString());

                //创建一个证书库，并将证书导入证书库
                keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(null, null); //双向验证时使用
                keystore.setCertificateEntry("trust", cer);

                // 实例化信任库
                trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keystore);

                SSLContext mSSLContext = SSLContext.getInstance("TLS");
                mSSLContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

                return mSSLContext;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Logger.e(e.getMessage());
                    }
                    inputStream = null;
                }
            }
            return null;
        }
    }

    /**
     * 添加请求头信息
     */
    static class HeadInterceptor implements Interceptor {

        Application application;

        public HeadInterceptor(Application application) {
            this.application = application;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request.Builder builder = chain.request().newBuilder();
            long timeMillis = System.currentTimeMillis();
            SharedPreferences preferences = application.getSharedPreferences("APP_PREFERENCE", Context.MODE_PRIVATE);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            String date = sdf.format(new Date(timeMillis));
            builder.addHeader("Date", date);
            builder.addHeader("Authorization", AuthHeaderUtil.getBasicAuthHeader(chain.request().method(), chain.request().url().url().toString(), date).trim());
            builder.addHeader("uid", preferences.getLong(Global.SERVER_TOKEN, 0l) + "");
            builder.addHeader("pass", "1");
            builder.addHeader("token", preferences.getLong(Global.USER_ID, 0l) + "");
            builder.addHeader("Content-Type", "application/json");
            Request request = builder.build();
            return chain.proceed(request);
        }
    }

    /**
     * 有网请求，没网不读缓存
     */
    static class CacheInterceptor implements Interceptor {
        Application application;

        public CacheInterceptor(Application application) {
            this.application = application;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable(application)) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response response = chain.proceed(request);
            if (NetUtil.isNetworkAvailable(application)) {
                int maxAge = 0;
                response.newBuilder().header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 7;
                response.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        }
    }

    /**
     * 缓存cookie
     */
    static class CookieInterceptor implements Interceptor {
        Application application;

        public CookieInterceptor(Application application) {
            this.application = application;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            //这里获取请求返回的cookie
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                final StringBuffer cookieBuffer = new StringBuffer();
                Observable.from(originalResponse.headers("Set-Cookie"))
                        .map(s -> {
                            String[] cookieArray = s.split(";");
                            return cookieArray[0];
                        })
                        .subscribe(cookie -> {
                            cookieBuffer.append(cookie).append(";");
                        });
                SharedPreferences sharedPreferences = application.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cookie", cookieBuffer.toString());
                editor.commit();
            }
            return originalResponse;
        }
    }

}
