package com.insoft.nextstep.data.model

import com.insoft.nextstep.domain.usecase.ConnectUser
import com.insoft.nextstep.domain.usecase.GetMessages
import com.insoft.nextstep.domain.usecase.GetMyChats
import com.insoft.nextstep.domain.usecase.GetMyConnections
import com.insoft.nextstep.domain.usecase.SendMessage
import com.insoft.nextstep.domain.usecase.StartChat
import javax.inject.Inject

data class ChatUseCases @Inject constructor(
    val getMyChats: GetMyChats,
    val startChat: StartChat,
    val getMessages: GetMessages,
    val sendMessage: SendMessage,
    val connectUser: ConnectUser,
    val getMyConnections: GetMyConnections

)
