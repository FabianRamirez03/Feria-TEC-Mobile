package com.example.feriatec

import MyJsonArrayRequest
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pantalla_usuario.*
import org.json.JSONArray
import org.json.JSONObject


class PantallaUsuario : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Productores en su Zona"
        setContentView(R.layout.activity_pantalla_usuario)
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Item1 -> Toast.makeText(this,"Clicked Item1",Toast.LENGTH_SHORT).show()
                R.id.Item2 -> Toast.makeText(applicationContext,"Clicked Item2", Toast.LENGTH_SHORT).show()
                R.id.Item3 -> Toast.makeText(applicationContext,"Clicked Item3", Toast.LENGTH_SHORT).show()
            }
            true
        }


        var cliente  = JSONObject(intent.getStringExtra("Cliente"))
        val url = "http://192.168.100.79/server/api/Productores/GetRegion"
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()

        val header = navView.getHeaderView(0)
        val user = header.findViewById<TextView>(R.id.NombreCliente)
        user.setText(cliente.getString("usuario"))



        jsonObject.put("canton",cliente.get("canton"))
        val JsonReq = MyJsonArrayRequest(Request.Method.POST,url,jsonObject,Response.Listener<JSONArray> {response-> gotProducers(response) },Response.ErrorListener {
            Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
        })
        queue.add(JsonReq)
    }
    //Después de Obtener los productores
    fun gotProducers(productores:JSONArray){
        val layout = findViewById<RelativeLayout>(R.id.layouts)
        var marginY = 100F
        val marginX = 550F
        for (i in 0 until productores.length()){
            val productor = productores[i] as JSONObject
            val imagen = ImageView(this.baseContext)
            val button = Button(this)
            val nombreProd = TextView(this)
            val view = View(this)
            view.background = ContextCompat.getDrawable(this,R.drawable.linea)


            //Nombre Productor
            nombreProd.setText("${productor.getString("nombre")} ${productor.getString("apellidos")}")
            nombreProd.x=marginX-500
            nombreProd.y=marginY + 70
            nombreProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            //Boton
            button.setOnClickListener{
                textView.text = productor.getString("nombre")
            }
            button.x = marginX + 130
            button.y = marginY + 350
            button.text = "Ver Productos"
            button.background = ContextCompat.getDrawable(this,R.drawable.round_button)
            button.setTextColor(Color.rgb(255,255,255))
            //Imagen
            imagen.layoutParams = LinearLayout.LayoutParams(400,400)
            imagen.x = marginX + 50
            imagen.y = marginY
            marginY += 500
            Picasso.get().load(productor.getString("foto")).into(imagen)
            //Agregar al layout
            layout.addView(imagen)
            layout.addView(button)
            layout.addView(nombreProd)
            layout.addView(view)
            //layout.background = ContextCompat.getDrawable(this,R.drawable.linea)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}