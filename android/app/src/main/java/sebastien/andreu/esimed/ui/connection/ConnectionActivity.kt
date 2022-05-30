package sebastien.andreu.esimed.ui.connection

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import sebastien.andreu.esimed.ui.home.HomeActivity
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.ui.register.RegisterActivity

@AndroidEntryPoint
class ConnectionActivity: AppCompatActivity() {

    private val viewModel: ConnectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connection_activity)

        findViewById<Button>(R.id.buttonLogin)?.setOnClickListener {
            HomeActivity.start(this)
        }

        findViewById<TextView>(R.id.register)?.setOnClickListener {
            RegisterActivity.start(this)
        }
    }
}