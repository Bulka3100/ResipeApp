package org.edc.sic.kirill.recipeAndroidApp.ui.recipes.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.edc.sic.kirill.recipeAndroidApp.R
import org.edc.sic.kirill.recipeAndroidApp.databinding.FragmentFavoritesBinding
import org.edc.sic.kirill.recipeAndroidApp.ui.recipes.recipeList.RecipeListAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.edc.sic.kirill.recipeAndroidApp.model.Recipe

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    private val favoriteAdapter = RecipeListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.e("FavoriteFragment", "created")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        binding.rvFavorites.adapter = favoriteAdapter
        favoriteViewModel.favoriteLiveData.observe(viewLifecycleOwner) { recipeState ->
            if (recipeState.isError) {
                Toast.makeText(context, getString(R.string.toast_error_message), Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding.tvEmptyText.isVisible = recipeState.recipeList.isEmpty()
                updateAdapter(recipeState.recipeList)
                favoriteAdapter.setOnItemClickListener(object :
                    RecipeListAdapter.OnItemClickListener {
                    override fun onItemClick(recipeId: Int) {
                        Log.e("!!!", "initRecycler $recipeId")
                        openRecipe(recipeId)
                    }
                })
            }
        }
        favoriteViewModel.loadFavorites()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapter(recipeList: List<Recipe>) {
        favoriteAdapter.dataSet = recipeList
        favoriteAdapter.notifyDataSetChanged()
    }

    private fun openRecipe(recipeId: Int) {
        findNavController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(
                recipeId
            )
        )
    }
}