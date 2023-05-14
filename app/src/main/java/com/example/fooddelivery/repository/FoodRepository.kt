package com.example.fooddelivery.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.fooddelivery.R
import com.example.fooddelivery.data.database.CarteDao
import com.example.fooddelivery.data.database.FavoriteDao
import com.example.fooddelivery.data.database.OrderDao
import com.example.fooddelivery.models.*
import com.example.fooddelivery.utils.Constante.Companion.Category_COLLECTION
import com.example.fooddelivery.utils.Constante.Companion.Food_COLLECTION
import com.example.fooddelivery.utils.Constante.Companion.INCOMPLETE_ORDERS
import com.example.fooddelivery.utils.Constante.Companion.Restaurant_COLLECTION
import com.example.fooddelivery.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FoodRepository @Inject constructor(
    private val firebaseDB: FirebaseFirestore,
    private val favoriteDao: FavoriteDao,
    private val carteDao: CarteDao,
    private val orderDao: OrderDao,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context

) {

    private val errorMessage by lazy { context.getString(R.string.errorMessage) }
    private val userUid by lazy { firebaseAuth.uid!! }

    suspend fun getFoodList(): Resource<ArrayList<Product>> {
        return try {
            var list = firebaseDB.collection(Food_COLLECTION)
                .get()
                .await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(Product::class.java)
                } as ArrayList<Product>
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(msg = errorMessage)

        }
    }


    suspend fun getFoodOfCategory(
        restaurantId: String,
        categoryId: Int
    ): Resource<ArrayList<Product>> {
        return try {
            var list =
                firebaseDB.collection(Food_COLLECTION).whereEqualTo("restaurantId", restaurantId)
                    .whereEqualTo("categoryId", categoryId)
                    .get()
                    .await().documents.mapNotNull { snapShot ->
                        snapShot.toObject(Product::class.java)
                    } as ArrayList<Product>
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(msg = errorMessage)

        }


    }

    suspend fun getProductForYou(
        restaurantId: String,
    ): Resource<ArrayList<Product>> {
        return try {
            var list =
                firebaseDB.collection(Food_COLLECTION).whereEqualTo("restaurantId", restaurantId)
                    .get()
                    .await().documents.mapNotNull { snapShot ->
                        snapShot.toObject(Product::class.java)
                    } as ArrayList<Product>
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(msg = errorMessage)

        }


    }

    suspend fun getRestaurantList(): Resource<ArrayList<Restaurant>> {
        return try {
            var list = firebaseDB.collection(Restaurant_COLLECTION)
                .get()
                .await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(Restaurant::class.java)
                } as ArrayList<Restaurant>
            Resource.Success(list)

        } catch (e: Exception) {
            Resource.Error(msg = errorMessage)
        }
    }

    suspend fun getCategoryList(restaurantId: String): Resource<ArrayList<Category>> {
        return try {
            var list = firebaseDB.collection(Category_COLLECTION)
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(Category::class.java)
                } as ArrayList<Category>
            Resource.Success(list)

        } catch (e: Exception) {
            Resource.Error(msg = errorMessage)
        }
    }


    suspend fun getFillterList(stars: Int, typefood: String): Resource<ArrayList<Product>> {
        return try {
            var list = ArrayList<Product>()
            if (stars.equals(5)) {
                list = firebaseDB.collection(Food_COLLECTION).whereEqualTo("rating", stars)
                    .whereEqualTo("productType", typefood)
                    .get()
                    .await().documents.mapNotNull { snapShot ->
                        snapShot.toObject(Product::class.java)
                    } as ArrayList<Product>

            } else {
                list = firebaseDB.collection(Food_COLLECTION).whereEqualTo("productType", typefood)
                    .get()
                    .await().documents.mapNotNull { snapShot ->
                        snapShot.toObject(Product::class.java)
                    } as ArrayList<Product>
            }

            Resource.Success(list)

        } catch (e: Exception) {
            Resource.Error(msg = errorMessage)
        }
    }

    // change product in database (save or remove) by check if it saved before or not.
    suspend fun saveOrRemoveRestaurantFromFavorite(restaurant: Restaurant) {
        val isSavedBefore = getRemoveRestaurantFromFavorite(restaurant.restaurantId)
        return if (isSavedBefore) {
            favoriteDao.removeRestaurantFromFavorites(restaurant)
        } else {
            favoriteDao.saveRestaurant(restaurant)
        }
    }

    // check if product is saved into favorite database or not .
    suspend fun getRemoveRestaurantFromFavorite(id: String): Boolean {
        val productModel = favoriteDao.getSpecificFavoriteRestaurant(id)
        return productModel != null
    }

    // change food in database (save or remove) by check if it saved before or not.
    suspend fun saveOrRemoveFoodFromFavorite(food: Product) {
        val isSavedBefore = getRemoveFoodFromFavorite(food.productId)
        return if (isSavedBefore) {
            favoriteDao.removeFoodFromFavorites(food)
        } else {
            favoriteDao.saveFood(food)
        }
    }

    // check if food is saved into favorite database or not .
    private suspend fun getRemoveFoodFromFavorite(id: String): Boolean {
        val foodModel = favoriteDao.getSpecificFavoriteFood(id)
        return foodModel != null
    }


    fun getAllFavoriteFoods(): LiveData<List<Product>> = favoriteDao.getAllFavoriteFoods()

    fun getAllFavoriteRestaurant(): LiveData<List<Restaurant>> =
        favoriteDao.getAllFavoriteRestaurant()

    fun getAllFoodCarte(): LiveData<List<Carte>> = carteDao.getAllCarte()
    fun getAllHistoryOrders(): LiveData<List<HistoryOrders>> = orderDao.getAllOrders()


    // observe to specific product when save or not to change favorite icon .
    fun getFoodFromFavoriteLiveData(id: String): LiveData<Product?> =
        favoriteDao.getSpecificFavoriteFoodLiveData(id)

    fun getFavoriteResaurantLiveData(): LiveData<List<Restaurant>> =
        favoriteDao.getAllFavoriteRestaurant()

    fun getSpecificFavoriteRestaurantLiveData(id: String): LiveData<Restaurant?> =
        favoriteDao.getSpecificFavoriteRestaurantLiveData(id)

    fun saveCarte(carteItem: Carte) {
        carteDao.saveCarte(carteItem)
    }
    fun saveOrder(orderModel: HistoryOrders) {
        orderDao.saveOrders(orderModel)
    }

    fun updateCarte(carteItem: Carte) {
        carteDao.updateCarte(carteItem)
    }

    suspend fun deleteCarte(carteItem: Carte) {
        carteDao.removeCarte(carteItem)
    }

    suspend fun deleteOrders(historyOrders: HistoryOrders) {
        orderDao.removeOrder(historyOrders)
    }

    /* after submit the order successfully here we'll add all cart products to incomplete orders to show to admin panel
      and delete it from user cart.
   */
    suspend fun uploadProductsToOrders(
        cartProductsList: Array<Carte>,
        userLocation: String,
        totalCost: Float
    ): Resource<OrderModel> {
        return try {
            val orderCollection = firebaseDB.collection(INCOMPLETE_ORDERS)
            val id = orderCollection.document().id
            val orderModel = OrderModel(
                id,
                userUid,
                System.currentTimeMillis(),
                userLocation,
                OrderEnums.PLACED,
                totalCost,
                cartProductsList.toList()
            )
            orderCollection.document(id).set(orderModel).await()
            Resource.Success(orderModel)
        } catch (e: Exception) {
            Resource.Error(errorMessage)
        }
    }

    suspend fun getUserOrders(): Resource<ArrayList<OrderModel>> {
        return try {
            val result = firebaseDB.collection(INCOMPLETE_ORDERS)
                .whereEqualTo("userUid", userUid)
                .get().await().mapNotNull { snapShot ->
                    snapShot.toObject(OrderModel::class.java)
                } as ArrayList<OrderModel>

            Resource.Success(result)
        } catch (e: Exception) {
            println(">>>>>>>>>>>>>> ${e.message}")
            Resource.Error(errorMessage)
        }
    }

}