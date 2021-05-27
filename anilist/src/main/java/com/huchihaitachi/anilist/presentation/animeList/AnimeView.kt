package com.huchihaitachi.anilist.presentation.animeList

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.airbnb.epoxy.ModelProp
import com.huchihaitachi.anilist.R
import com.huchihaitachi.base.setUrl

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class AnimeView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
  private lateinit var nameTv: TextView
  private lateinit var coverIv: ImageView

  init {
    inflate(context, R.layout.item_anime, this)
    nameTv = findViewById(R.id.name_tv)
    coverIv = findViewById(R.id.thumbnail_iv)
  }

  var name: CharSequence
    get() = nameTv.text
    @TextProp set(value) {
      nameTv.text = value
    }

  @ModelProp
  fun setCover(url: String) = coverIv.setUrl(url)
}