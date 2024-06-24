package Modelos

data class tbTickets(

    val UUID_Ticket: String,
    val Numero: Int,
    var Titulo: String,
    val Estado: String,
    val Descripcion: String,
    val Responsable:String,
    val Email: String,
    val Fecha_Ticket: String,
    val Fecha_Final: String

)
