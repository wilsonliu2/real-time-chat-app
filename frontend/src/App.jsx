import React, { useState, useEffect, useRef } from "react";
import Stomp from "stompjs";
import SockJS from "sockjs-client/dist/sockjs.js";

const App = () => {
  // States
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");
  const [nickname, setNickname] = useState("");

  // Refs
  const stompClientRef = useRef(null);
  const connectedRef = useRef(false);
  const messageInputRef = useRef(null);

  // Initialize WebSocket connection
  useEffect(() => {
    if (connectedRef.current) {
      return;
    }

    // Create SockJS Connection
    const socket = new SockJS("http://localhost:8080/ws");
    const client = Stomp.over(socket);

    // Connect STOMP client
    client.connect({}, () => {
      // Subscribe to "/topic/messages" endpoint
      client.subscribe("/topic/messages", (message) => {
        const receivedMessage = JSON.parse(message.body);
        // Update message state with the received message
        setMessages((prevMessages) => [...prevMessages, receivedMessage]);
      });
    });

    // Store client in the ref and mark connection as connected
    stompClientRef.current = client;
    connectedRef.current = true;

    // Disconnect client on component unmount
    return () => {
      if (client && client.connected) {
        client.disconnect();
      }
    };
  }, []);

  // Handle changes in the nickname field
  const handleNicknameChange = (event) => {
    setNickname(event.target.value);
  };

  // Handle changes in the message field
  const handleMessageChange = (event) => {
    setMessage(event.target.value);
  };

  // Function to send message
  const sendMessage = () => {
    if (
      message.trim() &&
      nickname.trim() &&
      stompClientRef.current &&
      stompClientRef.current.connected
    ) {
      const chatMessage = {
        nickname,
        content: message,
      };
      // Send the message to "/app/chat" endpoint
      stompClientRef.current.send("/app/chat", {}, JSON.stringify(chatMessage));
      // Clear the message input field
      setMessage("");
    }
  };

  // Keydown event listener to send message on enter key
  useEffect(() => {
    const handleKeyDown = (event) => {
      if (event.key === "Enter") {
        sendMessage();
      }
    };

    if (messageInputRef.current) {
      messageInputRef.current.addEventListener("keydown", handleKeyDown);
    }

    // Cleanup event listener
    return () => {
      if (messageInputRef.current) {
        messageInputRef.current.removeEventListener("keydown", handleKeyDown);
      }
    };
  }, [message, nickname]);

  return (
    <div className="flex min-h-screen flex-col bg-gray-800 p-12 text-white">
      {/* Chat message box */}
      <ul className="flex-1 space-y-4 overflow-auto">
        {messages.map((msg, index) => (
          <li
            key={index}
            className="flex items-start space-x-4 rounded p-2 hover:bg-gray-700"
          >
            {/* Avatar */}
            <div className="flex-shrink-0">
              <div className="flex h-10 w-10 items-center justify-center rounded-full bg-gray-600 text-white">
                {msg.nickname.charAt(0).toUpperCase()}
              </div>
            </div>
            {/* Message content */}
            <div>
              <div className="text-lg font-semibold text-white">
                {msg.nickname}
              </div>
              <p className="text-gray-400">{msg.content}</p>
            </div>
          </li>
        ))}
      </ul>

      {/* Input areas */}
      <div className="mt-4 flex space-x-4">
        <input
          type="text"
          placeholder="Enter nickname to chat"
          value={nickname}
          onChange={handleNicknameChange}
          className="flex-2 w-1/4 rounded border border-gray-600 bg-gray-700 p-2 text-white placeholder-gray-500"
        />
        <input
          type="text"
          placeholder="Type a message"
          value={message}
          onChange={handleMessageChange}
          className="flex-1 rounded border border-gray-600 bg-gray-700 p-2 text-white placeholder-gray-500 disabled:opacity-50"
          disabled={!nickname.trim()}
          ref={messageInputRef}
        />
        <button
          onClick={sendMessage}
          disabled={!message.trim() || !nickname.trim()}
          className="rounded bg-blue-600 p-2 text-white hover:bg-blue-500 disabled:opacity-50"
        >
          Send
        </button>
      </div>
    </div>
  );
};

export default App;
