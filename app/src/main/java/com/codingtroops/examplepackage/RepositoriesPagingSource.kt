package com.codingtroops.examplepackage

import androidx.paging.*

class RepositoriesPagingSource(
    private val restInterface: RepositoriesApiService = DependencyContainer.repositoriesRetrofitClient
): PagingSource<Int, Repository>(){

    // fetch more items asynchronously
    override suspend fun load(params: LoadParams<Int>):
            LoadResult<Int, Repository>{
        return try {
            val nextPage = params.key ?:1
            val repos = restInterface.getRepositories(nextPage).repos
            LoadResult.Page(
                data = repos,
                prevKey = if (nextPage == 1) null
                else nextPage - 1,
                nextKey = nextPage + 1
            )

        } catch (e: Exception){
            LoadResult.Error(e)
        }

    }

    //obtain the most recent page key in case of a refresh event
    override fun getRefreshKey(
        state:PagingState<Int, Repository>,
    ): Int? {
        return null
    }


}