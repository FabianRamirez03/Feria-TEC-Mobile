package com.example.feriatec
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val url = "http://192.168.100.79/server/api/Clientes/getlogin"
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()
        acceder.setOnClickListener{
            var usuario = usuario.text
            var password = password.text
            jsonObject.put("usuario",usuario)
            jsonObject.put("password", password)
            val stringRequest = JsonObjectRequest(Request.Method.POST,url, jsonObject,Response.Listener { response->
                if(response != null){
                    val intent = Intent(this, PantallaUsuario::class.java)
                    intent.putExtra("Cliente", response.toString())
                    startActivity(intent)
                }
            },Response.ErrorListener {Toast.makeText(this,"Usuario o contraseña incorrecta", Toast.LENGTH_LONG).show() })
            queue.add(stringRequest)

        }
    }



}