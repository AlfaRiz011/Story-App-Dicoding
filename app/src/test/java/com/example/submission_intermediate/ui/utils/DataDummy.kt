package com.example.submission_intermediate.ui.utils

import com.example.submission_intermediate.service.response.StoryDB
import com.example.submission_intermediate.service.response.StoryResponse

object DataDummy {
    fun generateDummyStories(): StoryResponse{
        val listStory = ArrayList<StoryDB>()
        for (i in 1..20) {
            val story = StoryDB(
                id = "id_$i",
                name = "Name $i",
                description = "Description $i",
                createdAt = "2022-02-22T22:22:22Z",
                lat = null,
                lon = null,
                photoUrl = "https://examplephotourl/$i/oke"
            )
            listStory.add(story)
        }
        return StoryResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }

    fun generateDummyToken() : String {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXNHamQzZWx0Wk1zckl1M3IiLCJpYXQiOjE2NTcyMTc2NjV9.ZlZaTNeZX3Db4KYwTkIaiUTBy5oX-3DkSmlSnpSuZws"
    }
}