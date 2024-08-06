import { useEffect, useRef, useState } from "react";
import Stomp from "stompjs";
import SockJS from "sockjs-client/dist/sockjs.js";

const useWebSocket = () => {
  const [messages, setMessages] = useState([]);
  const stompClientRef = useRef(null);
  const connectedRef = useRef(false);

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

  const sendMessage = (chatMessage) => {
    if (stompClientRef.current && stompClientRef.current.connected) {
      // Send the message to "/app/chat" endpoint
      stompClientRef.current.send("/app/chat", {}, JSON.stringify(chatMessage));
    }
  };

  return { messages, sendMessage };
};

export default useWebSocket;
