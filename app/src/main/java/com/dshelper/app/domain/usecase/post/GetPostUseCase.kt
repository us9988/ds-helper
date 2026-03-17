package com.dshelper.app.domain.usecase.post

import com.dshelper.app.domain.repository.PostListResult
import com.dshelper.app.domain.repository.PostRepository
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        page: Int = 0,
        sort: String = "desc",
        sortBy: String = "createdAt"
    ): Result<PostListResult> {
        return postRepository.getPosts(
            page = page,
            size = 10,
            sort = sort,
            sortBy = sortBy
        )
    }
}