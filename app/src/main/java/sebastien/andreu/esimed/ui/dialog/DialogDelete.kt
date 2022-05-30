package sebastien.andreu.esimed.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.listener.ListenerDialogDelete

class DialogDelete: DialogFragment() {

    private var listener: ListenerDialogDelete? = null

    fun setListener (listener: ListenerDialogDelete) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_delete, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.cancelButton)?.setOnClickListener {
            listener?.onCancel()
            this.dismiss()
        }

        view.findViewById<Button>(R.id.validateButton)?.setOnClickListener {
            listener?.onDelete()
            this.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener = null
        super.onDismiss(dialog)
    }

    override fun onResume() {
        super.onResume()

        if (dialog != null) {
            val window = dialog?.window ?: return
            window.setBackgroundDrawableResource(R.drawable.background_transparent)
        }
    }

    companion object {
        fun newInstance(): DialogDelete {
            return DialogDelete()
        }
    }
}