package com.arribas.myshoppinglist.data.utils

import android.content.Context
import com.arribas.myshoppinglist.R

enum class CrudMessageEnum {
    CREATED_SUCCESS,
    CREATED_ERROR,
    UPDATED_SUCCESS,
    UPDATED_ERROR,
    DELETED_SUCCESS,
    DELETED_ERROR,
    ELEMENT_EXIST
}
class CrudMessageManager(context: Context) {
    private val _context = context

    fun getMessage(key: CrudMessageEnum): String{
        return when(key){
            CrudMessageEnum.CREATED_SUCCESS -> _context.getString(R.string.crud_created_success)
            CrudMessageEnum.CREATED_ERROR   -> _context.getString(R.string.crud_created_error)
            CrudMessageEnum.UPDATED_SUCCESS -> _context.getString(R.string.crud_updated_success)
            CrudMessageEnum.UPDATED_ERROR   -> _context.getString(R.string.crud_updated_error)
            CrudMessageEnum.DELETED_SUCCESS -> _context.getString(R.string.crud_deleted_success)
            CrudMessageEnum.DELETED_ERROR   -> _context.getString(R.string.crud_deleted_error)
            CrudMessageEnum.ELEMENT_EXIST   -> _context.getString(R.string.crud_element_exist)
        }
    }
}