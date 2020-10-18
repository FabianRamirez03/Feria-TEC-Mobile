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
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_pantalla_usuario.*
import kotlinx.android.synthetic.main.activity_pantalla_usuario.drawer_layout
import kotlinx.android.synthetic.main.activity_pantalla_usuario.navView
import org.json.JSONArray
import org.json.JSONObject

class Cart : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var cliente: JSONObject
    lateinit var productos: JSONArray
    var total: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Item1 -> Toast.makeText(this,"Ya se encuentra en el carrito", Toast.LENGTH_SHORT).show()
                R.id.Item2 -> {
                    val intent = Intent(this,PantallaUsuario::class.java)
                    intent.putExtra("Cliente", cliente.toString())
                    finish()
                    startActivity(intent)
                }
                R.id.Item3 -> Toast.makeText(applicationContext,"Próximamente", Toast.LENGTH_SHORT).show()
            }
            true
        }
        cliente  = JSONObject(intent.getStringExtra("Cliente"))
        val header = navView.getHeaderView(0)
        val user = header.findViewById<TextView>(R.id.NombreCliente)
        user.setText(cliente.getString("usuario"))
        productos = cliente.getJSONArray("carrito")

        val layout = findViewById<LinearLayout>(R.id.linearLay)
        var marginY = -150F
        val marginX = 50F

        for (i in 0 until productos.length()){
            val producto = productos[i] as JSONObject
            val imagen = ImageView(this.baseContext)
            val buttonElim = FloatingActionButton(this)
            val buttonAdd = FloatingActionButton(this)
            val buttonRest = FloatingActionButton(this)
            val nombreProd = TextView(this)
            val modoProd = TextView(this)
            val dispProd = TextView(this)
            val cantProd = TextView(this)
            val precioProd = TextView(this)
            val cuadro = TextView(this)
            total += producto.getString("precio").toInt() * producto.getString("cantidad").toInt()

            //Cuadro
            cuadro.width=1080
            cuadro.height = 550
            cuadro.x = -5f
            cuadro.y = marginY - 750
            cuadro.background = ContextCompat.getDrawable(this,R.drawable.linea)

            //Imagen
            imagen.layoutParams = LinearLayout.LayoutParams(400,325)
            imagen.x = marginX + 550
            imagen.y = marginY + 300
            Picasso.get().load(producto.getString("foto")).into(imagen)

            //Nombre
            nombreProd.setText("${producto.getString("nombre")}")
            nombreProd.x=marginX
            nombreProd.y=marginY-450
            nombreProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            nombreProd.setTypeface(null, Typeface.BOLD)
            nombreProd.setGravity(Gravity.LEFT)

            //Modo
            modoProd.setText("-Modo de venta: ${producto.getString("modo")}")
            modoProd.x = marginX
            modoProd.y= marginY - 450
            modoProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

            //Precio
            precioProd.setText("₡ ${producto.getString("precio")}")
            precioProd.x = marginX
            precioProd.y= marginY - 430
            precioProd.setTypeface(null, Typeface.BOLD_ITALIC)
            precioProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)

            //Disponibilidad
            dispProd.setText("-Disponibilidad: ${producto.getString("disponibilidad")}")
            dispProd.x = marginX
            dispProd.y= marginY - 470
            dispProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

            //Cantidad
            cantProd.setText(producto.getString("cantidad"))
            cantProd.x = marginX + 170
            cantProd.y= marginY - 150
            cantProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            precioProd.setTypeface(null, Typeface.BOLD)

            //Boton Eliminar
            buttonElim.x = marginX + 900
            buttonElim.y = marginY - 300
            buttonElim.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            buttonElim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_cancel_24))
            buttonElim.size = FloatingActionButton.SIZE_MINI
            buttonElim.setOnClickListener{
                eliminar(producto)
            }

            //Boton Agregar
            buttonAdd.x = marginX + 250
            buttonAdd.y = marginY + 255
            buttonAdd.size = FloatingActionButton.SIZE_MINI
            buttonAdd.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            buttonAdd.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_add_circle_outline_24))
            buttonAdd.setOnClickListener{sumar(producto)}
            //Boton restar
            buttonRest.x = marginX + 10
            buttonRest.y = marginY + 150
            buttonRest.size = FloatingActionButton.SIZE_MINI
            buttonRest.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            buttonRest.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_remove_circle_outline_24))
            buttonRest.setOnClickListener{
                restar(producto)
            }

            //Agregar al layout
            marginY -= 800
            layout.addView(imagen)
            layout.addView(buttonAdd)
            layout.addView(buttonRest)
            layout.addView(buttonElim)
            layout.addView(nombreProd)
            layout.addView(dispProd)
            layout.addView(cantProd)
            layout.addView(precioProd)
            layout.addView(modoProd)
            layout.addView(cuadro)

        }
        totalText.setText(" \n Total: ₡ $total")
    }
    fun eliminar(producto: JSONObject){
        val carrito = cliente.getJSONArray("carrito")
        var cont = 0
        for (i in 0 until carrito.length()){
            val prod = carrito[i] as JSONObject
            if(prod.getString("nombre") == producto.getString("nombre")){
                break
            }
            cont++
        }
        carrito.remove(cont)
        cliente.put("carrito",carrito)
        modify()
    }
    fun restar(producto:JSONObject){
        val carrito = cliente.getJSONArray("carrito")
        var cont = 0
        for (i in 0 until carrito.length()){
            val prod = carrito[i] as JSONObject
            if(prod.getString("nombre") == producto.getString("nombre")){
                break
            }
            cont++
        }
        if(producto.getString("cantidad").toInt() > 1){
            producto.put("cantidad",(producto.getString("cantidad").toInt() -1).toString())
            carrito.remove(cont)
            carrito.put(producto)
            cliente.put("carrito",carrito)
            modify()
        }
        else{
            eliminar(producto)
        }
    }
    fun sumar(producto:JSONObject){
        val carrito = cliente.getJSONArray("carrito")
        var cont = 0
        for (i in 0 until carrito.length()){
            val prod = carrito[i] as JSONObject
            if(prod.getString("nombre") == producto.getString("nombre")){
                break
            }
            cont++
        }
        producto.put("cantidad",(producto.getString("cantidad").toInt() +1).toString())
        carrito.remove(cont)
        carrito.put(producto)
        cliente.put("carrito",carrito)
        modify()
    }
    fun modify(){
        val queue = Volley.newRequestQueue(this)
        val JsonReq = MyJsonArrayRequest(
            Request.Method.POST,"http://192.168.100.79/server/api/Clientes/modify",cliente,
            Response.Listener<JSONArray> { response-> },
            Response.ErrorListener {
                Toast.makeText(this,"Carrito actualizado exitosamente", Toast.LENGTH_LONG).show()
                intent.putExtra("Cliente",cliente.toString())
                finish()
                startActivity(getIntent())
            })
        queue.add(JsonReq)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}