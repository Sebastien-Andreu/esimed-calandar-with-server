package sebastien.andreu.esimed.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.api.Status
import sebastien.andreu.esimed.extension.toTreatFor
import sebastien.andreu.esimed.ui.home.HomeActivity
import sebastien.andreu.esimed.utils.ToastUtils

@AndroidEntryPoint
class RegisterActivity: AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inscription_activity)

        viewModel.apiResponse.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    ToastUtils.success(this, it.message)
                    this.finish()
                }
                Status.ERROR -> {
                    ToastUtils.error(this, it.message)
                }
            }
        })


        findViewById<Button>(R.id.buttonLogin)?.setOnClickListener {
            findViewById<EditText>(R.id.registerLogin)?.let { pseudo ->
                findViewById<EditText>(R.id.registerEmail)?.let { email ->
                    findViewById<EditText>(R.id.registerPassword)?.let { password ->
                        if (!pseudo.text.isNullOrBlank() && !email.text.isNullOrBlank() && !password.text.isNullOrBlank()) {
                            viewModel.createAccount(this, pseudo.text.toString(), email.text.toString(), password.text.toString())
                        } else {
                            ToastUtils.error(this, getString(R.string.input_failed))
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG: String = "RegisterActivity"

        fun start(context: Context) {
            try {
                context.startActivity(Intent(context, RegisterActivity::class.java))
            } catch (exception: Exception) {
                exception.toTreatFor(TAG)
            }
        }
    }
}