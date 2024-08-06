import React, { useState } from "react";
import useWebSocket from "./hooks/useWebSocket";
import ChatMessage from "./components/ChatMessage";
import MessageInput from "./components/MessageInput";

const App = () => {
  // States
  const [message, setMessage] = useState("");
  const [nickname, setNickname] = useState("");
  const { messages, sendMessage: sendWebSocketMessage } = useWebSocket();

  // Function to send message
  const sendMessage = () => {
    if (message.trim() && nickname.trim()) {
      const chatMessage = {
        nickname,
        content: message,
      };
      sendWebSocketMessage(chatMessage);
      // Clear the message input field
      setMessage("");
    }
  };

  return (
    <div className="flex min-h-screen flex-col bg-gray-800 p-12 text-white">
      {/* Chat message box */}
      <ul className="flex-1 space-y-4 overflow-auto">
        {messages.map((msg, index) => (
          <ChatMessage key={index} msg={msg} />
        ))}
      </ul>

      {/* Input areas */}
      <MessageInput
        nickname={nickname}
        message={message}
        setNickname={setNickname}
        setMessage={setMessage}
        sendMessage={sendMessage}
      />
    </div>
  );
};

export default App;
