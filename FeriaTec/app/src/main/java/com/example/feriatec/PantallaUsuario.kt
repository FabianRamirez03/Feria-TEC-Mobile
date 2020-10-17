package com.example.feriatec

import MyJsonArrayRequest
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pantalla_usuario.*
import org.json.JSONArray
import org.json.JSONObject

class PantallaUsuario : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_pantalla_usuario)
        var cliente  = JSONObject(intent.getStringExtra("Cliente"))
        val url = "http://192.168.100.79/server/api/Productores/GetRegion"
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()
        jsonObject.put("canton",cliente.get("canton"))
        val JsonReq = MyJsonArrayRequest(Request.Method.POST,url,jsonObject,Response.Listener<JSONArray> {response-> gotProducers(response) },Response.ErrorListener {
            Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
        })
        queue.add(JsonReq)
    }

    fun gotProducers(productores:JSONArray){
        val layout = findViewById<RelativeLayout>(R.id.layouts)
        var marginY = 20F
        for (i in 0 until productores.length()){
            val productor = productores[i] as JSONObject
            val imagen = ImageView(this.baseContext)
            imagen.layoutParams = LinearLayout.LayoutParams(400,400)
            imagen.x = 500F
            imagen.y = marginY
            marginY += 800
            Picasso.get().load(productor.getString("foto")).into(imagen)
            layout.addView(imagen)
        }
       // textView.text= productores.toString()
        val productor:JSONObject = productores[0] as JSONObject
//        Picasso.get().load(productor.getString("foto")).into(imageView2)
    }
}