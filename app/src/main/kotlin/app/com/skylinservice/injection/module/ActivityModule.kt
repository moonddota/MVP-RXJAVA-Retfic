package app.com.skylinservice.injection.module

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import dagger.Module

@Module
class ActivityModule(private val activity: Activity)