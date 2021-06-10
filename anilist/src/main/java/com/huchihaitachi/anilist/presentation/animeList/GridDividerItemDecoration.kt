package com.huchihaitachi.anilist.presentation.animeList

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State

class GridDividerItemDecoration @JvmOverloads constructor(
  private val width: Float,
  private val height: Float
) : RecyclerView.ItemDecoration() {

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
      parent.getChildAdapterPosition(view).let { itemPosition ->
        (parent.layoutManager as? GridLayoutManager)?.let { layoutManager ->
          if(itemPosition != RecyclerView.NO_POSITION) {
            val spanCount = layoutManager.spanCount
            parent.adapter?.let { adapter ->
              outRect.top = height.toInt()
              outRect.left = when (itemPosition % spanCount) {
                0 -> 0
                else -> width.toInt()
              }
              if (itemPosition > parent.layoutManager!!.childCount - spanCount - 1) {
                outRect.bottom = width.toInt()
              }
            }
          }
        }
      }
  }
}