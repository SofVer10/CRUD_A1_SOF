package RicyclerViewHelpers

import Modelos.Conexion
import Modelos.tbTickets
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sofia.palacios.crud_a1_sof.R
import sofia.palacios.crud_a1_sof.activity_detalle_ticket

class Adaptador(var Datos: List<tbTickets>): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista: List<tbTickets>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
    }

    fun actualicePantalla(uuid: String, nuevoTitulo: String){
        val index = Datos.indexOfFirst { it.UUID_Ticket == uuid }
        Datos[index].Titulo = nuevoTitulo
        notifyDataSetChanged()
    }


    /////////////////// TODO: Eliminar datos
    fun eliminarDatos(Titulo: String, posicion: Int){
        //Actualizo la lista de datos y notifico al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            //1- Creamos un objeto de la clase conexion
            val objConexion = Conexion().cadenaConexion()

            //2- Crear una variable que contenga un PrepareStatement
            val deletetbTickets = objConexion?.prepareStatement("delete from tbTicket where Titulo = ?")!!
            deletetbTickets.setString(1, Titulo)
            deletetbTickets.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        // Notificar al adaptador sobre los cambios
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //////////////////////TODO: Editar datos
    fun actualizarDato(nuevoTitulo: String, UUID_Ticket: String){
        GlobalScope.launch(Dispatchers.IO){

            //1- Creo un objeto de la clase de conexion
            val objConexion = Conexion().cadenaConexion()

            //2- creo una variable que contenga un PrepareStatement
            val updatetbTickets = objConexion?.prepareStatement("update tbTicket set Titulo = ? where UUID_Ticket = ?")!!
            updatetbTickets.setString(1, nuevoTitulo)
            updatetbTickets.setString(2, UUID_Ticket)
            updatetbTickets.executeUpdate()

            withContext(Dispatchers.Main){
                actualicePantalla(UUID_Ticket, nuevoTitulo)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)

    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = Datos[position]
        holder.txtCard.text = item.Titulo

        //todo: clic al icono de eliminar
        holder.imgBorrar.setOnClickListener {

            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el Ticket?")

            //Botones
            builder.setPositiveButton("Si") { dialog, which ->
                eliminarDatos(item.Titulo, position)
            }

            builder.setNegativeButton("No"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

        //Todo: icono de editar
        holder.imgEditar.setOnClickListener{
            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Actualizar")
            builder.setMessage("¿Desea actualizar el Ticket?")

            //Agregarle un cuadro de texto para
            //que el usuario escriba el nuevo nombre
            val cuadroTexto = EditText(context)
            cuadroTexto.setHint(item.Titulo)
            builder.setView(cuadroTexto)

            //Botones
            builder.setPositiveButton("Actualizar") { dialog, which ->
                actualizarDato(cuadroTexto.text.toString(), item.UUID_Ticket)
            }

            builder.setNegativeButton("Cancelar"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        //Todo: Clic a la card completa

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            //Cambiar de pantalla a la pantalla de detalle
            val pantallaDetalle = Intent(context, activity_detalle_ticket::class.java)
            //enviar a la otra pantalla todos mis valores
            pantallaDetalle.putExtra("UUID_Ticket", item.UUID_Ticket)
            pantallaDetalle.putExtra("Numero", item.Numero)
            pantallaDetalle.putExtra("Titulo", item.Titulo)
            pantallaDetalle.putExtra("Estado", item.Estado)
            pantallaDetalle.putExtra("Descripcion", item.Descripcion)
            pantallaDetalle.putExtra("Responsable", item.Responsable)
            pantallaDetalle.putExtra("Email", item.Email)
            pantallaDetalle.putExtra("Fecha_Ticket", item.Fecha_Ticket)
            pantallaDetalle.putExtra("Fecha_Final", item.Fecha_Final)
            context.startActivity(pantallaDetalle)
        }
    }


}