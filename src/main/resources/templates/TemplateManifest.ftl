<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
  package="${package}" android:versionCode="1" android:versionName="1.0">

  <application android:icon="@drawable/icon" android:label="@string/${appName}">
    <activity android:name=".${activityName}Activity">
      <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
    </activity>
  </application>

</manifest>

