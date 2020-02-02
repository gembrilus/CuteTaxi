package ua.com.cuteteam.cutetaxiproject.preferences

import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.preference.DialogPreference
import java.util.*

class ListBoxPreference(
    context: Context,
    attrs: AttributeSet? = null
) : DialogPreference(context, attrs) {

    private var _values = mutableSetOf<String>()
    var values: Set<String>
        get() = _values
        set(list) {
            _values.clear()
            _values.addAll(list)
            persistStringSet(values)
            notifyChanged()
        }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any? = mutableSetOf<String>()

    @Suppress("UNCHECKED_CAST")
    override fun onSetInitialValue(defaultValue: Any?) {
        if (defaultValue == null) return
        _values = getPersistedStringSet(defaultValue as MutableSet<String>)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        if (isPersistent) {
            return superState
        }
        return SavedState(superState).apply {
            mValues = _values
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state == null || state.javaClass != SavedState::class.java) {
            super.onRestoreInstanceState(state)
            return
        }
        val myState = state as SavedState
        super.onRestoreInstanceState(myState.superState)
        _values = myState.mValues
    }


    private class SavedState : BaseSavedState {

        var mValues = mutableSetOf<String>()

        internal constructor(source: Parcel) : super(source) {
            val size = source.readInt()
            val strings = arrayOfNulls<String>(size)
            source.readStringArray(strings)
            Collections.addAll<String>(mValues, *strings)
        }

        internal constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(mValues.size)
            dest.writeStringArray(mValues.toTypedArray())
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}