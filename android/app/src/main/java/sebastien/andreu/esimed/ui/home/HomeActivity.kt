package sebastien.andreu.esimed.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.*
import com.auth0.android.jwt.JWT
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.api.Status
import sebastien.andreu.esimed.extension.toTreatFor
import sebastien.andreu.esimed.listener.ListenerDialog
import sebastien.andreu.esimed.local.Storage
import sebastien.andreu.esimed.local.StorageEnum
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.ui.dialog.DialogCreateTask
import sebastien.andreu.esimed.utils.*
import android.view.View
import sebastien.andreu.esimed.listener.ListenerDialogLogout
import sebastien.andreu.esimed.ui.dialog.DialogLogout


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setNavigationGraph()

        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<NavigationView>(R.id.nav_view).let { navigation ->
            val header: View = navigation.getHeaderView(0)
            header.findViewById<TextView>(R.id.showPseudo)?.text = JWT(Token.value.toString()).getClaim("pseudo").asString().toString()
            header.findViewById<TextView>(R.id.showEmail)?.text = JWT(Token.value.toString()).getClaim("email").asString().toString()
        }

        findViewById<TextView>(R.id.logout)?.setOnClickListener {
            val dialogLogout = DialogLogout.newInstance()
            dialogLogout.let { dialog ->
                dialog.setListener(object : ListenerDialogLogout {
                    override fun onLogout() {
                        Token.value = null
                        CalendarUtils.selectedDate = null
                        this@HomeActivity.finish()
                    }

                    override fun onCancel() { /* WE DO NOTHING */}
                })
                dialog.isCancelable = false
                dialog.show(supportFragmentManager, "TAG_LOGOUT_DIALOG")
            }
        }

        viewModel.apiResponse.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    refreshCurrentFragment()
                }
                Status.ERROR -> {
                    ToastUtils.error(this, it.message)
                }
            }
        })

        findViewById<FloatingActionButton>(R.id.addNewTask)?.setOnClickListener {
            CalendarUtils.selectedDate?.let { date ->
                val dialog = DialogCreateTask.newInstance(date, object : ListenerDialog {
                    override fun onValidate(task: Task) {
                        viewModel.addTask(this@HomeActivity, task)
                    }

                    override fun onDelete(task: Task) { /* WE DO NOTHING */}
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

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.nav_month -> {
                    Storage(this).setString(StorageEnum.VIEW, MONTH)
                }
                R.id.nav_week -> {
                    Storage(this).setString(StorageEnum.VIEW, WEEK)
                }
                R.id.nav_day -> {
                    Storage(this).setString(StorageEnum.VIEW, DAY)
                }
            }
        }


    }

    private fun setNavigationGraph() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val navGraph: NavGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)

            when (Storage(this).getString(StorageEnum.VIEW)) {
                WEEK -> {
                    navGraph.setStartDestination(R.id.nav_week)
                }
                MONTH -> {
                    navGraph.setStartDestination(R.id.nav_month)
                }
                DAY -> {
                    navGraph.setStartDestination(R.id.nav_day)
                }
            }

        navController.graph = navGraph
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun refreshCurrentFragment(){
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val id = navController.currentDestination?.id
        navController.popBackStack(id!!,true)
        navController.navigate(id)
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