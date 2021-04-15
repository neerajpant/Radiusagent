package com.example.radiusagent.ui

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.radiusagent.R
import com.example.radiusagent.databinding.FragmentFacilitesBinding
import com.example.radiusagent.databinding.FragmentOthersBinding
import com.example.radiusagent.pojo.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OthersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class OthersFragment : Fragment(), ProductAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //private var binding: FragmentProductsBinding? = null
    private var _binding: FragmentOthersBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProductsViewModel>()
    private lateinit var productAdapter: ProductAdapter;
    private lateinit var facilites: Facilites
    private var options :ArrayList<Options>?=null
    private var facilty= arrayListOf<Facilty>()
    private var exclusion = arrayListOf<Exclusion>()
    private var displayItemList =arrayListOf<DisplayItem>()
    private var displayItemTrackList :ArrayList<DisplayItem>?=null
    private var displayItem :DisplayItem?=null
    private var id: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            options = it.getParcelableArrayList("optionDetails")
        //    displayItemTrackList = it.getParcelableArrayList("displayValue")
            //global list to track
            displayItemTrackList = it.getParcelableArrayList("totalDisplay")
            id = it.getString("id")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOthersBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productAdapter = ProductAdapter(options = options!!, listerner = this)
        binding?.apply {
            othersRecyle.apply {
                adapter = productAdapter
                othersRecyle.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            }
        }
        binding.choose.setOnClickListener {
            if(exclusion!!.size>1 ||displayItemList!!.size>1 )
            {
                //remove the item
                Toast.makeText(requireActivity(),"You can choose only one item",Toast.LENGTH_LONG).show()
                exclusion!!.clear()
                displayItemList!!.clear()
            }
            else
            {
                if(displayItemTrackList!=null)
                {
                    displayItemTrackList!!.add(displayItem!!)
                    viewModel.trackDisplayItem(displayItemTrackList)
                }
                //first time adding the item
                if(displayItemTrackList==null && displayItem!=null)
                {
                    displayItemTrackList= arrayListOf<DisplayItem>()
                    displayItemTrackList!!.add(displayItem!!)
                }

                viewModel.addToDisplayList(displayItem!!)

               // viewModel.addToDisplayList(displayItem = displayItemList.get(0))
                val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
                val bundle = Bundle()
                bundle.putParcelable("displayItem", displayItem)
                bundle.putParcelableArrayList("displayItemTrack", displayItemTrackList)
                bundle.putString("id",id)
                val fragment = FacilitesFragment()
               fragment.arguments = bundle
                ft.replace(R.id.frame, fragment, "ExclusionList")
                ft.commit()

            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OthersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OthersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemAdd(photo: Options, productId: String) {

        exclusion?.add(Exclusion(options_id = photo.id,facility_id = id!!))
        displayItem=DisplayItem(options_id = photo.id,facility_id = id!!,name = photo.name,icon = photo.icon)
        displayItemList?.add(displayItem!!)
        viewModel.addToExclusionList(Exclusion(options_id = photo.id,facility_id = id!!))


    }

    override fun onItemDelete(option: Options, id: String) {
        Log.d("OthersFragment","onItemDelete")
         displayItem=null
        Snackbar.make(binding.root,"Item Deleted", Snackbar.LENGTH_LONG).show()
    }
}