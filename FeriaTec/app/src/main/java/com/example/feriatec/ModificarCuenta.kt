package com.example.feriatec

import MyJsonArrayRequest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_modificar_cuenta.*
import kotlinx.android.synthetic.main.activity_pantalla_usuario.*
import kotlinx.android.synthetic.main.activity_pantalla_usuario.drawer_layout
import kotlinx.android.synthetic.main.activity_pantalla_usuario.navView
import org.json.JSONArray
import org.json.JSONObject

class ModificarCuenta : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var cliente: JSONObject
    lateinit var urlRec: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_cuenta)
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        cliente  = JSONObject(intent.getStringExtra("Cliente"))
        urlRec = intent.getStringExtra("url").toString()
        val header = navView.getHeaderView(0)
        val user = header.findViewById<TextView>(R.id.NombreCliente)
        user.setText(cliente.getString("usuario"))
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
            }
            true
        }
        fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
        nameInputMod.text= cliente.getString("nombre").toString().toEditable()
        apellidosInputMod.text = cliente.getString("apellidos").toString().toEditable()
        provinciaInputMod.text = cliente.getString("provincia").toString().toEditable()
        cantonInputMod.text = cliente.getString("canton").toString().toEditable()
        distritoInputMod.text = cliente.getString("distrito").toString().toEditable()
        direccionInputMod.text = cliente.getString("direccion").toString().toEditable()
        telefonoInputMod.text = cliente.getString("telefono").toString().toEditable()
        nacimientoInputMod.text = cliente.getString("nacimiento").toString().toEditable()
        passwordInputMod.text = cliente.getString("password").toString().toEditable()
        modificar.setOnClickListener{
            validar()
        }
    }
    fun validar(){
        if(passwordInputMod.text.toString() != confirmationInputMod.text.toString() && !passwordInputMod.text.isNullOrEmpty() && !confirmationInputMod.text.isNullOrEmpty()){
            Toast.makeText(this,"Las Contraseñas no Coinciden", Toast.LENGTH_LONG).show()
            return
        }
        val url = "${urlRec}server/api/Clientes/modify"
        val queue = Volley.newRequestQueue(this)
        cliente.put("nombre", nameInputMod.text)
        cliente.put("apellidos", apellidosInputMod.text)
        cliente.put("provincia", provinciaInputMod.text.toString())
        cliente.put("canton", cantonInputMod.text.toString())
        cliente.put("distrito", distritoInputMod.text.toString())
        cliente.put("direccion", direccionInputMod.text.toString())
        cliente.put("password", passwordInputMod.text.toString())
        cliente.put("nacimiento", nacimientoInputMod.text.toString())
        cliente.put("telefono", telefonoInputMod.text.toString())
        val JsonReq = MyJsonArrayRequest(
            Request.Method.POST,url,cliente,
            Response.Listener<JSONArray> { response-> },
            Response.ErrorListener {
                Toast.makeText(this,"Cuenta Modificada Satisfactoriamente", Toast.LENGTH_LONG).show()
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