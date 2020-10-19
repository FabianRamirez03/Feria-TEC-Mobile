package com.example.feriatec

import MyJsonArrayRequest
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pantalla_usuario.*
import org.json.JSONArray
import org.json.JSONObject

class Pedidos : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var cliente: JSONObject
    lateinit var urlRec: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Mis Pedidos"
        setContentView(R.layout.activity_pedidos)
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        cliente  = JSONObject(intent.getStringExtra("Cliente"))
        urlRec = intent.getStringExtra("url").toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Item1 -> {
                    val intent = Intent(this,Cart::class.java)
                    intent.putExtra("Cliente", cliente.toString())
                    intent.putExtra("url", urlRec)
                    finish()
                    startActivity(intent)
                }
                R.id.Item2 -> {
                    val intent = Intent(this,PantallaUsuario::class.java)
                    intent.putExtra("Cliente", cliente.toString())
                    intent.putExtra("url", urlRec)
                    finish()
                    startActivity(intent)
                }
                R.id.Item3 -> Toast.makeText(applicationContext,"Ya se encuentra en Mis Pedidos", Toast.LENGTH_SHORT).show()
                R.id.Item4 -> {
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                R.id.Item5 -> {
                    val intent = Intent(this,ModificarCuenta::class.java)
                    intent.putExtra("Cliente", cliente.toString())
                    intent.putExtra("url", urlRec)
                    startActivity(intent)
                }
            }
            true
        }

        val url = "${urlRec}server/api/Pedidos/getPedidoCliente"
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()

        val header = navView.getHeaderView(0)
        val user = header.findViewById<TextView>(R.id.NombreCliente)
        user.setText(cliente.getString("usuario"))



        jsonObject.put("cedula",cliente.get("cedula"))
        val JsonReq = MyJsonArrayRequest(
            Request.Method.POST,url,jsonObject,
            Response.Listener<JSONArray> { response->afterLoad(response) },
            Response.ErrorListener {
            Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
        })
        queue.add(JsonReq)
    }
    fun afterLoad(pedidos: JSONArray){
        val layout = findViewById<LinearLayout>(R.id.linearLay)
        var marginY = 300F
        val marginX = 50F
        for (i in 0 until pedidos.length()){
            val pedido = pedidos[i] as JSONObject
            val button = Button(this)
            val compPedido = TextView(this)
            val direccionPedido = TextView(this)
            val fechaPedido = TextView(this)
            val cuadro = TextView(this)
            //Cuadro
            cuadro.width=1080
            cuadro.height = 500
            cuadro.x = 0F
            cuadro.y = marginY - 500
            cuadro.background = ContextCompat.getDrawable(this,R.drawable.linea)

            //Localidad
            direccionPedido.setText("-Entrega en: ${pedido.getString("provincia")}, ${pedido.getString("canton")}, \n ${pedido.getString("distrito")}")
            direccionPedido.x = marginX
            direccionPedido.y= marginY - 240
            direccionPedido.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

            //Fecha
            fechaPedido.setText("-Fecha del pedido: ${pedido.getString("fecha")}")
            fechaPedido.x = marginX
            fechaPedido.y = marginY-280
            fechaPedido.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

            //Comprobante
            compPedido.setText("Pedido No. ${pedido.getString("comprobante")}")
            compPedido.x=marginX
            compPedido.y=marginY-300
            compPedido.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            compPedido.setTypeface(null, Typeface.BOLD)
            compPedido.setGravity(Gravity.LEFT)

            //Boton
            button.setOnClickListener{
                val intent = Intent(this, DetallesPedidos::class.java)
                intent.putExtra("Pedido", pedido.toString())
                intent.putExtra("Cliente", cliente.toString())
                intent.putExtra("url",urlRec)
                startActivity(intent)
            }
            button.x = marginX
            button.y = marginY + 120
            button.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            button.text = "Ver Pedido"
            button.background = ContextCompat.getDrawable(this,R.drawable.round_button)
            button.setTextColor(Color.rgb(255,255,255))

            //Agregar al layout
            marginY -= 200
            layout.addView(button)
            layout.addView(compPedido)
            layout.addView(fechaPedido)
            layout.addView(direccionPedido)
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