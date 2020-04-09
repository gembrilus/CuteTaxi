package ua.com.cuteteam.cutetaxiproject.fragments.passenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.bs_make_order_collapsed.*
import kotlinx.android.synthetic.main.fragment_make_order.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel
import ua.com.cuteteam.cutetaxiproject.extentions.mutation
import ua.com.cuteteam.cutetaxiproject.fragments.adapters.AddressAutoCompleteAdapter
import ua.com.cuteteam.cutetaxiproject.livedata.ViewAction
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel

class MakeOrderFragment : Fragment(),
    BottomSheetFragment, AdapterView.OnItemSelectedListener,
    View.OnClickListener {

    private val viewModel: PassengerViewModel by activityViewModels()

    private var callback: OnChildDrawnListener? = null

    private val adapter by lazy {
        AddressAutoCompleteAdapter(
            this.requireContext()
        )
    }

    private val onGlobalLayoutListener by lazy {
        ViewTreeObserver.OnGlobalLayoutListener {
            if (parentFragment?.isVisible == true) {
                callback?.onChildDrawn(make_order_bottom_view.measuredHeight)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_make_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        setObservers()
        showCollapsed()
    }

    override fun onStart() {
        make_order_bottom_view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        super.onStart()
    }

    override fun onPause() {
        make_order_bottom_view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        super.onPause()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (position) {
            0 -> viewModel.newOrder.mutation { it.value?.comfortLevel = ComfortLevel.STANDARD }
            1 -> viewModel.newOrder.mutation { it.value?.comfortLevel = ComfortLevel.COMFORT }
            2 -> viewModel.newOrder.mutation { it.value?.comfortLevel = ComfortLevel.ECO }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_make_order -> viewModel.makeOrder()
            R.id.btn_make_order_coll -> viewModel.makeOrder()
        }
    }

    private fun setObservers() {

        viewModel.newOrder.observe(viewLifecycleOwner, Observer { order ->
            et_start_address.setText(order.addressStart?.address)
            et_destination_address.setText(order.addressDestination?.address)
            btn_make_order.isEnabled = order.isReady()
            btn_make_order_coll.isEnabled = order.isReady()

            input_layout_start_address.isEndIconVisible = order.addressStart != null
            input_layout_destination_address.isEndIconVisible = order.addressDestination != null

            setSpinnersValue(order.comfortLevel)
        })

        viewModel.addresses.observe(viewLifecycleOwner, Observer {
            adapter.setAddresses(it)
        })
    }

    private fun initUI() {
        sp_comfort_level.onItemSelectedListener = this
        sp_comfort_level_coll.onItemSelectedListener = this
        btn_start_point.setOnClickListener(this)
        btn_end_point.setOnClickListener(this)
        btn_make_order.setOnClickListener(this)
        btn_make_order_coll.setOnClickListener(this)

        et_start_address.setAdapter(adapter)
        et_destination_address.setAdapter(adapter)

        input_layout_start_address.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (adapter.count == 1) {
                    val address = et_destination_address.adapter.getItem(0) as Address
                    viewModel.newOrder.mutation { it.value?.addressStart = address }
                    et_start_address.dismissDropDown()
                }
                false
            } else true
        }

        input_layout_start_address.setEndIconOnClickListener {
            viewModel.newOrder.mutation { it.value?.addressStart = null }
        }

        input_layout_destination_address.setEndIconOnClickListener {
            viewModel.newOrder.mutation { it.value?.addressDestination = null }
        }

        input_layout_destination_address.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (adapter.count == 1) {
                    val address = et_destination_address.adapter.getItem(0) as Address
                    viewModel.newOrder.mutation { it.value?.addressDestination = address }
                    et_destination_address.dismissDropDown()
                }
                false
            } else true
        }

        input_layout_start_address.editText?.setOnFocusChangeListener { _, _ ->
            viewModel.addresses.mutation { it.value = emptyList() }
            if (btn_start_point.isVisible) {
                viewModel.viewAction.value =
                    ViewAction.PassengerBottomSheetControl(
                        ViewAction.PassengerBottomSheetControl.SHOW_EXPANDED
                    )
            }
        }

        input_layout_destination_address.editText?.setOnFocusChangeListener { _, _ ->
            viewModel.addresses.mutation { it.value = emptyList() }
            if (btn_start_point.isVisible) {
                viewModel.viewAction.value =
                    ViewAction.PassengerBottomSheetControl(
                        ViewAction.PassengerBottomSheetControl.SHOW_EXPANDED
                    )
            }
        }

        et_start_address.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val address = parent?.getItemAtPosition(position) as Address
                viewModel.newOrder.mutation { it.value?.addressStart = address }
                et_start_address.dismissDropDown()
            }

        et_destination_address.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val address = parent?.getItemAtPosition(position) as Address
                viewModel.newOrder.mutation { it.value?.addressDestination = address }
                et_destination_address.dismissDropDown()
            }

        et_start_address.doAfterTextChanged {
            if (!it.isNullOrEmpty() && it.length > 5) {
                viewModel.fetchAddresses(it.toString())
            }
        }

        et_destination_address.doAfterTextChanged {
            if (!it.isNullOrEmpty() && it.length > 5) {
                viewModel.fetchAddresses(it.toString())
            }
        }
    }

    fun showCollapsed() {

        sp_comfort_level_coll.visibility = View.VISIBLE
        btn_make_order_coll.visibility = View.VISIBLE
        btn_start_point.visibility = View.VISIBLE
        btn_end_point.visibility = View.VISIBLE

    }

    fun showExpanded() {

        sp_comfort_level_coll.visibility = View.GONE
        btn_make_order_coll.visibility = View.GONE
        btn_start_point.visibility = View.GONE
        btn_end_point.visibility = View.GONE

    }

    fun showAppBar() {
        app_bar.visibility = View.VISIBLE
    }

    fun hideAppBar() {
        app_bar.visibility = View.GONE
    }

    override fun setOnChildDrawnListener(callback: OnChildDrawnListener) {
        this.callback = callback
    }

    override fun removeOnChildDrawnListener(callback: OnChildDrawnListener) {
        this.callback = null
    }

    private fun setSpinnersValue(comfortLevel: ComfortLevel) {
        when (comfortLevel) {
            ComfortLevel.STANDARD -> setSpinnersSelection(0)
            ComfortLevel.COMFORT -> setSpinnersSelection(1)
            ComfortLevel.ECO -> setSpinnersSelection(2)
        }
    }

    private fun setSpinnersSelection(index: Int) {
        sp_comfort_level_coll.setSelection(index)
        sp_comfort_level.setSelection(index)
    }
}
