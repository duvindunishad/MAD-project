package com.example.onlineshop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.R
import com.example.onlineshop.activity.activity.CartList
import com.example.onlineshop.firestore.FirestoreClass
import com.example.onlineshop.models.CartItem
import com.example.onlineshop.utils.Constants
import com.example.onlineshop.utils.GlideLoader
import kotlinx.android.synthetic.main.item_cart_layout.view.*

open class CartItemListAdapter (
    private val context: Context,
    private var list: ArrayList<CartItem>,
    private val updateCartItems: Boolean
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_layout,parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image,holder.itemView.iv_cart_item_image)
            holder.itemView.tv_cart_item_title.text = model.title
            holder.itemView.tv_cart_item_price.text = "Rs:${model.price}"
            holder.itemView.tv_cart_quantity.text = model.cart_quantity

            if(model.cart_quantity == "0"){
                holder.itemView.ib_remove_cart_item.visibility = View.GONE
                holder.itemView.ib_add_cart_item.visibility = View.GONE

                if(updateCartItems){
                    holder.itemView.ib_delete_cart_item.visibility = View.VISIBLE
                }
                else{
                    holder.itemView.ib_delete_cart_item.visibility = View.GONE
                }

                holder.itemView.tv_cart_quantity.text = context.resources.getString(R.string.lbl_out_of_stock)
                holder.itemView.tv_cart_quantity.setTextColor(ContextCompat.getColor(context,
                R.color.colorSnackBarError))
            }
            else{
                if (updateCartItems){
                    holder.itemView.ib_remove_cart_item.visibility = View.VISIBLE
                    holder.itemView.ib_add_cart_item.visibility = View.VISIBLE
                    holder.itemView.ib_delete_cart_item.visibility = View.VISIBLE
                }else
                {
                    holder.itemView.ib_remove_cart_item.visibility = View.GONE
                    holder.itemView.ib_add_cart_item.visibility = View.GONE
                    holder.itemView.ib_delete_cart_item.visibility = View.GONE
                }

                holder.itemView.tv_cart_quantity.setTextColor(ContextCompat.getColor(context,
                    R.color.colorDarkGrey))
            }

            holder.itemView.ib_delete_cart_item.setOnClickListener{
                when (context){
                    is CartList ->{
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                }
                FirestoreClass().removeItemFromCart(context, model.id)
            }
            holder.itemView.ib_remove_cart_item.setOnClickListener{

                if (model.cart_quantity == "1"){
                    FirestoreClass().removeItemFromCart(context,model.id)
                }
                else{
                    val cartQuantity: Int = model.cart_quantity.toInt()
                    val itemHashMap = HashMap<String,Any>()
                    itemHashMap[Constants.CART_QUANTITY]=(cartQuantity-1).toString()
                    if (context is CartList){
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                    FirestoreClass().updateMyCart(context,model.id,itemHashMap)
                }
            }
            holder.itemView.ib_add_cart_item.setOnClickListener{

                val cartQuantity: Int = model.cart_quantity.toInt()
                if (cartQuantity < model.stock_quantity.toInt()){
                    val itemHashMap = HashMap<String,Any>()
                    itemHashMap[Constants.CART_QUANTITY]=(cartQuantity + 1).toString()

                    if (context is CartList){
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                    FirestoreClass().updateMyCart(context,model.id,itemHashMap)
                }
                else{
                    if (context is CartList){
                        context.showErrorSnackBar(
                            context.resources.getString(
                                R.string.msg_for_available_stock,model.stock_quantity),true)
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    private class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
}

