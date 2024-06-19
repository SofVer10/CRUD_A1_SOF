package sofia.palacios.crud_a1_sof

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class pantalla_bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_bienvenida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnContinuarBv = findViewById<Button>(R.id.btnContinuarBv)


        btnContinuarBv.setOnClickListener {

            GlobalScope.launch (Dispatchers.Main){
                //Tiempo de cambio 3 segundos
                delay(3000)

                //Cambio de pantalla
                val pantallaSiguiente = Intent(this@pantalla_bienvenida, activity_registro::class.java)
                startActivity(pantallaSiguiente)
                //Final del activity
                finish()
            }
        }
    }
}