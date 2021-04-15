package com.example.radiusagent.ui

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.radiusagent.R
import com.example.radiusagent.databinding.FragmentFacilitesBinding
import com.example.radiusagent.pojo.*
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FacilitesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FacilitesFragment : Fragment(), ViewPagerAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //private var binding: FragmentProductsBinding? = null
    private var _binding: FragmentFacilitesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductsViewModel by activityViewModels()
  //  private val viewModel by viewModels<ProductsViewModel>()
    private var displayValue: DisplayItem? = null
    private var displayItemList = arrayListOf<DisplayItem>()
    private var totalDisplayItem = arrayListOf<DisplayItem>() //global value
    val viewPagerAdapter = ViewPagerAdapter(arrayListOf(), this)
    private lateinit var facilites: Facilites
    private  var exclusions: ArrayList<ArrayList<Exclusion>>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            displayValue = it.getParcelable("displayItem")
            totalDisplayItem = it.getParcelableArrayList("displayItemTrack")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFacilitesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FacilitesFragment", "onViewCreated")
        binding?.apply {
            propertyRecycle.apply {
                adapter = viewPagerAdapter
                propertyRecycle.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            }
        }


        binding.floatingActionButton.setOnClickListener {
            //viewModel.addToDisplayList(displayItem)
            if(exclusions!=null)
            viewModel.getExclusionItem(exclusions)
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            val bundle = Bundle()
            bundle.putParcelableArrayList("displayItemList", totalDisplayItem)
           // bundle.putParcelableArrayList("exclusionList", exclusions)

            val fragment = SelectItemFragment()
            fragment.arguments = bundle
            ft.replace(R.id.frame, fragment, "displayItem")
            ft.commit()
            ft.addToBackStack("displayItem")

        }
        viewModel.getDataFromDb()
        viewModel.updateBackGroundData()
        //viewModel.getFacilites()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
            if(displayValue!=null )
            {
                displayItemList.add(displayValue!!)
            }
        viewModel.displayItemValue.observe(requireActivity(), Observer {
          totalDisplayItem!!.add(it!!)
        })
       viewModel.displayItem.observe(requireActivity(), Observer {
           this.totalDisplayItem=it
       })

        viewModel.faciltyItem.observe(viewLifecycleOwner, Observer {
           it?.let { resource ->
               when(resource.status)
               {
                   Status.SUCCESS ->
                   {
                      val list= it.data
                       Log.d("FacilitiesFragment","facilty Size ${list!!.size}")

                       Log.d("FacilitiesFragment","faciltyItem Observer onSuccessCalled")
                       //first time on load
                       Log.d("Facilites", "name" + list.get(1).name)
                       binding.progressBar.visibility=View.GONE
                       viewPagerAdapter.apply {
                           addOption(list.toMutableList())
                           notifyDataSetChanged()
                       }
                     //  Toast.makeText(requireActivity(), "Success", Toast.LENGTH_LONG).show()
                   }

                       Status.ERROR ->
                       {
                           Log.d("FacilitiesFragment","Error")
                           viewModel.getFacilites()
                           binding.progressBar.visibility=View.VISIBLE

                         //  Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                       }

                   Status.LOADING ->
                   {
                       Log.d("FacilitiesFragment","Loading")
                       binding.progressBar.visibility=View.VISIBLE
                       //Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
                   }

               }
           }
        })


        viewModel.exclusionItem.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility=View.GONE
                        exclusions = it.data!!
                        //first time on load
                       // Toast.makeText(requireActivity(), "SuccessCalled", Toast.LENGTH_LONG).show()
                        Log.d("FacilitiesFragment","exclusionItem Observer onSuccessCalled")
                        Log.d("FacilitiesFragment","exclusionItem List ${exclusions!!.size}")

                      /*  val list = facilites.exclusions;
                        if (list != null) {

                            viewModel.createExclusionList(list)
                        }*/

//                     viewModel.createExclusionList(facilites.exclusions)

                        //  println("Success Call  ${it.data.}")
                        //initView(it.data)
                        // tweets_recycler_view.visibility = View.VISIBLE
                        //progress_bar.visibility = View.GONE
                        // error_image.visibility = View.GONE
                        // resource.data?.let { tweets -> retrieveList(tweets) }
                    }
                    Status.ERROR -> {
                        //  println("MovieDetailsFragment ${it.message}")
                        //  tweets_recycler_view.visibility = View.VISIBLE
                        // progress_bar.visibility = View.GONE
                        //error_image.visibility = View.VISIBLE
                        ///someFunction

                    }
                    Status.LOADING -> {
                              binding.progressBar.visibility=View.VISIBLE
                        //  progress_bar.visibility = View.VISIBLE
                        // tweets_recycler_view.visibility = View.GONE
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
         * @return A new instance of fragment FacilitesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FacilitesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(options: List<Options>, id: String) {
        val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        val bundle = Bundle()
        val arrayList = ArrayList<Options>(options)
        bundle.putParcelableArrayList("optionDetails", arrayList)
        bundle.putParcelableArrayList("displayValue",displayItemList)
        //this the global list to track
        bundle.putParcelableArrayList("totalDisplay",totalDisplayItem)
        bundle.putString("id", id)
        val fragment = OthersFragment()
        fragment.arguments = bundle
        ft.replace(R.id.frame, fragment, "optionDetails")
        ft.commit()
        ft.addToBackStack("Options")
    }
}