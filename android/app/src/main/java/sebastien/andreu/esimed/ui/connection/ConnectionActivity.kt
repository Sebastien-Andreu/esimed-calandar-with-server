package sebastien.andreu.esimed.ui.connection

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import sebastien.andreu.esimed.ui.home.HomeActivity
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.api.Status
import sebastien.andreu.esimed.ui.register.RegisterActivity
import sebastien.andreu.esimed.utils.ToastUtils
import sebastien.andreu.esimed.utils.Token

@AndroidEntryPoint
class ConnectionActivity: AppCompatActivity() {

    private val viewModel: ConnectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connection_activity)

        viewModel.apiResponse.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    Token.value = it.message
                    HomeActivity.start(this)
                }
                Status.ERROR -> {
                    ToastUtils.error(this, it.message)
                }
            }
        })

        findViewById<Button>(R.id.buttonLogin)?.setOnClickListener {
            findViewById<EditText>(R.id.registerLogin)?.let { pseudo ->
                findViewById<EditText>(R.id.registerPassword)?.let { password ->
                    if (!pseudo.text.isNullOrBlank() && !password.text.isNullOrBlank()) {
                        viewModel.connect(this, pseudo.text.toString(), password.text.toString())
                    } else {
                        ToastUtils.error(this, getString(R.string.input_failed))
                    }
                }
            }
        }

        findViewById<TextView>(R.id.register)?.setOnClickListener {
            RegisterActivity.start(this)
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<EditText>(R.id.registerLogin)?.let {
            it.text.clear()
            it.requestFocus()
        }
        findViewById<EditText>(R.id.registerPassword)?.text?.clear()
    }
}