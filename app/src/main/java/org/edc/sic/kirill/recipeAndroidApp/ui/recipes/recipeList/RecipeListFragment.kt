package org.edc.sic.kirill.recipeAndroidApp.ui.recipes.recipeList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.edc.sic.kirill.recipeAndroidApp.Constants
import org.edc.sic.kirill.recipeAndroidApp.R
import org.edc.sic.kirill.recipeAndroidApp.databinding.FragmentRecipeListBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.edc.sic.kirill.recipeAndroidApp.model.Recipe

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    private val recipeListViewModel: RecipeListViewModel by viewModels()

    private val args: RecipeListFragmentArgs by navArgs()
    private val recipeListAdapter = RecipeListAdapter()
    private var _binding: FragmentRecipeListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeListBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = args.category
        binding.tvHeaderRecipeList.text = category.title

        val imageHeaderUrl = Constants.IMAGE_URL + category.imageUrl
        loadHeaderImage(imageHeaderUrl)

        recipeListViewModel.loadRecipe(category.id)
        initRecycle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycle() {
        binding.rvRecipes.adapter = recipeListAdapter
        recipeListAdapter.setOnItemClickListener(object : RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
        recipeListViewModel.recipeListLiveData.observe(viewLifecycleOwner) { recipeState ->
            if (recipeState.isError) {
                Toast.makeText(context, getString(R.string.toast_error_message), Toast.LENGTH_SHORT)
                    .show()
            } else {
                updateAdapter(recipeState.recipeList)
            }
        }
    }

    private fun loadHeaderImage(imageUrl: String) {
        binding.ivFragmentRecipeListHeader.setImageDrawable(null)
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivFragmentRecipeListHeader)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapter(recipeList: List<Recipe>) {
        recipeListAdapter.dataSet = recipeList
        recipeListAdapter.notifyDataSetChanged()
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            RecipeListFragmentDirections.actionRecipeListFragmentToRecipeFragment(
                recipeId
            )
        )
    }
}