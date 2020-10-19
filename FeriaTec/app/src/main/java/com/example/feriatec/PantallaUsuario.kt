package com.example.feriatec

import MyJsonArrayRequest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.solver.widgets.Rectangle
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
    lateinit var cliente: JSONObject
    lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Productores en su Zona"
        setContentView(R.layout.activity_pantalla_usuario)
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        cliente  = JSONObject(intent.getStringExtra("Cliente"))
        url = intent.getStringExtra("url").toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Item1 -> {
                    val intent = Intent(this,Cart::class.java)
                    intent.putExtra("Cliente", cliente.toString())
                    intent.putExtra("url", url)
                    finish()
                    startActivity(intent)
                }
                R.id.Item2 -> Toast.makeText(applicationContext,"Ya se encuentra en productores", Toast.LENGTH_SHORT).show()
                R.id.Item3 -> {
                    val intent = Intent(this,Pedidos::class.java)
                    intent.putExtra("Cliente", cliente.toString())
                    intent.putExtra("url", url)
                    finish()
                    startActivity(intent)
                }
                R.id.Item4 -> {
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                R.id.Item5 -> {
                    val intent = Intent(this,ModificarCuenta::class.java)
                    intent.putExtra("Cliente", cliente.toString())
                    intent.putExtra("url", url)
                    startActivity(intent)
                }
            }
            true
        }
        val sendUrl = "${url}server/api/Productores/GetRegion"
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()

        val header = navView.getHeaderView(0)
        val user = header.findViewById<TextView>(R.id.NombreCliente)
        user.setText(cliente.getString("usuario"))



        jsonObject.put("canton",cliente.get("canton"))
        val JsonReq = MyJsonArrayRequest(Request.Method.POST,sendUrl,jsonObject,Response.Listener<JSONArray> {response-> gotProducers(response) },Response.ErrorListener {
            Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
        })
        queue.add(JsonReq)
    }
    //Después de Obtener los productores
    @SuppressLint("SetTextI18n")
    fun gotProducers(productores:JSONArray){
        val layout = findViewById<LinearLayout>(R.id.linearLay)
        var marginY = 0F
        val marginX = 50F
        for (i in 0 until productores.length()){
            val productor = productores[i] as JSONObject
            val imagen = ImageView(this.baseContext)
            val button = Button(this)
            val nombreProd = TextView(this)
            val localProd = TextView(this)
            val telProd = TextView(this)
            val cuadro = TextView(this)
            //Cuadro
            cuadro.width=1080
            cuadro.height = 500
            cuadro.x = 5F
            cuadro.y = marginY - 500
            cuadro.background = ContextCompat.getDrawable(this,R.drawable.linea)

            //Localidad
            localProd.setText("-Ubicación: ${productor.getString("provincia")}, ${productor.getString("canton")}, \n ${productor.getString("distrito")}")
            localProd.x = marginX
            localProd.y= marginY - 240
            localProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

            //Telefono Productor
            telProd.setText("-Teléfono: ${productor.getString("telefono")}")
            telProd.x = marginX
            telProd.y = marginY-280
            telProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

            //Nombre Productor
            nombreProd.setText("${productor.getString("nombre")} ${productor.getString("apellidos")}")
            nombreProd.x=marginX
            nombreProd.y=marginY-300
            nombreProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            nombreProd.setTypeface(null,Typeface.BOLD)
            nombreProd.setGravity(Gravity.LEFT)

            //Boton
            button.setOnClickListener{
                val intent = Intent(this, Productos::class.java)
                intent.putExtra("Productor", productor.toString())
                intent.putExtra("Cliente", cliente.toString())
                intent.putExtra("url",url)
                startActivity(intent)
            }
            button.x = marginX
            button.y = marginY + 120
            button.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            button.text = "Ver Productos"
            button.background = ContextCompat.getDrawable(this,R.drawable.round_button)
            button.setTextColor(Color.rgb(255,255,255))
            //Imagen
            imagen.layoutParams = LinearLayout.LayoutParams(400,325)
            imagen.x = marginX + 550
            imagen.y = marginY + 200
            Picasso.get().load(productor.getString("foto")).into(imagen)

            //Agregar al layout
            marginY -= 500
            layout.addView(imagen)
            layout.addView(button)
            layout.addView(nombreProd)
            layout.addView(telProd)
            layout.addView(localProd)
            layout.addView(cuadro)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}