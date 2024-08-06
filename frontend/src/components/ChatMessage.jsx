import React from "react";

const ChatMessage = ({ msg }) => {
  return (
    <li className="flex items-start space-x-4 rounded p-2 hover:bg-gray-700">
      {/* Avatar */}
      <div className="flex-shrink-0">
        <div className="flex h-10 w-10 items-center justify-center rounded-full bg-gray-600 text-white">
          {msg.nickname.charAt(0).toUpperCase()}
        </div>
      </div>
      {/* Message content */}
      <div>
        <div className="text-lg font-semibold text-white">{msg.nickname}</div>
        <p className="text-gray-400">{msg.content}</p>
      </div>
    </li>
  );
};

export default ChatMessage;
