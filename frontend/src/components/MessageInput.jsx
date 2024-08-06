import React, { useRef } from "react";

const MessageInput = ({
  nickname,
  message,
  setNickname,
  setMessage,
  sendMessage,
}) => {
  const messageInputRef = useRef(null);

  // Handle changes in the nickname field
  const handleNicknameChange = (event) => {
    setNickname(event.target.value);
  };

  // Handle changes in the message field
  const handleMessageChange = (event) => {
    setMessage(event.target.value);
  };

  // Keydown event listener to send message on enter key
  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      sendMessage();
    }
  };

  React.useEffect(() => {
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
  );
};

export default MessageInput;
