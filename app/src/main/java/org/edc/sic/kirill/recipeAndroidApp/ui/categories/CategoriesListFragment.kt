package org.edc.sic.kirill.recipeAndroidApp.ui.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.edc.sic.kirill.recipeAndroidApp.Constants
import org.edc.sic.kirill.recipeAndroidApp.R
import org.edc.sic.kirill.recipeAndroidApp.databinding.FragmentListCategoriesBinding
import org.edc.sic.kirill.recipeAndroidApp.model.Category
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesListFragment : Fragment() {

    private val categoriesListViewModel: CategoriesListViewModel by viewModels()
    private val categoriesListAdapter = CategoriesListAdapter()
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.e("CategoriesListFragment", "create")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycle()
    }

    override fun onDestroyView() {
        Log.e("CategoriesListFragment", "destroy")
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycle() {
        categoriesListViewModel.loadCategories()
        val recyclerView: RecyclerView = binding.rvCategories
        recyclerView.adapter = categoriesListAdapter
        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(category: Category) {
                val imageHeaderUrl = Constants.IMAGE_URL + category.imageUrl
                loadImageHeader(imageHeaderUrl)
                openRecipesByCategoryId(category)
            }
        })
        categoriesListViewModel.categoriesListLiveData.observe(viewLifecycleOwner) { categoryState ->
            if (categoryState.isError) {
                Toast.makeText(context, getString(R.string.toast_error_message), Toast.LENGTH_SHORT)
                    .show()
            } else {
                updateAdapter(categoryState.categories)
            }
        }
    }

    private fun loadImageHeader(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivFragmentListCategoriesHeader)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapter(categories: List<Category>) {
        categoriesListAdapter.dataSet = categories
        categoriesListAdapter.notifyDataSetChanged()
    }

    private fun openRecipesByCategoryId(category: Category) {
        findNavController().navigate(
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipeListFragment(
                category
            )
        )
    }
}