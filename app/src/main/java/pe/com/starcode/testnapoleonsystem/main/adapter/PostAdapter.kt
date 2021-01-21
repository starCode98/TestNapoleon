package pe.com.starcode.testnapoleonsystem.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pe.com.starcode.testnapoleonsystem.R
import pe.com.starcode.testnapoleonsystem.base.room.Post
import pe.com.starcode.testnapoleonsystem.databinding.ItemPostBinding

class PostAdapter (private val list: List<Post>, private val listener:PostListener) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            DataBindingUtil.inflate<ItemPostBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_post, parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.itemPostBinding.itempost = list[position]
        holder.itemPostBinding.cardView.setOnClickListener {
            listener.onclick(list[position])
        }
    }

    fun updateItem(post:Post){
        var p = list.indexOf(post)
        notifyItemChanged(p)
    }

    inner class PostViewHolder(var itemPostBinding: ItemPostBinding) :
        RecyclerView.ViewHolder(itemPostBinding.root) {
    }
}