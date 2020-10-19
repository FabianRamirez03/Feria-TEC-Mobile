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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pantalla_usuario.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

class Productos : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var cliente: JSONObject
    lateinit var productor: JSONObject
    lateinit var urlRec: String

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = "Productos Disponibles"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        cliente  = JSONObject(intent.getStringExtra("Cliente"))
        productor  = JSONObject(intent.getStringExtra("Productor"))
        urlRec = intent.getStringExtra("url").toString()
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
                R.id.Item3 -> {
                    val intent = Intent(this,Pedidos::class.java)
                    intent.putExtra("Cliente", cliente.toString())
                    intent.putExtra("url", urlRec)
                    finish()
                    startActivity(intent)
                }
                R.id.Item4 -> {
                    val intent = Intent(this,MainActivity::class.java)
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
        val url = "${urlRec}server/api/Productos/GetPorProductor"
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()

        val header = navView.getHeaderView(0)
        val user = header.findViewById<TextView>(R.id.NombreCliente)
        user.setText(cliente.getString("usuario"))

        jsonObject.put("productor",productor.getString("cedula"))
        val JsonReq = MyJsonArrayRequest(
            Request.Method.POST,url,jsonObject,
            Response.Listener<JSONArray> { response-> afterLoad(response) },
            Response.ErrorListener {
            Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
        })
        queue.add(JsonReq)

    }
    fun afterLoad(productos: JSONArray){
        val layout = findViewById<LinearLayout>(R.id.linearLay)
        var marginY = 0F
        val marginX = 50F
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.setMaximumFractionDigits(0)
        format.setCurrency(Currency.getInstance("CRC"))
        var carrito = cliente.getJSONArray("carrito")
        for (i in 0 until productos.length()){
            val producto = productos[i] as JSONObject
            val imagen = ImageView(this.baseContext)
            val button = Button(this)
            val nombreProd = TextView(this)
            val modoProd = TextView(this)
            val dispProd = TextView(this)
            val catProd = TextView(this)
            val precioProd = TextView(this)
            val cuadro = TextView(this)
            //Cuadro
            cuadro.width=1080
            cuadro.height = 500
            cuadro.x = 5F
            cuadro.y = marginY - 550
            cuadro.background = ContextCompat.getDrawable(this,R.drawable.linea)

            //Precio
            precioProd.setText(format.format(producto.getString("precio").toInt()))
            precioProd.x = marginX
            precioProd.y= marginY - 230
            precioProd.setTypeface(null, Typeface.BOLD_ITALIC)
            precioProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)

            //Disponibilidad
            dispProd.setText("-Disponibilidad: ${producto.getString("disponibilidad")}")
            dispProd.x = marginX
            dispProd.y= marginY - 270
            dispProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

            //Modo
            modoProd.setText("-Modo de venta: ${producto.getString("modo")}")
            modoProd.x = marginX
            modoProd.y= marginY - 250
            modoProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

            //Categoria
            catProd.setText("-Categoría: ${producto.getString("categoria")}")
            catProd.x = marginX
            catProd.y = marginY-290
            catProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

            //Nombre
            nombreProd.setText("${producto.getString("nombre")}")
            nombreProd.x=marginX
            nombreProd.y=marginY-290
            nombreProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            nombreProd.setTypeface(null, Typeface.BOLD)
            nombreProd.setGravity(Gravity.LEFT)

            //Boton
            button.setOnClickListener {
                var found = false
                var cont = 0
                for (i in 0 until carrito.length()){
                    val prod = carrito[i] as JSONObject
                    if(prod.getString("nombre") == producto.getString("nombre")){
                        found = true
                        break
                    }
                    cont++
                }
                if(found){
                    producto.put("cantidad",(producto.getString("cantidad").toInt() +1).toString())
                    carrito.remove(cont)
                }
                else{
                    producto.put("cantidad",(1).toString())
                }
                carrito.put(producto)
                cliente.put("carrito", carrito)
                val queue = Volley.newRequestQueue(this)
                val JsonReq = MyJsonArrayRequest(
                    Request.Method.POST,"${urlRec}server/api/Clientes/modify",cliente,
                    Response.Listener<JSONArray> { response-> },
                    Response.ErrorListener {
                        Toast.makeText(this,"Producto agregado al carrito exitosamente", Toast.LENGTH_LONG).show()
                    })
                queue.add(JsonReq)
            }
            button.x = marginX
            button.y = marginY + 180
            button.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            button.text = "Agregar al Carrito"
            button.background = ContextCompat.getDrawable(this,R.drawable.round_button_orange)
            button.setTextColor(Color.rgb(255,255,255))

            //Imagen
            imagen.layoutParams = LinearLayout.LayoutParams(400,325)
            imagen.x = marginX + 550
            imagen.y = marginY + 200
            Picasso.get().load(producto.getString("foto")).into(imagen)

            //Agregar al layout
            marginY -= 550
            layout.addView(imagen)
            layout.addView(button)
            layout.addView(nombreProd)
            layout.addView(catProd)
            layout.addView(dispProd)
            layout.addView(modoProd)
            layout.addView(precioProd)
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