package cl.nery.android.evaluacionu1

import java.text.NumberFormat
import java.util.Locale
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cl.nery.android.evaluacionu1.modelo.CuentaMesa
import cl.nery.android.evaluacionu1.modelo.ItemMenu

class MainActivity : AppCompatActivity() {
    //Declaramos las Variables
    private lateinit var cuentaMesa: CuentaMesa
    private var etCantidadPastel:EditText? = null
    private var etCantidadCazuela:EditText? = null
    private var textValorComida:TextView? = null
    private var textPropina:TextView? = null
    private var textTotal:TextView? = null
    private var swPropina:Switch? = null
    private var tvValorPastel:TextView? = null
    private var tvValorCazuela:TextView? = null

    // Variables principales
    val formatoMonedaChilena = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    val pastelChoclo = "Pastel de Choclo"
    val costoPastel = "12000"
    val cazuela = "Cazuela"
    val costoCazuela = "10000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cuentaMesa = CuentaMesa(1)
        swPropina = findViewById<Switch>(R.id.swPropina)
        etCantidadPastel = findViewById<EditText>(R.id.etCantidadPastel)
        etCantidadCazuela = findViewById<EditText>(R.id.etCantidadCazuela)
        textValorComida = findViewById<TextView>(R.id.tvValorComida)
        textPropina = findViewById<TextView>(R.id.tvValorPropina)
        textTotal = findViewById<TextView>(R.id.tvValorTotal)
        tvValorPastel = findViewById<TextView>(R.id.tvValorPastel)
        tvValorCazuela = findViewById<TextView>(R.id.tvValorCazuela)
        actualizarTotales()

        swPropina?.setOnCheckedChangeListener { _, isChecked ->
            cuentaMesa.aceptaPropina  = isChecked
            actualizarTotales()
        }

        val textWatcher:TextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                actualizarTotales()
            }
        }
        etCantidadPastel?.addTextChangedListener(textWatcher)
        etCantidadCazuela?.addTextChangedListener(textWatcher)
    }

    fun actualizarTotales() {
        cuentaMesa.limpiarItems()

        val cantPastelDeChoclo = etCantidadPastel?.text.toString().toIntOrNull() ?: 0
        val cantCazuela = etCantidadCazuela?.text.toString().toIntOrNull() ?: 0
        cuentaMesa.agregarItem(ItemMenu(pastelChoclo, costoPastel), cantPastelDeChoclo)
        cuentaMesa.agregarItem(ItemMenu(cazuela, costoCazuela), cantCazuela)
        actualizarSubTotal()
        actualizarPropina()
        actualizarPrecioTotal()
        actualizarPrecioPorProducto()
    }

    fun actualizarPropina(){
        val propina = cuentaMesa.calcularPropina()
        textPropina?.setText(propina.toString())
        textPropina?.text = formatoMonedaChilena.format(propina)
    }

    fun actualizarPrecioTotal(){
        val total = cuentaMesa.calcularTotalConPropina()
        textTotal?.setText(total.toString())
        textTotal?.text = formatoMonedaChilena.format(total)
    }

    fun actualizarSubTotal(){
        val valorComida = cuentaMesa.calcularTotalSinPropina()
        textValorComida?.setText(valorComida.toString())
        textValorComida?.text = formatoMonedaChilena.format(valorComida)
    }

    fun actualizarPrecioPorProducto(){
        val cantPastelDeChoclo = etCantidadPastel?.text.toString().toIntOrNull() ?: 0
        val cantCazuela = etCantidadCazuela?.text.toString().toIntOrNull() ?: 0
        val totalValorPastel = cantPastelDeChoclo * costoPastel.toInt()
        val totalValorCazuela = cantCazuela * costoCazuela.toInt()
        tvValorPastel?.setText(totalValorPastel.toString())
        tvValorCazuela?.setText(totalValorCazuela.toString())
        tvValorPastel?.text = formatoMonedaChilena.format(totalValorPastel)
        tvValorCazuela?.text = formatoMonedaChilena.format(totalValorCazuela)

    }
}