package com.example.feriatec

import MyJsonArrayRequest
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalles_pedidos.*
import kotlinx.android.synthetic.main.activity_pantalla_usuario.drawer_layout
import kotlinx.android.synthetic.main.activity_pantalla_usuario.navView
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

class DetallesPedidos : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var cliente: JSONObject
    lateinit var pedido: JSONObject
    lateinit var productor: JSONObject
    lateinit var urlRec: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_pedidos)
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        cliente  = JSONObject(intent.getStringExtra("Cliente"))
        pedido = JSONObject(intent.getStringExtra("Pedido"))
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

        val url = "${urlRec}server/api/Productores/getProductId"
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()

        val header = navView.getHeaderView(0)
        val user = header.findViewById<TextView>(R.id.NombreCliente)
        user.setText(cliente.getString("usuario"))

        jsonObject.put("cedula",pedido.getString("productor"))
        val JsonReq = JsonObjectRequest(
            Request.Method.POST,url,jsonObject,
            Response.Listener{ response-> afterLoad(response) },
            Response.ErrorListener {
                Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
            })
        queue.add(JsonReq)



    }
    fun afterLoad(productorEntrada:JSONObject){
        productor = productorEntrada
        comprobanteView.setText("Comprobante No. ${pedido.getString("comprobante")}")
        productorView.setText("Productor: ${productor.getString("nombre")} ${productor.getString("apellidos")}")
        telefonoView.setText("Teléfono: ${productor.getString("telefono")}")
        fechaView.setText("Fecha de Compra: ${pedido.getString("fecha")}")
        estadoView.setText("Estado del Pedido: ${pedido.getString("estado")}")
        direccionView.setText("Dirección de entrega: \n ${pedido.getString("direccion")}")
        instruccionesView.setText("Instrucciones Adicionales: ${pedido.getString("comentarios")}")
        feedback.visibility = View.INVISIBLE
        enviarFeedback.visibility = View.INVISIBLE
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.setMaximumFractionDigits(0)
        format.setCurrency(Currency.getInstance("CRC"))
        val productos = pedido.getJSONArray("listado")
        val layout = findViewById<LinearLayout>(R.id.linearLay)
        var marginY = 100F
        val marginX = 50F
        var total = 0
        for (i in 0 until productos.length()){
            val producto = productos[i] as JSONObject
            val imagen = ImageView(this.baseContext)
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
            precioProd.setText("₡ ${producto.getString("precio")}")
            precioProd.x = marginX
            precioProd.y= marginY - 230
            precioProd.setTypeface(null, Typeface.BOLD_ITALIC)
            precioProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)

            //Cantidad
            dispProd.setText("-Cantidad: ${producto.getString("cantidad")}")
            dispProd.x = marginX
            dispProd.y= marginY - 270
            dispProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
            total += producto.getString("cantidad").toInt() * producto.getString("precio").toInt()

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

            //Imagen
            imagen.layoutParams = LinearLayout.LayoutParams(400,325)
            imagen.x = marginX + 550
            imagen.y = marginY + 200
            Picasso.get().load(producto.getString("foto")).into(imagen)

            //Agregar al layout
            marginY -= 550
            layout.addView(imagen)
            layout.addView(nombreProd)
            layout.addView(catProd)
            layout.addView(dispProd)
            layout.addView(modoProd)
            layout.addView(precioProd)
            layout.addView(cuadro)
        }
        totalView.setText(format.format(total))
        if(pedido.getString("estado") == "Entregado"){
            feedback.visibility = View.VISIBLE
            enviarFeedback.visibility = View.VISIBLE
            feedback.hint = "Feedback"
            enviarFeedback.setOnClickListener{
                val feed = feedback.text.toString()
                val url = "${urlRec}server/api/api/Pedidos/modify"
                val queue = Volley.newRequestQueue(this)
                pedido.put("feedback",feed)

                val JsonReq = MyJsonArrayRequest(
                    Request.Method.POST,url,pedido,
                    Response.Listener{ response-> },
                    Response.ErrorListener {
                        Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
                    })
                queue.add(JsonReq)

            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}