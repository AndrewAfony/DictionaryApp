package com.myapp.dictionaryapp.feature_dictionary.domain.use_cases

import com.myapp.dictionaryapp.core.util.Resource
import com.myapp.dictionaryapp.feature_dictionary.domain.model.WordInfo
import com.myapp.dictionaryapp.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWordInfoUseCase @Inject constructor(
    private val repository: WordInfoRepository
) {
    operator fun invoke(word: String): Flow<Resource<List<WordInfo>>> {
        if (word.isBlank()) {
            return flow {}
        }
        return repository.getWordInfo(word)
    }
}