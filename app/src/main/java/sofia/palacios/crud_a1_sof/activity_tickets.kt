package sofia.palacios.crud_a1_sof

import Modelos.Conexion
import Modelos.tbTickets
import RicyclerViewHelpers.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class activity_tickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tickets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val txtNumTicket = findViewById<EditText>(R.id.txtNumTicket)
        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtEstadoTicket = findViewById<EditText>(R.id.txtEstadoTicket)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val txtResponsable = findViewById<EditText>(R.id.txtResponsable)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtFechaInicio = findViewById<EditText>(R.id.txtFechaInicial)
        val txtFechaFinal = findViewById<EditText>(R.id.txtFechaFinal)
        val rcvTicket = findViewById<RecyclerView>(R.id.rcvTicket)

        rcvTicket.layoutManager = LinearLayoutManager(this)

        fun obtenerTickets(): List<tbTickets> {

            val objConexion = Conexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbTicket")!!

            val ListaTicket = mutableListOf<tbTickets>()

            while (resultSet.next()) {

                val UUID_TICKET = resultSet.getString("UUID_Ticket")
                val num_ticket = resultSet.getInt("Numero")
                val titulo = resultSet.getString("Titulo")
                val estado = resultSet.getString("estado")
                val descripcion = resultSet.getString("Descripcion")
                val autor = resultSet.getString("Responsable")
                val email_autor = resultSet.getString("Email")
                val fecha_ticket = resultSet.getString("Fecha_Ticket")
                val fecha_fin_ticket = resultSet.getString("Fecha_Final")

                val valoresJuntos = tbTickets(
                    UUID_TICKET,
                    num_ticket,
                    titulo,
                    descripcion,
                    autor,
                    email_autor,
                    fecha_ticket,
                    estado,
                    fecha_fin_ticket
                )

                ListaTicket.add(valoresJuntos)
            }
            return ListaTicket
        }

        CoroutineScope(Dispatchers.IO).launch {
            val TicketDB = obtenerTickets()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(TicketDB)
                rcvTicket.adapter = adapter
            }
        }

        val btnGenerarTicket = findViewById<Button>(R.id.btnGenerarTicket)

        btnGenerarTicket.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = Conexion().cadenaConexion()

                try {


                    // Validación de campos vacíos
                    if (txtNumTicket.text.isEmpty() || txtTitulo.text.isEmpty() || txtDescripcion.text.isEmpty() ||
                        txtResponsable.text.isEmpty() || txtEmail.text.isEmpty() || txtFechaInicio.text.isEmpty() ||
                        txtFechaFinal.text.isEmpty()
                    ) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@activity_tickets,
                                "Por favor, completa todos los campos.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@launch
                    }


                    val addTicket =
                        objConexion?.prepareStatement("insert into tbTicket (UUID_Ticket, Numero, Titulo, Estado, Descripcion, Responsable, Email, Fecha_Ticket, Fecha_Final) values (?,?,?,?,?,?,?,?,?)")!!

                    addTicket.setString(1, UUID.randomUUID().toString())
                    addTicket.setInt(2, txtNumTicket.text.toString().toInt())
                    addTicket.setString(3, txtTitulo.text.toString())
                    addTicket.setString(4, txtEstadoTicket.text.toString())
                    addTicket.setString(5, txtDescripcion.text.toString())
                    addTicket.setString(6, txtResponsable.text.toString())
                    addTicket.setString(7, txtEmail.text.toString())
                    addTicket.setString(8, txtFechaInicio.text.toString())
                    addTicket.setString(9, txtFechaFinal.text.toString())

                    addTicket.executeUpdate()

                    //Refresco la lista
                    val nuevosTickets = obtenerTickets()
                    withContext(Dispatchers.Main) {
                        (rcvTicket.adapter as? Adaptador)?.actualizarLista(nuevosTickets)
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@activity_tickets, "El ticket ha sido creado", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@activity_tickets, "Error: ${e.message}", Toast.LENGTH_LONG)
                            .show()
                        println("Error: ${e.message}")
                        e.printStackTrace()
                    }

                }
            }


        }

    }
}