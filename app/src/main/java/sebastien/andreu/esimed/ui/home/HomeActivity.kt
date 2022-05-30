package sebastien.andreu.esimed.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.extension.toTreatFor
import sebastien.andreu.esimed.listener.ListenerDialog
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.ui.dialog.DialogCreateTask
import sebastien.andreu.esimed.utils.CalendarUtils
import sebastien.andreu.esimed.utils.ToastUtils

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.addNewTask)?.setOnClickListener {
            CalendarUtils.selectedDate?.let { date ->
                val dialog = DialogCreateTask.newInstance(date, object : ListenerDialog {
                    override fun onValidate(task: Task) {
                        ToastUtils.success(this@HomeActivity, task.toString())
                        this@HomeActivity.onRestart()
                    }

                    override fun onDelete(task: Task) {
                        /* delete */
                    }
                })
                dialog.isCancelable = false
                dialog.show(supportFragmentManager, TAG)
            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_month, R.id.nav_week, R.id.nav_day
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        private const val TAG: String = "MainActivity"

        fun start(context: Context) {
            try {
                context.startActivity(Intent(context, HomeActivity::class.java))
            } catch (exception: Exception) {
                exception.toTreatFor(TAG)
            }
        }
    }
}