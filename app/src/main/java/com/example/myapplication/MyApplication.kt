package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp         //  ←  genera el SingletonComponent de Hilt
class MyApplication : Application()
