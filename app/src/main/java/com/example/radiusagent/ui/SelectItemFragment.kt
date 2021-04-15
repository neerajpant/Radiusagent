package com.example.radiusagent.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.radiusagent.R
import com.example.radiusagent.databinding.FragmentOthersBinding
import com.example.radiusagent.databinding.FragmentSelectItemBinding
import com.example.radiusagent.pojo.DisplayItem
import com.example.radiusagent.pojo.Exclusion
import com.example.radiusagent.pojo.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SelectItemFragment : Fragment() ,ProductExclusionAdapter.OnItemClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentSelectItemBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductsViewModel by activityViewModels()
  //  private val viewModel by viewModels<ProductsViewModel>()
    private var displayItemList:ArrayList<DisplayItem>?=null
    //private lateinit var productAdapter: DisplayListAdapter
    private lateinit var productAdapter: ProductExclusionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            displayItemList = it.getParcelableArrayList("displayItemList")
          //  id = it.getString("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectItemBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.exclusionItemsList.observe(viewLifecycleOwner, Observer {
            it?.let { exclusionList ->
            viewModel.extractExclusionList(displayItemList,exclusionList)
//        //        extractExclusionList(displayItemList,exclusionList)
//                productAdapter = DisplayListAdapter(
//                    options = displayItemList!!,
//                    listerner = this,
//                    exclusionList =exclusionList
//                )
//                binding?.apply {
//                    othersRecyle.apply {
//                        adapter = productAdapter
//                        othersRecyle.layoutManager =
//                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//                    }
//                }

                //first time on load
                // Toast.makeText(requireActivity(), "SuccessCalled", Toast.LENGTH_LONG).show()
                Log.d("SelectedItemFragment","exclusionItem Observer onSuccessCalled ${it.get(0).get(0).options_id}")
            }
        })
        viewModel.exclusionshowList.observe(viewLifecycleOwner, Observer {
            it?.let { exclusionList ->

                productAdapter = ProductExclusionAdapter(
                    options = displayItemList!!,
                    listerner = this,
                    exclusionList =exclusionList
                )
                binding?.apply {
                    othersRecyle.apply {
                        adapter = productAdapter
                        othersRecyle.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                    }
                }



            }
        })




    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SelectItemFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelectItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemAdd(photo: DisplayItem, id: String) {
        Log.d("SelectItemFragmetn","onItemAdd")
        Snackbar.make(binding.root,"Item Addeded", Snackbar.LENGTH_LONG).show()
    }

    override fun onItemDelete(photo: DisplayItem, id: String) {
        Log.d("SelectItemFragmetn","onItemDelete")
        Snackbar.make(binding.root,"Item Deleted", Snackbar.LENGTH_LONG).show()
    }
}