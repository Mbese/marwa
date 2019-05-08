
import android.support.v7.app.AppCompatActivity

/**
 * Created by Lungelo on 19/11/2018.
 */
abstract class PermissionsActivity : AppCompatActivity() {

    private var permissionsDelegate: PermissionsDelegate? = null


    fun requestPermissions(permissions: String) {
        permissionsDelegate!!.requestPermissions(permissions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionsDelegate!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected abstract fun permissionGranted(permission: String)

    protected abstract fun permissionDenied(permission: String)

    init {
        this.permissionsDelegate = object : PermissionsDelegate(this@PermissionsActivity) {
            override fun permissionGranted(permission: String) {
                this@PermissionsActivity.permissionGranted(permission)
            }

            override fun permissionDenied(permission: String) {
                this@PermissionsActivity.permissionDenied(permission)
            }
        }
    }
}