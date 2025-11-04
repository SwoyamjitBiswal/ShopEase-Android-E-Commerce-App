package com.example.shopease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.ShopEaseApplication
import com.example.shopease.adapter.OrderAdapter
import com.example.shopease.ui.OrderViewModel
import com.example.shopease.ui.ViewModelFactory
import kotlinx.coroutines.launch

class MyOrdersFragment : Fragment() {

    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var noOrdersView: LinearLayout

    private val viewModel: OrderViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as ShopEaseApplication).container.shoppingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersRecyclerView = view.findViewById(R.id.orders_recycler_view)
        noOrdersView = view.findViewById(R.id.no_orders_view)

        ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.orders.collect { orders ->
                    if (orders.isEmpty()) {
                        noOrdersView.visibility = View.VISIBLE
                        ordersRecyclerView.visibility = View.GONE
                    } else {
                        noOrdersView.visibility = View.GONE
                        ordersRecyclerView.visibility = View.VISIBLE
                        orderAdapter = OrderAdapter(orders)
                        ordersRecyclerView.adapter = orderAdapter
                    }
                }
            }
        }
    }
}
