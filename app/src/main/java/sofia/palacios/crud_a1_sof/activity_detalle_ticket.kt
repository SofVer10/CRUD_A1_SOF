package sofia.palacios.crud_a1_sof

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class activity_detalle_ticket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_ticket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Recibir los valores
        val UUIDRecibido = intent.getStringExtra("UUID_Ticket")
        val num_ticketRecibido = intent.getIntExtra("Numero", 0)
        val tituloRecibido = intent.getStringExtra("Titulo")
        val estadoRecibida = intent.getStringExtra("Estado")
        val descripcionRecibido = intent.getStringExtra("Descripcion")
        val responsableRecibido = intent.getStringExtra("Responsable")
        val email_Recibida = intent.getStringExtra("Email")
        val fecha_ticketRecibida = intent.getStringExtra("Fecha_Ticket")
        val fecha_fin_ticketRecibida = intent.getStringExtra("Fecha_Final")

        //llamo los elementos
        val txtUUIDetalle = findViewById<TextView>(R.id.txtUUIDetalle1)
        val txtNumTicketDet = findViewById<TextView>(R.id.txtNumTicketDet1)
        val txtEstadoDet = findViewById<TextView>(R.id.txtEstadoDet1)
        val txtFechaInicioDet = findViewById<TextView>(R.id.txtFechaInicioDet1)
        val txtFechaFinalDet = findViewById<TextView>(R.id.txtFechaFinalDet1)
        val txtTituloDet = findViewById<TextView>(R.id.txtTituloDet1)
        val txtDescripcionDet = findViewById<TextView>(R.id.txtDescripcionDet1)
        val txtResponsableDet = findViewById<TextView>(R.id.txtResponsableDet1)
        val txtEmailDet = findViewById<TextView>(R.id.txtEmailDet1)



        //Asigarle los datos recibidos a mis TextView
        txtUUIDetalle.text = UUIDRecibido
        txtNumTicketDet.text = num_ticketRecibido.toString()
        txtEstadoDet.text = estadoRecibida
        txtFechaInicioDet.text = fecha_ticketRecibida
        txtFechaFinalDet.text = fecha_fin_ticketRecibida
        txtTituloDet.text = tituloRecibido
        txtDescripcionDet.text = descripcionRecibido
        txtResponsableDet.text = responsableRecibido
        txtEmailDet.text = email_Recibida


        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener{

            GlobalScope.launch (Dispatchers.Main){
                //Tiempo de cambio 3 segundos
                delay(2000)

                //Cambio de pantalla
                val pantallaSiguiente = Intent(this@activity_detalle_ticket, activity_tickets::class.java)
                startActivity(pantallaSiguiente)
                //Final del activity
                finish()
            }
        }
    }
}