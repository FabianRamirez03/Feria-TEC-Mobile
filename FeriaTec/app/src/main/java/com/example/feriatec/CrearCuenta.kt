package com.example.feriatec

import MyJsonArrayRequest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_crear_cuenta.*
import kotlinx.android.synthetic.main.activity_pantalla_usuario.*
import org.json.JSONArray
import org.json.JSONObject

class CrearCuenta : AppCompatActivity() {
    lateinit var urlRec: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        urlRec = intent.getStringExtra("url").toString()
        crear.setOnClickListener{
            validar()
        }
    }
    fun validar(){
        if(passwordInput.text.toString() != confirmationInput.text.toString()){
            Toast.makeText(this,"Las Contraseñas no Coinciden", Toast.LENGTH_LONG).show()
            return
        }
        val url = "${urlRec}server/api/Clientes/insert"
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()
        jsonObject.put("nombre", nompreInput.text)
        jsonObject.put("apellidos", apellidosInput.text)
        jsonObject.put("cedula", cedulaInput.text.toString())
        jsonObject.put("provincia", provinciaInput.text.toString())
        jsonObject.put("canton", cantonInput.text.toString())
        jsonObject.put("distrito", distritoInput.text.toString())
        jsonObject.put("direccion", direccionInput.text.toString())
        jsonObject.put("usuario", userInput.text.toString())
        jsonObject.put("password", passwordInput.text.toString())
        jsonObject.put("nacimiento", nacimientoInput.text.toString())
        jsonObject.put("telefono", telefonoInput.text.toString())
        jsonObject.put("carrito",JSONArray())
        val JsonReq = MyJsonArrayRequest(
            Request.Method.POST,url,jsonObject,
            Response.Listener<JSONArray> { response-> },
            Response.ErrorListener {
                Toast.makeText(this,"Cuenta Creada Satisfactoriamente", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            })
        queue.add(JsonReq)
    }
}