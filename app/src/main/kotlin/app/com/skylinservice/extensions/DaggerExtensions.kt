package app.com.skylinservice.exception

import android.content.Context
import app.com.skylinservice.AppApplication

val Context.appComponent
    get() = (applicationContext as AppApplication).appComponent