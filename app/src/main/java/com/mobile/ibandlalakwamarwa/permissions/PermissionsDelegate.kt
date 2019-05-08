
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import java.util.*

/**
 * Created by Lungelo on 19/11/2018.
 */
abstract class PermissionsDelegate (private val activity: AppCompatActivity) {

    companion object {
        val REQUEST_CODE_ASK_PERMISSIONS = 123
    }
    protected abstract fun permissionGranted(permission: String)

    protected abstract fun permissionDenied(permission: String)

    fun getPermissionFriendlyName(permission: String): String {
        val lastIndex = permission.lastIndexOf("")
        return permission.substring(lastIndex + 1).replace("_".toRegex(), " ")
    }

    private fun showMessageOKCancel(permission: String) {
        android.support.v7.app.AlertDialog.Builder(activity)
                .setMessage("To access this functionality please grant permission to: \n" + getPermissionFriendlyName(permission))
                .setPositiveButton("OK", OkListener())
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    private inner class OkListener : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface, i: Int) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:" + activity.packageName)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            activity.startActivityForResult(intent, REQUEST_CODE_ASK_PERMISSIONS)
        }
    }

    private fun userFlaggedNeverAskAgainAndDeniedPermission(permission: String): Boolean {
        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    private fun devicePermissionIsDenied(permission: String): Boolean {
        val hasPermission = ContextCompat.checkSelfPermission(activity, permission)
        return hasPermission != PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(vararg permissions: String) {
        val deniedPermissions = LinkedList<String>()
        val grantedPermissions = LinkedList<String>()

        for (permission in permissions) {
            if (devicePermissionIsDenied(permission)) {
                deniedPermissions.add(permission)
            } else {
                grantedPermissions.add(permission)
            }
        }

        if (!deniedPermissions.isEmpty()) {
            val deniedPermissionsArray = deniedPermissions.toTypedArray()
            ActivityCompat.requestPermissions(activity, deniedPermissionsArray, REQUEST_CODE_ASK_PERMISSIONS)
        }

        if (!grantedPermissions.isEmpty()) {
            for (permission in grantedPermissions) {
                permissionGranted(permission)
            }
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> for (i in permissions.indices) {
                val permission = permissions[i]
                val grant = grantResults[i]

                if (grant == PackageManager.PERMISSION_DENIED) {
                    if (userFlaggedNeverAskAgainAndDeniedPermission(permission)) {
                        showMessageOKCancel(permission)
                    } else {
                        permissionDenied(permission)
                    }
                }

                if (grant == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted(permission)
                }
            }
            else -> activity.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }


}