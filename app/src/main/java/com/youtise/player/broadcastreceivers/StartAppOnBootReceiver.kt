package com.youtise.player.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.youtise.player.ui.LeanbackActivity
import com.youtise.player.ui.MainActivity

class StartAppOnBootReceiver: BroadcastReceiver() {
   private val typeTelevision = "TELEVISION"
   private val typePhoneOrTablet = "PHONE"

   override fun onReceive(context: Context, intent: Intent) {
      if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
         val i: Intent
         when(checkDeviceType(context)) {
            typeTelevision -> {
               i = Intent(context, LeanbackActivity::class.java)
               i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               context.startActivity(i)
            }
            typePhoneOrTablet -> {
               i = Intent(context, MainActivity::class.java)
               i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               context.startActivity(i)
            }
         }
      }
   }

   private fun checkDeviceType(context: Context): String {
      return if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK))
         typeTelevision
      else
         typePhoneOrTablet
   }
}