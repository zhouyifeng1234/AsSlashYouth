/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slash.youth.v2.di.components;

import android.app.Activity;

import com.google.gson.Gson;
import com.slash.youth.data.ApiClient;
import com.slash.youth.domain.executor.PostExecutionThread;
import com.slash.youth.domain.executor.ThreadExecutor;
import com.slash.youth.domain.repository.LoginRepository;
import com.slash.youth.domain.repository.MainRepository;
import com.slash.youth.v2.MainApplication;
import com.slash.youth.v2.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(Activity baseActivity);

    MainApplication context();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    ApiClient apiClient();

    MainRepository mainRepository();

    LoginRepository loginRepository();

    Gson gson();

}
