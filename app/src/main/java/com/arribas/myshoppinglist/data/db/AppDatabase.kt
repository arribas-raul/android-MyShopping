package com.arribas.myshoppinglist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.arribas.myshoppinglist.data.dao.ArticleCategoryDao
import com.arribas.myshoppinglist.data.dao.ArticleDao
import com.arribas.myshoppinglist.data.dao.ArticleShopDao
import com.arribas.myshoppinglist.data.dao.CategoryDao
import com.arribas.myshoppinglist.data.dao.ShoplistArticleDao
import com.arribas.myshoppinglist.data.dao.ShoplistDao
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleCategory
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.model.Category
import com.arribas.myshoppinglist.data.model.Shoplist
import com.arribas.myshoppinglist.data.model.ShoplistArticle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(
    entities = [
        Article::class,
        ArticleShop::class,
        Category::class,
        ArticleCategory::class,
        Shoplist::class,
        ShoplistArticle::class
    ],
    version = 1,
    exportSchema = false)

abstract class AppDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun articleShopDao(): ArticleShopDao
    abstract fun categoryDao(): CategoryDao
    abstract fun articleCategoryDao(): ArticleCategoryDao
    abstract fun shoplistDao(): ShoplistDao
    abstract fun shoplistArticleDao(): ShoplistArticleDao




    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(roomCallback)
                .build()
                .also { Instance = it }
            }
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                /*Executors.newSingleThreadExecutor().execute {
                    CoroutineScope(Dispatchers.IO).launch {
                        createCategories()
                        createArticles()
                        createArticleCategories()
                    }
                }*/
            }
        }

        private suspend fun createCategories(){
            val dao = Instance?.categoryDao()

            val categories = listOf(
                Category(id = 1, name = "Lácteos"),
                Category(id = 2, name = "Margarinas y Aceites"),
                Category(id = 3, name = "Galletas y Dulces"),
                Category(id = 4, name = "Bebidas"),
                Category(id = 5, name = "Huevos"),
                Category(id = 6, name = "Carnes"),
                Category(id = 7, name = "Frutas"),
                Category(id = 8, name = "Verduras"),
                Category(id = 9, name = "Panadería y Pastelería"),
                Category(id = 10, name = "Pescados"),
                Category(id = 11, name = "Limpieza"),
                Category(id = 12, name = "Condimentos"),
                Category(id = 13, name = "Quesos"),
                Category(id = 14, name = "Cafés y Infusiones"),
                Category(id = 15, name = "Pizzas y Pastas"),
                Category(id = 16, name = "Embutidos"),
                Category(id = 17, name = "Carne Picada")
            )

            dao?.insert(categories)
        }

        private suspend fun createArticles(){
            val dao = Instance?.articleDao()

            val articles = listOf(
                Article (1, "Yogur líquido de fresa", false),
                Article (2,"Yogur de fresa sin lactosa", false),
                Article (3, "Leche semidesnatada sin lactosa", false),
                Article (4, "Leche entera", false),
                Article (5, "Margarina", false),
                Article (6, "Aceite de girasol tapa amarilla", false),
                Article (7, "Galleta cookies con trozos de chocolate", false),
                Article (8,"Galleta María", false),
                Article (9,"Croissants rellenos surtidos (4 cacao - 4 blanco)", false),
                Article (10,"Croissant mantequilla mini horno", false),
                Article (11,"Cerveza shandy con limón sin alcohol", false),
                Article (12,"Néctar de maracuyá", false),
                Article (13,"Zumo de piña y uva", false),
                Article (14,"Zumo de naranja exprimido 1falsefalse%", false),
                Article (15,"Zumo de naranja exprimido 1falsefalse%", false),
                Article (16,"Agua mineral natural", false),
                Article (17,"Cerveza rubia clásica ***Bajada PVP Canarias***", false),
                Article (18,"Huevo grande L", false),
                Article (19,"Pollo alas partidas fresco", false),
                Article (20,"Pollo alas adobadas frescas", false),
                Article (21,"Longaniza magra fresca", false),
                Article (22,"Cerdo costilla carnosa troceada fresca", false),
                Article (23,"Carne picada mixta (ternera/cerdo) para hamburguesas fresca", false),
                Article (24,"Cereza", false),
                Article (25,"Mango", false),
                Article (26,"Mandarina", false),
                Article (27,"Plátano de Canarias IGP", false),
                Article (28,"Lima dulce", false),
                Article (29,"Cebolla Figueres", false),
                Article (30,"Patata", false),
                Article (31,"Pan barra rústica", false),
                Article (32,"Lluc congelado filete empanado", false),
                Article (33,"Lluc congelado filete sin piel del cap", false),
                Article (34,"Limpiacalzado autoaplicador blanco", false),
                Article (35,"Lejía detergente limón", false),
                Article (36, "Brazalete citronela talla S", false),
                Article (37,"Queso dulce en lonchas sándwich", false),
                Article (38,"Queso rallado pizza", false),
                Article (39,"Queso barra en lonchas sin lactosa", false),
                Article (40,"Café molido espresso mezcla Nº4", false),
                Article (41,"Pizza fresca calzone jamón y queso", false),
                Article (42,"Tortellini carne", false),
                Article (43,"Macarrones pasta", false),
                Article (44,"Fuet espetec extra pieza", false),
                Article (45,"Mortadela en lonchas", false),
                Article (46,"Carne picada mixta (ternera/cerdo) para hamburguesas fresca", false),
                Article (47,"Lluc congelado filete empanado", false),
                Article (48,"Lluc congelado filete sin piel del cap", false)
            )

            dao?.insert(articles)
        }

        private suspend fun createArticleCategories(){
            val dao = Instance?.articleCategoryDao()

            val articleCategories = listOf(
                ArticleCategory (1,1, 1),
                ArticleCategory (2,2, 1),
                ArticleCategory (3,3, 1),
                ArticleCategory (4,4, 1),
                ArticleCategory (5,5, 2),
                ArticleCategory (6,6, 2),
                ArticleCategory (7,7, 3),
                ArticleCategory (8,8, 3),
                ArticleCategory (9,29, 3),
                ArticleCategory (10,30, 3),
                ArticleCategory (11,9, 4),
                ArticleCategory (12,10, 4),
                ArticleCategory (13,11, 4),
                ArticleCategory (14,12, 4),
                ArticleCategory (15,13, 4),
                ArticleCategory (16,14, 4),
                ArticleCategory (17,15, 4),
                ArticleCategory (18,16, 5),
                ArticleCategory (19,17, 6),
                ArticleCategory (20,18, 6),
                ArticleCategory (21,19, 6),
                ArticleCategory (22,20, 6),
                ArticleCategory (23,21, 7),
                ArticleCategory (24,22, 7),
                ArticleCategory (25,23, 7),
                ArticleCategory (26,24, 7),
                ArticleCategory (27,25, 7),
                ArticleCategory (28,32, 8),
                ArticleCategory (29,33, 8),
                ArticleCategory (30,34, 9),
                ArticleCategory (31,37, 10),
                ArticleCategory (32,8, 10),
                ArticleCategory (33,39, 11),
                ArticleCategory (34,40, 11),
                ArticleCategory (35,41, 12),
                ArticleCategory (36,42, 13),
                ArticleCategory (37,43, 13),
                ArticleCategory (38,44, 13),
                ArticleCategory (39,45, 14),
                ArticleCategory (40,46, 15),
                ArticleCategory (41,47, 15),
                ArticleCategory (42,48, 15)
            )

            dao?.insert(articleCategories)
        }
    }


}