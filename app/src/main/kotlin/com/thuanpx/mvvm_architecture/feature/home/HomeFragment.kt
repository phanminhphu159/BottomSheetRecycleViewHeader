package com.thuanpx.mvvm_architecture.feature.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
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
    private var topPeekHeight: Float = 0F
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
        setUpBottomSheetBehavior()
        initRecyclerViewTab()
        initRecyclerViewContent()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpBottomSheetBehavior() {
        BottomSheetBehavior.from(viewBinding.bottomSheetParent.root).peekHeight = 220
        BottomSheetBehavior.from(viewBinding.bottomSheetParent.root)
            .setBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                    Log.e("onStateChanged", "onStateChanged:$newState")
                    with(BottomSheetBehavior.from(viewBinding.bottomSheetParent.root)) {
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                            state = stateBottomSheet()
                        }
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                            state = stateBottomSheet()
                        }
                        if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
//                            state = stateBottomSheet()
                        }
                    }
                }

                override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                    topPeekHeight = slideOffset
                    Log.e("onSlide", topPeekHeight.toString())
                    if
                    BottomSheetBehavior.from(viewBinding.bottomSheetParent.root).state = stateBottomSheet()
                }
            })

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

    private fun stateBottomSheet() : Int {
        return if (topPeekHeight <  0.25F ) {
            BottomSheetBehavior.STATE_COLLAPSED
        } else if (topPeekHeight > 0.75F ) {
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_HALF_EXPANDED
        }
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

    private fun initRecyclerViewContent() {
        bottomSheetAdapter = BottomSheetAdapter()
        viewBinding.bottomSheetParent.rvBottomSheetContent.initRecyclerViewAdapter(
            bottomSheetAdapter,
            GridLayoutManager(requireContext(), 1),
            true
        )
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
