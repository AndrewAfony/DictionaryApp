package com.myapp.dictionaryapp.feature_dictionary.data.repository

import com.myapp.dictionaryapp.core.util.Resource
import com.myapp.dictionaryapp.feature_dictionary.data.local.WordInfoDao
import com.myapp.dictionaryapp.feature_dictionary.data.remote.DictionaryApi
import com.myapp.dictionaryapp.feature_dictionary.domain.model.WordInfo
import com.myapp.dictionaryapp.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: WordInfoDao
): WordInfoRepository {

    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())

        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() } // cached words
        emit(Resource.Loading(data = wordInfos))

        try {
            val remoteWordInfos = api.getWordInfo(word)
            dao.deleteWordInfos(remoteWordInfos.map { it.word } )
            dao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() } )
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "Ops, something went wrong!",
                data = wordInfos
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server!",
                data = wordInfos
            ))
        }

        val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfos))
    }

}