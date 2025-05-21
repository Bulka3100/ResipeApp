package org.edc.sic.kirill.recipeAndroidApp.di

import android.content.Context
import androidx.room.Room
import org.edc.sic.kirill.recipeAndroidApp.Constants
import org.edc.sic.kirill.recipeAndroidApp.data.AppDatabase
import org.edc.sic.kirill.recipeAndroidApp.data.CategoriesDao
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.edc.sic.kirill.recipeAndroidApp.data.RecipeApiService
import org.edc.sic.kirill.recipeAndroidApp.data.RecipesDao
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database-categories"
        ).build()

    @Provides
    @Singleton
    fun provideCategoriesDao(appDatabase: AppDatabase) : CategoriesDao = appDatabase.categoryDao()

    @Provides
    @Singleton
    fun provideRecipesDao(appDatabase: AppDatabase) : RecipesDao = appDatabase.recipesDao()

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = Constants.CONTENT_TYPE.toMediaType()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
        return retrofit
    }

    @Provides
    @Singleton
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }
}
