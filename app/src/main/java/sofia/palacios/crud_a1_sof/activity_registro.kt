package sofia.palacios.crud_a1_sof

import Modelos.Conexion
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class activity_registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgAtrasregistrarse = findViewById<ImageView>(R.id.imgAtrasregistrarse)
        val txtCorreoRegistro = findViewById<EditText>(R.id.txtCorreoRegistro)
        val txtContraRegistro = findViewById<EditText>(R.id.txtContraRegistro)
        val txtConfirmarContra = findViewById<EditText>(R.id.txtConfirmarContra)
        val imgVerContra = findViewById<ImageView>(R.id.imgVerContra)
        val imgVerConfirmacionContra = findViewById<ImageView>(R.id.imgVerConfirmacionContra)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val btnRegresarLogin = findViewById<Button>(R.id.btnRegresarLogin)

        btnCrearCuenta.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                //Creo un objeto de la clase conexion
                val objConexion = Conexion().cadenaConexion()

                //Creo una variable que contenga un PrepareStatement
                val crearUsuario =
                    objConexion?.prepareStatement("INSERT INTO tbUsuarios(UUID_usuario, correoElectronico, clave) VALUES (?, ?, ?)")!!
                crearUsuario.setString(1, UUID.randomUUID().toString())
                crearUsuario.setString(2, txtCorreoRegistro.text.toString())
                crearUsuario.setString(3, txtContraRegistro.text.toString())
                crearUsuario.executeUpdate()
                withContext(Dispatchers.Main) {
                    //Abro otra corrutina o "Hilo" para mostrar un mensaje y limpiar los campos
                    //Lo hago en el Hilo Main por que el hilo IO no permite mostrar nada en pantalla
                    Toast.makeText(
                        this@activity_registro,
                        "Usuario creado con éxito",
                        Toast.LENGTH_SHORT
                    ).show()
                    txtCorreoRegistro.setText("")
                    txtContraRegistro.setText("")
                    txtConfirmarContra.setText("")
                }
            }

        }

        //Programo los botones para mostrar u ocultar la contraseña
        imgVerContra.setOnClickListener {
            if (txtContraRegistro.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                txtContraRegistro.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                txtContraRegistro.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        imgVerConfirmacionContra.setOnClickListener {
            if (txtContraRegistro.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                txtContraRegistro.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                txtContraRegistro.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        //Al darle clic a la flechita de arriba - Regresar al Login
        imgAtrasregistrarse.setOnClickListener {
            val pantallaLogin = Intent(this, activity_login::class.java)
            startActivity(pantallaLogin)
        }
        //Al darle clic a al boton que ya tengo una cuenta - Regresar al Login
        btnRegresarLogin.setOnClickListener {
            val pantallaLogin = Intent(this, activity_login::class.java)
            startActivity(pantallaLogin)
        }


    }
}