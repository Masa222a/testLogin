package com.hannibal.testlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var fbImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button_logout)

        btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //fb logout
            LoginManager.getInstance().logOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //fb login
        fbImage = findViewById(R.id.fb_image)

        val accessToken = AccessToken.getCurrentAccessToken()

        val request = GraphRequest.newMeRequest(
            accessToken,
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {
                    Log.d("fb_response", "$obj")
                    val img = obj?.getJSONObject("picture")
                        ?.getJSONObject("data")
                        ?.getString("url")

                    Glide.with(applicationContext).load(img).into(fbImage)
                }

            }
            )

        val parameters = Bundle()
        parameters.putString("field", "pictire")

        request.executeAsync()
    }
}