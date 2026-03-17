package com.dshelper.app.domain.usecase.post

import com.dshelper.app.domain.model.PostDetail
import com.dshelper.app.domain.repository.PostRepository
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String): Result<PostDetail> {
        return postRepository.getPostDetail(postId)
    }
}
// claude 기준 GetPostDetailUseCase까지 함