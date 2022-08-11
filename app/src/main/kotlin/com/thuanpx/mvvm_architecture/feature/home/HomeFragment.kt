package com.thuanpx.mvvm_architecture.feature.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.thuanpx.ktext.context.launchAndRepeatWithViewLifecycle
import com.thuanpx.ktext.recyclerView.initRecyclerViewAdapter
import com.thuanpx.mvvm_architecture.R
import com.thuanpx.mvvm_architecture.base.fragment.BaseFragment
import com.thuanpx.mvvm_architecture.databinding.FragmentHomeBinding
import com.thuanpx.mvvm_architecture.feature.home.bottomsheet.BottomSheetAdapter
import com.thuanpx.mvvm_architecture.feature.home.bottomsheet.CategoryAdapter
import com.thuanpx.mvvm_architecture.model.entity.ItemCatergory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(HomeViewModel::class),
    CategoryAdapter.OnItemClickedListener {

    private var categoryAdapter: CategoryAdapter? = null
    private var bottomSheetAdapter: BottomSheetAdapter? = null
    private val listCategory: ArrayList<ItemCatergory> = arrayListOf()
    private val listTabTitles =
        arrayListOf(
            R.string.All,
            R.string.FoodAndDrinks,
            R.string.BeautyAndSpas,
            R.string.ThingsToDo,
            R.string.Services,
            R.string.Shopping,
            R.string.Education,
            R.string.PlacesToStay,
            R.string.Repairs,
            R.string.HomeAndGarden,
            R.string.Pets,
            R.string.Jobs,
            R.string.RealEstate,
            R.string.HealthCare
        )
    private val listTabIcons =
        arrayListOf(
            R.drawable.ic_all,
            R.drawable.ic_food_and_drinks,
            R.drawable.ic_beauty_and_spas,
            R.drawable.ic_things_to_do,
            R.drawable.ic_services,
            R.drawable.ic_shoppping,
            R.drawable.ic_education,
            R.drawable.ic_places_to_stay,
            R.drawable.ic_repairs,
            R.drawable.ic_home_and_garden,
            R.drawable.ic_pets,
            R.drawable.ic_jobs,
            R.drawable.ic_real_estate,
            R.drawable.ic_health_care
        )
//    private val listCategory: ArrayList<ItemCatergory> = arrayListOf(
//        ItemCatergory(
//            resources.getString(R.string.All),
//            R.drawable.ic_all,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.FoodAndDrinks),
//            R.drawable.ic_food_and_drinks,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.BeautyAndSpas),
//            R.drawable.ic_beauty_and_spas,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.ThingsToDo),
//            R.drawable.ic_things_to_do,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.Services),
//            R.drawable.ic_services,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.Shopping),
//            R.drawable.ic_shoppping,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.Education),
//            R.drawable.ic_education,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.PlacesToStay),
//            R.drawable.ic_places_to_stay,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.Repairs),
//            R.drawable.ic_repairs,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.HomeAndGarden),
//            R.drawable.ic_home_and_garden,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.Pets),
//            R.drawable.ic_pets,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.Jobs),
//            R.drawable.ic_jobs,
//            false//        ),
//        ItemCatergory(
//            resources.getString(R.string.RealEstate),
//            R.drawable.ic_real_estate,
//            false
//        ),
//        ItemCatergory(
//            resources.getString(R.string.HealthCare),
//            R.drawable.ic_health_care,
//            false
//        )
//    )

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initialize() {
        initBottomSheet()
    }

    override fun onSubscribeObserver() {
        super.onSubscribeObserver()
        launchAndRepeatWithViewLifecycle {
            with(viewModel) {
                launchAndRepeatWithViewLifecycle {
                    launch {
                        pagingItemFlow.collectLatest {
                            bottomSheetAdapter?.submitData(it)
                        }
                    }
                }
            }
        }
    }

    override fun onItemClicked(category: ItemCatergory, newPosition: Int, oldPosition: Int) {
        categoryAdapter?.getData()?.get(oldPosition)?.apply {
            this.isSelected = !isSelected
        }
        categoryAdapter?.notifyItemChanged(oldPosition)
        category.isSelected = !category.isSelected
        categoryAdapter?.notifyItemChanged(newPosition)
    }

    private fun initBottomSheet() {
        BottomSheetBehavior.from(viewBinding.bottomSheetParent.root).peekHeight = 220
        initRecyclerViewTab()
        initRecyclerViewContent()
    }

    private fun initRecyclerViewTab() {
        setUpCategory()
        categoryAdapter = CategoryAdapter(this)
        categoryAdapter?.addData(listCategory)
        viewBinding.bottomSheetParent.rvBottomSheetHeader.initRecyclerViewAdapter(
            categoryAdapter,
            LinearLayoutManager.HORIZONTAL,
            true
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initRecyclerViewContent() {
        bottomSheetAdapter = BottomSheetAdapter()
        viewBinding.bottomSheetParent.rvBottomSheetContent.initRecyclerViewAdapter(
            bottomSheetAdapter,
            GridLayoutManager(requireContext(), 1),
            true
        )
        viewBinding.bottomSheetParent.rvBottomSheetContent.setOnTouchListener(OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->                         // Disallow NestedScrollView to intercept touch events.
                    v.parent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP ->                         // Allow NestedScrollView to intercept touch events.
                    v.parent.requestDisallowInterceptTouchEvent(false)
            }
            // Handle RecyclerView touch events.
            v.onTouchEvent(event)
            true
        })
    }

    private fun setUpCategory() {
        for (i in 0 until listTabTitles.size) {
            listCategory.add(
                ItemCatergory(
                    resources.getString(listTabTitles[i]),
                    listTabIcons[i],
                    false
                )
            )
        }
    }
}
