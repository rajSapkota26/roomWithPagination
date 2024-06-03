package com.bandhu.myapplication.utill

import android.R.attr.name
import android.content.Context
import android.content.SharedPreferences


class SharedPref {

    companion object{
         fun saveData(context: Context, data: Boolean) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstTime", data)
            editor.apply()
        }

         fun loadData(context: Context): Boolean {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("isFirstTime", false)
        }
    }

}