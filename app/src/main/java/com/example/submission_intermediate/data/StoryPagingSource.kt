package com.example.submission_intermediate.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submission_intermediate.service.api.ApiService
import com.example.submission_intermediate.service.response.StoryDB

class StoryPagingSource(private val apiService: ApiService, private val token: String) : PagingSource<Int, StoryDB>() {
    override fun getRefreshKey(state: PagingState<Int, StoryDB>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryDB> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoryList(token, position, params.loadSize)
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}