package com.naeggeodo.presentation.view.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.ViewSizeResolver
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemChatListBinding
import com.naeggeodo.presentation.utils.Util.getTimeDiff
import com.naeggeodo.presentation.utils.Util.getTimeStr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ChatListAdapter(
    private val context: Context,
    private var datas: ArrayList<Chat>,
    private var listener: (pos: Int) -> Unit = {}
) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>(), ImageLoaderFactory {

    val drawableList = hashMapOf<Int, Drawable>()
    val imageLoader by lazy { newImageLoader() }

    inner class ViewHolder(val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_list, parent, false)
        return ViewHolder(ItemChatListBinding.bind(view))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            val prevDate = datas[position].createDate

            val timeDiff = getTimeDiff(prevDate)

            Timber.e("werr ${datas[position].chatId} ${datas[position].idx}")

            title.text = datas[position].title
            time.text = getTimeStr(timeDiff)
            count.text = "인원 ${datas[position].currentCount}명 / ${datas[position].maxCount}명"

            // 이미지를 한번 불러왔다면 또 url로부터 로드하지 않고 사용
            if (drawableList[position] == null) {
                val imgRequest = ImageRequest
                    .Builder(context)
                    .data(datas[position].imgPath)
                    .size(ViewSizeResolver(image))
                    .target { drawable ->
                        CoroutineScope(Dispatchers.Main).launch {
                            image.setImageDrawable(drawable)
                        }
                    }
                    .listener(
                        onSuccess = { _, result ->
                            Timber.e("imageLoad Success. pos:$position")
                            drawableList[position] = result.drawable
                        },
                        onError = { _, result ->
                            Timber.e("imageLoad Fail. pos:$position")
                        }
                    )
                    .build()
                CoroutineScope(Dispatchers.IO).launch {
                    val a = imageLoader.execute(imgRequest)
                    Timber.e("position $position : ${a.request.listener}")
                }

                enterContainer.setOnClickListener {
                    listener(position)
                }
            } else {
                Timber.e("position $position : ${drawableList[position]}")
                image.setImageDrawable(drawableList[position])
            }
        }
    }


    override fun getItemCount() = datas.size
    fun setDatas(chatList: ArrayList<Chat>) {
        clearData()
        datas.addAll(chatList)
        notifyItemRangeInserted(0, chatList.size)
    }

    fun getData(pos: Int) = datas[pos]

    fun clearData() {
        val size = datas.size
        datas.clear()
        drawableList.clear()
        notifyItemRangeRemoved(0, size)
    }


    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

    fun setListener(l : (pos: Int) -> Unit){
        listener = l
    }
}