package RicyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sofia.palacios.crud_a1_sof.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val txtCard = view.findViewById<TextView>(R.id.txtCard)
    val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
    val imgBorrar: ImageView = view.findViewById(R.id.imgBorrar)

}