package org.edc.sic.kirill.recipeAndroidApp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.edc.sic.kirill.recipeAndroidApp.Constants
import org.edc.sic.kirill.recipeAndroidApp.R
import org.edc.sic.kirill.recipeAndroidApp.databinding.FragmentRecipeBinding
import org.edc.sic.kirill.recipeAndroidApp.model.Ingredient
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()

    private val args: RecipeFragmentArgs by navArgs()
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    private val ingredientsAdapter = IngredientsAdapter()
    private val methodAdapter = MethodAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        Log.e("RecipeFragment", "created")
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeViewModel.resetPortionCount()
        recipeViewModel.loadRecipe(args.recipeId)
        initUI()
    }

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.e("RecipeFragment", "destroy")
    }

    private fun initUI() {
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter

        val sizeInDp =
            resources.getDimensionPixelSize(R.dimen.cardview_item_ingredient_divider_horizontal_indent)
        val itemDecorationIngredient = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        val itemDecorationMethod =
            MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )

        binding.rvIngredients.addItemDecoration(itemDecorationIngredient)
        binding.rvMethod.addItemDecoration(itemDecorationMethod)
        itemDecorationIngredient.apply {
            isLastItemDecorated = false
            dividerInsetStart = sizeInDp
            dividerInsetEnd = sizeInDp
            setDividerColorResource(
                requireContext(),
                R.color.cardview_item_ingredient_divider_color
            )
        }
        itemDecorationMethod.apply {
            isLastItemDecorated = false
            dividerInsetStart = sizeInDp
            dividerInsetEnd = sizeInDp
            setDividerColorResource(
                requireContext(),
                R.color.cardview_item_ingredient_divider_color
            )
        }
        val portionSeekBarListener = PortionSeekBarListener { progress ->
            recipeViewModel.updatingPortionCount(progress)
        }
        binding.seekBar.setOnSeekBarChangeListener(portionSeekBarListener)
        binding.ibFavoriteRecipe.setOnClickListener {
            recipeViewModel.onFavoritesClicked()
        }
        recipeViewModel.recipeLiveData.observe(viewLifecycleOwner) { recipeState ->
            recipeState?.let {
                if (it.isError) {
                    Toast.makeText(context, getString(R.string.toast_error_message), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    with(binding) {
                        tvRecipeHeader.text = recipeState.recipe?.title
                        val imageHeaderUrl =
                            Constants.IMAGE_URL + recipeState.recipe?.imageUrl
                        loadImageHeader(imageHeaderUrl)

                        ibFavoriteRecipe.setImageResource(
                            if (!recipeState.isFavorite) R.drawable.ic_heart_empty else R.drawable.ic_heart
                        )
                        recipeState.recipe?.let { recipe ->
                            updateIngredientAdapter(recipe.ingredients)
                            updateMethodAdapter(recipe.method)
                        }
                        ingredientsAdapter.updateIngredients(recipeState.portionCount)
                        tvNumberOfPortions.text = recipeState.portionCount.toString()
                    }
                }
            }
        }
    }

    private fun loadImageHeader(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivFragmentRecipeHeader)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateIngredientAdapter(ingredients: List<Ingredient>) {
        ingredientsAdapter.dataSet = ingredients
        ingredientsAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateMethodAdapter(descriptions: List<String>) {
        val descriptionWithIndex = descriptions.mapIndexed { index, description ->
            "${index + 1}. $description"
        }
        methodAdapter.dataSet = descriptionWithIndex
        methodAdapter.notifyDataSetChanged()
    }
}